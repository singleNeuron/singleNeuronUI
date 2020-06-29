package me.singleneuron.singleneuronui

import android.util.Log
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import me.singleneuron.singleneuronui.utils.HookStatue

class InitHook : IXposedHookLoadPackage {

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        if ("me.singleneuron.singleneuronui.demo" != lpparam!!.packageName) return
        XposedHelpers.findAndHookMethod(HookStatue::class.java.name, lpparam.classLoader, "isEnabled", object : XC_MethodReplacement(){
            override fun replaceHookedMethod(param: MethodHookParam?): Any {
                Log.d("singleNeuronUI.demo","hooked")
                return true
            }

        })
    }

}