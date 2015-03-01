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
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.Property;
import com.google.gdata.client.spreadsheet.SpreadsheetQuery;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.TextConstruct;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * @author justin@tfgridiron.com (Justin Moore)
 * 
 */
public class ApiUtils {
  private final Drive drive;
  private final SpreadsheetService spreadsheetService;
  private final String rootFolderId;
  private SpreadsheetEntry indexSpreadsheetEntry = null;

  public ApiUtils(Drive drive, SpreadsheetService spreadsheetService, String rootFolderId) {
    this.drive = drive;
    this.spreadsheetService = spreadsheetService;
    this.rootFolderId = rootFolderId;
  }

  public Drive getDrive() {
    return drive;
  }

  public SpreadsheetService getSpreadsheetService() {
    return spreadsheetService;
  }

  public SpreadsheetEntry getIndexSpreadsheetEntry() throws IOException, ServiceException {
    if (indexSpreadsheetEntry != null) {
      // System.out.println("Using cached index spreadsheet entry");
      return indexSpreadsheetEntry;
    }
    // System.out.println("Fetching index spreadsheet entry anew");
    indexSpreadsheetEntry =
        getSpreadsheetByTitleAndFolder(Constants.INDEX_DOCUMENT_TITLE, rootFolderId);
    return indexSpreadsheetEntry;
  }

  public SpreadsheetEntry getSpreadsheetByTitleAndFolder(String title, String parentFolderId)
      throws IOException, ServiceException {
    if (title == null || title.isEmpty()) {
      return null;
    }
    String query = "title = '" + title + "'";
    if (parentFolderId != null && !parentFolderId.isEmpty()) {
      query += " and '" + parentFolderId + "' in parents";
    }
    Files.List fileList = drive.files().list().setQ(query);
    FileList files = fileList.execute();
    if (files.getItems().size() != 1) {
      System.err.println("Found " + files.getItems().size() + " files matching query " + query);
      return null;
    }
    return getSpreadsheetByDriveFile(files.getItems().get(0));
  }

  public SpreadsheetEntry getSpreadsheetByDriveFile(File driveFile) throws IOException,
      ServiceException {
    if (driveFile == null || driveFile.getTitle().isEmpty()) {
      return null;
    }
    SpreadsheetQuery query;
    try {
      query = new SpreadsheetQuery(new URL(Constants.PRIVATE_FULL_SPREADSHEET_FEED_URL));
    } catch (MalformedURLException exception) {
      System.err.println("Invalid spreadsheet feed url: "
          + Constants.PRIVATE_FULL_SPREADSHEET_FEED_URL);
      return null;
    }
    query.setTitleQuery(driveFile.getTitle());
    query.setTitleExact(true);
    query.setUpdatedMax(new DateTime(driveFile.getModifiedDate().getValue() + 1000));
    query.setUpdatedMin(new DateTime(driveFile.getModifiedDate().getValue() - 1000));
    SpreadsheetFeed spreadsheetFeed = spreadsheetService.getFeed(query, SpreadsheetFeed.class);
    List<SpreadsheetEntry> spreadsheets = spreadsheetFeed.getEntries();
    if (spreadsheets.size() != 1) {
      return null;
    }
    System.out.println("Found '" + driveFile.getTitle() + "'");
    return spreadsheets.get(0);
  }

  public String getFileIdByFileTitle(String fileTitle) throws IOException {
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
      throws IOException, ServiceException {
    for (WorksheetEntry worksheet : spreadsheet.getWorksheets()) {
      if (title.equals(worksheet.getTitle().getPlainText())) {
        return worksheet;
      }
    }
    return null;
  }

  public void listAllDriveEntries() throws IOException {
    Drive.Files.List fileList = drive.files().list();
    FileList files = fileList.execute();
    for (File f : files.getItems()) {
      System.out.println("File " + f.getTitle() + " ID: " + f.getId());
      System.out.println("\tLast modified: " + f.getModifiedDate() + " MD5: " + f.getMd5Checksum());
      System.out.println("\tKind: " + f.getKind() + " Mime: " + f.getMimeType());
      List<Property> props = f.getProperties();
      String pStr = (props != null) ? props.toString() : "(null-properties)";
      System.out.println("\tProperties: " + pStr);
    }
  }

  public void listAllSpreadsheets() throws MalformedURLException, IOException, ServiceException {
    // Make a request to the API and get all spreadsheets.
    SpreadsheetFeed feed =
        spreadsheetService.getFeed(new URL(Constants.PRIVATE_FULL_SPREADSHEET_FEED_URL),
            SpreadsheetFeed.class);
    List<SpreadsheetEntry> spreadsheets = feed.getEntries();

    // Iterate through all of the spreadsheets returned
    for (SpreadsheetEntry spreadsheet : spreadsheets) {
      System.out.println("Title: " + spreadsheet.getTitle().getPlainText());
      System.out.println("\tID: " + spreadsheet.getId());
      TextConstruct t = spreadsheet.getSummary();
      String gameId = (t != null) ? t.getPlainText() : "(null-construct)";
      System.out.println("\tGameID: " + gameId);
      System.out.println("\tKey: " + spreadsheet.getKey());
    }
  }

  public static void dumpSpreadsheetInfo(SpreadsheetEntry spreadsheet) {
    // Print the title of this spreadsheet to the screen
    System.out.println("\n" + spreadsheet.getTitle().getPlainText() + "\t" + spreadsheet.getId());
    System.out.println("\t\"" + spreadsheet.getSummary().getPlainText() + "\"\n");
  }
}
