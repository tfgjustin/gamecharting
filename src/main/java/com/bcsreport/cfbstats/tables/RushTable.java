/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
public class RushTable {
    SortedMap<PlayKey,RushRow> table;
    
    public static RushTable loadTable(File file) throws IOException{
        RushTable table = new RushTable();
        BufferedReader stdin =  new BufferedReader(new FileReader(file));
        String s = stdin.readLine();
        while ((s = stdin.readLine()) != null){
            table.addRow(RushRow.makeRow(s));
        }
        return table;
    }
    
    public RushTable(){
        table = new TreeMap<>();
    }
    
    public void addRow(RushRow row){
        table.put(new PlayKey(row.getGameCode(),row.getPlayNum()),row);
    }
    
    public RushRow getRow(PlayKey key){
        return table.get(key);
    }
}
