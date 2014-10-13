/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.bcsreport.cfbstats.tables;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 
 * @author ryan.hoes
 */
public class GameTable {
  SortedMap<String, GameRow> table;

  public static GameTable loadTable(File file) throws IOException {
    GameTable table = new GameTable();
    BufferedReader stdin = new BufferedReader(new FileReader(file));
    String s = stdin.readLine();
    while ((s = stdin.readLine()) != null) {
      GameRow row = GameRow.makeRow(s);
      table.addRow(row.getGameCode(), row);
    }
    return table;
  }

  public GameTable() {
    table = new TreeMap<String, GameRow>();
  }

  public void addRow(GameRow row) {
    table.put(row.getGameCode(), row);
  }

  public void addRow(String key, GameRow row) {
    table.put(key, row);
  }

  public List<GameRow> getGameList() {
    return new ArrayList<GameRow>(table.values());
  }

  public boolean hasGame(String id) {
    return getGame(id) != null;
  }

  public GameRow getGame(String id) {
    return table.get(id);
  }

  public Set<String> getMatchingGames(String targetGameId) {
    Set<String> gameIds = new HashSet<String>();
    for (GameRow gameRow : getGameList()) {
      String thisGameId = gameRow.getGameCode();
      if ((targetGameId != null) && !targetGameId.equals(thisGameId)) {
        // A specific game ID was provided, but this isn't it.
        continue;
      }
      gameIds.add(thisGameId);
    }
    return gameIds;
  }
}
