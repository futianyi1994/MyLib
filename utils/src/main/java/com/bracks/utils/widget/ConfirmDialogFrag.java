package com.bracks.utils.widget;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bracks.utils.R;
import com.bracks.utils.util.FindViewUtlis;


/**
 * good programmer.
 *
 * @date : 2020/11/25 9:56
 * @author: futia
 * @email : futianyi1994@126.com
 * @description :
 */
public class ConfirmDialogFrag extends DialogFragment implements View.OnClickListener {
    private static final int X_OFFSET = 100;
    private static final int Y_TOP_OFFSET = 100;
    private static final int WIDTH = 500;
    private static final int HEIGHT = 300;
    private String title, left, right;
    private boolean isFullScreen;
    private OnClickListener onClickListener;

    public static ConfirmDialogFrag newInstance(String title, String left, String right, boolean isFullScreen) {
        ConfirmDialogFrag fragment = new ConfirmDialogFrag();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("left", left);
        args.putString("right", right);
        args.putBoolean("isFullScreen", isFullScreen);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getArguments().getString("title");
        left = getArguments().getString("left");
        right = getArguments().getString("right");
        isFullScreen = getArguments().getBoolean("isFullScreen");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        if (isFullScreen) {
            attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
            attributes.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setDimAmount(0.5f);
        } else {
            attributes.x = X_OFFSET;
            attributes.y = Y_TOP_OFFSET;
            attributes.gravity = Gravity.START | Gravity.TOP;
            window.setDimAmount(0f);
            window.getDecorView().setPadding(0, 0, 0, 0);
            window.setLayout(WIDTH, HEIGHT);
        }
        attributes.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //不设置背景会有默认的pading存在
        window.setBackgroundDrawableResource(R.color.transparent);
        window.setAttributes(attributes);
        getDialog().setCanceledOnTouchOutside(false);

        View inflate = inflater.inflate(R.layout.common_dialog_layout, container, false);
        ((TextView) FindViewUtlis.findViewById(inflate, R.id.tvTitle)).setText(title);
        TextView tvleft = (TextView) FindViewUtlis.findViewById(inflate, R.id.positiveButton);
        TextView tvRight = (TextView) FindViewUtlis.findViewById(inflate, R.id.negativeButton);
        tvleft.setText(left);
        tvRight.setText(right);
        tvleft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        return inflate;
    }

    @Override
    public void onClick(View v) {
        if (onClickListener != null) {
            onClickListener.onClick(v.getId() == R.id.positiveButton);
        }
        dismiss();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(boolean isConfirm);
    }
}