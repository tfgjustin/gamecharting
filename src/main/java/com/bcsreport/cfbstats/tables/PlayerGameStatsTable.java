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
public class PlayerGameStatsTable {
    SortedSet<PlayerGameStatsRow> table;
    
    public static PlayerGameStatsTable loadTable(File file) throws IOException{
        PlayerGameStatsTable table = new PlayerGameStatsTable();
        BufferedReader stdin =  new BufferedReader(new FileReader(file));
        String s = stdin.readLine();
        while ((s = stdin.readLine()) != null){
            table.addRow(PlayerGameStatsRow.makeRow(s));
        }
        return table;
    }
    
    public PlayerGameStatsTable(){
        table = new TreeSet();
    }
    
    public void addRow(PlayerGameStatsRow row){
        table.add(row);
    }
}
