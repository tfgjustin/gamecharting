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

import com.google.api.client.util.DateTime;

/**
 * @author justin@tfgridiron.com (Justin Moore)
 * 
 */
public final class SpreadsheetMetadata implements Comparable<SpreadsheetMetadata> {
  private final String title;
  private final String gameId;
  private final String url;
  private final String entryKey;
  private String assignedTo;
  private DateTime lastUpdated;
  private boolean isDone;
  private boolean useThis;
  private String notes;
  private String checksum;
  private String stringForm = null;

  public SpreadsheetMetadata(String title, String gameId, String url, String entryKey,
      String assignedTo, DateTime lastUpdated, boolean isDone, String notes, boolean useThis,
      String checksum) {
    this.title = title;
    this.gameId = gameId;
    this.url = url;
    this.entryKey = entryKey;
    setAssignedTo(assignedTo);
    setLastUpdated(lastUpdated);
    setIsDone(isDone);
    setNotes(notes);
    setUseThis(useThis);
    setChecksum(checksum);
  }

  @Override
  public int compareTo(SpreadsheetMetadata other) {
    return title.compareTo(other.title);
  }

  public boolean isOlderThan(DateTime newTime) {
    return newTime.getValue() > lastUpdated.getValue();
  }

  public String getTitle() {
    return title;
  }

  public String getGameId() {
    return gameId;
  }

  public String getUrl() {
    return url;
  }

  public String getEntryKey() {
    return entryKey;
  }

  public String getAssignedTo() {
    return assignedTo;
  }

  public DateTime getLastUpdated() {
    return lastUpdated;
  }

  public Boolean getIsDone() {
    return isDone;
  }

  public String getNotes() {
    return notes;
  }

  public Boolean getUseThis() {
    return useThis;
  }

  public String getLastUpdatedAsString() {
    return lastUpdated.toStringRfc3339();
  }

  public String getChecksum() {
    return checksum;
  }

  public void setAssignedTo(String assignedTo) {
    this.assignedTo = (assignedTo != null) ? assignedTo : "";
    stringForm = null;
  }

  public void setLastUpdated(DateTime lastUpdated) {
    this.lastUpdated = lastUpdated;
    stringForm = null;
  }

  public void setIsDone(boolean isDone) {
    this.isDone = isDone;
    stringForm = null;
  }

  public void setNotes(String notes) {
    this.notes = (notes != null) ? notes : "";
    stringForm = null;
  }

  public void setUseThis(boolean useThis) {
    this.useThis = useThis;
  }

  public void setChecksum(String checksum) {
    this.checksum = (checksum != null) ? checksum : "";
    stringForm = null;
  }

  @Override
  public String toString() {
    if (stringForm != null) {
      return stringForm;
    }
    StringBuilder sb = new StringBuilder();
    sb.append("Title: \"").append(title).append("\" ");
    sb.append("GameID: \"").append(gameId).append("\" ");
    sb.append("URL: \"").append(url).append("\" ");
    sb.append("EntryKey: \"").append(entryKey).append("\" ");
    sb.append("AssignedTo: \"").append(assignedTo).append("\" ");
    sb.append("Updated: \"").append(lastUpdated.toStringRfc3339()).append("\" ");
    sb.append("IsDone: \"").append(isDone).append("\" ");
    sb.append("UseThis: \"").append(useThis).append("\" ");
    sb.append("Checksum: \"").append(checksum).append("\"");
    stringForm = sb.toString();
    return stringForm;
  }
}
