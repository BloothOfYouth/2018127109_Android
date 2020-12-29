package com.hjf.tally.utils;

import java.math.BigDecimal;

/**
 * @author hjf
 * @create 2020-12-28 18:52
 */
public class FloatUtils {

    /**
     * 进行除法，保留4位小数
     * @param v1
     * @param v2
     * @return
     */
    public static float div(float v1, float v2) {
        float v3 = v1 / v2;
        BigDecimal bigDecimal = new BigDecimal(v3);
        // newScale：保留四位，roundingMode：四舍五入
        float value = bigDecimal.setScale(4, 4).floatValue();
        return value;
    }

    /**
     * 小数转百分数，保留百分比的小数点后两位
     * @param decimal
     * @return
     */
    public static String decimalToPercentage(float decimal) {
        float value = decimal * 100;
        BigDecimal bigDecimal = new BigDecimal(value);
        float number = bigDecimal.setScale(2, 4).floatValue();
        String percentage = "%"+number;
        return percentage;
    }

}
