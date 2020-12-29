package com.hjf.tally.bean;

/**
 * 用于描述绘制柱状图时，每一个柱子表示的对象
 * @author hjf
 * @create 2020-12-29 0:58
 */
public class BarChartItemBean {
    /**
     * 年份
     */
    private int year;
    /**
     * 月份
     */
    private int month;
    /**
     * 这个月的的第几号
     */
    private int day;
    /**
     * 这天的总钱数
     */
    private float sumMoney;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public float getSumMoney() {
        return sumMoney;
    }

    public void setSumMoney(float sumMoney) {
        this.sumMoney = sumMoney;
    }

    public BarChartItemBean() {
    }

    public BarChartItemBean(int year, int month, int day, float sumMoney) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.sumMoney = sumMoney;
    }

    @Override
    public String toString() {
        return "BarChartItemBean{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", sumMoney=" + sumMoney +
                '}';
    }
}
