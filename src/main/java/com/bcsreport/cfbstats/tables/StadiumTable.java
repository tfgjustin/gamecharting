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
public class StadiumTable {
    SortedSet<StadiumRow> table;
    
    public static StadiumTable loadTable(File file) throws IOException{
        StadiumTable table = new StadiumTable();
        BufferedReader stdin =  new BufferedReader(new FileReader(file));
        String s = stdin.readLine();
        while ((s = stdin.readLine()) != null){
            table.addRow(StadiumRow.makeRow(s));
        }
        return table;
    }
    
    public StadiumTable(){
        table = new TreeSet();
    }
    
    public void addRow(StadiumRow row){
        table.add(row);
    }
}
