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

import com.google.api.services.drive.model.PermissionId;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetEntry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author justin@tfgridiron.com (Justin Moore)
 * 
 */
public class AssignmentIndexer {
  // TODO(P1): Make this class inherit from some base index assembly class
  private final static String EMAIL_HEADER = "Email";
  private final static String TEAM_ID_HEADER = "NcaaTeamId";
  private final static String PRIORITY_HEADER = "Priority";

  private final static String ADMIN_TAG = "Admin";
  private final static String ANY_TAG = "Any";

  private final static int MUST_HAVE_PRIORITY = 100;

  private final ApiUtils apiUtils;
  private final TeamIndexer teamIndexer;
  // The spreadsheet and worksheet we care about
  private SpreadsheetEntry spreadsheetEntry = null;
  private WorksheetEntry worksheetEntry = null;

  private Set<String> adminAccounts = null;
  private Set<String> anyPoolAccounts = null;
  private Map<Integer, Map<String, Integer>> assignmentsByTeam = null;
  private Map<String, Map<Integer, Integer>> assignmentsByUser = null;
  // This maps a (user,team) tuple to spreadsheet list entries
  private Map<String, ListEntry> assignmentsToRow = null;
  // Maps a permission ID to a user email address
  private Map<String, String> permissionIdToEmail = null;

  public AssignmentIndexer(ApiUtils apiUtils, TeamIndexer teamIndexer) {
    this.apiUtils = apiUtils;
    this.teamIndexer = teamIndexer;
  }

  public void loadIndex() throws Exception {
    if (adminAccounts != null && assignmentsByTeam != null && assignmentsByUser != null
        && assignmentsToRow != null && permissionIdToEmail != null) {
      return;
    }
    if (spreadsheetEntry == null) {
      spreadsheetEntry = apiUtils.getIndexSpreadsheetEntry();
    }
    if (spreadsheetEntry == null) {
      System.err.println("Could not get the spreadsheet entry for the index");
      return;
    }
    for (WorksheetEntry worksheet : spreadsheetEntry.getWorksheets()) {
      if (Constants.INDEX_ASSIGNMENTS_WORKSHEET_TITLE.equals(worksheet.getTitle().getPlainText())) {
        worksheetEntry = worksheet;
        buildIndex();
        break;
      }
    }
  }

  public Set<String> getAdmins() {
    return adminAccounts;
  }

  public Set<String> getAnyPool() {
    return anyPoolAccounts;
  }

  public String emailForPermissionId(String permissionId) {
    if (permissionIdToEmail != null && permissionId != null) {
      return permissionIdToEmail.get(permissionId);
    }
    return null;
  }

  public boolean isUserAdmin(String userAccount) throws Exception {
    return userAccount != null && adminAccounts != null && adminAccounts.contains(userAccount);
  }

  public boolean isPermissionIdAdmin(String permissionId) throws Exception {
    if (permissionId == null || permissionId.isEmpty()) {
      return false;
    }
    if (permissionIdToEmail == null || permissionIdToEmail.isEmpty()) {
      return false;
    }
    return isUserAdmin(permissionIdToEmail.get(permissionId));
  }

  public Set<String> getChartersForTeams(int teamOne, int teamTwo) {
    Map<String, Integer> charters;
    charters = getUsersForTeam(teamOne);
    charters.putAll(getUsersForTeam(teamTwo));
    return extractCharters(charters).keySet();
  }

  public void addAdmin(String userEmail) throws Exception {
    if (userEmail == null) {
      return;
    }
    addAdminInternal(userEmail, null);
  }

  public void delAdmin(String userEmail) throws Exception {
    if (userEmail == null) {
      return;
    }
    delAdminInternal(userEmail);
  }

  public void setCharter(String email, Integer teamId, Integer priority) throws Exception {
    if (email == null || teamId == null) {
      return;
    }
    addCharterInternal(email, teamId, priority, null);
  }

  public void delCharter(String email, Set<Integer> teamIds) throws Exception {
    Map<Integer, Integer> userTeams = assignmentsByUser.get(email);
    if (teamIds == null || teamIds.isEmpty()) {
      // If teamIds is null, that means we want to remove all the teams with which this person is
      // associated.
      for (Map.Entry<Integer, Integer> teamCharter : userTeams.entrySet()) {
        delCharterInternal(email, teamCharter.getKey());
      }
    } else {
      for (Integer teamId : teamIds) {
        delCharterInternal(email, teamId);
      }
    }
  }

  private Map<String, Integer> getUsersForTeam(int teamId) {
    return extractCharters(assignmentsByTeam.get(teamId));
  }

  private Map<String, Integer> extractCharters(Map<String, Integer> inputMap) {
    Map<String, Integer> mustHaveUsers = new HashMap<String, Integer>();
    if (inputMap == null || inputMap.isEmpty()) {
      return mustHaveUsers;
    }
    String bestMaybeUser = null;
    Integer bestMaybePriority = -1;
    for (Map.Entry<String, Integer> user : inputMap.entrySet()) {
      if (user.getValue() >= MUST_HAVE_PRIORITY) {
        mustHaveUsers.put(user.getKey(), user.getValue());
      } else if (user.getValue() > bestMaybePriority) {
        bestMaybeUser = user.getKey();
        bestMaybePriority = user.getValue();
      }
    }
    if (!mustHaveUsers.isEmpty()) {
      return mustHaveUsers;
    } else if (bestMaybeUser != null) {
      Map<String, Integer> maybeUsers = new HashMap<String, Integer>();
      maybeUsers.put(bestMaybeUser, bestMaybePriority);
      return maybeUsers;
    } else {
      return mustHaveUsers;
    }
  }

  private void buildIndex() throws Exception {
    if (worksheetEntry == null) {
      return;
    }
    adminAccounts = new HashSet<String>();
    anyPoolAccounts = new HashSet<String>();
    assignmentsByTeam = new HashMap<Integer, Map<String, Integer>>();
    assignmentsByUser = new HashMap<String, Map<Integer, Integer>>();
    assignmentsToRow = new HashMap<String, ListEntry>();
    permissionIdToEmail = new HashMap<String, String>();
    ListFeed listFeed =
        apiUtils.getSpreadsheetService().getFeed(worksheetEntry.getListFeedUrl(), ListFeed.class);
    for (ListEntry rowEntry : listFeed.getEntries()) {
      createOneAssignmentEntry(worksheetEntry.getTitle().getPlainText(), rowEntry);
    }
  }

  private void createOneAssignmentEntry(String worksheetTitle, ListEntry rowEntry) throws Exception {
    String userEmail = rowEntry.getCustomElements().getValue(EMAIL_HEADER);
    String teamIdStr = rowEntry.getCustomElements().getValue(TEAM_ID_HEADER);
    if (ADMIN_TAG.equalsIgnoreCase(teamIdStr)) {
      addAdminInternal(userEmail, rowEntry);
      return;
    } else if (ANY_TAG.equalsIgnoreCase(teamIdStr)) {
      addAnyPoolInternal(userEmail, rowEntry);
    }
    Integer teamId = Integer.parseInt(teamIdStr);
    String priorityStr = rowEntry.getCustomElements().getValue(PRIORITY_HEADER);
    Integer priority = 0;
    if (priorityStr != null) {
      priority = Integer.parseInt(priorityStr);
    }
    addCharterInternal(userEmail, teamId, priority, rowEntry);
  }

  private void addAdminInternal(String userEmail, ListEntry row) throws Exception {
    if (assignmentsByUser.containsKey(userEmail) || anyPoolAccounts.contains(userEmail)) {
      throw new IllegalArgumentException("User " + userEmail
          + " cannot be an admin; they are an assigned charter or in the random pool");
    }
    if (adminAccounts.contains(userEmail)) {
      // No action needed
      return;
    }
    adminAccounts.add(userEmail);
    cachePermissionId(userEmail);
    if (row != null) {
      // We don't need to create a new entry
      String key = userTeamTupleToString(userEmail, ADMIN_TAG);
      assignmentsToRow.put(key, row);
    } else {
      createNewRow(userEmail, ADMIN_TAG, "");
    }
  }

  private void delAdminInternal(String userEmail) throws Exception {
    if (userEmail == null) {
      return;
    }
    String key = userTeamTupleToString(userEmail, ADMIN_TAG);
    ListEntry row = assignmentsToRow.get(key);
    if (row == null) {
      return;
    }
    row.delete();
    // We have to rebuild the index because deleting the row borks the spreadsheet
    buildIndex();
  }

  private void addCharterInternal(String userEmail, Integer teamId, Integer priority, ListEntry row)
      throws Exception {
    if (adminAccounts.contains(userEmail) || anyPoolAccounts.contains(userEmail)) {
      throw new IllegalArgumentException("User " + userEmail
          + " is an admin or in the random pool; they cannot be a charter");
    }
    if (priority == null) {
      priority = 0;
    }
    String shortName = teamIndexer.getShortName(teamId);
    if (shortName == null) {
      System.err.println("Error: team " + teamId + " is not defined in "
          + Constants.INDEX_TEAMS_WORKSHEET_TITLE + " worksheet");
      return;
    }
    cachePermissionId(userEmail);
    String key = userTeamTupleToString(userEmail, teamId.toString());
    ListEntry currentRow = assignmentsToRow.get(key);
    if (currentRow != null) {
      // This user is already assigned to this team. Update the priority on the current row.
      currentRow.getCustomElements().setValueLocal(PRIORITY_HEADER, priority.toString());
      currentRow.update();
      return;
    }
    Map<String, Integer> teamUsers = assignmentsByTeam.get(teamId);
    if (teamUsers == null) {
      teamUsers = new HashMap<String, Integer>();
      assignmentsByTeam.put(teamId, teamUsers);
    }
    teamUsers.put(userEmail, priority);
    Map<Integer, Integer> userTeams = assignmentsByUser.get(userEmail);
    if (userTeams == null) {
      userTeams = new HashMap<Integer, Integer>();
      assignmentsByUser.put(userEmail, userTeams);
    }
    userTeams.put(teamId, priority);
    // Do we need to add a row to the spreadsheet?
    if (row != null) {
      assignmentsToRow.put(key, row);
    } else {
      // Create a new row
      createNewRow(userEmail, teamId.toString(), priority.toString());
      cachePermissionId(userEmail);
    }
  }

  private void delCharterInternal(String userEmail, Integer teamId) throws Exception {
    if (userEmail == null || teamId == null) {
      return;
    }
    String key = userTeamTupleToString(userEmail, teamId.toString());
    ListEntry row = assignmentsToRow.get(key);
    if (row == null) {
      return;
    }
    row.delete();
    // We have to rebuild the index because deleting the row borks the spreadsheet
    buildIndex();
  }

  private void addAnyPoolInternal(String userEmail, ListEntry row) throws Exception {
    if (assignmentsByUser.containsKey(userEmail) || adminAccounts.contains(userEmail)) {
      throw new IllegalArgumentException("User " + userEmail
          + " cannot be in random pool; they are either an assigned charter or an admin");
    }
    if (anyPoolAccounts.contains(userEmail)) {
      // No action needed
      return;
    }
    anyPoolAccounts.add(userEmail);
    cachePermissionId(userEmail);
    if (row != null) {
      // We don't need to create a new entry
      String key = userTeamTupleToString(userEmail, ANY_TAG);
      assignmentsToRow.put(key, row);
    } else {
      createNewRow(userEmail, ANY_TAG, "");
    }
  }

  private void delAnyPoolInternal(String userEmail) throws Exception {
    if (userEmail == null) {
      return;
    }
    String key = userTeamTupleToString(userEmail, ANY_TAG);
    ListEntry row = assignmentsToRow.get(key);
    if (row == null) {
      return;
    }
    row.delete();
    // We have to rebuild the index because deleting the row borks the spreadsheet
    buildIndex();
  }


  private void cachePermissionId(String userEmail) throws Exception {
    if (userEmail == null || userEmail.isEmpty()) {
      return;
    }
    PermissionId permission = apiUtils.getDrive().permissions().getIdForEmail(userEmail).execute();
    permissionIdToEmail.put(permission.getId(), userEmail);
  }

  private void createNewRow(String userEmail, String teamId, String priority) throws Exception {
    ListEntry row = new ListEntry();
    row.getCustomElements().setValueLocal(EMAIL_HEADER, userEmail);
    row.getCustomElements().setValueLocal(TEAM_ID_HEADER, teamId);
    row.getCustomElements().setValueLocal(PRIORITY_HEADER, priority);
    apiUtils.getSpreadsheetService().insert(worksheetEntry.getListFeedUrl(), row);
    assignmentsToRow.put(userTeamTupleToString(userEmail, teamId), row);
  }

  private String userTeamTupleToString(String userEmail, String teamId) {
    // teamId could also be "admin"
    if (teamId == null || userEmail == null) {
      return null;
    }
    return userEmail + ":" + teamId;
  }
}
