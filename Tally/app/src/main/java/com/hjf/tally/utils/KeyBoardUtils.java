package com.hjf.tally.utils;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.hjf.tally.R;

/**
 * @author hjf
 * @create 2020-12-24 14:39
 */
public class KeyBoardUtils {
    private final Keyboard keyboard;    //自定义键盘
    private KeyboardView keyboardView;
    private EditText editText;

    public interface OnEnsureListener {
        void onEnsure();
    }
    OnEnsureListener onEnsureListener;

    public void setOnEnsureListener(OnEnsureListener onEnsureListener) {
        this.onEnsureListener = onEnsureListener;
    }

    public KeyBoardUtils(KeyboardView keyboardView, EditText editText) {
        this.keyboardView = keyboardView;
        this.editText = editText;
        this.editText.setInputType(InputType.TYPE_NULL);    //取消editText的默认弹出系统键盘
        keyboard = new Keyboard(this.editText.getContext(), R.xml.key);

        this.keyboardView.setKeyboard(keyboard);        //设置要显示的键盘
        this.keyboardView.setEnabled(true);
        this.keyboardView.setPreviewEnabled(false);
        this.keyboardView.setOnKeyboardActionListener(listener);   //设置键盘按钮被点击了的监听
    }

    KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {

        @Override
        public void onPress(int i) {

        }

        @Override
        public void onRelease(int i) {

        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable text = editText.getText();         //目前写入进去的Data
            int start = editText.getSelectionStart();   //文本焦点处的开始
            switch (primaryCode) {
                case -5:   //点击了删除
                    if (text !=null && text.length()>0) {
                        if (start>0) {
                            text.delete(start-1,start);
                        }
                    }
                    break;
                case -3:   //点击了清零
                    text.clear();
                    break;
                case -4:    //点击了完成
                    if (onEnsureListener != null) {
                        onEnsureListener.onEnsure();
                    }
                    break;
                default:                       //其他数字直接在后面添加上去
                    text.insert(start, Character.toString((char)primaryCode));
                    break;
            }
        }

        @Override
        public void onText(CharSequence charSequence) {

        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }
    };

    /**
     * VISIBLE：显示
     * INVISIBLE：不显示，但是占地方
     * GONE：不显示，也不占地方
     * 显示键盘
     */
    public void showKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.INVISIBLE || visibility == View.GONE) {
            keyboardView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * VISIBLE：显示
     * INVISIBLE：不显示，但是占地方
     * GONE：不显示，也不占地方
     * 隐藏键盘
     */
    public void hideKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.VISIBLE || visibility == View.INVISIBLE) {
            keyboardView.setVisibility(View.GONE);
        }
    }
}
