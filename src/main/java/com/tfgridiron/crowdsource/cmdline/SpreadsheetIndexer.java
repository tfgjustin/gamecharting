/*
 * Copyright (c) 2014 Justin Moore
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
import com.google.api.services.drive.model.Permission;
import com.google.api.services.drive.model.PermissionList;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * @author justin@tfgridiron.com (Justin Moore)
 * 
 */
public final class SpreadsheetIndexer {
  // TODO(P1): Make this class inherit from some base index assembly class
  private final static String TITLE_HEADER = "Title";
  private final static String GAME_ID_HEADER = "GameId";
  private final static String ASSIGNED_TO_HEADER = "AssignedTo";
  private final static String URL_HEADER = "URL";
  private final static String LAST_UPDATED_HEADER = "LastUpdated";
  private final static String IS_DONE_HEADER = "IsDone";
  private final static String USE_THIS_HEADER = "UseThis";
  private final static String NOTES_HEADER = "Notes";
  private final static String KEY_HEADER = "Key";
  private final static String CHECKSUM_HEADER = "Checksum";

  private final static String CHARTING_STATUS_WORKSHEET_TITLE = "Charting Status";

  private final ApiUtils apiUtils;
  private final AssignmentIndexer assignmentIndexer;
  // The spreadsheet and the worksheets we care about
  private SpreadsheetEntry spreadsheetEntry;
  private Map<String, WorksheetEntry> worksheetsBySeason;

  // Spreadsheets indexed by season ~> title ~> data
  private Map<String, Map<String, SpreadsheetMetadata>> perSeasonMetadata;
  private Map<String, Map<String, ListEntry>> perSeasonListEntries;

  public SpreadsheetIndexer(ApiUtils apiUtils, AssignmentIndexer assignmentIndexer) {
    this.apiUtils = apiUtils;
    this.assignmentIndexer = assignmentIndexer;
  }

  // Always call this first
  public void loadIndex() throws Exception {
    if (perSeasonMetadata != null && perSeasonListEntries != null) {
      System.out.println("Index of spreadsheets already loaded");
      return;
    }
    perSeasonMetadata = new HashMap<String, Map<String, SpreadsheetMetadata>>();
    perSeasonListEntries = new HashMap<String, Map<String, ListEntry>>();
    if (spreadsheetEntry == null) {
      spreadsheetEntry = apiUtils.getIndexSpreadsheetEntry();
    }
    if (spreadsheetEntry == null) {
      System.err.println("Could not get the spreadsheet entry for the index");
      return;
    }
    if (worksheetsBySeason == null) {
      worksheetsBySeason = new HashMap<String, WorksheetEntry>();
      for (WorksheetEntry worksheet : spreadsheetEntry.getWorksheets()) {
        buildIndex(worksheet);
      }
    }
  }

  public void refreshMetadataFromSources(String season) throws Exception {
    if (perSeasonMetadata == null || perSeasonListEntries == null) {
      System.out.println("Spreadsheets have not been indexed for " + season);
      return;
    }
    Map<String, SpreadsheetMetadata> seasonGames = perSeasonMetadata.get(season);
    for (SpreadsheetMetadata existingMetadata : seasonGames.values()) {
      System.out.println("Forcing metadata refresh of " + existingMetadata.getTitle());
      SpreadsheetEntry gameSpreadsheet =
          apiUtils.getUniqueSpreadsheetByName(existingMetadata.getTitle());
      if (gameSpreadsheet == null) {
        System.err.println("Cannot find spreadsheet " + existingMetadata.getTitle());
        continue;
      }
      updateAndSyncMetadata(season, gameSpreadsheet, existingMetadata, false);
    }
    return;
  }

  public Map<String, Map<String, SpreadsheetMetadata>> getAllSpreadsheetMetadata() {
    return perSeasonMetadata;
  }

  public Map<String, SpreadsheetMetadata> getSpreadsheetMetadataBySeason(String season) {
    if (perSeasonMetadata == null) {
      return null;
    }
    return perSeasonMetadata.get(season);
  }

  public SpreadsheetMetadata getMetadataBySpreadsheetTitle(String season, String spreadsheetTitle) {
    System.out.println("getMetadataBySpreadsheetTitle(" + season + ", " + spreadsheetTitle + ")");
    if (perSeasonMetadata == null) {
      return null;
    }
    Map<String, SpreadsheetMetadata> m = perSeasonMetadata.get(season);
    if (m == null) {
      return null;
    }
    return m.get(spreadsheetTitle);
  }

  public boolean insertMetadata(String season, SpreadsheetMetadata metadata) throws Exception {
    // System.out.println("insertMetadata(" + season + ", " + metadata.getTitle() + ")");
    if (worksheetsBySeason == null) {
      return false;
    }
    WorksheetEntry worksheetEntry = worksheetsBySeason.get(season);
    if (worksheetEntry == null) {
      return false;
    }
    if (perSeasonMetadata == null || perSeasonListEntries == null) {
      return false;
    }
    Map<String, SpreadsheetMetadata> spreadsheetMetadata = perSeasonMetadata.get(season);
    if (spreadsheetMetadata == null) {
      spreadsheetMetadata = new HashMap<String, SpreadsheetMetadata>();
      perSeasonMetadata.put(season, spreadsheetMetadata);
    }
    Map<String, ListEntry> listMetadata = perSeasonListEntries.get(season);
    if (listMetadata == null) {
      listMetadata = new HashMap<String, ListEntry>();
      perSeasonListEntries.put(season, listMetadata);
    }
    if (spreadsheetMetadata.containsKey(metadata.getTitle())
        || listMetadata.containsKey(metadata.getTitle())) {
      System.out.println("Not inserting metadata because it already exists for "
          + metadata.getTitle());
      return true;
    }
    ListEntry rowEntry = new ListEntry();
    spreadsheetMetadataToListEntry(metadata, rowEntry);
    rowEntry = apiUtils.getSpreadsheetService().insert(worksheetEntry.getListFeedUrl(), rowEntry);
    // System.out.println("Inserted new metadata entry for " + metadata.getTitle());
    spreadsheetMetadata.put(metadata.getTitle(), metadata);
    listMetadata.put(metadata.getTitle(), rowEntry);
    return true;
  }

  public SpreadsheetMetadata fetchPartialSpreadsheetMetadata(SpreadsheetEntry spreadsheet)
      throws Exception {
    String title = spreadsheet.getTitle().getPlainText();
    // System.out.println("fetchPartialSpreadsheetMetadata(" + title + ")");
    Matcher matcher = Constants.GAME_ID_EXTRACTOR.matcher(title);
    if (!matcher.matches()) {
      System.err.println("Invalid game title: could not extract game ID");
      return null;
    }
    String gameId = matcher.group(1);
    String url = spreadsheet.getHtmlLink().getHref();
    DateTime lastUpdated = new DateTime(spreadsheet.getUpdated().getValue());
    String entryKey = spreadsheet.getKey().toString();
    return new SpreadsheetMetadata(title, gameId, url, entryKey, null, lastUpdated, false, null,
        false, null);
  }

  public void updateAndSyncMetadata(String season, SpreadsheetEntry spreadsheet,
      SpreadsheetMetadata currentMetadata, boolean force) throws Exception {
    SpreadsheetMetadata partial = fetchPartialSpreadsheetMetadata(spreadsheet);
    if (partial == null) {
      return;
    }
    if (!currentMetadata.getTitle().equals(partial.getTitle())) {
      return;
    }
    if (!currentMetadata.getUrl().equals(partial.getUrl())) {
      return;
    }
    if (!currentMetadata.getGameId().equals(partial.getGameId())) {
      return;
    }
    // If we've gotten here, we're talking about the same title, the same URL, and the same Game ID.
    // Check to see if the current metadata is older than the one we just loaded.
    if (!force && !currentMetadata.isOlderThan(partial.getLastUpdated())) {
      // No, the current metadata is not outdated. Nothing to do here.
      System.out.println("Not updating metadata for " + currentMetadata.getTitle()
          + " because no timestamp is updated");
      return;
    }
    // The current metadata may be outdated. Check the checksum.
    String newChecksum = this.calculateSpreadsheetChecksum(spreadsheet);
    if (!force && currentMetadata.getChecksum().equals(newChecksum)) {
      // The timestamp has been updated, but the actual contents of the spreadsheet haven't changed.
      // Update the current current metadata to reflect this, and update the row in the actual
      // spreadsheet, too.
      currentMetadata.setLastUpdated(partial.getLastUpdated());
      System.out.println("Updating timestamp for " + currentMetadata.getTitle()
          + " because checksums still match");
    } else {
      // The checksums have changed. We need to grab the new data.
      List<String> isDoneInfo = getIsDoneInfo(spreadsheet);
      currentMetadata.setChecksum(newChecksum);
      currentMetadata.setIsDone(determineIfYes(isDoneInfo.get(0)));
      currentMetadata.setNotes(isDoneInfo.get(1));
      currentMetadata.setUseThis(determineIfYes(isDoneInfo.get(2)));
    }
    syncMetadata(season, currentMetadata);
  }

  private void syncMetadata(String season, SpreadsheetMetadata currentMetadata) throws Exception {
    Map<String, ListEntry> seasonEntries = this.perSeasonListEntries.get(season);
    if (seasonEntries == null || seasonEntries.isEmpty()) {
      System.out.println("No spreadsheet rows for " + season + " season; inserting");
      insertMetadata(season, currentMetadata);
      return;
    }
    ListEntry row = seasonEntries.get(currentMetadata.getTitle());
    if (row == null) {
      System.out.println("No spreadsheet entry for " + currentMetadata.getTitle() + " in season "
          + season + "; inserting");
      insertMetadata(season, currentMetadata);
    } else {
      spreadsheetMetadataToListEntry(currentMetadata, row);
      row.update();
    }
  }

  private void buildIndex(WorksheetEntry seasonWorksheet) throws Exception {
    String seasonWorksheetTitle = seasonWorksheet.getTitle().getPlainText();
    // Not one of the per-year spreadsheets
    if (!seasonWorksheetTitle.matches("20[012][0-9]")) {
      return;
    }
    worksheetsBySeason.put(seasonWorksheetTitle, seasonWorksheet);
    ListFeed listFeed =
        apiUtils.getSpreadsheetService().getFeed(seasonWorksheet.getListFeedUrl(), ListFeed.class);
    for (ListEntry rowEntry : listFeed.getEntries()) {
      createOneMetadataEntry(seasonWorksheetTitle, rowEntry);
    }
  }

  private void createOneMetadataEntry(String worksheetTitle, ListEntry rowEntry) throws Exception {
    SpreadsheetMetadata metadata = listEntryToSpreadsheetMetadata(rowEntry);
    if (metadata == null) {
      return;
    }
    Map<String, ListEntry> worksheetEntries = null;
    if (perSeasonListEntries.containsKey(worksheetTitle)) {
      worksheetEntries = perSeasonListEntries.get(worksheetTitle);
    } else {
      worksheetEntries = new HashMap<String, ListEntry>();
      perSeasonListEntries.put(worksheetTitle, worksheetEntries);
    }
    worksheetEntries.put(metadata.getTitle(), rowEntry);
    Map<String, SpreadsheetMetadata> worksheetMetadata = null;
    if (perSeasonMetadata.containsKey(worksheetTitle)) {
      worksheetMetadata = perSeasonMetadata.get(worksheetTitle);
    } else {
      worksheetMetadata = new HashMap<String, SpreadsheetMetadata>();
      perSeasonMetadata.put(worksheetTitle, worksheetMetadata);
    }
    worksheetMetadata.put(metadata.getTitle(), metadata);
  }

  private List<String> getIsDoneInfo(SpreadsheetEntry playByPlaySpreadsheet) throws Exception {
    List<String> isDoneInfo = new ArrayList<String>(3);
    isDoneInfo.add("");
    isDoneInfo.add("");
    isDoneInfo.add("");
    WorksheetEntry worksheet =
        ApiUtils.getWorksheetEntryByTitle(playByPlaySpreadsheet, CHARTING_STATUS_WORKSHEET_TITLE);
    if (worksheet == null) {
      System.out.println("No such worksheet in spreadsheet: " + CHARTING_STATUS_WORKSHEET_TITLE);
      return isDoneInfo;
    }
    extractIsDoneInfo(worksheet, isDoneInfo);
    return isDoneInfo;
  }

  private void extractIsDoneInfo(WorksheetEntry worksheet, List<String> isDoneInfo)
      throws Exception {
    isDoneInfo.set(0, "");
    isDoneInfo.set(1, "");
    isDoneInfo.set(2, "");
    ListFeed listFeed =
        apiUtils.getSpreadsheetService().getFeed(worksheet.getListFeedUrl(), ListFeed.class);
    List<ListEntry> rows = listFeed.getEntries();
    if (rows.size() != 1) {
      System.out.println("For 'isDone' info expected 1 row but found " + rows.size());
      return;
    }
    ListEntry row = rows.get(0);
    boolean isDone = determineIfYes(row.getCustomElements().getValue(IS_DONE_HEADER));
    boolean useThis = determineIfYes(row.getCustomElements().getValue(USE_THIS_HEADER));
    isDoneInfo.set(0, Boolean.toString(isDone));
    isDoneInfo.set(1, row.getCustomElements().getValue(NOTES_HEADER));
    isDoneInfo.set(2, Boolean.toString(useThis));
  }

  public String calculateSpreadsheetChecksum(SpreadsheetEntry playByPlaySpreadsheet)
      throws Exception {
    WorksheetEntry pbpWorksheet =
        ApiUtils.getWorksheetEntryByTitle(playByPlaySpreadsheet,
            Constants.PLAY_BY_PLAY_WORKSHEET_NAME);
    if (pbpWorksheet == null) {
      return null;
    }
    List<String> isDoneInfo = getIsDoneInfo(playByPlaySpreadsheet);
    return calculateWorksheetChecksum(pbpWorksheet, isDoneInfo);
  }

  public String calculateWorksheetChecksum(WorksheetEntry playByPlayWorksheet,
      List<String> isDoneInfo) throws Exception {
    CellFeed cellFeed =
        apiUtils.getSpreadsheetService().getFeed(playByPlayWorksheet.getCellFeedUrl(),
            CellFeed.class);
    Hasher hasher = Hashing.sha512().newHasher();
    for (CellEntry cell : cellFeed.getEntries()) {
      hasher.putString(cell.getId());
      hasher.putString(cell.getCell().getValue());
    }
    for (String doneInfo : isDoneInfo) {
      if (doneInfo != null) {
        hasher.putString(doneInfo);
      } else {
        hasher.putString("");
      }
    }
    return hasher.hash().toString();
  }

  private void spreadsheetMetadataToListEntry(SpreadsheetMetadata metadata, ListEntry rowEntry)
      throws Exception {
    rowEntry.getCustomElements().setValueLocal(TITLE_HEADER, metadata.getTitle());
    rowEntry.getCustomElements().setValueLocal(GAME_ID_HEADER, metadata.getGameId());
    rowEntry.getCustomElements().setValueLocal(ASSIGNED_TO_HEADER, metadata.getAssignedTo());
    rowEntry.getCustomElements().setValueLocal(URL_HEADER, metadata.getUrl());
    rowEntry.getCustomElements().setValueLocal(LAST_UPDATED_HEADER,
        metadata.getLastUpdatedAsString());
    rowEntry.getCustomElements().setValueLocal(IS_DONE_HEADER, metadata.getIsDone().toString());
    rowEntry.getCustomElements().setValueLocal(NOTES_HEADER, metadata.getNotes());
    rowEntry.getCustomElements().setValueLocal(USE_THIS_HEADER, metadata.getUseThis().toString());
    rowEntry.getCustomElements().setValueLocal(KEY_HEADER, metadata.getEntryKey());
    rowEntry.getCustomElements().setValueLocal(CHECKSUM_HEADER, metadata.getChecksum());
  }

  private SpreadsheetMetadata listEntryToSpreadsheetMetadata(ListEntry rowEntry) throws Exception {
    String title = rowEntry.getCustomElements().getValue(TITLE_HEADER);
    String gameId = rowEntry.getCustomElements().getValue(GAME_ID_HEADER);
    String assignedTo = rowEntry.getCustomElements().getValue(ASSIGNED_TO_HEADER);
    String url = rowEntry.getCustomElements().getValue(URL_HEADER);
    String lastUpdatedStr = rowEntry.getCustomElements().getValue(LAST_UPDATED_HEADER);
    String isDoneStr = rowEntry.getCustomElements().getValue(IS_DONE_HEADER);
    String notes = rowEntry.getCustomElements().getValue(NOTES_HEADER);
    String useThisStr = rowEntry.getCustomElements().getValue(USE_THIS_HEADER);
    String entryKey = rowEntry.getCustomElements().getValue(KEY_HEADER);
    String checksum = rowEntry.getCustomElements().getValue(CHECKSUM_HEADER);
    DateTime lastUpdated = DateTime.parseRfc3339(lastUpdatedStr);
    boolean isDone = determineIfYes(isDoneStr);
    boolean useThis = determineIfYes(useThisStr);
    return new SpreadsheetMetadata(title, gameId, url, entryKey, assignedTo, lastUpdated, isDone,
        notes, useThis, checksum);
  }

  private boolean determineIfYes(String isDoneStr) {
    if (isDoneStr == null) {
      return false;
    }
    if (isDoneStr.isEmpty()) {
      return false;
    }
    if (isDoneStr.equalsIgnoreCase("Y") || isDoneStr.equalsIgnoreCase("yes")) {
      return true;
    }
    return Boolean.parseBoolean(isDoneStr);
  }

  private String getAssignedTo(String title) throws Exception {
    String fileId = apiUtils.getFileIdByFileTitle(title);
    if (fileId == null) {
      System.err.println("Could not get file ID for '" + title + "'");
      return "";
    }
    // TODO(P2): Move this somewhere more efficient
    PermissionList permissionList = apiUtils.getDrive().permissions().list(fileId).execute();
    for (Permission permission : permissionList.getItems()) {
      if (!assignmentIndexer.isPermissionIdAdmin(permission.getId())) {
        String userEmail = assignmentIndexer.emailForPermissionId(permission.getId());
        if (userEmail != null) {
          return userEmail;
        }
      }
    }
    return "";
  }
}
