package com.hjf.tally.bean;

/**
 * @author hjf
 * @create 2020-12-28 18:28
 */
public class ChartLvItemBean {
    /**
     * 该类型被选中的图片样式
     */
    private int sImageId;
    /**
     * 该类型名字
     */
    private String typename;
    /**
     * 该类型所占的百分比
     */
    private float ratio;
    /**
     * 该类型的总金额
     */
    private float totalMoney;

    public ChartLvItemBean() {
    }

    public ChartLvItemBean(int sImageId, String typename, float ratio, float totalMoney) {
        this.sImageId = sImageId;
        this.typename = typename;
        this.ratio = ratio;
        this.totalMoney = totalMoney;
    }

    public int getsImageId() {
        return sImageId;
    }

    public void setsImageId(int sImageId) {
        this.sImageId = sImageId;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public float getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(float totalMoney) {
        this.totalMoney = totalMoney;
    }

    @Override
    public String toString() {
        return "ChartLvItemBean{" +
                "sImageId=" + sImageId +
                ", typename='" + typename + '\'' +
                ", ratio=" + ratio +
                ", totalMoney=" + totalMoney +
                '}';
    }
}
