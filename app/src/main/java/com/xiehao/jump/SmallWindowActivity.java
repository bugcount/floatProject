package com.xiehao.jump;

/**
 * Created by Administrator on 2018/1/3.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.WindowManager;

import java.lang.reflect.Field;

public class SmallWindowActivity extends LinearLayout {


    public static int viewWidth;//小悬浮宽度
    public static int viewHeight;//小悬浮高度

    private static int statusBarHeight;//状态栏高度

    private WindowManager windowManager;//更新小悬浮的位置
    private WindowManager.LayoutParams mParams;//小悬浮高度

    private float xInScreen;//记录当前手指位置在屏幕上的横坐标值
    private float yInScreen;//记录当前手指位置在屏幕上的纵坐标值

    private float xDownInScreen;//记录手指按下时在屏幕上的横坐标的值
    private float yDownInScreen;//记录手指按下时在屏幕上的纵坐标的值

    private float xInView;//记录手指按下时在小悬浮窗的View上的横坐标的值
    private float yInView;//记录手指按下时在小悬浮窗的View上的纵坐标的值
    /**
     *
     * @param context
     */
    public SmallWindowActivity(Context context) {
        super(context);
        windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.activity_small_window,this);
        View view = findViewById(R.id.small_window_layout);
        //获取手机屏幕宽高
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        //显示手机内存空间百分比
        TextView percentView = (TextView)findViewById(R.id.percent);
        percentView.setText(MyWindowManager.getUsedPercentValue(context));
    }
    /**
     * 手指触摸屏幕处理
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //手指按下时记录必要数据，纵坐标的值都需要减去状态栏高度
                xInView = event.getX();
                yInView = event.getY();
                xDownInScreen = event.getRawX();
                yDownInScreen = event.getRawY() - getStatusBarHeight();
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                break;
            case MotionEvent.ACTION_MOVE:
                xInScreen = event.getRawX();
                yInScreen = event.getRawY()-getStatusBarHeight();
                //手指一动的时候就更新悬浮窗位置
                updateViewPosition();
                break;
            case MotionEvent.ACTION_UP:
                //如果手指离开屏幕时，xDownInScreen和xInScreen相等，且yDownInScreen == yInScreen
                //则视为触发
                if (xDownInScreen == xInScreen && yDownInScreen == yInScreen) {
                    MyWindowManager.createBigWindow(getContext());//创建大窗口
                    MyWindowManager.removeSmallWindow(getContext());//移除小窗口
                    Toast.makeText(getContext(), "手指离开屏幕！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
        return true;
    }

    //将悬浮窗的参数传入，用于更新小悬浮窗的位置
    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
    }

    //更新小悬浮窗在屏幕中的位置
    private void updateViewPosition() {
        mParams.x = (int) (xInScreen - xInView);
        mParams.y = (int) (yInScreen - yInView);
        windowManager.updateViewLayout(this, mParams);
    }

    /**
     * 用于获取状态栏高度
     * @return 返回状态栏高度的像素值
     */
    private int getStatusBarHeight() {
        if (statusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                statusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusBarHeight;
    }
}