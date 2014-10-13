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

import com.bcsreport.cfbstats.tables.PlayRow;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author justin@tfgridiron.com (Justin Moore)
 * 
 */
public class GameSpreadsheetCreator {
  private static final int NUM_PLAY_ROWS_PADDING = 15;

  private final ApiUtils apiUtils;
  private final DataArchiveParser seasonData;

  private enum GameInfoColumns {
    GAME_DATE(0), HOME_ID(1), HOME_NAME(2), HOME_POINTS(3), AWAY_ID(4), AWAY_NAME(5), AWAY_POINTS(6);

    private int index;

    private GameInfoColumns(int index) {
      this.index = index;
    }

    public int getValue() {
      return index;
    }
  }

  public GameSpreadsheetCreator(ApiUtils apiUtils, DataArchiveParser seasonData) {
    this.apiUtils = apiUtils;
    this.seasonData = seasonData;
  }

  public SpreadsheetEntry populateSpreadsheet(String title, String gameId, List<String> gameInfo,
      List<PlayRow> gamePlays, SpreadsheetEntry spreadsheet) throws IOException, ServiceException {
    for (WorksheetEntry worksheet : spreadsheet.getWorksheets()) {
      if (Constants.PLAY_BY_PLAY_WORKSHEET_NAME.equals(worksheet.getTitle().getPlainText())) {
        updatePlayByPlayWorksheet(gameId, title, worksheet, gameInfo, gamePlays);
        return spreadsheet;
      }
    }
    return null;
  }

  private void updatePlayByPlayWorksheet(String gameId, String newTitle, WorksheetEntry worksheet,
      List<String> gameInfo, List<PlayRow> gamePlays) throws IOException, ServiceException {
    ListFeed listFeed =
        apiUtils.getSpreadsheetService().getFeed(worksheet.getListFeedUrl(), ListFeed.class);
    System.out.println("Found " + listFeed.getEntries().size() + " rows in spreadsheet and "
        + gamePlays.size() + " plays in game");
    ProgressBar pBar = new ProgressBar("Uploading plays", gamePlays.size());
    for (int i = 0; i < gamePlays.size(); i++) {
      ListEntry rowEntry = listFeed.getEntries().get(i);
      if (rowEntry == null) {
        System.err.println("Missing row entry " + i + " in spreadsheet " + newTitle);
      }
      else {
        updatePlayByPlayRow(gameId, gameInfo, rowEntry, i + 1);
        pBar.printProgress(i);
      }
    }
    pBar.finish();
    setFinalScore(gameInfo, gamePlays.size() + 1, listFeed.getEntries().get(gamePlays.size()));
    int numValidRows = gamePlays.size() + NUM_PLAY_ROWS_PADDING + 1; // 1 for the final score
    shrinkWorksheet(worksheet, numValidRows);
  }


  private void updatePlayByPlayRow(String gameId, List<String> gameInfo, ListEntry rowEntry,
      int playId) throws IOException, ServiceException {
    Map<String, String> outputColumns = new HashMap<String, String>(30);
    if (!seasonData.fetchRowData(gameId, playId, outputColumns)) {
      return;
    }
    for (Map.Entry<String, String> cell : outputColumns.entrySet()) {
      rowEntry.getCustomElements().setValueLocal(cell.getKey(), cell.getValue());
    }
    rowEntry.update();
  }

  private void setFinalScore(List<String> gameInfo, int numPlays, ListEntry rowEntry)
      throws IOException, ServiceException {
    rowEntry.getCustomElements().setValueLocal("Play", Integer.toString(numPlays));
    rowEntry.getCustomElements().setValueLocal("QTR", "Final");
    rowEntry.getCustomElements().setValueLocal("Home",
        gameInfo.get(GameInfoColumns.HOME_NAME.getValue()));
    rowEntry.getCustomElements().setValueLocal("HS",
        gameInfo.get(GameInfoColumns.HOME_POINTS.getValue()));
    rowEntry.getCustomElements().setValueLocal("Away",
        gameInfo.get(GameInfoColumns.AWAY_NAME.getValue()));
    rowEntry.getCustomElements().setValueLocal("AS",
        gameInfo.get(GameInfoColumns.AWAY_POINTS.getValue()));
    rowEntry.update();
  }

  private void shrinkWorksheet(WorksheetEntry worksheet, int numRows) throws IOException,
      ServiceException {
    // The worksheet has been updated since we initially did this, so refresh the metadata so we can
    // shrink it down.
    worksheet = worksheet.getSelf();
    worksheet.setRowCount(numRows);
    worksheet.update();
  }
}
