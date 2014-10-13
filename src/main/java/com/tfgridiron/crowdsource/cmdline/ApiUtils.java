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

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.gdata.client.spreadsheet.SpreadsheetQuery;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;

import java.net.URL;
import java.util.List;

/**
 * @author justin@tfgridiron.com (Justin Moore)
 * 
 */
public class ApiUtils {
  private final Drive drive;
  private final SpreadsheetService spreadsheetService;
  private SpreadsheetEntry indexSpreadsheetEntry = null;

  public ApiUtils(Drive drive, SpreadsheetService spreadsheetService) {
    this.drive = drive;
    this.spreadsheetService = spreadsheetService;
  }

  public Drive getDrive() {
    return drive;
  }

  public SpreadsheetService getSpreadsheetService() {
    return spreadsheetService;
  }

  public SpreadsheetEntry getIndexSpreadsheetEntry() throws Exception {
    if (indexSpreadsheetEntry != null) {
      // System.out.println("Using cached index spreadsheet entry");
      return indexSpreadsheetEntry;
    }
    // System.out.println("Fetching index spreadsheet entry anew");
    indexSpreadsheetEntry = getUniqueSpreadsheetByName(Constants.INDEX_DOCUMENT_TITLE);
    return indexSpreadsheetEntry;
  }

  public SpreadsheetEntry getUniqueSpreadsheetByName(String spreadsheetName) throws Exception {
    if (spreadsheetName == null || spreadsheetName.isEmpty()) {
      System.err.println("getUniqueSpreadsheetByName: no name requested");
      return null;
    }
    SpreadsheetQuery query =
        new SpreadsheetQuery(new URL(Constants.PRIVATE_FULL_SPREADSHEET_FEED_URL));
    query.setTitleQuery(spreadsheetName);
    query.setTitleExact(true);
    SpreadsheetFeed spreadsheetFeed = spreadsheetService.getFeed(query, SpreadsheetFeed.class);
    List<SpreadsheetEntry> spreadsheets = spreadsheetFeed.getEntries();
    if (spreadsheets.size() != 1) {
      System.out.println("Found " + spreadsheets.size() + " spreadsheets named '" + spreadsheetName
          + "'");
      return null;
    }
    System.out.println("Found '" + spreadsheetName + "'");
    return spreadsheets.get(0);
  }

  public String getFileIdByFileTitle(String fileTitle) throws Exception {
    if (fileTitle == null || fileTitle.isEmpty()) {
      return null;
    }
    // TODO(P1): Add parent folder support
    String q = "title = '" + fileTitle + "'";
    FileList fileList = drive.files().list().setQ(q).execute();
    List<File> files = fileList.getItems();
    if (files.size() != 1) {
      System.out.println("Found " + files.size() + " files named '" + fileTitle + "'");
      return null;
    }
    return files.get(0).getId();
  }

  public static WorksheetEntry getWorksheetEntryByTitle(SpreadsheetEntry spreadsheet, String title)
      throws Exception {
    for (WorksheetEntry worksheet : spreadsheet.getWorksheets()) {
      if (title.equals(worksheet.getTitle().getPlainText())) {
        return worksheet;
      }
    }
    return null;
  }
}
