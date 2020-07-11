package me.singleneuron.singleneuronui.hook

import android.util.Log
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodReplacement
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import me.singleneuron.singleneuronui.utils.HookStatue

open class InitHook : IXposedHookLoadPackage, Runnable {

    private val xposedTasks : ArrayList<XposedTask> = ArrayList()

    fun addXposedTasks(vararg xposedTask: XposedTask) {
        xposedTasks.addAll(xposedTask)
    }

    final override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        if ("me.singleneuron.singleneuronui.demo" == lpparam!!.packageName) {
            XposedHelpers.findAndHookMethod(HookStatue::class.java.name, lpparam.classLoader, "isEnabled", object : XC_MethodReplacement() {
                override fun replaceHookedMethod(param: MethodHookParam?): Any {
                    Log.d("singleNeuronUI.demo", "hooked")
                    return true
                }

            })
            return
        }
        if ("me.singleneuron.singleneuronui.debugtool" == lpparam.packageName) {
            DebugToolHook(lpparam).init()
        }
        run()
        for (xposedTask in xposedTasks) {
            if (!xposedTask.mHasFilter) {
                    try {
                        xposedTask.mTask(lpparam)
                    } catch (e:Exception) {
                        e.printStackTrace()
                    }
                    return
            }
            for (string in xposedTask.mPackagesNotLoad) {
                if (lpparam.packageName == string) break
            }
            for (string in xposedTask.mPackagesToLoad) {
                if (lpparam.packageName == string) {
                    try {
                        xposedTask.mTask(lpparam)
                    } catch (e:Exception) {
                        e.printStackTrace()
                    }
                    break
                }
            }
        }
    }

    class XposedTask @JvmOverloads constructor (task: (XC_LoadPackage.LoadPackageParam?)->Unit, hasFilter: Boolean = false, packagesToLoad: Array<String> = emptyArray(), packagesNotLoad: Array<String> = emptyArray()) {
        var mTask = task
        var mHasFilter = hasFilter
        var mPackagesToLoad = packagesToLoad
        var mPackagesNotLoad = packagesNotLoad
    }

    override fun run() {

    }

}