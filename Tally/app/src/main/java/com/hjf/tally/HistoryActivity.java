package com.hjf.tally;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.hjf.tally.adapter.AccountAdapter;
import com.hjf.tally.bean.AccountBean;
import com.hjf.tally.db.DBManager;
import com.hjf.tally.utils.CalendarDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private TextView timeTv;

    private ListView historyLv;

    private AccountAdapter accountAdapter;

    private List<AccountBean> accountList = new ArrayList<>();

    private int year, month;

    private int selectYearPosition = -1, selectMonthPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        timeTv = findViewById(R.id.history_tv_time);
        historyLv = findViewById(R.id.history_lv);
        accountAdapter = new AccountAdapter(this, accountList);
        historyLv.setAdapter(accountAdapter);
        getCurrentTime();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAccountList();
        setLvLongClickListener();
    }

    private void setLvLongClickListener() {
        historyLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                AccountBean clickBean = (AccountBean) accountAdapter.getItem(position);
                //弹出提示用户是否删除的对话框
                showDeleteItemDialog(clickBean);
                return false;
            }
        });
    }

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

    private void getCurrentTime() {
        Calendar today = Calendar.getInstance();
        year = today.get(Calendar.YEAR);
        month = today.get(Calendar.MONTH) + 1;
        setTextOfTimeTv();
    }

    private void loadAccountList() {
        List<AccountBean> list = DBManager.getAccountByTime(year, month);
        accountList.clear();
        accountList.addAll(list);
        accountAdapter.notifyDataSetChanged();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.history_iv_back:
                finish();
                break;
            case R.id.history_iv_calendar:
                CalendarDialog calendarDialog = new CalendarDialog(this);
                calendarDialog.show();
                calendarDialog.initCalendar(selectYearPosition, selectMonthPosition);
                calendarDialog.setDialogSize();

                calendarDialog.setOnClickListener(new CalendarDialog.OnClickListener() {
                    @Override
                    public void onClick(int changeYear, int changeMonth,  int selectYearPos, int selectMonthPos) {
                        year = changeYear;
                        month = changeMonth;
                        setTextOfTimeTv();
                        selectYearPosition = selectYearPos;
                        selectMonthPosition = selectMonthPos;
                        List<AccountBean> list = DBManager.getAccountByTime(year, month);
                        accountList.clear();
                        accountList.addAll(list);
                        accountAdapter.notifyDataSetChanged();
                    }
                });
                break;
        }
    }

    private void setTextOfTimeTv() {
        String monthStr = month + "";
        if (month < 10) {
            monthStr = "0" + monthStr;
        }
        timeTv.setText(year + "年" + monthStr + "月");
    }
}
