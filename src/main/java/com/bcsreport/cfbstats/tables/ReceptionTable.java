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
public class ReceptionTable {
    SortedSet<ReceptionRow> table;
    
    public static ReceptionTable loadTable(File file) throws IOException{
        ReceptionTable table = new ReceptionTable();
        BufferedReader stdin =  new BufferedReader(new FileReader(file));
        String s = stdin.readLine();
        while ((s = stdin.readLine()) != null){
            table.addRow(ReceptionRow.makeRow(s));
        }
        return table;
    }
    
    public ReceptionTable(){
        table = new TreeSet();
    }
    
    public void addRow(ReceptionRow row){
        table.add(row);
    }
}
