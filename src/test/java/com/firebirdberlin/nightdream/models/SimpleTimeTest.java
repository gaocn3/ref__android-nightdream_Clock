package com.firebirdberlin.nightdream.models;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class SimpleTimeTest extends TestCase {

    private Calendar getReference(int hour, int minute) {
        Calendar reference = Calendar.getInstance();
        reference.set(Calendar.YEAR, 2017);
        reference.set(Calendar.MONTH, Calendar.OCTOBER);
        reference.set(Calendar.DAY_OF_MONTH, 6);
        reference.set(Calendar.HOUR_OF_DAY, hour);
        reference.set(Calendar.MINUTE, minute);
        reference.set(Calendar.SECOND, 33);
        reference.set(Calendar.MILLISECOND, 20);
        return reference;
    }

    public void testGetNextAlarmTime() throws Exception {
        SimpleTime time = new SimpleTime(13, 25);
        Calendar reference = getReference(12, 00);
        Calendar next = time.getNextAlarmTime(reference);
        assertEquals(1507289100000L, next.getTimeInMillis());
    }

    public void testGetNextAlarmTimeTomorrow() throws Exception {
        SimpleTime time = new SimpleTime(13, 25);
        Calendar reference = getReference(14, 00);
        Calendar next = time.getNextAlarmTime(reference);
        assertEquals(1507375500000L, next.getTimeInMillis());
    }

    public void testGetNextAlarmTimeRecurring() throws Exception {
        Calendar reference = getReference(12, 00);
        SimpleTime time = new SimpleTime(13, 25, SimpleTime.TUESDAY | SimpleTime.WEDNESDAY);

        Calendar next = time.getNextAlarmTime(reference);
        assertEquals(1507634700000L, next.getTimeInMillis());
    }

    public void testGetNextAlarmTimeRecurring2() throws Exception {
        Calendar reference = getReference(12, 00);
        SimpleTime time = new SimpleTime(13, 25, SimpleTime.TUESDAY | SimpleTime.SATURDAY);

        Calendar next = time.getNextAlarmTime(reference);
        assertEquals(1507375500000L, next.getTimeInMillis());
    }

    public void testGetNextAlarmTimeRecurringToday() throws Exception {
        Calendar reference = getReference(12, 00);
        SimpleTime time = new SimpleTime(13, 25, SimpleTime.FRIDAY | SimpleTime.SATURDAY);

        Calendar next = time.getNextAlarmTime(reference);
        assertEquals(1507289100000L, next.getTimeInMillis());
    }

    public void testGetNextAlarmTimeRecurringSameWeekDay() throws Exception {
        Calendar reference = getReference(14, 00);
        SimpleTime time = new SimpleTime(13, 25, SimpleTime.FRIDAY);

        Calendar next = time.getNextAlarmTime(reference);
        assertEquals(1507893900000L, next.getTimeInMillis());
    }

    public void testGetNextAlarmTimeFromList() throws Exception {
        List<SimpleTime> times = Arrays.asList(
                new SimpleTime(10, 20, SimpleTime.TUESDAY | SimpleTime.FRIDAY),
                new SimpleTime(13, 25, SimpleTime.MONDAY | SimpleTime.WEDNESDAY)
        );

        Calendar reference = getReference(14, 00);
        long result = SimpleTime.getNextAlarmTime(times, reference);
        assertEquals(1507548300000L, result);

        Calendar reference2 = getReference(10, 00);
        long result2 = SimpleTime.getNextAlarmTime(times, reference2);
        assertEquals(1507278000000L, result2);
    }

    public void testGetNextAlarmTimeFromEmptyList() throws Exception {
        Calendar reference = getReference(14, 00);
        List<SimpleTime> times = Arrays.asList();

        long result = SimpleTime.getNextAlarmTime(times, reference);
        assertEquals(-1L, result);
    }
}
