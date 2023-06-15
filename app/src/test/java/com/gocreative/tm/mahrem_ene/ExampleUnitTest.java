package com.gocreative.tm.mahrem_ene;

import org.junit.Test;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        assertEquals("06-04-2022", simpleDateFormat.format(date));
    }
}