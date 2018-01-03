package com.xiehao.jump

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
    }

    private fun initUI() {
        // TODO Auto-generated method stub
        //  WindowService wind = new WindowService();
        suspend.setOnClickListener(suspendListener())
        text.setText(MyWindowManager.getUsedPercentValue(applicationContext))
    }

    inner class suspendListener : View.OnClickListener {

        override fun onClick(arg0: View) {
             //启动悬浮窗口关闭本窗口
            if (Build.VERSION.SDK_INT >= 23) {
                if (Settings.canDrawOverlays(this@MainActivity)) {
                    showFloatView()
                } else {
                    val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                    startActivity(intent)
                }
            } else {
                showFloatView()
            }

        }
    }

    private fun showFloatView() {
         val intent = Intent(this@MainActivity, WindowService::class.java)
        startService(intent)
        finish()
    }
}
