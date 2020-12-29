package com.hjf.tally.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.hjf.tally.R;
import com.hjf.tally.bean.TypeBean;

/**
 * @author hjf
 * @create 2020-12-24 16:36
 */
public class DBOpenHelper extends SQLiteOpenHelper {

    public static final String CREATE_TYPE ="create table type(" +
            "id integer primary key autoincrement," +
            "typename varchar(10)," +
            "imageId integer," +
            "simageId integer," +
            "kind integer)";

    public static final String CREATE_ACCOUNT ="create table account(" +
            "id integer primary key autoincrement," +
            "typename varchar(10)," +
            "simageId integer," +
            "note varchar(100)," +
            "money float," +
            "kind integer," +
            "time varchar(26)," +
            "year integer," +
            "month integer," +
            "day integer," +
            "hour integer," +
            "minute integer)";

    public DBOpenHelper(@Nullable Context context) {
        super(context, "tally.db", null, 1);
    }

    /**
     * 创建数据库的方法，只有项目第一次运行时会被调用
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TYPE);
        sqLiteDatabase.execSQL(CREATE_ACCOUNT);
        insertType(sqLiteDatabase);
    }

    /**
     * 向type表中插入数据
     * @param sqLiteDatabase
     */
    private void insertType(SQLiteDatabase sqLiteDatabase) {
        String sql = "insert into type (typename,imageId,sImageId,kind) values (?,?,?,?)";
        //支出
        sqLiteDatabase.execSQL(sql,new Object[]{"其他", R.mipmap.ic_qita,R.mipmap.ic_qita_fs, TypeBean.KIND_OUTCOME});
        sqLiteDatabase.execSQL(sql,new Object[]{"餐饮", R.mipmap.ic_canyin,R.mipmap.ic_canyin_fs,TypeBean.KIND_OUTCOME});
        sqLiteDatabase.execSQL(sql,new Object[]{"交通", R.mipmap.ic_jiaotong,R.mipmap.ic_jiaotong_fs,TypeBean.KIND_OUTCOME});
        sqLiteDatabase.execSQL(sql,new Object[]{"购物", R.mipmap.ic_gouwu,R.mipmap.ic_gouwu_fs,TypeBean.KIND_OUTCOME});
        sqLiteDatabase.execSQL(sql,new Object[]{"服饰", R.mipmap.ic_fushi,R.mipmap.ic_fushi_fs,TypeBean.KIND_OUTCOME});
        sqLiteDatabase.execSQL(sql,new Object[]{"日用品", R.mipmap.ic_riyongpin,R.mipmap.ic_riyongpin_fs,TypeBean.KIND_OUTCOME});
        sqLiteDatabase.execSQL(sql,new Object[]{"娱乐", R.mipmap.ic_yule,R.mipmap.ic_yule_fs,TypeBean.KIND_OUTCOME});
        sqLiteDatabase.execSQL(sql,new Object[]{"零食", R.mipmap.ic_lingshi,R.mipmap.ic_lingshi_fs,TypeBean.KIND_OUTCOME});
        sqLiteDatabase.execSQL(sql,new Object[]{"烟酒茶", R.mipmap.ic_yanjiu,R.mipmap.ic_yanjiu_fs,TypeBean.KIND_OUTCOME});
        sqLiteDatabase.execSQL(sql,new Object[]{"学习", R.mipmap.ic_xuexi,R.mipmap.ic_xuexi_fs,TypeBean.KIND_OUTCOME});
        sqLiteDatabase.execSQL(sql,new Object[]{"医疗", R.mipmap.ic_yiliao,R.mipmap.ic_yiliao_fs,TypeBean.KIND_OUTCOME});
        sqLiteDatabase.execSQL(sql,new Object[]{"住宅", R.mipmap.ic_zhufang,R.mipmap.ic_zhufang_fs,TypeBean.KIND_OUTCOME});
        sqLiteDatabase.execSQL(sql,new Object[]{"水电煤", R.mipmap.ic_shuidianfei,R.mipmap.ic_shuidianfei_fs,TypeBean.KIND_OUTCOME});
        sqLiteDatabase.execSQL(sql,new Object[]{"通讯", R.mipmap.ic_tongxun,R.mipmap.ic_tongxun_fs,TypeBean.KIND_OUTCOME});
        sqLiteDatabase.execSQL(sql,new Object[]{"人情往来", R.mipmap.ic_renqingwanglai,R.mipmap.ic_renqingwanglai_fs,TypeBean.KIND_OUTCOME});

        //收入
        sqLiteDatabase.execSQL(sql,new Object[]{"其他", R.mipmap.in_qt,R.mipmap.in_qt_fs,TypeBean.KIND_INCOME});
        sqLiteDatabase.execSQL(sql,new Object[]{"薪资", R.mipmap.in_xinzi,R.mipmap.in_xinzi_fs,TypeBean.KIND_INCOME});
        sqLiteDatabase.execSQL(sql,new Object[]{"奖金", R.mipmap.in_jiangjin,R.mipmap.in_jiangjin_fs,TypeBean.KIND_INCOME});
        sqLiteDatabase.execSQL(sql,new Object[]{"借入", R.mipmap.in_jieru,R.mipmap.in_jieru_fs,TypeBean.KIND_INCOME});
        sqLiteDatabase.execSQL(sql,new Object[]{"收债", R.mipmap.in_shouzhai,R.mipmap.in_shouzhai_fs,TypeBean.KIND_INCOME});
        sqLiteDatabase.execSQL(sql,new Object[]{"利息收入", R.mipmap.in_lixifuji,R.mipmap.in_lixifuji_fs,TypeBean.KIND_INCOME});
        sqLiteDatabase.execSQL(sql,new Object[]{"投资回报", R.mipmap.in_touzi,R.mipmap.in_touzi_fs,TypeBean.KIND_INCOME});
        sqLiteDatabase.execSQL(sql,new Object[]{"二手交易", R.mipmap.in_ershoushebei,R.mipmap.in_ershoushebei_fs,TypeBean.KIND_INCOME});
        sqLiteDatabase.execSQL(sql,new Object[]{"意外所得", R.mipmap.in_yiwai,R.mipmap.in_yiwai_fs,TypeBean.KIND_INCOME});
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
