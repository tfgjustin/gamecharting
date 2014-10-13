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
public class ConferenceTable {
    SortedSet<ConferenceRow> table;
    
    public static ConferenceTable loadTable(File file) throws IOException{
        ConferenceTable table = new ConferenceTable();
        BufferedReader stdin =  new BufferedReader(new FileReader(file));
        String s = stdin.readLine();
        while ((s = stdin.readLine()) != null){
            table.addRow(ConferenceRow.makeRow(s));
        }
        return table;
    }
    
    
    
    
    public ConferenceTable(){
        table = new TreeSet();
    }
    
    public void addRow(ConferenceRow row){
        table.add(row);
    }
}
