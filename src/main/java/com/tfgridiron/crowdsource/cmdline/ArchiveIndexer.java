/*
 * Copyright (c) Justin Moore.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.tfgridiron.crowdsource.cmdline;

import com.google.api.client.util.DateTime;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;

import java.util.HashMap;
import java.util.Map;

/**
 * @author justin@tfgridiron.com (Justin Moore)
 * 
 */
public final class ArchiveIndexer {
  private final static String TITLE_HEADER = "Title";
  private final static String ZIP_FILE_URL_HEADER = "ZipFileUrl";
  private final static String NUM_SPREADSHEETS_HEADER = "NumSpreadsheets";
  private final static String FILE_ID_HEADER = "FileID";
  private final static String LAST_UPDATED_HEADER = "LastUpdated";
  private final static String CHECKSUM_HEADER = "Checksum";

  private final ApiUtils apiUtils;
  // The spreadsheet and worksheet we care about
  private SpreadsheetEntry spreadsheetEntry;
  private WorksheetEntry worksheetEntry;

  // Archives indexed by title
  private Map<String, ArchiveMetadata> archiveMetadata;
  private Map<String, ListEntry> indexMetadata;

  public ArchiveIndexer(ApiUtils apiUtils) {
    this.apiUtils = apiUtils;
  }

  // Always call this first
  public void loadIndex() throws Exception {
    if (archiveMetadata != null && indexMetadata != null) {
      System.out.println("Index of spreadsheets already loaded");
      return;
    }
    archiveMetadata = new HashMap<String, ArchiveMetadata>();
    indexMetadata = new HashMap<String, ListEntry>();
    if (spreadsheetEntry == null) {
      spreadsheetEntry = apiUtils.getIndexSpreadsheetEntry();
    }
    if (spreadsheetEntry == null) {
      System.err.println("Could not get the spreadsheet entry for the index");
      return;
    }
    if (worksheetEntry == null) {
      for (WorksheetEntry worksheet : spreadsheetEntry.getWorksheets()) {
        if (Constants.INDEX_ARCHIVE_WORKSHEET_TITLE.equals(worksheet.getTitle().getPlainText())) {
          worksheetEntry = worksheet;
          break;
        }
      }
    }
    if (worksheetEntry == null) {
      System.err.println("Could not find the index worksheet in the spreadsheet");
    } else {
      buildIndex(worksheetEntry);
    }
  }

  public Map<String, ArchiveMetadata> getAllArchiveMetadata() {
    return archiveMetadata;
  }

  public ArchiveMetadata getMetadataByTitle(String title) {
    if (archiveMetadata == null) {
      return null;
    }
    return archiveMetadata.get(title);
  }

  public boolean insertMetadata(ArchiveMetadata metadata) throws Exception {
    if (worksheetEntry == null) {
      return false;
    }
    if (archiveMetadata == null || indexMetadata == null) {
      return false;
    }
    if (archiveMetadata.containsKey(metadata.getTitle())
        || indexMetadata.containsKey(metadata.getTitle())) {
      System.out.println("Not inserting metadata because it already exists for "
          + metadata.getTitle());
      return true;
    }
    ListEntry rowEntry = new ListEntry();
    archiveMetadataToListEntry(metadata, rowEntry);
    rowEntry = apiUtils.getSpreadsheetService().insert(worksheetEntry.getListFeedUrl(), rowEntry);
    System.out.println("Inserted new metadata entry for " + metadata.getTitle());
    archiveMetadata.put(metadata.getTitle(), metadata);
    indexMetadata.put(metadata.getTitle(), rowEntry);
    return true;
  }

  public boolean insertOrUpdateMetadata(ArchiveMetadata currentMetadata) throws Exception {
    if (worksheetEntry == null) {
      return false;
    }
    if (archiveMetadata == null || indexMetadata == null) {
      return false;
    }
    if (!archiveMetadata.containsKey(currentMetadata.getTitle())
        && !indexMetadata.containsKey(currentMetadata.getTitle())) {
      return insertMetadata(currentMetadata);
    }
    ArchiveMetadata existingData = archiveMetadata.get(currentMetadata.getTitle());
    if (!existingData.isOlderThan(currentMetadata.getLastUpdated())) {
      // Current data is not newer than the existing one. return false because no update occurred
      System.out.println("Not updating metadata for " + currentMetadata.getTitle()
          + " because no timestamp is updated");
      return false;
    }
    if (currentMetadata.getChecksum().equals(existingData.getChecksum())) {
      System.out.println("Not updating metadata for " + currentMetadata.getTitle()
          + " because no contents (via checksum) did not change");
      return false;
    }
    ListEntry rowEntry = indexMetadata.get(currentMetadata.getTitle());
    archiveMetadataToListEntry(currentMetadata, rowEntry);
    ListEntry newEntry = rowEntry.update();
    archiveMetadata.put(currentMetadata.getTitle(), currentMetadata);
    indexMetadata.put(currentMetadata.getTitle(), newEntry);
    deleteArchiveFile(existingData);
    return true;
  }

  private void deleteArchiveFile(ArchiveMetadata metadata) throws Exception {
    apiUtils.getDrive().files().delete(metadata.getFileId());
  }

  private void buildIndex(WorksheetEntry worksheet) throws Exception {
    ListFeed listFeed =
        apiUtils.getSpreadsheetService().getFeed(worksheet.getListFeedUrl(), ListFeed.class);
    for (ListEntry rowEntry : listFeed.getEntries()) {
      createOneMetadataEntry(rowEntry);
    }
  }

  private void createOneMetadataEntry(ListEntry rowEntry) throws Exception {
    ArchiveMetadata metadata = listEntryToArchiveMetadata(rowEntry);
    indexMetadata.put(metadata.getTitle(), rowEntry);
    archiveMetadata.put(metadata.getTitle(), metadata);
  }

  private ArchiveMetadata listEntryToArchiveMetadata(ListEntry rowEntry) throws Exception {
    String title = rowEntry.getCustomElements().getValue(TITLE_HEADER);
    String zipFileUrl = rowEntry.getCustomElements().getValue(ZIP_FILE_URL_HEADER);
    String numSpreadsheetsStr = rowEntry.getCustomElements().getValue(NUM_SPREADSHEETS_HEADER);
    String lastUpdatedStr = rowEntry.getCustomElements().getValue(LAST_UPDATED_HEADER);
    String fileId = rowEntry.getCustomElements().getValue(FILE_ID_HEADER);
    String checksum = rowEntry.getCustomElements().getValue(CHECKSUM_HEADER);
    DateTime lastUpdated = DateTime.parseRfc3339(lastUpdatedStr);
    return new ArchiveMetadata(title, zipFileUrl, Integer.parseInt(numSpreadsheetsStr),
        lastUpdated, fileId, checksum);
  }

  private void archiveMetadataToListEntry(ArchiveMetadata metadata, ListEntry rowEntry)
      throws Exception {
    rowEntry.getCustomElements().setValueLocal(TITLE_HEADER, metadata.getTitle());
    rowEntry.getCustomElements().setValueLocal(ZIP_FILE_URL_HEADER, metadata.getZipFileUrl());
    rowEntry.getCustomElements().setValueLocal(NUM_SPREADSHEETS_HEADER,
        Integer.toString(metadata.getNumSpreadsheets()));
    rowEntry.getCustomElements().setValueLocal(LAST_UPDATED_HEADER,
        metadata.getLastUpdatedAsString());
    rowEntry.getCustomElements().setValueLocal(FILE_ID_HEADER, metadata.getFileId());
    rowEntry.getCustomElements().setValueLocal(CHECKSUM_HEADER, metadata.getChecksum());
  }
}
