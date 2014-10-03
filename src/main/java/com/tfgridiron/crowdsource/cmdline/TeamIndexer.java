/*
 * Copyright (c) Justin Moore
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
public class TeamIndexer {
  // TODO(P1): Make this class inherit from some base index assembly class
  private final static String NCAA_ID_HEADER = "NcaaId";
  private final static String PADDED_ID_HEADER = "PaddedId";
  private final static String CAPS_NAME_HEADER = "CapsName";
  private final static String SHORT_NAME_HEADER = "ShortName";
  private final static String FULL_NAME_HEADER = "FullName";

  private final ApiUtils apiUtils;
  // The spreadsheet and worksheet we care about
  private SpreadsheetEntry spreadsheetEntry = null;
  private WorksheetEntry worksheetEntry = null;

  private Map<Integer, Integer> ncaaIdToPaddedId;
  private Map<Integer, String> ncaaIdToCapsName;
  private Map<Integer, String> ncaaIdToShortName;
  private Map<Integer, String> ncaaIdToFullName;

  public TeamIndexer(ApiUtils apiUtils) {
    this.apiUtils = apiUtils;
  }

  public void loadIndex() throws Exception {
    if (ncaaIdToPaddedId != null && ncaaIdToCapsName != null && ncaaIdToShortName != null
        && ncaaIdToFullName != null) {
      return;
    }
    if (spreadsheetEntry == null) {
      spreadsheetEntry = apiUtils.getIndexSpreadsheetEntry();
    }
    if (spreadsheetEntry == null) {
      System.err.println("Could not get the spreadsheet entry for the index");
      return;
    }
    ncaaIdToPaddedId = new HashMap<Integer, Integer>();
    ncaaIdToCapsName = new HashMap<Integer, String>();
    ncaaIdToShortName = new HashMap<Integer, String>();
    ncaaIdToFullName = new HashMap<Integer, String>();
    for (WorksheetEntry worksheet : spreadsheetEntry.getWorksheets()) {
      if (Constants.INDEX_TEAMS_WORKSHEET_TITLE.equals(worksheet.getTitle().getPlainText())) {
        worksheetEntry = worksheet;
        buildIndex();
        break;
      }
    }
  }

  public Integer getPaddedId(int teamId) {
    return ncaaIdToPaddedId.get(teamId);
  }

  public String getCapsName(int teamId) {
    return ncaaIdToCapsName.get(teamId);
  }

  public String getShortName(int teamId) {
    return ncaaIdToShortName.get(teamId);
  }

  public String getFullName(int teamId) {
    return ncaaIdToFullName.get(teamId);
  }

  private void buildIndex() throws Exception {
    if (worksheetEntry == null) {
      return;
    }
    ListFeed listFeed =
        apiUtils.getSpreadsheetService().getFeed(worksheetEntry.getListFeedUrl(), ListFeed.class);
    for (ListEntry rowEntry : listFeed.getEntries()) {
      createOneTeamEntry(worksheetEntry.getTitle().getPlainText(), rowEntry);
    }
  }

  private void createOneTeamEntry(String worksheetTitle, ListEntry rowEntry) throws Exception {
    String ncaaIdStr = rowEntry.getCustomElements().getValue(NCAA_ID_HEADER);
    String paddedIdStr = rowEntry.getCustomElements().getValue(PADDED_ID_HEADER);
    String capsName = rowEntry.getCustomElements().getValue(CAPS_NAME_HEADER);
    String shortName = rowEntry.getCustomElements().getValue(SHORT_NAME_HEADER);
    String fullName = rowEntry.getCustomElements().getValue(FULL_NAME_HEADER);
    if (ncaaIdStr == null || paddedIdStr == null) {
      return;
    }
    if (capsName == null || shortName == null || fullName == null) {
      return;
    }
    Integer ncaaId = Integer.parseInt(ncaaIdStr);
    Integer paddedId = Integer.parseInt(paddedIdStr);
    ncaaIdToPaddedId.put(ncaaId, paddedId);
    ncaaIdToCapsName.put(ncaaId, capsName);
    ncaaIdToShortName.put(ncaaId, shortName);
    ncaaIdToFullName.put(ncaaId, fullName);
  }
}
