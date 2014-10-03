/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.bcsreport.cfbstats.tables;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 
 * @author ryan.hoes
 */
public class TeamTable {
  SortedMap<Integer, TeamRow> table;

  public static TeamTable loadTable(File file) throws IOException {
    TeamTable table = new TeamTable();
    BufferedReader stdin = new BufferedReader(new FileReader(file));
    String s = stdin.readLine();
    while ((s = stdin.readLine()) != null) {
      table.addRow(TeamRow.makeRow(s));
    }
    return table;
  }

  public TeamTable() {
    table = new TreeMap<Integer, TeamRow>();
  }

  public void addRow(TeamRow row) {
    table.put(row.getTeamCode(), row);

  }

  public String getTeamName(Integer code) {
    return table.get(code).getName();
  }
}
