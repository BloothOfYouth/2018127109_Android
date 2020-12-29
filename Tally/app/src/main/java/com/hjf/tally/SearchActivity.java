package com.hjf.tally;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hjf.tally.adapter.AccountAdapter;
import com.hjf.tally.bean.AccountBean;
import com.hjf.tally.db.DBManager;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private EditText searchEt;

    private ListView searchLv;

    private TextView searchEmptyTv;

    private AccountAdapter accountAdapter;

    private List<AccountBean> accountList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchEt = findViewById(R.id.search_et);
        searchLv = findViewById(R.id.search_lv);
        searchEmptyTv = findViewById(R.id.search_tv_empty);
        accountAdapter = new AccountAdapter(this, accountList);
        searchLv.setAdapter(accountAdapter);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_iv_back:
                finish();
                break;
            case R.id.search_iv_search:
                searchListByNote();
                break;
        }
    }

    /**
     * 通过备注模糊查询相关Account
     */
    private void searchListByNote() {
        String searchNote = searchEt.getText().toString().trim();
        if (TextUtils.isEmpty(searchNote)) {
            Toast.makeText(this, "搜索内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        List<AccountBean> list =  DBManager.getAccountByNote(searchNote);
        if (list.size() != 0) {
            accountList.clear();
            accountList.addAll(list);
            accountAdapter.notifyDataSetChanged();
            searchEmptyTv.setVisibility(View.GONE);
            searchLv.setVisibility(View.VISIBLE);
        }else {
            searchEmptyTv.setVisibility(View.VISIBLE);
            searchLv.setVisibility(View.GONE);
        }

    }
}
