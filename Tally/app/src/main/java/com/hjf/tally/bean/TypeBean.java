package com.hjf.tally.bean;

/**
 * 表示收入或者支出具体类型的类
 * @author hjf
 * @create 2020-12-24 16:31
 */
public class TypeBean {
    private Integer id;
    /**
     * 类型名称
     */
    private String typename;
    /**
     * 未被选中图片id
     */
    private Integer imageId;
    /**
     * 被选中图片id
     */
    private Integer simageId;
    /**
     * 收入：1
     * 支出：0
     */
    private Integer kind;
    /**
     * 支出：0
     */
    public final static int KIND_OUTCOME = 0;
    /**
     * 收入：1
     */
    public final static int KIND_INCOME = 1;

    public TypeBean(Integer id, String typename, Integer imageId, Integer simgeId, Integer kind) {
        this.id = id;
        this.typename = typename;
        this.imageId = imageId;
        this.simageId = simgeId;
        this.kind = kind;
    }

    public TypeBean() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public Integer getSimageId() {
        return simageId;
    }

    public void setSimageId(Integer simgeId) {
        this.simageId = simgeId;
    }

    public Integer getKind() {
        return kind;
    }

    public void setKind(Integer kind) {
        this.kind = kind;
    }

    @Override
    public String toString() {
        return "TypeBean{" +
                "id=" + id +
                ", typename='" + typename + '\'' +
                ", imageId=" + imageId +
                ", simgeId=" + simageId +
                ", kind=" + kind +
                '}';
    }
}
