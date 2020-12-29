package com.hjf.tally.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hjf.tally.bean.AccountBean;
import com.hjf.tally.bean.BarChartItemBean;
import com.hjf.tally.bean.ChartLvItemBean;
import com.hjf.tally.bean.TypeBean;
import com.hjf.tally.utils.FloatUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 负责管理数据库的类
 * 主要对于表中的内容进行操作，增删改查
 * @author hjf
 * @create 2020-12-24 17:13
 */
public class DBManager {

    private static SQLiteDatabase sqLiteDatabase;

    /**
     * 初始化数据库对象
     * @param context
     */
    public static void initDataBase(Context context) {
        DBOpenHelper dbOpenHelper = new DBOpenHelper(context);  //得到帮助类对象
        sqLiteDatabase = dbOpenHelper.getWritableDatabase();    //得到数据库对象
    }


    /**
     * 读取数据库当中的type数据，写入集合中
     * @param kind：表示收入或者支出
     * @return
     */
    public static List<TypeBean> getTypeList(Integer kind) {
        ArrayList<TypeBean> typeList = new ArrayList<>();
        //读取type表中的数据
        Cursor cursor = sqLiteDatabase.rawQuery("select * from type where kind = ?", new String[]{String.valueOf(kind)});
        while (cursor.moveToNext()) {
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            int imageId = cursor.getInt(cursor.getColumnIndex("imageId"));
            int simageId = cursor.getInt(cursor.getColumnIndex("simageId"));
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            TypeBean typeBean = new TypeBean(id, typename, imageId, simageId, kind);
            typeList.add(typeBean);
        }
        return typeList;
    }

    /**
     * 向记账表当中插入一条记账记录
     * @param accountBean
     */
    public static void insertToAccount(AccountBean accountBean) {
        String sql = "insert into account(typename, simageId, note, money, kind, time, year, month, day, hour, minute) " +
                "values(?,?,?,?,?,?,?,?,?,?,?)";
        String typename = accountBean.getTypename();
        String simageId = String.valueOf(accountBean.getSimageId());
        String note = accountBean.getNote();
        String money = String.valueOf(accountBean.getMoney());
        String kind = String.valueOf(accountBean.getKind());
        String time = accountBean.getTime();
        String year = String.valueOf(accountBean.getYear());
        String month = String.valueOf(accountBean.getMonth());
        String day = String.valueOf(accountBean.getDay());
        String hour = String.valueOf(accountBean.getHour());
        String minute = String.valueOf(accountBean.getMinute());
        sqLiteDatabase.execSQL(sql, new String[] {typename, simageId, note, money, kind, time, year, month, day, hour, minute});
    }

    /**
     * 根据时间（xxxx年xx月xx日）读取数据库当中的account数据，写入集合中
     * 按照当天时间的降序排序
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static List<AccountBean> getAccountByTime(int year, int month, int day) {
        ArrayList<AccountBean> accountList = new ArrayList<>();
        //读取account表中的数据
        Cursor cursor = sqLiteDatabase.rawQuery("select * from account where year = ? and month = ? and day = ? " +
                        "order by hour desc, minute desc",
                new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(day)});
        while (cursor.moveToNext()) {
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            int simageId = cursor.getInt(cursor.getColumnIndex("simageId"));
            String note = cursor.getString(cursor.getColumnIndex("note"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            AccountBean accountBean = new AccountBean(id, typename, simageId, note, money, kind, time);
            accountList.add(accountBean);
        }
        return accountList;
    }

    /**
     * 根据月份读取数据库当中的account数据，写入集合中
     * 按照时间的降序排序
     * @param year
     * @param month
     * @return
     */
    public static List<AccountBean> getAccountByTime(int year, int month) {
        ArrayList<AccountBean> accountList = new ArrayList<>();
        //读取account表中的数据
        Cursor cursor = sqLiteDatabase.rawQuery("select * from account where year = ? and month = ? " +
                        "order by year desc, month desc, day desc, hour desc, minute desc",
                new String[]{String.valueOf(year), String.valueOf(month)});
        while (cursor.moveToNext()) {
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            int simageId = cursor.getInt(cursor.getColumnIndex("simageId"));
            String note = cursor.getString(cursor.getColumnIndex("note"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            AccountBean accountBean = new AccountBean(id, typename, simageId, note, money, kind, time);
            accountList.add(accountBean);
        }
        return accountList;
    }

    /**
     * 统计一个月中收入或支出的总金额
     * @param year
     * @param month
     * @param kind
     * @return
     */
    public static float getSumMoneyByOneMonth(int year, int month, int kind) {
        float sumMoney = 0.0f;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT sum(money) as sumMoney FROM account where year = ? " +
                        "and month = ? and kind = ?",
                new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(kind)});
        while (cursor.moveToNext()) {
            sumMoney = cursor.getFloat(cursor.getColumnIndex("sumMoney"));
        }
        return sumMoney;
    }

    /**
     * 统计一天中收入或支出的总金额
     * @param year
     * @param month
     * @param kind
     * @return
     */
    public static float getSumMoneyByOneDay(int year, int month, int day, int kind) {
        float sumMoney = 0.0f;
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT sum(money) as sumMoney FROM account where year = ? " +
                        "and month = ? and day = ? and kind = ?",
                new String[]{String.valueOf(year), String.valueOf(month), String.valueOf(day), String.valueOf(kind)});
        while (cursor.moveToNext()) {
            sumMoney = cursor.getFloat(cursor.getColumnIndex("sumMoney"));
        }
        return sumMoney;
    }

    /**
     * 根据id删除Account
     * @param click_id
     */
    public static void deleteOneAccountById(int click_id) {
        sqLiteDatabase.execSQL("delete from account where id = ?", new Integer[] {click_id});
    }

    /**
     * 通过note搜索Account
     * 按时间降序排序
     * @param searchNote
     * @return
     */
    public static List<AccountBean> getAccountByNote(String searchNote) {
        ArrayList<AccountBean> accountList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM account where note like ? " +
                "order by year desc, month desc, day desc, hour desc, minute desc", new String[]{"%" + searchNote + "%"});
        while (cursor.moveToNext()) {
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            int simageId = cursor.getInt(cursor.getColumnIndex("simageId"));
            String note = cursor.getString(cursor.getColumnIndex("note"));
            float money = cursor.getFloat(cursor.getColumnIndex("money"));
            int kind = cursor.getInt(cursor.getColumnIndex("kind"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            AccountBean accountBean = new AccountBean(id, typename, simageId, note, money, kind, time);
            accountList.add(accountBean);
        }
        return accountList;
    }

    /**
     * 查询年份的集合
     * @return
     */
    public static List<Integer> getYearList() {
        List<Integer> yearList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT distinct year FROM account order by year ", null);
        while (cursor.moveToNext()) {
            int year = cursor.getInt(cursor.getColumnIndex("year"));
            yearList.add(year);
        }
        return yearList;
    }

    /**
     * 根据年份查询有的月份的集合
     * @param year
     * @return
     */
    public static List<Integer> getMonthList(int year) {
        List<Integer> monthList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT distinct month FROM account where year = ? order by month",
                new String[]{year + ""});
        while (cursor.moveToNext()) {
            int month = cursor.getInt(cursor.getColumnIndex("month"));
            monthList.add(month);
        }
        return monthList;
    }

    /**
     * 删除所有Account
     */
    public static void deleteAllAccount() {
        sqLiteDatabase.execSQL("delete from account");
    }

    /**
     * 统计某月份支出或收入情况有多少条
     * @param year
     * @param month
     * @param kind
     * @return
     */
    public static int getCountItemOneMonth(int year, int month, int kind) {
        int count = 0;
        Cursor cursor = sqLiteDatabase.rawQuery("select count(money) as count from account where year = ? and month = ? and kind = ?",
                new String[]{year + "", month + "", kind + ""});
        if (cursor.moveToNext()) {
            count = cursor.getInt(cursor.getColumnIndex("count"));
        }
        return count;
    }

    /**
     * 得到关于ChartLvItem的集合
     * @param year
     * @param month
     * @param kind
     * @return
     */
    public static List<ChartLvItemBean> getChartLvItemList(int year, int month, int kind) {
        List<ChartLvItemBean> chartLvItemList = new ArrayList<>();
        float sumMoneyByOneMonth = getSumMoneyByOneMonth(year, month, kind);
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT typename, simageId, sum(money) as totalMoney FROM account " +
                        "where year = ? and month = ? and kind = ? group by typename order by totalMoney desc",
                new String[]{year + "", month + "", kind + ""});
        while (cursor.moveToNext()) {
            int sImageId = cursor.getInt(cursor.getColumnIndex("simageId"));
            String typename = cursor.getString(cursor.getColumnIndex("typename"));
            float totalMoney = cursor.getFloat(cursor.getColumnIndex("totalMoney"));
            float ratio = FloatUtils.div(totalMoney, sumMoneyByOneMonth);
            ChartLvItemBean chartLvItemBean = new ChartLvItemBean(sImageId, typename, ratio, totalMoney);
            chartLvItemList.add(chartLvItemBean);
        }
        return chartLvItemList;
    }

    /**
     * 获取这个月当中某一天收入支出最大的金额，金额是多少
     * @param year
     * @param month
     * @param kind
     * @return
     */
    public static float getMaxMoneyOneDayInMonth(int year, int month, int kind) {
        float money = 0;
        Cursor cursor = sqLiteDatabase.rawQuery("select sum(money) as sumMoney from account " +
                "where year = ? and month = ? and kind = ? " +
                "group by day order by sumMoney desc", new String[]{year + "", month + "", kind + ""});
        if (cursor.moveToFirst()) {
            money = cursor.getFloat(cursor.getColumnIndex("sumMoney"));
        }
        return money;
    }

    /**
     * 根据指定月份每一日收入或者支出的总钱数的集合
     * @param year
     * @param month
     * @param kind
     * @return
     */
    public static List<BarChartItemBean> getSumMoneyOneDayInMonth(int year, int month, int kind) {
        List<BarChartItemBean> barChartItemList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("select day, sum(money) as SumMoney from account " +
                        "where year = ? and month = ? and kind = ? group by day",
                new String[]{year + "", month + "", kind + ""});
        while (cursor.moveToNext()) {
            int day = cursor.getInt(cursor.getColumnIndex("day"));
            float sunMoney = cursor.getFloat(cursor.getColumnIndex("SumMoney"));
            BarChartItemBean itemBean = new BarChartItemBean(year, month, day, sunMoney);
            barChartItemList.add(itemBean);
        }
        return barChartItemList;
    }
}
