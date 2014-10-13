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
public class TeamGameStatsTable {
  SortedMap<String, TeamGameStatsRow> table;

  public static TeamGameStatsTable loadTable(File file) throws IOException {
    TeamGameStatsTable tgstable = new TeamGameStatsTable();
    BufferedReader stdin = new BufferedReader(new FileReader(file));
    String s = stdin.readLine();
    while ((s = stdin.readLine()) != null) {
      tgstable.addRow(TeamGameStatsRow.makeRow(s));
    }
    return tgstable;
  }

  public TeamGameStatsTable() {
    table = new TreeMap();
  }

  public void addRow(TeamGameStatsRow row) {
    TeamGameKey key = new TeamGameKey(row.getGameCode(), row.getTeamCode());
    // System.out.println("TeamGame " + key.toString() + " row " + row.toString());
    table.put(key.toString(), row);
  }

  public TeamGameStatsRow getTeamGameStats(TeamGameKey key) {
    // System.out.println("LookupTeamGame " + key.toString());
    return table.get(key.toString());
  }

  public TeamGameStatsRow getTeamGameStats(String gameCode, int teamCode) {
    return getTeamGameStats(new TeamGameKey(gameCode, teamCode));
  }
}
