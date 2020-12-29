package com.hjf.tally;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hjf.tally.adapter.AccountAdapter;
import com.hjf.tally.bean.AccountBean;
import com.hjf.tally.bean.TypeBean;
import com.hjf.tally.db.DBManager;
import com.hjf.tally.utils.BudgetDialog;
import com.hjf.tally.utils.MoreDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 展示今日收支情况的ListView
     */
    private ListView todayLv;

    private List<AccountBean> accountList  = new ArrayList<>();

    private  AccountAdapter accountAdapter;
    /**
     * ListView头布局相关控件
     */
    private View headerView;

    private TextView topOutTv, topInTv, topCurrentBudgetTv, topTodayTv, chartTv, topInitBudgetTv;

    private ImageView topToggleIV;

    private boolean isShow = true;

    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        todayLv = findViewById(R.id.main_lv);
        //添加ListView的头布局
        addLVHeaderView();
        //设置适配器：加载每一行数据到ListView当中
        accountAdapter = new AccountAdapter(this, accountList);
        todayLv.setAdapter(accountAdapter);
    }

    /**
     * 给ListView添加头布局
     */
    private void addLVHeaderView() {
        //将布局转换成View对象
        headerView = getLayoutInflater().inflate(R.layout.item_main_lv_top, null);
        todayLv.addHeaderView(headerView);
        //头布局所需要的控件
        topOutTv = findViewById(R.id.item_main_lv_top_out);
        topInTv = findViewById(R.id.item_main_lv_top_in);
        topCurrentBudgetTv = findViewById(R.id.item_main_lv_top_budget);
        topInitBudgetTv = findViewById(R.id.item_main_lv_top_init_budget);
        topTodayTv = findViewById(R.id.item_main_lv_top_today);
        chartTv = findViewById(R.id.item_main_lv_top_tv4);
        topToggleIV = findViewById(R.id.item_main_lv_top_hide);
        topCurrentBudgetTv.setOnClickListener(this);
        topInitBudgetTv.setOnClickListener(this);
        chartTv.setOnClickListener(this);
        topToggleIV.setOnClickListener(this);
    }

    /**
     * 初始化头布局的本月（天）总收入和本月（天）总支出
     */
    private void initSumMoney() {
        float sumIncomeByMonth = DBManager.getSumMoneyByOneMonth(year, month, TypeBean.KIND_INCOME);
        float sumOutcomeByMonth = DBManager.getSumMoneyByOneMonth(year, month, TypeBean.KIND_OUTCOME);
        float sumIncomeByDay = DBManager.getSumMoneyByOneDay(year, month, day, TypeBean.KIND_INCOME);
        float sumOutcomeByDay = DBManager.getSumMoneyByOneDay(year, month, day, TypeBean.KIND_OUTCOME);
        String todaySumMoney = "今日支出 ￥ "+sumOutcomeByDay+"  收入 ￥ "+sumIncomeByDay;
        topInTv.setText(String.valueOf(sumIncomeByMonth));
        topOutTv.setText(String.valueOf(sumOutcomeByMonth));
        topTodayTv.setText(todaySumMoney);
        setLVLongClickListener();

    }

    /**
     * 设置ListView的长按事件
     */
    private void setLVLongClickListener() {
        todayLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == 0) {
                    //如果点击的是头布局，不进行任何操作
                    return false;
                }
                int pos = position - 1; //减一是因为第一个石头布局
                AccountBean clickBean = (AccountBean) accountAdapter.getItem(pos);
                //弹出提示用户是否删除的对话框
                showDeleteItemDialog(clickBean);
                return false;
            }
        });
    }

    /**
     * 弹出是否删除某一条记录的对话框
     */
    private void showDeleteItemDialog(final AccountBean clickBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示信息").setMessage("你确定要删除这条记录么？")
                .setNegativeButton("取消",null)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //执行删除操作
                        int click_id = clickBean.getId();
                        DBManager.deleteOneAccountById(click_id);
                        onResume();
                    }
                });
        builder.create().show();    //显示对话框
    }

    private void loadAccountList() {
        List<AccountBean> list = DBManager.getAccountByTime(year, month, day);
        accountList.clear();
        accountList.addAll(list);
        accountAdapter.notifyDataSetChanged();
    }

    /**
     * 每次活动获得焦点就会调用
     */
    @Override
    protected void onResume() {
        super.onResume();
        getCurrentTime();
        loadAccountList();
        initSumMoney();
        initBudget();
    }

    /**
     * 初始化支出预算
     */
    private void initBudget() {
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        float initialBudget = 0.0f;
        float currentBudget = 0.0f;
        float prefFloat = pref.getFloat("initialBudget", 0.0f);
        if ( pref!=null && prefFloat != 0.0f ) {
            initialBudget = prefFloat;

            SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
            //计算剩余预算
            float sumOutcomeByThisMonth = DBManager.getSumMoneyByOneMonth(year, month, TypeBean.KIND_OUTCOME);
            currentBudget = initialBudget - sumOutcomeByThisMonth;
            editor.putFloat("currentBudget", currentBudget);
            editor.commit();
        }
        topCurrentBudgetTv.setText(String.valueOf(currentBudget));
        topInitBudgetTv.setText(" / "+initialBudget);
    }

    private void getCurrentTime() {
        Calendar today = Calendar.getInstance();
        year = today.get(Calendar.YEAR);
        month = today.get(Calendar.MONTH) + 1;
        day = today.get(Calendar.DAY_OF_MONTH);
    }

    public void onButtonClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.main_lv_search:
                intent = new Intent(this, SearchActivity.class);    //跳转搜索界面
                startActivity(intent);
                break;
            case R.id.main_btn_edit:
                intent = new Intent(this, RecordActivity.class);    //跳转记一笔界面
                startActivity(intent);
                break;
            case R.id.main_btn_more:
                MoreDialog moreDialog = new MoreDialog(this);
                moreDialog.show();
                moreDialog.setDialogSize();
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_main_lv_top_init_budget:

            case R.id.item_main_lv_top_budget:
                showBudgetDialog();
                break;
            case R.id.item_main_lv_top_tv4:
                Intent intent = new Intent(this, MonthChartActivity.class);
                startActivity(intent);
                break;
            case R.id.item_main_lv_top_hide:
                toggleShow();
                break;
        }
    }

    private void showBudgetDialog() {
        final BudgetDialog budgetDialog = new BudgetDialog(this);
        budgetDialog.show();
        budgetDialog.setDialogSize();

        budgetDialog.setOnEnsureListener(new BudgetDialog.OnEnsureListener() {
            @Override
            public void onEnsure(float budget) {
                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                editor.putFloat("initialBudget", budget);

                //计算剩余预算
                float sumOutcomeByThisMonth = DBManager.getSumMoneyByOneMonth(year, month, TypeBean.KIND_OUTCOME);
                float currentBudget = budget - sumOutcomeByThisMonth;
                editor.putFloat("currentBudget", currentBudget);

                editor.commit();
                topCurrentBudgetTv.setText(String.valueOf(currentBudget));
                topInitBudgetTv.setText(" / "+budget);

                budgetDialog.cancel();
                initBudget();

            }
        });
    }

    /**
     * 点击头布局眼睛时，如果原来是明文，就加密，如果是密文，就显示出来
     */
    private void toggleShow() {
        if (isShow) {
            //从明文到密文
            PasswordTransformationMethod instance = PasswordTransformationMethod.getInstance();
            topInTv.setTransformationMethod(instance);
            topOutTv.setTransformationMethod(instance);
            topCurrentBudgetTv.setTransformationMethod(instance);
            topInitBudgetTv.setTransformationMethod(instance);
            topToggleIV.setImageResource(R.mipmap.ih_hide);
            isShow = false;
        }else {
            //从密文到明文
            HideReturnsTransformationMethod instance = HideReturnsTransformationMethod.getInstance();
            topInTv.setTransformationMethod(instance);
            topOutTv.setTransformationMethod(instance);
            topCurrentBudgetTv.setTransformationMethod(instance);
            topInitBudgetTv.setTransformationMethod(instance);
            topToggleIV.setImageResource((R.mipmap.ih_show));
            isShow = true;
        }
    }
}
