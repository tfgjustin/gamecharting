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
