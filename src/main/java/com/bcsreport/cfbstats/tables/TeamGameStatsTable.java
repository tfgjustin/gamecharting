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
public class TeamGameStatsTable {
    SortedSet<TeamGameStatsRow> table;
    
    public static TeamGameStatsTable loadTable(File file) throws IOException{
        TeamGameStatsTable table = new TeamGameStatsTable();
        BufferedReader stdin =  new BufferedReader(new FileReader(file));
        String s = stdin.readLine();
        while ((s = stdin.readLine()) != null){
            table.addRow(TeamGameStatsRow.makeRow(s));
        }
        return table;
    }
    
    public TeamGameStatsTable(){
        table = new TreeSet();
    }
    
    public void addRow(TeamGameStatsRow row){
        table.add(row);
    }
}
