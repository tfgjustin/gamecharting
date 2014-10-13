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
public class PlayerTable {
    SortedMap<Integer,PlayerRow> table;
    
    public static PlayerTable loadTable(File file) throws IOException{
        return loadTable(file,"[\t,]");
    }
    
    public static PlayerTable loadTable(File file, String delim) throws IOException{
        PlayerTable table = new PlayerTable();
        BufferedReader stdin =  new BufferedReader(new FileReader(file));
        String s = stdin.readLine();
        while ((s = stdin.readLine()) != null){
            PlayerRow row = PlayerRow.makeRow(s, delim);
            table.addRow(row.getPlayerCode(),row);
        }
        return table;
    }
    
    public PlayerTable(){
        table = new TreeMap<>();
    }
    
    public void addRow(Integer key, PlayerRow row){
        table.put(key, row);
    }
    
    public String getPlayerName(Integer key){
        PlayerRow row = table.get(key);
        if (row != null){
            String tmp = row.getLastName()+", "+row.getFirstName();
            return "\""+tmp.replaceAll("\"","")+"\"";
        }
        else{
            return "";
        }
    }
}
