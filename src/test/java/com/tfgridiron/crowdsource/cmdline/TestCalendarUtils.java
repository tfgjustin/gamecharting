/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tfgridiron.crowdsource.cmdline;

import java.text.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author ryan.hoes
 */
public class TestCalendarUtils {
    @Test
    public void testSeasonWeek() throws ParseException{
        // 2013 season
        assertEquals(CalendarUtils.gameDateToWeek("20130822"),"PreSeason");
        assertEquals(CalendarUtils.gameDateToWeek("20130823"),"PreSeason");
        assertEquals(CalendarUtils.gameDateToWeek("20130824"),"PreSeason");
        assertEquals(CalendarUtils.gameDateToWeek("20130825"),"PreSeason");
        assertEquals(CalendarUtils.gameDateToWeek("20130826"),"PreSeason");
        assertEquals(CalendarUtils.gameDateToWeek("20130827"),"Week 01");
        assertEquals(CalendarUtils.gameDateToWeek("20130828"),"Week 01");
        assertEquals(CalendarUtils.gameDateToWeek("20130829"),"Week 01");
        assertEquals(CalendarUtils.gameDateToWeek("20130830"),"Week 01");
        assertEquals(CalendarUtils.gameDateToWeek("20130831"),"Week 01");
        assertEquals(CalendarUtils.gameDateToWeek("20130901"),"Week 01");
        assertEquals(CalendarUtils.gameDateToWeek("20130902"),"Week 01");
        assertEquals(CalendarUtils.gameDateToWeek("20130903"),"Week 02");
        assertEquals(CalendarUtils.gameDateToWeek("20130904"),"Week 02");
        
        // 2014 season
        assertEquals(CalendarUtils.gameDateToWeek("20140822"),"PreSeason");
        assertEquals(CalendarUtils.gameDateToWeek("20140823"),"PreSeason");
        assertEquals(CalendarUtils.gameDateToWeek("20140824"),"PreSeason");
        assertEquals(CalendarUtils.gameDateToWeek("20140825"),"PreSeason");
        assertEquals(CalendarUtils.gameDateToWeek("20140826"),"Week 01");
        assertEquals(CalendarUtils.gameDateToWeek("20140827"),"Week 01");
        assertEquals(CalendarUtils.gameDateToWeek("20140828"),"Week 01");
        assertEquals(CalendarUtils.gameDateToWeek("20140829"),"Week 01");
        assertEquals(CalendarUtils.gameDateToWeek("20140830"),"Week 01");
        assertEquals(CalendarUtils.gameDateToWeek("20140831"),"Week 01");
        assertEquals(CalendarUtils.gameDateToWeek("20140901"),"Week 01");
        assertEquals(CalendarUtils.gameDateToWeek("20140902"),"Week 02");
        assertEquals(CalendarUtils.gameDateToWeek("20140903"),"Week 02");
        assertEquals(CalendarUtils.gameDateToWeek("20140904"),"Week 02");
        assertEquals(CalendarUtils.gameDateToWeek("20140905"),"Week 02");
        assertEquals(CalendarUtils.gameDateToWeek("20140906"),"Week 02");
        assertEquals(CalendarUtils.gameDateToWeek("20140920"),"Week 04");
        assertEquals(CalendarUtils.gameDateToWeek("20140922"),"Week 04");
        assertEquals(CalendarUtils.gameDateToWeek("20141206"),"Week 15");
        assertEquals(CalendarUtils.gameDateToWeek("20141213"),"Week 16");
        assertEquals(CalendarUtils.gameDateToWeek("20141214"),"Week 16");
        assertEquals(CalendarUtils.gameDateToWeek("20141215"),"Bowls");
        assertEquals(CalendarUtils.gameDateToWeek("20141216"),"Bowls");
        assertEquals(CalendarUtils.gameDateToWeek("20141217"),"Bowls");
        assertEquals(CalendarUtils.gameDateToWeek("20141218"),"Bowls");
        assertEquals(CalendarUtils.gameDateToWeek("20141219"),"Bowls");
        assertEquals(CalendarUtils.gameDateToWeek("20141220"),"Bowls");
        assertEquals(CalendarUtils.gameDateToWeek("20141227"),"Bowls");
        assertEquals(CalendarUtils.gameDateToWeek("20150103"),"Bowls");
        assertEquals(CalendarUtils.gameDateToWeek("20150112"),"Bowls");
        assertEquals(CalendarUtils.gameDateToWeek("20150201"),"PreSeason");
        
        //2015
        assertEquals(CalendarUtils.gameDateToWeek("20150903"),"Week 01");
        assertEquals(CalendarUtils.gameDateToWeek("20150904"),"Week 01");
        assertEquals(CalendarUtils.gameDateToWeek("20150905"),"Week 01");
        assertEquals(CalendarUtils.gameDateToWeek("20150906"),"Week 01");
        assertEquals(CalendarUtils.gameDateToWeek("20150907"),"Week 01");
        assertEquals(CalendarUtils.gameDateToWeek("20150912"),"Week 02");
        assertEquals(CalendarUtils.gameDateToWeek("20150917"),"Week 03");
        assertEquals(CalendarUtils.gameDateToWeek("20150918"),"Week 03");
        assertEquals(CalendarUtils.gameDateToWeek("20150919"),"Week 03");
        assertEquals(CalendarUtils.gameDateToWeek("20150924"),"Week 04");
        assertEquals(CalendarUtils.gameDateToWeek("20150925"),"Week 04");
        assertEquals(CalendarUtils.gameDateToWeek("20150926"),"Week 04");
        assertEquals(CalendarUtils.gameDateToWeek("20151001"),"Week 05");
        assertEquals(CalendarUtils.gameDateToWeek("20151002"),"Week 05");
        assertEquals(CalendarUtils.gameDateToWeek("20151003"),"Week 05");
        assertEquals(CalendarUtils.gameDateToWeek("20151008"),"Week 06");
        assertEquals(CalendarUtils.gameDateToWeek("20151009"),"Week 06");
        assertEquals(CalendarUtils.gameDateToWeek("20151010"),"Week 06");
        assertEquals(CalendarUtils.gameDateToWeek("20151015"),"Week 07");
        assertEquals(CalendarUtils.gameDateToWeek("20151016"),"Week 07");
        assertEquals(CalendarUtils.gameDateToWeek("20151017"),"Week 07");
        assertEquals(CalendarUtils.gameDateToWeek("20151022"),"Week 08");
        assertEquals(CalendarUtils.gameDateToWeek("20151023"),"Week 08");
        assertEquals(CalendarUtils.gameDateToWeek("20151024"),"Week 08");
        assertEquals(CalendarUtils.gameDateToWeek("20151029"),"Week 09");
        assertEquals(CalendarUtils.gameDateToWeek("20151030"),"Week 09");
        assertEquals(CalendarUtils.gameDateToWeek("20151031"),"Week 09");
        assertEquals(CalendarUtils.gameDateToWeek("20151105"),"Week 10");
        assertEquals(CalendarUtils.gameDateToWeek("20151106"),"Week 10");
        assertEquals(CalendarUtils.gameDateToWeek("20151107"),"Week 10");
        assertEquals(CalendarUtils.gameDateToWeek("20151112"),"Week 11");
        assertEquals(CalendarUtils.gameDateToWeek("20151113"),"Week 11");
        assertEquals(CalendarUtils.gameDateToWeek("20151114"),"Week 11");
        assertEquals(CalendarUtils.gameDateToWeek("20151119"),"Week 12");
        assertEquals(CalendarUtils.gameDateToWeek("20151120"),"Week 12");
        assertEquals(CalendarUtils.gameDateToWeek("20151121"),"Week 12");
        assertEquals(CalendarUtils.gameDateToWeek("20151126"),"Week 13");
        assertEquals(CalendarUtils.gameDateToWeek("20151127"),"Week 13");
        assertEquals(CalendarUtils.gameDateToWeek("20151128"),"Week 13");
        assertEquals(CalendarUtils.gameDateToWeek("20151205"),"Week 14");
        assertEquals(CalendarUtils.gameDateToWeek("20151212"),"Week 15");
        
        //2016
        assertEquals(CalendarUtils.gameDateToWeek("20160903"),"Week 01");
        
        //2017
        assertEquals(CalendarUtils.gameDateToWeek("20170902"),"Week 01");
        
        //2018
        assertEquals(CalendarUtils.gameDateToWeek("20180901"),"Week 01");
        
        
        try{
            CalendarUtils.gameDateToWeek("2015010");
            fail("Expected ParseException from 2015010");
        }
        catch (ParseException ex){
            
        }
        try{
            CalendarUtils.gameDateToWeek("2015/01/01");
            fail("Expected ParseException from 2015/01/01");
        }
        catch (ParseException ex){
            
        }
        try{
            CalendarUtils.gameDateToWeek("2015010100");
            fail("Expected ParseException from 2015010100");
        }
        catch (ParseException ex){
            
        }
        try{
            CalendarUtils.gameDateToWeek(null);
            fail("Expected ParseException from null");
        }
        catch (ParseException ex){
            
        }
    }
    
    @Test
    public void testValidDateFormat(){
        assertFalse(CalendarUtils.isValidGameDate("2015010"));
        assertFalse(CalendarUtils.isValidGameDate("2015/01/01"));
        assertFalse(CalendarUtils.isValidGameDate("2015010100"));
        assertFalse(CalendarUtils.isValidGameDate(null));
        assertTrue(CalendarUtils.isValidGameDate("20150101"));
    }

    
    @Test
    public void testSeasonYear() throws ParseException{
        assertEquals(CalendarUtils.gameDateToSeason("20130922"),"2013");
        assertEquals(CalendarUtils.gameDateToSeason("20140922"),"2014");
        assertEquals(CalendarUtils.gameDateToSeason("20140201"),"2014");
        assertEquals(CalendarUtils.gameDateToSeason("20140131"),"2013");
        assertEquals(CalendarUtils.gameDateToSeason("20120131"),"2011");
        assertEquals(CalendarUtils.gameDateToSeason("20121201"),"2012");
        try{
            CalendarUtils.gameDateToSeason("2015010");
            fail("Expected ParseException from 2015010");
        }
        catch (ParseException ex){
            
        }
        try{
            CalendarUtils.gameDateToSeason("2015/01/01");
            fail("Expected ParseException from 2015/01/01");
        }
        catch (ParseException ex){
            
        }
        try{
            CalendarUtils.gameDateToSeason("2015010100");
            fail("Expected ParseException from 2015010100");
        }
        catch (ParseException ex){
            
        }
        try{
            CalendarUtils.gameDateToSeason(null);
            fail("Expected ParseException from null");
        }
        catch (ParseException ex){
            
        }
    }
    
    @Ignore
    public void testFutureDate() throws ParseException{
        assertFalse(CalendarUtils.isFutureGame("20140101"));
        assertFalse(CalendarUtils.isFutureGame("20150301"));
        assertTrue(CalendarUtils.isFutureGame("20150302"));
        try{
            CalendarUtils.isFutureGame("2015010");
            fail("Expected ParseException from 2015010");
        }
        catch (ParseException ex){
            
        }
        try{
            CalendarUtils.isFutureGame("2015/01/01");
            fail("Expected ParseException from 2015/01/01");
        }
        catch (ParseException ex){
            
        }
        try{
            CalendarUtils.isFutureGame("2015010100");
            fail("Expected ParseException from 2015010100");
        }
        catch (ParseException ex){
            
        }
        try{
            CalendarUtils.isFutureGame(null);
            fail("Expected ParseException from null");
        }
        catch (ParseException ex){
            
        }
    
    }
}
