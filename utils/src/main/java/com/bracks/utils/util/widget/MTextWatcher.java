package com.bracks.utils.util.widget;

import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Good programmer.
 *
 * @author futia
 * @date 2017-11-15 下午 05:01
 * Email:futianyi1994@126.com
 * Description:自定义的文本变化监听接口
 */
public class MTextWatcher implements TextWatcher {
    private TextInputEditText[] inputViews;
    private TextInputEditText inputView;
    private CharSequence inputText;
    private int editStart;
    private int editEnd;
    private Editable editable;
    private EditText editText;
    private TextView textView;
    private int count;
    private boolean isLimit = false;
    private boolean isCount = false;

    public MTextWatcher() {
    }

    public MTextWatcher(EditText editText, TextView textView) {
        this.editText = editText;
        this.textView = textView;
    }

    public MTextWatcher(TextInputEditText[] inputViews) {
        this.inputViews = inputViews;
    }

    public MTextWatcher(TextInputEditText inputView) {
        this.inputView = inputView;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        inputText = charSequence;
        //文本有变化，所有的错误提示取消
        clearErrors();
    }

    @Override
    public void afterTextChanged(Editable editable) {
        this.editable = editable;
        if (isCount) {
            limit();
        }
    }

    /**
     * 清除错误文本
     */
    private void clearErrors() {
        if (inputView != null && inputView.getError() != null) {
            inputView.setError(null);
        }
        if (inputViews == null) {
            return;
        }
        for (int i = 0; i < inputViews.length; i++) {
            if (inputViews[i].getError() != null) {
                inputViews[i].setError(null);
            }
        }
    }

    /**
     * 设置是否计数和是否做字数限制
     *
     * @param isCount
     * @param isLimit
     */
    public void setLimit(boolean isCount, boolean isLimit, int count) {
        this.isCount = isCount;
        this.isLimit = isLimit;
        this.count = count;
    }

    /**
     * 限制的方法
     */
    private void limit() {
        textView.setText(inputText.length() + "");
        if (!isLimit) {
            return;
        }
        editStart = editText.getSelectionStart();
        editEnd = editText.getSelectionEnd();
        if (inputText.length() > count) {
            editable.delete(editStart - 1, editEnd);
            int tempSelection = editStart;
            //防止当前输入字符到达Limit，引起递归问题
            editText.removeTextChangedListener(this);
            editText.setText(editable);
            editText.addTextChangedListener(this);
            editText.setSelection(tempSelection);
        }
    }
}
