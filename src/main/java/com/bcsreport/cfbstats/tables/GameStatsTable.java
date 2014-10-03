/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bcsreport.cfbstats.tables;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * @author ryan.hoes
 */
public class GameStatsTable {
    SortedSet<GameStatsRow> table;
    
    public static GameStatsTable loadTable(File file) throws IOException{
        GameStatsTable table = new GameStatsTable();
        BufferedReader stdin =  new BufferedReader(new FileReader(file));
        String s = stdin.readLine();
        while ((s = stdin.readLine()) != null){
            table.addRow(GameStatsRow.makeRow(s));
        }
        return table;
    }
    
    public GameStatsTable(){
        table = new TreeSet();
    }
    
    public void addRow(GameStatsRow row){
        table.add(row);
    }
}
