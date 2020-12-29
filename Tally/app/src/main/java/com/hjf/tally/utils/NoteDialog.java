package com.hjf.tally.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.hjf.tally.R;

/**
 * 在记录页面弹出备注对话框
 * @author hjf
 * @create 2020-12-24 23:32
 */
public class NoteDialog extends Dialog implements View.OnClickListener {

    private EditText noteText;

    private Button ensureBtn;

    private Button cancelBtn;

    private OnEnsureListener onEnsureListener;

    public EditText getNoteText() {
        return noteText;
    }

    public Button getEnsureBtn() {
        return ensureBtn;
    }

    public Button getCancelBtn() {
        return cancelBtn;
    }

    public OnEnsureListener getOnEnsureListener() {
        return onEnsureListener;
    }

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_note);   //设置对话框显示布局
        noteText = findViewById(R.id.dialog_note_et);
        ensureBtn = findViewById(R.id.dialog_note_btn_ensure);
        cancelBtn = findViewById(R.id.dialog_note_btn_cancel);
        ensureBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
    }

    public NoteDialog(@NonNull Context context) {
        super(context);
    }

    public interface OnEnsureListener {
        void onEnsure();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_note_btn_ensure:
                if (onEnsureListener != null) {
                    onEnsureListener.onEnsure();
                }
                break;
            case R.id.dialog_note_btn_cancel:
                cancel();
                break;
        }
    }

    /**
     * 设置Dialog的尺寸和屏幕尺寸一致，以及位置
     */
    public void setDialogSize() {
        //获取当前窗口对象
        Window window = getWindow();
        //获取窗口对象的参数
        WindowManager.LayoutParams attributes = window.getAttributes();
        //获取屏幕宽度
        Display display = window.getWindowManager().getDefaultDisplay();
        attributes.width = (int)(display.getWidth());   //对话框窗口为屏幕宽度
        attributes.gravity = Gravity.BOTTOM;    //设置位置为底部
        window.setBackgroundDrawableResource(android.R.color.transparent);
        window.setAttributes(attributes);
        // 必须等NoteDialog加载完成才能打开
        handler.sendEmptyMessageDelayed(1,100);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            //自动弹出软键盘的方法
            InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    };
}
