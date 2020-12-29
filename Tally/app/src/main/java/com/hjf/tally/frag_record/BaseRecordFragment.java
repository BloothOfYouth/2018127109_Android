package com.hjf.tally.frag_record;

import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.hjf.tally.R;
import com.hjf.tally.adapter.TypeBaseAdapter;
import com.hjf.tally.bean.AccountBean;
import com.hjf.tally.db.DBManager;
import com.hjf.tally.bean.TypeBean;
import com.hjf.tally.utils.KeyBoardUtils;
import com.hjf.tally.utils.NoteDialog;
import com.hjf.tally.utils.SelectTimeDialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 记录页面当中的记录的基础模块（用于支出和收入模块继承）
 */
public class BaseRecordFragment extends Fragment implements View.OnClickListener {

    private KeyboardView keyboardView;

    private EditText moneyText;

    private ImageView selectImage;

    private TextView selectTypeText;

    private TextView noteText;

    private TextView timeText;

    private GridView typeGrid;

    private List<TypeBean> typeList;

    private TypeBaseAdapter typeBaseAdapter;

    private AccountBean accountBean;

    /**
     * 收入：1
     * 支出：0
     * 类型
     */
    private Integer kindOfType;

    /**
     * 获得默认图片Id
     * @return
     */
    public Integer getDefaultImgId() {
        if (kindOfType == TypeBean.KIND_INCOME) {
            //收入的默认图片样式
            return R.mipmap.in_qt_fs;
        }else {
            //支出的默认图片样式
            return R.mipmap.ic_qita_fs;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment, container, false);
        accountBean = new AccountBean();
        // 初始化fragment的View
        initView(view);
        // 给GridView填充数据
        loadDataToGV();
        // 初始化最上方图片和文字为其他
        initSelect();
        // 设置GridView的点击事件
        setGVListener();
        // 设置初始化时间（当前时间）
        setInitTime();
        return view;
    }

    /**
     * 初始化最上方图片和文字为其他
     */
    private void initSelect() {
        int selectPosition = typeBaseAdapter.getSelectPosition();
        TypeBean typeBean = typeList.get(selectPosition);
        selectImage.setImageResource(typeBean.getSimageId());
        selectTypeText.setText(typeBean.getTypename());
    }

    /**
     * 获取当前时间，显示
     */
    private void setInitTime() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
//        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        String time = dateFormat.format(date);
        timeText.setText(time);
    }

    /**
     * 设置GridView的点击事件
     */
    private void setGVListener() {
        typeGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                typeBaseAdapter.setSelectPosition(position);
                typeBaseAdapter.notifyDataSetChanged(); //提示绘制发生变化
                TypeBean typeBean = typeList.get(position);
                selectImage.setImageResource(typeBean.getSimageId());
                selectTypeText.setText(typeBean.getTypename());
                accountBean.setSimageId(typeBean.getSimageId());
            }
        });
    }

    /**
     * 给GridView填充数据
     */
    private void loadDataToGV() {
        typeList = DBManager.getTypeList(kindOfType);    //从DBManager中拿出数据
        typeBaseAdapter = new TypeBaseAdapter(getContext(), typeList);
        typeGrid.setAdapter(typeBaseAdapter);
    }

    public void setKindOfType(Integer kindOfType) {
        this.kindOfType = kindOfType;
    }

    /**
     * 初始化fragment的View
     * @param view
     */
    private void initView(View view) {
        keyboardView = view.findViewById(R.id.frag_record_keyboard);
        moneyText = view.findViewById(R.id.frag_record_top_money);
        selectImage = view.findViewById(R.id.frag_record_top_iv);
        selectTypeText = view.findViewById(R.id.frag_record_top_type);
        noteText = view.findViewById(R.id.frag_record_tv_note);
        timeText = view.findViewById(R.id.frag_record_tv_time);
        typeGrid = view.findViewById(R.id.frag_record_gv);

        noteText.setOnClickListener(this);
        timeText.setOnClickListener(this);

        //设置默认图片Id
        accountBean.setSimageId(getDefaultImgId());

        //让自定义软键盘显示出来
        KeyBoardUtils keyBoardUtils = new KeyBoardUtils(keyboardView, moneyText);
        keyBoardUtils.showKeyboard();
        //设置接口，监听确定按钮被点击了
        keyBoardUtils.setOnEnsureListener(new KeyBoardUtils.OnEnsureListener() {
            //点击了确定按钮
            @Override
            public void onEnsure() {
                //获取记录的信息
                //获取输入钱数
                String money = moneyText.getText().toString();
                String note = noteText.getText().toString().trim();
                if (TextUtils.isEmpty(money) || money.equals("0")) {
                    Toast.makeText(getContext(), "金额不能为0或空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(note)) {
                   note = "无";
                }
                accountBean.setTypename(selectTypeText.getText().toString());
                accountBean.setNote(note);
                accountBean.setMoney(Float.parseFloat(money));
                accountBean.setKind(kindOfType);
                accountBean.setTime(timeText.getText().toString());
                //保存记账记录到数据库当中
                saveAccountToDB();
                //返回上一级页面
                getActivity().finish();
            }
        });

    }

    /**
     * 保存记账记录到数据库当中
     */
    public void saveAccountToDB() {
        DBManager.insertToAccount(accountBean);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.frag_record_tv_note:
                showNoteDialog();
                break;
            case R.id.frag_record_tv_time:
                showTimeDialog();
                break;
        }
    }

    /**
     * 弹出时间对话框
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showTimeDialog() {
        final SelectTimeDialog selectTimeDialog = new SelectTimeDialog(getContext());
        selectTimeDialog.show();
        String currentTime = timeText.getText().toString();
        writeBackDateTime(currentTime, selectTimeDialog);   //回写时间

        selectTimeDialog.setOnEnsureListener(new SelectTimeDialog.OnEnsureListener() {
            @Override
            public void onEnsure(String time) {
                timeText.setText(time);
                selectTimeDialog.cancel();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void writeBackDateTime(String time, SelectTimeDialog timeDialog) {
        int yearChar = time.indexOf("年");
        int monthChar = time.indexOf("月");
        int dayChar = time.indexOf("日");
        int colon = time.indexOf(":");
        int year = Integer.parseInt(time.substring(0, yearChar));
        int month = Integer.parseInt(time.substring(yearChar + 1, monthChar)) - 1;
        int day = Integer.parseInt(time.substring(monthChar + 1, dayChar));
        int hour = Integer.parseInt(time.substring(dayChar + 2, colon));
        int minute = Integer.parseInt(time.substring(colon + 1));
        timeDialog.getDatePicker().init(year, month, day, null);
        timeDialog.getTimePicker().setHour(hour);
        timeDialog.getTimePicker().setMinute(minute);
    }

    /**
     * 弹出备注对话框
     */
    private void showNoteDialog() {
        final NoteDialog noteDialog = new NoteDialog(getContext());
        noteDialog.show();
        noteDialog.setDialogSize();
        noteDialog.getNoteText().setText(noteText.getText());   //回写备注

        noteDialog.setOnEnsureListener(new NoteDialog.OnEnsureListener() {
            @Override
            public void onEnsure() {
                String note = noteDialog.getNoteText().getText().toString().trim();
                noteText.setText(note);
                noteDialog.cancel();
            }
        });
    }
}
