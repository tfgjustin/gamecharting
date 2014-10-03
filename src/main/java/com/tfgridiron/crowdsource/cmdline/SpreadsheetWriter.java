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

import com.google.gdata.client.spreadsheet.CellQuery;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.Cell;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;

import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author justin@tfgridiron.com (Justin Moore)
 * 
 */
public final class SpreadsheetWriter {
  private static final Pattern ROW_COLUMN_PATTERN = Pattern.compile(".*/R(\\d+)C(\\d+)$");
  private static int MIN_REQUIRED_COLUMN = 4;
  private static int MAX_REQUIRED_COLUMN = 7;

  public static String worksheetToCsv(SpreadsheetService spreadsheetService,
      WorksheetEntry worksheet) throws Exception {
    // Get the cell feed, including all empty cells
    CellQuery query = new CellQuery(worksheet.getCellFeedUrl());
    query.setReturnEmpty(true);
    CellFeed cellFeed = spreadsheetService.query(query, CellFeed.class);
    // Create a 2-D map of the entries
    Map<Integer, Map<Integer, CellEntry>> perRowCells = buildPerRowCellMap(cellFeed);
    if (perRowCells.isEmpty()) {
      return null;
    }
    // Create the output string builder
    StringBuilder sb = new StringBuilder();
    // For each row check to see if it's valid
    for (Map.Entry<Integer, Map<Integer, CellEntry>> entry : perRowCells.entrySet()) {
      addOneRow(entry.getValue(), sb);
    }
    return sb.toString();
  }

  private static Map<Integer, Map<Integer, CellEntry>> buildPerRowCellMap(CellFeed cellFeed)
      throws Exception {
    Map<Integer, Map<Integer, CellEntry>> perRowCells =
        new TreeMap<Integer, Map<Integer, CellEntry>>();
    for (CellEntry cell : cellFeed.getEntries()) {
      Matcher m = ROW_COLUMN_PATTERN.matcher(cell.getId());
      if (!m.matches()) {
        System.err.println("Invalid row/column pattern: " + cell.getId());
        continue;
      }
      Integer rowNum = Integer.parseInt(m.group(1));
      Integer colNum = Integer.parseInt(m.group(2));
      if (!perRowCells.containsKey(rowNum)) {
        Map<Integer, CellEntry> r = new TreeMap<Integer, CellEntry>();
        r.put(colNum, cell);
        perRowCells.put(rowNum, r);
      } else {
        perRowCells.get(rowNum).put(colNum, cell);
      }
    }
    System.out.println("Found " + perRowCells.size() + " rows");
    return perRowCells;
  }

  private static void addOneRow(Map<Integer, CellEntry> rowCells, StringBuilder builder) {
    if (!isValidRow(rowCells)) {
      return;
    }
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<Integer, CellEntry> entry : rowCells.entrySet()) {
      if (entry.getValue().getCell() == null) {
        sb.append(",");
      } else if (entry.getValue().getCell().getValue() == null) {
        sb.append(",");
      } else {
        sb.append(entry.getValue().getCell().getValue()).append(",");
      }
    }
    int idx = sb.indexOf("\n");
    while (idx >= 0) {
      sb.setCharAt(idx, ' ');
      idx = sb.indexOf("\n", idx);
    }
    sb.deleteCharAt(sb.length() - 1);
    sb.append("\n");
    builder.append(sb.toString());
  }

  private static boolean isValidRow(Map<Integer, CellEntry> cells) {
    if (cells == null) {
      return false;
    }
    for (int i = MIN_REQUIRED_COLUMN; i <= MAX_REQUIRED_COLUMN; ++i) {
      if (!cells.containsKey(i)) {
        return false;
      }
      Cell cell = cells.get(i).getCell();
      if (cell == null || cell.getValue() == null) {
        return false;
      }
      if (cell.getValue().isEmpty()) {
        return false;
      }
    }
    return true;
  }
}
