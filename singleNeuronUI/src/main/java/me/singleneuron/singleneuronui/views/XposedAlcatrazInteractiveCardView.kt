package me.singleneuron.singleneuronui.views

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import me.singleneuron.singleneuronui.R
import me.singleneuron.singleneuronui.activities.AbstractMainActivity
import me.singleneuron.singleneuronui.utils.HookStatue.getStatue
import me.singleneuron.singleneuronui.utils.HookStatue.getStatueName
import me.singleneuron.singleneuronui.utils.HookStatue.isActive

class XposedAlcatrazInteractiveCardView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = R.style.googleBlue) : SimpleAlcatrazInteractiveCardView(context, attrs, defStyleAttr) {

    init {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.SimpleAlcatrazInteractiveCardView, 0, 0)
        var clazz: Class<out Fragment>? = null
        try {
            val clazzString = a.getString(R.styleable.SimpleAlcatrazInteractiveCardView_jumpFragmentClass)
            if (!clazzString.isNullOrBlank())
                try {
                    clazz = Class.forName(clazzString) as Class<out Fragment>
                } catch (e: Exception) {
                    e.printStackTrace()
                }
        } finally {
            a.recycle()
        }

        val statue = context.getStatue()
        titleLine = context.getString(statue.getStatueName())
        active = statue.isActive()

        if (statue.name.contains("taichi", true)) {
            if (statue.isActive()) {
                binding.alcatrazInteractiveCard.setOnClickListener {
                    if (clazz != null) {
                        jumpFragment(clazz.newInstance())
                    }
                }
                binding.alcatrazInteractiveCard.apply {
                    setOnClickListener {
                        //activity!!.toast("跳转到太极")
                        val t = Intent("me.weishu.exp.ACTION_MODULE_MANAGE")
                        t.data = Uri.parse("package:" + context.packageName)
                        t.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        try {
                            context.startActivity(t)
                        } catch (e: ActivityNotFoundException) {
                            //ignore
                        }
                    }
                }
            } else {
                binding.alcatrazInteractiveCard.setOnClickListener {
                    //activity!!.toast("跳转到太极")
                    val t = Intent("me.weishu.exp.ACTION_MODULE_MANAGE")
                    t.data = Uri.parse("package:" + context.packageName)
                    t.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    try {
                        context.startActivity(t)
                    } catch (e: ActivityNotFoundException) {
                        //ignore
                    }
                }
            }
        } else {
            binding.alcatrazInteractiveCard.setOnClickListener {
                if (clazz != null) {
                    jumpFragment(clazz.newInstance())
                }
            }
        }
    }

    private fun getActivity(): AppCompatActivity {
        var context = context
        while (context is ContextWrapper) {
            if (context is AppCompatActivity) {
                return context
            }
            context = context.baseContext
        }
        throw RuntimeException("Can't get Activity")
    }

    private fun jumpFragment(fragment: Fragment) {
        val activity = getActivity()
        if (activity is AbstractMainActivity) {
            activity.switchFragment(fragment)
        } else {
            activity.supportFragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out).replace(R.id.content_frame, fragment, fragment::class.java.simpleName).addToBackStack(fragment::class.java.simpleName).commit()
        }
    }

}