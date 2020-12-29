package com.hjf.tally;

import com.hjf.tally.bean.AccountBean;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test() {
        Date date = new Date();
        AccountBean accountBean = new AccountBean();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String time = dateFormat.format(date);
        accountBean.setTime("2020年08月09日 08:08");

        System.out.println("accountBean = " + accountBean);
    }
}
