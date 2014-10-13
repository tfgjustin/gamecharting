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

import org.apache.commons.csv.CSVParser;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author justin@tfgridiron.com (Justin Moore)
 * 
 */
public final class TeamGameStatsParser {
  private static final int MIN_LINE_PARTS = 12;
  private static final int DEFAULT_NUM_GAMES = 400;
  private final String filename;
  private final Map<String, Map<String, Integer>> perGameTeamPoints;

  public TeamGameStatsParser(String filename) {
    this.perGameTeamPoints = new HashMap<String, Map<String, Integer>>(DEFAULT_NUM_GAMES);
    this.filename = filename;
  }

  public void parse() throws IOException {
    perGameTeamPoints.clear();
    CSVParser parser = new CSVParser(new FileReader(filename));
    String[] values = parser.getLine();
    if (values == null) {
      // Header line
      return;
    }
    values = parser.getLine();
    while (values != null) {
      parseLine(Arrays.asList(values), parser.getLineNumber());
      values = parser.getLine();
    }
  }

  Integer getPerGameTeamPoints(String gameDate, String teamId) {
    if (!perGameTeamPoints.containsKey(gameDate)) {
      return null;
    }
    return perGameTeamPoints.get(gameDate).get(teamId);
  }

  private void parseLine(List<String> parts, int lineNumber) {
    if (parts.size() < MIN_LINE_PARTS) {
      System.err.println("Invalid line in " + filename + " at line " + lineNumber);
      return;
    }
    // TODO(P2): enum these
    if (parts.get(1).length() != 16) {
      return;
    }
    String gameDate = parts.get(1).substring(8);
    int teamId = -1;
    int numPoints = -1;
    try {
      teamId = Integer.parseInt(parts.get(0));
      numPoints = Integer.parseInt(parts.get(35));
    } catch (Exception e) {
      return;
    }
    if (teamId < 0 || numPoints < 0) {
      return;
    }
    if (!perGameTeamPoints.containsKey(gameDate)) {
      Map<String, Integer> p = new HashMap<String, Integer>();
      perGameTeamPoints.put(gameDate, p);
    }
    perGameTeamPoints.get(gameDate).put(Integer.toString(teamId), numPoints);
  }
}
