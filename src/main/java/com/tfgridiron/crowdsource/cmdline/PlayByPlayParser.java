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
import java.util.TreeMap;

/**
 * @author justin@tfgridiron.com (Justin Moore)
 * 
 */
public final class PlayByPlayParser {
  private static final int MIN_LINE_PARTS = 12;
  private static final int DEFAULT_NUM_GAMES = 400;
  private final String filename;
  private final Map<String, Map<Integer, List<String>>> perGamePlays;

  public PlayByPlayParser(String filename) {
    this.perGamePlays = new HashMap<String, Map<Integer, List<String>>>(DEFAULT_NUM_GAMES);
    this.filename = filename;
  }

  public void parse() throws IOException {
    perGamePlays.clear();
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

  Map<String, Map<Integer, List<String>>> getPerGamePlays() {
    return perGamePlays;
  }

  private void parseLine(List<String> parts, int lineNumber) {
    if (parts.size() < MIN_LINE_PARTS) {
      System.err.println("Invalid line in " + filename + " at line " + lineNumber);
      return;
    }
    String gameId = parts.get(0);
    int playNumber = Integer.parseInt(parts.get(1));
    if (!perGamePlays.containsKey(gameId)) {
      Map<Integer, List<String>> p = new TreeMap<Integer, List<String>>();
      perGamePlays.put(gameId, p);
    }
    perGamePlays.get(gameId).put(playNumber, parts);
    // System.out.println("Parsed game " + gameId + " play " + playNumber);
  }
}
