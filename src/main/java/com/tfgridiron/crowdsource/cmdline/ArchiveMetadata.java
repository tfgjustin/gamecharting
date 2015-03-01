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

import com.google.api.client.util.DateTime;

/**
 * @author justin@tfgridiron.com (Justin Moore)
 * 
 *         TODO(P1): Make this and SpreadsheetMetadata similar
 */
public final class ArchiveMetadata {
  private final String title;
  private final String zipFileUrl;
  private final int numSpreadsheets;
  private final String fileId;
  private final String checksum;
  private DateTime lastUpdated;

  public ArchiveMetadata(String title, String zipFileUrl, int numSpreadsheets,
      DateTime lastUpdated, String fileId, String checksum) {
    this.title = title;
    this.zipFileUrl = zipFileUrl;
    this.numSpreadsheets = numSpreadsheets;
    this.lastUpdated = lastUpdated;
    this.fileId = fileId;
    this.checksum = checksum;
  }

  public boolean isOlderThan(DateTime newTime) {
    return newTime.getValue() > lastUpdated.getValue();
  }

  public String getTitle() {
    return title;
  }

  public String getZipFileUrl() {
    return zipFileUrl;
  }

  public int getNumSpreadsheets() {
    return numSpreadsheets;
  }

  public DateTime getLastUpdated() {
    return lastUpdated;
  }

  public String getLastUpdatedAsString() {
    return lastUpdated.toStringRfc3339();
  }

  public String getFileId() {
    return fileId;
  }

  public String getChecksum() {
    return checksum;
  }

  public void setLastUpdated(DateTime lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  @Override
  public String toString() {
    return "TITLE:\"" + title + "\" ZipFileUrl: \"" + zipFileUrl + "\" NumSpreadsheets: \""
        + numSpreadsheets + "\" UPDATED: \"" + lastUpdated.toStringRfc3339() + "\" FILEID: \""
        + fileId + "\" CHECKSUM: " + checksum;
  }
}
