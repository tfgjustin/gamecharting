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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author justin@tfgridiron.com (Justin Moore)
 * 
 */
public class Constants {
  public static final String APPLICATION_NAME = "TempoFreeGridiron-Crowdsource/1.0";
  public static final String FOLDER_MIME = "application/vnd.google-apps.folder";
  public static final String GOOGLE_DRIVE_HOST = "drive.google.com";
  public static final Pattern FOLDER_ID_EXTRACTOR = Pattern.compile("^folders/(.*)$");
  // TODO(P0): Handle path joining properly
  public static final String BASE_DIRECTORY = System.getProperty("user.home") + "/.game_charting";
  public static final String CONFIG_FILE = BASE_DIRECTORY + "/config.properties";
  public static final String CONFIG_FILE_HEADER = "Configuration parameters for "
      + APPLICATION_NAME;
  public static final String PARENT_FOLDER_PROPERTY = "parent_folder_id";
  public static final String DATA_DIRECTORY_PROPERTY = "data_directory";
  public static final String PLAY_BY_PLAY_WORKSHEET_NAME = "PBP";
  public static final String ARCHIVE_PLAY_BY_PLAY_OUTFILE = "play.csv";
  public static final String PLAY_BY_PLAY_TEMPLATE_TITLE = "Play-by-Play Template";
  public static final String INDEX_DOCUMENT_TITLE = "Game Charting Index";
  public static final String INDEX_ARCHIVE_WORKSHEET_TITLE = "Archives";
  public static final String INDEX_ASSIGNMENTS_WORKSHEET_TITLE = "Assignments";
  public static final String INDEX_TEAMS_WORKSHEET_TITLE = "Teams";
  public static final String OAUTH_SPREADSHEET_SCOPE = "https://spreadsheets.google.com/feeds";
  public static final String PRIVATE_FULL_SPREADSHEET_FEED_URL =
      "https://spreadsheets.google.com/feeds/spreadsheets/private/full";

  public static final Pattern GAME_ID_EXTRACTOR = Pattern
      .compile("^.*\\s\\(GID:(\\d{16})-(\\d+)\\)$");

  public static final Map<String, String> NORMALIZED_PLAY_NAME_MAP = makeNormalizedPlayNameMap();

  private Constants() {
    // A static class
  }

  private static final Map<String, String> makeNormalizedPlayNameMap() {
    Map<String, String> m = new HashMap<String, String>(8);
    m.put("ATTEMPT", "Kick Attempt");
    m.put("FIELD_GOAL", "Field Goal");
    m.put("KICKOFF", "Kickoff");
    m.put("PASS", "Pass");
    m.put("PENALTY", "Penalty");
    m.put("PUNT", "Punt");
    m.put("RUSH", "Rush");
    m.put("TIMEOUT", "Timeout");
    return m;
  }
}
