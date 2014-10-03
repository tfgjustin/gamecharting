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
public class DriveTable {
    SortedSet<DriveRow> table;
    
    public static DriveTable loadTable(File file) throws IOException{
        DriveTable table = new DriveTable();
        BufferedReader stdin =  new BufferedReader(new FileReader(file));
        String s = stdin.readLine();
        while ((s = stdin.readLine()) != null){
            table.addRow(DriveRow.makeRow(s));
        }
        return table;
    }
    
    public DriveTable(){
        table = new TreeSet();
    }
    
    public void addRow(DriveRow row){
        table.add(row);
    }
}
