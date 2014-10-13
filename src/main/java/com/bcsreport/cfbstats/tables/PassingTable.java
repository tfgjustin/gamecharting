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
public class PassingTable {
    SortedMap<PlayKey,PassingRow> table;
    
    public static PassingTable loadTable(File file) throws IOException{
        PassingTable table = new PassingTable();
        BufferedReader stdin =  new BufferedReader(new FileReader(file));
        String s = stdin.readLine();
        while ((s = stdin.readLine()) != null){
            table.addRow(PassingRow.makeRow(s));
        }
        return table;
    }
    
    public PassingTable(){
        table = new TreeMap<>();
    }
    
    public void addRow(PassingRow row){
        table.put(new PlayKey(row.getGameCode(),row.getPlayNum()),row);
    }
    
    public PassingRow getRow(PlayKey key){
        return table.get(key);
    }
}
