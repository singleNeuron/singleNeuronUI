package me.singleneuron.singleneuronui.activities

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import me.singleneuron.singleneuronui.R
import me.singleneuron.singleneuronui.databinding.MainActivityBinding
import me.singleneuron.singleneuronui.fragments.OuterSettingFragment
import me.singleneuron.singleneuronui.interfaces.RunBeforeAndAfterAble
import me.singleneuron.singleneuronui.utils.ContextUtils.Companion.getSharedPreferenceOnUI

@Keep
abstract class AbstractMainActivity : AppCompatActivity(), RunBeforeAndAfterAble {

    protected lateinit var binding : MainActivityBinding
    private var mToolbar: Toolbar? = null
    abstract var innerFragment: Fragment?

    companion object {
        var nowNightMode = false
        init {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        runBefore()
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v: View, insets: WindowInsetsCompat ->
            v.setPadding(0, 0, 0, insets.tappableElementInsets.bottom)
            insets
        }
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val isNightMode = getSharedPreferenceOnUI().getBoolean("forceNight", false)
        val nightMode = if (isNightMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        @ColorInt val colorInt = ContextCompat.getColor(this, R.color.toolbarBackground)

        //https://blog.csdn.net/maosidiaoxian/article/details/51734895
        val window = window
        //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        //设置状态栏颜色
        window.statusBarColor = colorInt
        mToolbar = binding.toolbarPreference
        mToolbar!!.title = resources.getString(R.string.app_name)
        //mToolbar.setBackgroundColor(colorInt);
        setSupportActionBar(mToolbar)
        val docker = getWindow().decorView
        var ui = docker.systemUiVisibility
        nowNightMode = currentNightMode == Configuration.UI_MODE_NIGHT_YES || isNightMode
        if (!nowNightMode) {
            ui = ui or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ui = ui or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            }
        } else {
            ui = ui and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ui = ui and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
            }
        }
        docker.systemUiVisibility = ui
        //https://blog.csdn.net/polo2044/article/details/81708196
        val needRecreat = delegate.localNightMode != nightMode
        if (needRecreat) {
            delegate.localNightMode = nightMode
            recreate()
            return
        }
        val fragmentManager = supportFragmentManager
        if (!OuterSettingFragment.isInnerSettingFragmentInitialized()) {
            OuterSettingFragment.innerSettingFragment = innerFragment!!
        }
        if (OuterSettingFragment.isAdded) fragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out).show(OuterSettingFragment)
        else switchFragment(OuterSettingFragment)
        runAfter()
    }

    override fun onBackPressed() {
        if (!supportFragmentManager.popBackStackImmediate()) super.onBackPressed()
    }

    fun switchFragment(clazz: Class <out Fragment>) {
        val fragment = clazz.newInstance()
        switchFragment(fragment)
    }

    fun switchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out).replace(R.id.content_frame, fragment, fragment::class.java.simpleName).addToBackStack(fragment::class.java.simpleName).commit()
    }

}