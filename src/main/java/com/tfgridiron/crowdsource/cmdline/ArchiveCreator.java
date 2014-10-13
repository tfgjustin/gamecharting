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

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.util.DateTime;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;
import com.google.api.services.drive.model.Permission;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author justin@tfgridiron.com (Justin Moore)
 * 
 */
public class ArchiveCreator {
  private final ApiUtils apiUtils;

  public ArchiveCreator(ApiUtils apiUtils) {
    this.apiUtils = apiUtils;
  }

  public ArchiveMetadata createArchive(String season, SpreadsheetIndexer spreadsheetIndexer,
      String destinationFolderId) throws Exception {
    // 0: Refresh everything
    spreadsheetIndexer.refreshMetadataFromSources(season);
    // 1: Check to make sure the worksheet exists
    Map<String, SpreadsheetMetadata> spreadsheetMetadata =
        spreadsheetIndexer.getSpreadsheetMetadataBySeason(season);
    if (spreadsheetMetadata == null) {
      return null;
    }
    // 2: Create an in-memory byte buffer with a zip wrapper around it
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    ZipOutputStream zipfile = new ZipOutputStream(outputStream);
    // TODO(P2): Per-game files or one big file?
    zipfile.putNextEntry(new ZipEntry(Constants.ARCHIVE_PLAY_BY_PLAY_OUTFILE));
    // 3: One-by-one add the files
    Set<SpreadsheetMetadata> allMetadata =
        new TreeSet<SpreadsheetMetadata>(spreadsheetMetadata.values());
    Set<SpreadsheetMetadata> includedMetadata = new TreeSet<SpreadsheetMetadata>();
    for (SpreadsheetMetadata metadata : allMetadata) {
      if (!metadata.getIsDone()) {
        continue;
      }
      if (!addOneSpreadsheet(metadata, zipfile)) {
        System.err.println("Error adding spreadsheet " + metadata.getTitle() + " to archive");
        return null;
      }
      includedMetadata.add(metadata);
    }
    zipfile.close();
    // 4: Checksum the completed archive (take the sorted set of checksums and checksum those)
    String checksum = checksumFileCollection(spreadsheetIndexer, includedMetadata);
    DateTime maxLastUpdated = getMaxLastUpdated(spreadsheetIndexer, includedMetadata);
    // 5: Upload the file
    File uploadedFile =
        uploadFile(season + ".zip", outputStream.toByteArray(), destinationFolderId);
    System.out.println("New ZIP file at " + uploadedFile.getWebContentLink());
    // 6: Create an ArchiveMetadata object and return it
    return new ArchiveMetadata(season, uploadedFile.getWebContentLink(), includedMetadata.size(),
        maxLastUpdated, uploadedFile.getId(), checksum);
  }

  public DateTime getMaxLastUpdated(SpreadsheetIndexer spreadsheetIndexer,
      Set<SpreadsheetMetadata> includedMetadata) {
    DateTime maxLastUpdated = null;
    for (SpreadsheetMetadata m : includedMetadata) {
      if (maxLastUpdated == null) {
        maxLastUpdated = m.getLastUpdated();
      } else if (m.getLastUpdated().getValue() > maxLastUpdated.getValue()) {
        maxLastUpdated = m.getLastUpdated();
      }
    }
    return maxLastUpdated;
  }

  public String checksumFileCollection(SpreadsheetIndexer spreadsheetIndexer,
      Set<SpreadsheetMetadata> includedMetadata) {
    Set<String> allChecksums = new TreeSet<String>();
    for (SpreadsheetMetadata m : includedMetadata) {
      allChecksums.add(m.getChecksum());
    }
    Hasher hasher = Hashing.sha512().newHasher();
    for (String checksum : allChecksums) {
      hasher.putString(checksum);
    }
    return hasher.hash().toString();
  }

  private boolean addOneSpreadsheet(SpreadsheetMetadata metadata, ZipOutputStream zipfile)
      throws Exception {
    String entryUrl =
        "https://spreadsheets.google.com/feeds/spreadsheets/" + metadata.getEntryKey();
    SpreadsheetEntry spreadsheetEntry =
        apiUtils.getSpreadsheetService().getEntry(new URL(entryUrl), SpreadsheetEntry.class);
    for (WorksheetEntry worksheet : spreadsheetEntry.getWorksheets()) {
      if (Constants.PLAY_BY_PLAY_WORKSHEET_NAME.equals(worksheet.getTitle().getPlainText())) {
        String csvContents =
            SpreadsheetWriter.worksheetToCsv(apiUtils.getSpreadsheetService(), worksheet);
        zipfile.write(csvContents.getBytes());
        return true;
      }
    }
    return false;
  }

  private File uploadFile(String fileName, byte[] fileContents, String destinationFolderId)
      throws Exception {
    File fileMetadata = new File();
    fileMetadata.setTitle(fileName);
    if (destinationFolderId != null && !destinationFolderId.isEmpty()) {
      ParentReference parent = new ParentReference();
      parent.setId(destinationFolderId);
      fileMetadata.setParents(Collections.singletonList(parent));
    }
    ByteArrayContent content = new ByteArrayContent("application/zip", fileContents);
    File f = apiUtils.getDrive().files().insert(fileMetadata, content).execute();
    Permission p = new Permission();
    p.setRole("reader").setType("anyone").setValue("me");
    apiUtils.getDrive().permissions().insert(f.getId(), p).setSendNotificationEmails(false)
        .execute();
    return apiUtils.getDrive().files().get(f.getId()).execute();
  }
}
