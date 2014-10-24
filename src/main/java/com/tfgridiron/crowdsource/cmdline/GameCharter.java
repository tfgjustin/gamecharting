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

import com.bcsreport.cfbstats.tables.PlayRow;
import com.bcsreport.cfbstats.tables.TeamGameStatsRow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Children;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.ChildReference;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentReference;
import com.google.api.services.drive.model.Permission;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.util.ServiceException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The stand-alone game-charting administration application for Football Study Hall's Game Charting
 * project.
 * <ul>
 * <li>Does stuff</li>
 * </ul>
 * 
 * @author justin@tfgridiron.com (Justin Moore)
 */
public class GameCharter {
  // Top-level tasks:
  // TODO(P1): Add 'charter (un)assign' support
  // TODO(P1): Break this file up more
  // TODO(P1): Implement 'forcechecksums'
  // TODO(P2): Progress bars everywhere
  private static final String SET_PARENT_FOLDER_COMMAND = "setparentfolder";
  private static final String SET_DATA_DIRECTORY_COMMAND = "setdatadirectory";
  private static final String ADMIN_COMMAND = "admin";
  private static final String CHARTER_COMMAND = "charter";
  private static final String GAME_COMMAND = "game";
  private static final String FORCE_CHECKSUMS_COMMAND = "forcechecksums";
  private static final String ARCHIVE_COMMAND = "archive";
  private static final Set<String> VALID_COMMANDS = makeValidCommands();
  private static final String GAME_ADDNEW_COMMAND = "new";

  /** Directory to store user credentials. */
  private static final java.io.File DATA_STORE_DIR = new java.io.File(Constants.BASE_DIRECTORY,
      "store");

  private static final int MAX_CREATION_RETRIES = 2;

  private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyyMMdd");
  private static final Pattern TEAM_ID_PARSER = Pattern.compile("^0*(\\d+)$");

  /**
   * Property object which stores all of the configuration data.
   */
  private static Properties properties;

  /**
   * ID of the file we use as the template for all other play-by-play documents
   */
  private static String playByPlayTemplateId;

  /**
   * Global instance of the {@link DataStoreFactory}.
   */
  private static FileDataStoreFactory dataStoreFactory;

  /** Global instance of the HTTP transport. */
  private static HttpTransport httpTransport;

  /** Global instance of the JSON factory. */
  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

  /** Credentials for the Google APIs. */
  private static Credential credential;

  /** Global Drive API client. */
  private static Drive drive;

  /** Global Spreadsheet API client. */
  private static SpreadsheetService spreadsheetService;

  /** Utils for convenience functions to the API */
  private static ApiUtils apiUtils;

  /** Global Spreadsheet index metadata */
  private static SpreadsheetIndexer spreadsheetIndexer;
  private static AssignmentIndexer assignmentIndexer;
  private static TeamIndexer teamIndexer;
  private static ArchiveIndexer archiveIndexer;
  private static ArchiveCreator archiveCreator;
  private static GameSpreadsheetCreator gameSpreadsheetCreator;

  /** Maps seasons to the IDs of the folders in which they should be placed */
  private static Map<Integer, String> seasonToFolderId;

  /** Data for the current season from the local data directory */
  private static DataArchiveParser localSeasonData;

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


  public static void main(String[] args) {
    System.out.println(Arrays.toString(args));
    if (args.length == 0) {
      printUsage(0);
    }
    if (!VALID_COMMANDS.contains(args[0])) {
      System.err.println("Invalid command: " + args[0]);
      printUsage(1);
    }
    try {
      if (SET_PARENT_FOLDER_COMMAND.equals(args[0])) {
        setParentFolder(args);
        System.exit(0);
      } else if (SET_DATA_DIRECTORY_COMMAND.equals(args[0])) {
        setDataDirectory(args);
        System.exit(0);
      }
      loadConfiguration(true);
      authorize();
      connectToServices();
      loadIndexes();
      if (ADMIN_COMMAND.equals(args[0])) {
        adminOp(args);
      } else if (GAME_COMMAND.equals(args[0])) {
        loadDataDirectory();
        gameOp(args);
      } else if (CHARTER_COMMAND.equals(args[0])) {
        charterOp(args);
      } else if (FORCE_CHECKSUMS_COMMAND.equals(args[0])) {
        forceChecksums(args);
      } else if (ARCHIVE_COMMAND.equals(args[0])) {
        archiveOp(args);
      } else {
        System.err.println("Unhandled command: " + args[0]);
        printUsage(1);
      }
      System.exit(0);
    } catch (IOException e) {
      System.err.println(e.getMessage());
    } catch (Throwable t) {
      t.printStackTrace();
    }
    System.exit(1);
  }

  private static void connectToServices() throws Exception {
    if (credential == null) {
      throw new Exception("No valid credentials present; cannot connect to services.");
    }
    // set up the global Drive instance
    drive =
        new Drive.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(
            Constants.APPLICATION_NAME).build();
    cachePlayByPlayTemplateId();
    getPerYearFolders();
    spreadsheetService = new SpreadsheetService(Constants.APPLICATION_NAME);
    spreadsheetService.setOAuth2Credentials(credential);
    apiUtils = new ApiUtils(drive, spreadsheetService);
  }

  private static void loadIndexes() throws Exception {
    if (apiUtils == null) {
      throw new Exception("Cannot parse index doc; not connected to Google services yet");
    }
    teamIndexer = new TeamIndexer(apiUtils);
    teamIndexer.loadIndex();
    assignmentIndexer = new AssignmentIndexer(apiUtils, teamIndexer);
    assignmentIndexer.loadIndex();
    spreadsheetIndexer = new SpreadsheetIndexer(apiUtils, assignmentIndexer);
    spreadsheetIndexer.loadIndex();
  }

  protected static void printUsage(int exitCode) {
    // TODO(P0): Expand this to be a real usage
    System.out.println("List of valid commands:");
    for (String command : VALID_COMMANDS) {
      System.out.println("  " + command);
    }
    System.exit(exitCode);
  }

  protected static void setParentFolder(String[] args) throws Exception {
    if (args.length != 2) {
      System.err.println("No parent folder URL specified");
      printUsage(1);
    }
    try {
      loadConfiguration(false);
    } catch (Exception e) {
      // Continue on our merry way.
    }
    if (properties == null) {
      properties = new Properties();
    }
    URL parentFolder = null;
    try {
      parentFolder = new URL(args[1]);
    } catch (MalformedURLException e) {
      System.exit(1);
    }
    if (!Constants.GOOGLE_DRIVE_HOST.equals(parentFolder.getHost())) {
      System.err.println("Invalid Google Drive hostname: " + parentFolder.getHost());
      System.exit(1);
    }
    Matcher m = Constants.FOLDER_ID_EXTRACTOR.matcher(parentFolder.getRef());
    if (!m.matches()) {
      System.err.println("Invalid folder reference: " + parentFolder.getRef());
      System.err.println("Are you sure this is actually a Google Drive Folder?");
      System.exit(1);
    }
    properties.setProperty(Constants.PARENT_FOLDER_PROPERTY, m.group(1));
    storeConfiguration();
  }

  protected static void setDataDirectory(String[] args) throws Exception {
    if (args.length != 2) {
      System.err.println("No data directory specified");
      printUsage(1);
    }
    try {
      loadConfiguration(false);
    } catch (Exception e) {
      // Continue on our merry way.
    }
    if (properties == null) {
      properties = new Properties();
    }
    java.io.File dataDirectory = new java.io.File(args[1]);
    if (!dataDirectory.exists()) {
      System.err.println("Specified data directory does not exist: " + args[1]);
      printUsage(1);
    }
    if (!dataDirectory.isDirectory()) {
      System.err.println("Specified data directory path is not a directory: " + args[1]);
      printUsage(1);
    }
    properties.setProperty(Constants.DATA_DIRECTORY_PROPERTY, args[1]);
    storeConfiguration();
  }

  protected static void createIndex(String[] args) throws Exception {
    throw new Exception("createIndex not yet implemented");
  }

  protected static void adminOp(String[] args) throws Exception {
    if (args.length != 3) {
      System.err.println("Invalid 'admin' operation");
      printUsage(1);
    }
    if (args[1].equals("add")) {
      assignmentIndexer.addAdmin(args[2]);
    } else if (args[1].equals("del")) {
      assignmentIndexer.delAdmin(args[2]);
    } else {
      System.err.println("Invalid 'admin' command: " + args[2]);
      printUsage(1);
    }
  }

  protected static void charterOp(String[] args) throws Exception {
    if (args.length < 2) {
      System.err.println("Invalid 'charter' operation");
      printUsage(1);
    }
    if (args[1].equals("set")) {
      if (args.length == 4) {
        assignmentIndexer.setCharter(args[2], Integer.parseInt(args[3]), null);
      } else if (args.length == 5) {
        assignmentIndexer.setCharter(args[2], Integer.parseInt(args[3]), Integer.parseInt(args[4]));
      } else {
        System.err.println("Invalid 'charter set' command");
        printUsage(1);
      }
    } else if (args[1].equals("del")) {
      if (args.length < 3) {
        System.err.println("Invalid 'charter del' operation");
        printUsage(1);
      }
      Set<Integer> teamIds = new HashSet<Integer>();
      for (int i = 3; i < args.length; ++i) {
        teamIds.add(Integer.parseInt(args[i]));
      }
      assignmentIndexer.delCharter(args[2], teamIds);
    } else if (args[1].equals("assign")) {
      throw new Exception("'charter assign' operation not yet implemented");
    } else if (args[1].equals("unassign")) {
      throw new Exception("'charter unassign' operation not yet implemented");
    } else {
      System.err.println("Invalid 'charter' operation");
      printUsage(1);
    }
  }

  protected static void gameOp(String[] args) throws Exception {
    if (args.length < 2) {
      System.err.println("Invalid 'game' operation.");
      printUsage(1);
    }
    // By default, add all the games in the file.
    String gameId = GAME_ADDNEW_COMMAND;
    boolean dry_run = false;
    if (args[1].startsWith("test")) {
      args[1] = args[1].substring(4);
      dry_run = true;
    }
    if (args[1].equals("add")) {
      if (args.length != 3) {
        System.err.println("Invalid 'game add' command.");
        printUsage(1);
      }
      gameId = args[2];
    }
    cloneGame(gameId, dry_run);
  }

  protected static void forceChecksums(String[] args) throws Exception {
    throw new Exception("forceChecksums not yet implemented");
  }

  protected static void archiveOp(String[] args) throws Exception {
    if (drive == null || spreadsheetService == null) {
      throw new Exception("Missing connection to either Drive or Spreadsheets");
    }
    if (args.length != 3) {
      System.err.println("Invalid archive operation.");
      printUsage(1);
    }
    archiveIndexer = new ArchiveIndexer(apiUtils);
    archiveIndexer.loadIndex();
    if (args[1].equals("make")) {
      archiveCreator = new ArchiveCreator(apiUtils);
      updateArchive(args[2]);
    } else {
      System.err.println("Invalid archive operation.");
      printUsage(1);
    }
  }

  protected static void loadDataDirectory() throws IOException {
    if (properties == null) {
      System.err.println("No properties set; please run the '" + SET_DATA_DIRECTORY_COMMAND
          + "' command.");
      printUsage(1);
    }
    String dataDirectory = properties.getProperty(Constants.DATA_DIRECTORY_PROPERTY);
    if (dataDirectory == null || dataDirectory.isEmpty()) {
      System.err.println("Data directory property not set; please run the '"
          + SET_DATA_DIRECTORY_COMMAND + "' command.");
      printUsage(1);
    }
    localSeasonData = new DataArchiveParser(teamIndexer);
    localSeasonData.loadData(dataDirectory);
    gameSpreadsheetCreator = new GameSpreadsheetCreator(apiUtils, localSeasonData);
  }

  private static void loadConfiguration(boolean requireInitialization) throws Exception {
    if (properties == null) {
      properties = new Properties();
      InputStream inputStream = new FileInputStream(Constants.CONFIG_FILE);
      properties.load(inputStream);
    }
    if (requireInitialization) {
      checkProperty(Constants.PARENT_FOLDER_PROPERTY);
      checkProperty(Constants.DATA_DIRECTORY_PROPERTY);
    }
  }

  private static void storeConfiguration() throws Exception {
    if (properties == null) {
      throw new Exception("No configuration values to store");
    }
    if (properties.getProperty(Constants.PARENT_FOLDER_PROPERTY) == null) {
      throw new Exception("Configuration requires at least the locaton of the parent folder");
    }
    OutputStream outputStream = new FileOutputStream(Constants.CONFIG_FILE);
    properties.store(outputStream, Constants.CONFIG_FILE_HEADER);
    outputStream.close();
  }

  private static void checkProperty(String name) throws Exception {
    if (properties == null || name == null) {
      throw new Exception("Configuration file not loaded or missing configuration property name");
    }
    if (properties.getProperty(name) == null) {
      throw new Exception("Configuration property " + name + " is missing from "
          + Constants.CONFIG_FILE);
    }
  }

  private static void authorize() throws Exception {
    httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
    // load client secrets
    InputStream inputStream = GameCharter.class.getResourceAsStream("/client_secrets.json");
    if (inputStream == null) {
      inputStream = GameCharter.class.getResourceAsStream("client_secrets.json");
      if (inputStream == null) {
        inputStream = GameCharter.class.getResourceAsStream("resources/client_secrets.json");
        if (inputStream == null) {
          inputStream = GameCharter.class.getResourceAsStream("/resources/client_secrets.json");
        }
      }
    }
    if (inputStream == null) {
      throw new IOException("Could not get client_secrets.json from ... anywhere");
    }
    GoogleClientSecrets clientSecrets =
        GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(inputStream));
    if (clientSecrets.getDetails().getClientId().startsWith("Enter")
        || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
      System.err.println("Internal erorr: Enter Client ID and Secret from "
          + "https://code.google.com/apis/console/?api=drive into client_secrets.json");
      System.exit(1);
    }
    Set<String> scopes = new HashSet<String>(2);
    scopes.add(DriveScopes.DRIVE);
    scopes.add(Constants.OAUTH_SPREADSHEET_SCOPE);
    // set up authorization code flow
    GoogleAuthorizationCodeFlow flow =
        new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets, scopes)
            .setDataStoreFactory(dataStoreFactory).build();
    // authorize
    credential =
        new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
  }

  private static void cloneGame(String targetGameId, boolean dry_run) throws Exception {
    if (targetGameId == null) {
      throw new IllegalArgumentException("No game ID provided to cloneGame().");
    }
    if (playByPlayTemplateId == null || playByPlayTemplateId.isEmpty()) {
      System.err
          .println("Cannot find 'Play-by-Play Template' file in the parent directory; cannot clone it");
      System.exit(1);
    }
    System.out.println("cloneGame(" + targetGameId + ")");
    Set<String> gameIds =
        localSeasonData.getSeason().getGameTable()
            .getMatchingGames(targetGameId.equals(GAME_ADDNEW_COMMAND) ? null : targetGameId);
    System.out.println("Matched " + gameIds.size() + " games");
    System.out.println("matching: " + gameIds);
    int MAX_TO_CLONE = 250;
    int num_cloned = 0;
    List<String> perGameInfo = new ArrayList<String>();
    for (String thisGameId : gameIds) {
      // TODO(P0): Remove this before distribution
      if (num_cloned >= MAX_TO_CLONE) {
        return;
      }
      String gameDate = thisGameId.substring(8);
      if (isFutureGame(gameDate)) {
        continue;
      }
      if (!gameIdToInfo(thisGameId, perGameInfo) || perGameInfo.size() != 7) {
        continue;
      }
      String season = gameDateToSeason(gameDate);
      if (!seasonToFolderId.containsKey(Integer.parseInt(season))) {
        System.err.println("There is no folder for season " + season
            + " under the root play-by-play folder; please create it");
        continue;
      }
      List<PlayRow> gamePlays = localSeasonData.getSeason().getPlayTable().getPlays(thisGameId);
      // Does this game already exist?
      List<String> titles = new ArrayList<String>();
      Map<String, String> titleToCharter = new HashMap<String, String>();
      subGamesToClone(season, thisGameId, gameDate, perGameInfo, gamePlays, dry_run,
          getCharters(perGameInfo), titles, titleToCharter);
      num_cloned += titles.size();
      // We now know the sets of (title, charter) tuples we're going to create. Clone the first game
      // from the blank template, populate the template with the play-by-play data, and then clone
      // the remaining ones off that.
      if (titles.isEmpty()) {
        continue;
      }
      Map<String, String> titleToId = new HashMap<String, String>();
      // System.out.println("Cloning and populating base data for game " + thisGameId + "; "
      // + titles.get(0));
      String baseFileId =
          cloneAndFillUsingTemplate(playByPlayTemplateId, season, titles.get(0), thisGameId,
              perGameInfo, gamePlays);
      if (baseFileId == null) {
        continue;
      }
      titleToId.put(titles.get(0), baseFileId);
      for (int i = 1; i < titles.size(); ++i) {
        System.out.println("Cloning duplicate data for game " + thisGameId + "; from "
            + titles.get(0) + " to " + titles.get(i));
        String fileId =
            cloneAndFillUsingTemplate(baseFileId, season, titles.get(i), thisGameId, null, null);
        titleToId.put(titles.get(i), fileId);
      }
      for (String title : titles) {
        setFilePermissions(titleToId.get(title), titleToCharter.get(title));
        SpreadsheetEntry spreadsheet = apiUtils.getUniqueSpreadsheetByName(title);
        SpreadsheetMetadata metadata =
            spreadsheetIndexer.fetchPartialSpreadsheetMetadata(spreadsheet);
        metadata.setAssignedTo(titleToCharter.get(title));
        spreadsheetIndexer.updateAndSyncMetadata(season, spreadsheet, metadata, true);
      }
    }
  }

  private static String gameInfoToTitle(String gameId, String gameDate, List<String> perGameInfo,
      Integer index) {
    if (index == null) {
      index = 0;
    }
    String homeTeam = perGameInfo.get(GameInfoColumns.HOME_NAME.getValue());
    String awayTeam = perGameInfo.get(GameInfoColumns.AWAY_NAME.getValue());
    return gameDate + ": " + awayTeam + " at " + homeTeam + " (GID:" + gameId + "-" + index + ")";
  }

  private static String gameDateToSeason(String gameDate) throws ParseException {
    Calendar cal = Calendar.getInstance();
    cal.setTime(DATE_FORMATTER.parse(gameDate));
    Integer year = cal.get(Calendar.YEAR);
    Integer month = cal.get(Calendar.MONTH);
    if (month < Calendar.AUGUST) {
      // If it's earlier than august, this is from the previous year
      year -= 1;
    }
    return year.toString();
  }

  private static boolean isFutureGame(String gameDate) throws ParseException {
    Calendar gameCal = Calendar.getInstance();
    gameCal.setTime(DATE_FORMATTER.parse(gameDate));
    Calendar currCal = Calendar.getInstance();
    if (gameCal.after(currCal)) {
      return true;
    }
    return false;
  }

  private static Set<String> getCharters(List<String> perGameInfo) {
    String homeIdStr = perGameInfo.get(GameInfoColumns.HOME_ID.getValue());
    String awayIdStr = perGameInfo.get(GameInfoColumns.AWAY_ID.getValue());
    Set<String> charters =
        assignmentIndexer.getChartersForTeams(Integer.parseInt(homeIdStr),
            Integer.parseInt(awayIdStr));
    if (charters.isEmpty()) {
      charters = new HashSet<String>(1);
      charters.add("");
    }
    return charters;
  }

  private static void subGamesToClone(String season, String thisGameId, String gameDate,
      List<String> perGameInfo, List<PlayRow> thisGameData, boolean dry_run, Set<String> charters,
      List<String> titles, Map<String, String> titleToCharter) {
    int i = 0;
    Map<String, SpreadsheetMetadata> seasonMetadata =
        spreadsheetIndexer.getSpreadsheetMetadataBySeason(season);
    for (String charter : charters) {
      String title = gameInfoToTitle(thisGameId, gameDate, perGameInfo, i++);
      if (seasonMetadata == null || !seasonMetadata.containsKey(title)) {
        // We haven't indexed this game yet. It's likely we'll be creating a new spreadsheet.
        System.out.println("Creating new spreadsheet '" + title + "' (" + thisGameData.size()
            + " plays); charted by " + charter);
        if (!dry_run) {
          titles.add(title);
          titleToCharter.put(title, charter);
        }
      } else {
        System.out.println("Skipping existing spreadsheet '" + title + "'");
      }
    }
  }

  private static void updateArchive(String season) throws Exception {
    System.out.println("updateArchive(" + season + ")");
    // TODO(P0): Force a refresh of all the metadata
    Set<SpreadsheetMetadata> includedMetadata = new TreeSet<SpreadsheetMetadata>();
    Map<String, SpreadsheetMetadata> allFiles =
        spreadsheetIndexer.getSpreadsheetMetadataBySeason(season);
    if (allFiles != null) {
      Set<SpreadsheetMetadata> allMetadata = new HashSet<SpreadsheetMetadata>(allFiles.values());
      for (SpreadsheetMetadata spreadsheetMetadata : allMetadata) {
        if (spreadsheetMetadata.getIsDone()) {
          includedMetadata.add(spreadsheetMetadata);
        }
      }
    }
    ArchiveMetadata archiveMetadata = archiveIndexer.getMetadataByTitle(season);
    // Scenarios:
    // 1) Completed spreadsheets exist, archived metadata does not: create archive
    // 2) Completed spreadsheets exist, archived metadata exists: update archive
    // 3) No completed spreadsheets exist, no archived data exists: bail
    // 4) No completed spreadsheets exist, archived data does: print warning, exit
    if (includedMetadata.isEmpty()) {
      System.out.println("No completed worksheets present");
      if (archiveMetadata != null) {
        System.err.println("Archive for worksheet " + season
            + " exists, but no completed worksheets; bailing");
      }
      return;
    }
    if (archiveMetadata != null) {
      // TODO(P2): Move getMaxLastUpdated to spreadsheetIndexer
      DateTime maxLastUpdated =
          archiveCreator.getMaxLastUpdated(spreadsheetIndexer, includedMetadata);
      String checksum = archiveCreator.checksumFileCollection(spreadsheetIndexer, includedMetadata);
      if (archiveMetadata.getLastUpdated().getValue() >= maxLastUpdated.getValue()) {
        // This archive is newer than the set of files.
        System.out.println("Current archive is not older than the spreadsheets in the archive");
        return;
      }
      // The timestamp on the archive is older than the collective spreadsheet timestamp, but what
      // about the checksums?
      if (checksum.equals(archiveMetadata.getChecksum())) {
        System.out
            .println("Current archive is older than spreadsheets, but checksums are the same");
        // TODO(P1): update the timestamp on the archive
        return;
      }
    }
    System.out.println("Adding new archive " + season);
    // If we got here, we need to either add a new archive or update an existing one.
    ArchiveMetadata newMetadata =
        archiveCreator.createArchive(season, spreadsheetIndexer,
            properties.getProperty(Constants.PARENT_FOLDER_PROPERTY));
    archiveIndexer.insertOrUpdateMetadata(newMetadata);
  }

  private static boolean gameIdToInfo(String gameId, List<String> gameInfo) throws Exception {
    String awayIdStr = gameId.substring(0, 4);
    String homeIdStr = gameId.substring(4, 8);
    String gameDate = gameId.substring(8);
    if (!TEAM_ID_PARSER.matcher(awayIdStr).matches()
        || !TEAM_ID_PARSER.matcher(homeIdStr).matches()) {
      System.err
          .println("Either away ID " + awayIdStr + " or home ID " + homeIdStr + " is invalid");
      return false;
    }
    try {
      DATE_FORMATTER.parse(gameDate);
    } catch (ParseException e) {
      System.err.println("Invalid game date: " + gameDate);
      return false;
    }
    int awayId = Integer.parseInt(awayIdStr);
    int homeId = Integer.parseInt(homeIdStr);
    String awayTeam = teamIndexer.getShortName(awayId);
    String homeTeam = teamIndexer.getShortName(homeId);
    if (awayTeam == null || homeTeam == null) {
      System.err.println("Game: " + gameId + "; one of these IDs is invalid: " + awayId + " or "
          + homeId);
      return false;
    }
    if (!localSeasonData.hasGameData(gameId)) {
      System.err.println("Missing game data for game " + gameId);
      return false;
    }
    TeamGameStatsRow homeStats =
        localSeasonData.getSeason().getTeamGameStatsTable().getTeamGameStats(gameId, homeId);
    TeamGameStatsRow awayStats =
        localSeasonData.getSeason().getTeamGameStatsTable().getTeamGameStats(gameId, awayId);
    if (homeStats == null) {
      System.err.println("Missing home stats for game " + gameId + " team " + homeId);
      return false;
    }
    if (awayStats == null) {
      System.err.println("Missing away stats for game " + gameId + " team " + awayId);
      return false;
    }
    gameInfo.clear();
    gameInfo.add(gameDate);
    gameInfo.add(Integer.toString(homeId));
    gameInfo.add(homeTeam);
    gameInfo.add(Integer.toString(homeStats.getPoints()));
    gameInfo.add(Integer.toString(awayId));
    gameInfo.add(awayTeam);
    gameInfo.add(Integer.toString(awayStats.getPoints()));
    return true;
  }

  private static void cachePlayByPlayTemplateId() throws Exception {
    String parentFolder = properties.getProperty(Constants.PARENT_FOLDER_PROPERTY);
    if (parentFolder == null || parentFolder.isEmpty()) {
      System.err.println("No parent folder set; must run 'setparent' command first");
      System.exit(1);
    }
    String q =
        "title = '" + Constants.PLAY_BY_PLAY_TEMPLATE_TITLE + "' and '" + parentFolder
            + "' in parents";
    FileList fileList = drive.files().list().setQ(q).execute();
    List<File> files = fileList.getItems();
    if (files.size() != 1) {
      System.err.println("Cannot find template file named '"
          + Constants.PLAY_BY_PLAY_TEMPLATE_TITLE + "' in folder " + parentFolder);
      System.exit(1);
    }
    playByPlayTemplateId = files.get(0).getId();
  }

  private static String cloneAndFillUsingTemplate(String templateFileId, String season,
      String fileTitle, String gameId, List<String> gameInfo, List<PlayRow> gamePlays)
      throws Exception {
    if (apiUtils.getUniqueSpreadsheetByName(fileTitle) != null) {
      System.out.println("\nAlready have an entry for spreadsheet " + fileTitle);
      return null;
    }
    String parentFolderId = seasonToFolderId.get(Integer.parseInt(season));
    ParentReference parent = new ParentReference();
    parent.setId(parentFolderId);
    File copiedFile = new File();
    copiedFile.setTitle(fileTitle);
    copiedFile.setParents(Collections.singletonList(parent));
    File newFile = drive.files().copy(templateFileId, copiedFile).execute();
    if (gameInfo != null && gamePlays != null) {
      SpreadsheetEntry newSpreadsheet = insertBaseData(gameId, fileTitle, gameInfo, gamePlays);
      if (newSpreadsheet == null) {
        System.err.println("Error inserting base data for " + fileTitle);
        return null;
      }
    }
    return newFile.getId();
  }

  private static void getPerYearFolders() throws IOException {
    if (seasonToFolderId != null) {
      return;
    }
    String rootParentId = properties.getProperty(Constants.PARENT_FOLDER_PROPERTY);
    if (rootParentId == null) {
      return;
    }
    seasonToFolderId = new HashMap<Integer, String>();
    Children.List children = drive.children().list(rootParentId);
    for (ChildReference child : children.execute().getItems()) {
      File f = drive.files().get(child.getId()).execute();
      if (Constants.FOLDER_MIME.equals(f.getMimeType())) {
        try {
          Integer year = Integer.parseInt(f.getTitle());
          if (year == null || year < 2000 || year > 2050) {
            continue;
          }
          seasonToFolderId.put(year, f.getId());
        } catch (NumberFormatException nfe) {
          continue;
        }
      }
    }
  }

  private static void setFilePermissions(String fileId, String charterUser) throws Exception {
    if (fileId == null || fileId.isEmpty()) {
      return;
    }
    for (String userEmail : assignmentIndexer.getAdmins()) {
      addWriter(fileId, userEmail);
    }
    if (charterUser != null && !charterUser.isEmpty()) {
      addWriter(fileId, charterUser);
    }
  }

  private static void addWriter(String fileId, String userEmail) throws Exception {
    Permission p = new Permission();
    p.setValue(userEmail).setRole("writer").setType("user");
    drive.permissions().insert(fileId, p).setSendNotificationEmails(false).execute();
  }

  private static void delWriter(String fileId, String userEmail) throws Exception {
    List<Permission> permissions = drive.permissions().list(fileId).execute().getItems();
    for (Permission p : permissions) {
      if (!p.getType().equals("user")) {
        continue;
      }
      if (!p.getRole().equals("writer")) {
        continue;
      }
      if (!p.getValue().equals(userEmail)) {
        continue;
      }
      drive.permissions().delete(fileId, p.getId()).execute();
    }
  }

  private static SpreadsheetEntry insertBaseData(String gameId, String newTitle,
      List<String> gameInfo, List<PlayRow> plays) {
    SpreadsheetEntry spreadsheet = null;
    for (int i = 0; i < MAX_CREATION_RETRIES; ++i) {
      try {
        spreadsheet = apiUtils.getUniqueSpreadsheetByName(newTitle);
        if (spreadsheet == null) {
          return null;
        }
        return gameSpreadsheetCreator.populateSpreadsheet(newTitle, gameId, gameInfo, plays,
            spreadsheet);
      } catch (IOException exception) {
        System.err.println("I/O error while populating data for game " + gameId);
      } catch (ServiceException exception) {
        System.err.println("Google services error while populating data for game " + gameId);
      }
      if ((i + 1) < MAX_CREATION_RETRIES) {
        System.err.println("Retrying game " + gameId);
      }
    }
    return null;
  }

  private static void listAllIndexedSpreadsheets(String worksheetName) throws Exception {
    Map<String, SpreadsheetMetadata> allMetadata =
        spreadsheetIndexer.getSpreadsheetMetadataBySeason(worksheetName);
    if (allMetadata == null) {
      return;
    }
    for (Map.Entry<String, SpreadsheetMetadata> entry : allMetadata.entrySet()) {
      String urlString =
          "https://spreadsheets.google.com/feeds/spreadsheets/" + entry.getValue().getEntryKey();
      System.out.println("\nAttempting to load entry from " + urlString);
      SpreadsheetEntry spreadsheet =
          spreadsheetService.getEntry(new URL(urlString), SpreadsheetEntry.class);
      System.out.println("Title: " + spreadsheet.getTitle().getPlainText());
      System.out.println("GameID: " + spreadsheet.getSummary().getPlainText());
      System.out.println("\tID: " + spreadsheet.getId());
      System.out.println("\tKey: " + spreadsheet.getKey());
      System.out.println("\tChecksum: " + entry.getValue().getChecksum());
    }
    return;
  }

  private static Set<String> makeValidCommands() {
    Set<String> s = new HashSet<String>(10);
    s.add(SET_PARENT_FOLDER_COMMAND);
    s.add(SET_DATA_DIRECTORY_COMMAND);
    s.add(ADMIN_COMMAND);
    s.add(CHARTER_COMMAND);
    s.add(GAME_COMMAND);
    s.add(FORCE_CHECKSUMS_COMMAND);
    s.add(ARCHIVE_COMMAND);
    return s;
  }
}
