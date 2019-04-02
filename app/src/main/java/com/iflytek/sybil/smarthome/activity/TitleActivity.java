package com.iflytek.sybil.smarthome.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.iflytek.sybil.smarthome.R;


/**
 * Created by sybil on 2017/8/24.
 */

public class TitleActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTitleTextView;
    private ImageButton mBackwardButton;
    private Button mForwardButton;
    private FrameLayout mContentLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViews();   //加载 activity_title 布局 ，并获取标题及两侧按钮
    }

    private void setupViews() {
        super.setContentView(R.layout.activity_title);
        mTitleTextView = (TextView) findViewById(R.id.text_title);
        mBackwardButton = (ImageButton) findViewById(R.id.button_backward);
        mForwardButton = (Button) findViewById(R.id.button_forward);
        mContentLayout = (FrameLayout) findViewById(R.id.layout_content);
    }

    //设置标题内容
    @Override
    public void setTitle(int titleId) {
        mTitleTextView.setVisibility(View.VISIBLE);
        mTitleTextView.setText(titleId);
    }

    //设置标题内容
    @Override
    public void setTitle(CharSequence title) {
        mTitleTextView.setVisibility(View.VISIBLE);
        mTitleTextView.setText(title);
    }

    //设置标题文字颜色
    @Override
    public void setTitleColor(int textColor) {
        mTitleTextView.setVisibility(View.VISIBLE);
        mTitleTextView.setTextColor(textColor);
    }


    /**
     * 是否显示返回按钮
     * @param show  true则显示
     */
    protected void showBackwardView(boolean show) {
        if (mBackwardButton != null) {
            if (show) {
                mBackwardButton.setVisibility(View.VISIBLE);
            } else {
                mBackwardButton.setVisibility(View.INVISIBLE);
            }
        } // else ignored
    }

    /**
     * 提供是否显示提交按钮
     * @param forwardResId  文字
     * @param show  true则显示
     */
    protected void showForwardView(int forwardResId, boolean show) {
        if (mForwardButton != null) {
            if (show) {
                mForwardButton.setVisibility(View.VISIBLE);
                mForwardButton.setText(forwardResId);
            } else {
                mForwardButton.setVisibility(View.INVISIBLE);
            }
        } // else ignored
    }

    /**
     * 返回按钮点击后触发
     * @param backwardView
     */
    protected void onBackward(View backwardView) {
        finish();
    }

    /**
     * 提交按钮点击后触发
     * @param forwardView
     */
    protected void onForward(View forwardView) {
        //若要实现点击提交按钮触发相关操作，则重写此方法
    }


    //取出FrameLayout并调用父类removeAllViews()方法
    @Override
    public void setContentView(int layoutResID) {
        mContentLayout.removeAllViews();
        View.inflate(this, layoutResID, mContentLayout);
        onContentChanged();
    }

    @Override
    public void setContentView(View view) {
        mContentLayout.removeAllViews();
        mContentLayout.addView(view);
        onContentChanged();
    }

    /* (non-Javadoc)
     * @see android.app.Activity#setContentView(android.view.View, android.view.ViewGroup.LayoutParams)
     */
    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        mContentLayout.removeAllViews();
        mContentLayout.addView(view, params);
        onContentChanged();
    }


    /* (non-Javadoc)
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     * 按钮点击调用的方法
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button_backward:
                onBackward(v);
                break;

            case R.id.button_forward:
                onForward(v);
                break;

            default:
                break;
        }
    }

}
