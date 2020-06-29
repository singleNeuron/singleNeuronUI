package me.singleneuron.singleneuronui.activities

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity

@Keep
class BackgroundActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("LockNotification", "BackgroundActivity已启动")
        moveTaskToBack(true)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        this.finish()
        return super.dispatchTouchEvent(ev)
    }

    override fun onStart() {
        super.onStart()
        setVisible(true)
    }

}