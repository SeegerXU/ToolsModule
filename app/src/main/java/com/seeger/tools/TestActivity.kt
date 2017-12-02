package com.seeger.tools

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.seeger.library.bean.PieBean
import kotlinx.android.synthetic.main.activity_test.*

/**
 * Created by Administrator on 2017/11/17.
 */
@Route(path = "/test/1")
class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)


        var p1 = PieBean()
        p1.title = "短贷"
        p1.percent = 33
        var p2 = PieBean()
        p2.title = "现金分期"
        p2.percent = 32
        var p3 = PieBean()
        p3.title = "商品分期"
        p3.percent = 25
        var p4 = PieBean()
        p4.title = "测试"
        p4.percent = 10

        val data = listOf(p1, p2, p3, p4)
        pcv.setData(data)
    }
}