package com.seeger.tools

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv_test.setOnClickListener {
            ARouter.getInstance().build("/test/1").navigation(this)
        }
        tv_note.setOnClickListener {
            ARouter.getInstance().build("/note/1").navigation(this)
        }
        tv_password.setOnClickListener {
            ARouter.getInstance().build("/password/1").navigation(this)
        }

    }
}
