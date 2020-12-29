package com.hjf.tally.bean;

/**
 * 记账本的记账数据
 * @author hjf
 * @create 2020-12-24 20:09
 */
public class AccountBean {
    private int id;
    /**
     * 被选中的类型名
     */
    private String typename;
    /**
     * 被选中的类型图片
     */
    private int simageId;
    /**
     * 备注
     */
    private String note;
    /**
     * 金额
     */
    private float money;
    /**
     * 收入：1
     * 支出：0
     * 类型
     */
    private int kind;
    /**
     * 时间
     */
    private String time;
    /**
     * 时间：年
     */
    private int year;
    /**
     * 时间：月
     */
    private int month;
    /**
     * 时间：日
     */
    private int day;
    /**
     * 一天中的详细时间
     * XX:XX
     */
    private String timeOfDay;
    /**
     * 时间：小时
     */
    private int hour;
    /**
     * 时间：分钟
     */
    private int minute;

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public AccountBean() {
    }

    public AccountBean(int id, String typename, int simageId, String note, float money, int kind, String time) {
        this.id = id;
        this.typename = typename;
        this.simageId = simageId;
        this.note = note;
        this.money = money;
        this.kind = kind;
        setTime(time);
    }

    public String getTime() {
        return time;
    }

    public String getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(String timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    /**
     * 设置时间字符串时会把Year、Month、Day分割出来
     * @param time
     */
    public void setTime(String time) {
        this.time = time;
        int yearChar = time.indexOf("年");
        int monthChar = time.indexOf("月");
        int dayChar = time.indexOf("日");
        String year = time.substring(0, yearChar);
        String month = time.substring(yearChar + 1, monthChar);
        String day = time.substring(monthChar + 1, dayChar);
        String timeOfDay = time.substring(dayChar + 2);
        String[] split = timeOfDay.split(":");
        setYear(Integer.parseInt(year));
        setMonth(Integer.parseInt(month));
        setDay(Integer.parseInt(day));
        setTimeOfDay(timeOfDay);
        setHour(Integer.parseInt(split[0]));
        setMinute(Integer.parseInt(split[1]));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public int getSimageId() {
        return simageId;
    }

    public void setSimageId(int simageId) {
        this.simageId = simageId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

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

    @Override
    public String toString() {
        return "AccountBean{" +
                "id=" + id +
                ", typename='" + typename + '\'' +
                ", simageId=" + simageId +
                ", note='" + note + '\'' +
                ", money=" + money +
                ", kind=" + kind +
                ", time='" + time + '\'' +
                ", year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", timeOfDay='" + timeOfDay + '\'' +
                ", hour=" + hour +
                ", minute=" + minute +
                '}';
    }
}
