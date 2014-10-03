/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.bcsreport.cfbstats.tables;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * 
 * @author ryan.hoes
 */
public class PlayTable {
  SortedMap<PlayKey, PlayRow> table;

  public static PlayTable loadTable(File file) throws IOException {
    PlayTable table = new PlayTable();
    BufferedReader stdin = new BufferedReader(new FileReader(file));
    String s = stdin.readLine();
    while ((s = stdin.readLine()) != null) {
      table.addRow(PlayRow.makeRow(s));
    }
    return table;
  }

  private static class PlayOrder implements Comparator<PlayRow> {

    @Override
    public int compare(PlayRow o1, PlayRow o2) {
      return o1.getPlayNum() - o2.getPlayNum();
    }
  }

  public PlayTable() {
    table = new TreeMap<PlayKey, PlayRow>();
  }

  public void addRow(PlayRow row) {
    PlayKey key = new PlayKey(row.getGameCode(), row.getPlayNum());
    table.put(key, row);
  }

  public PlayRow getPlay(String gameCode, int playNum) {
    return getPlay(new PlayKey(gameCode, playNum));
  }

  public PlayRow getPlay(PlayKey key) {
    return table.get(key);
  }

  public List<PlayRow> getPlays(String gameCode) {
    SortedSet<PlayRow> playSet = new TreeSet<PlayRow>(new PlayOrder());
    for (PlayRow row : table.values()) {
      if (row.getGameCode().equals(gameCode)) {
        playSet.add(row);
      }
    }

    return new ArrayList<PlayRow>(playSet);
  }
}
