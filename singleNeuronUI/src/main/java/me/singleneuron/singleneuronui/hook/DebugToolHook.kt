package me.singleneuron.singleneuronui.hook

import android.app.Application
import android.app.Instrumentation
import android.content.Context
import android.widget.Toast
import androidx.annotation.Keep
import de.robv.android.xposed.*
import de.robv.android.xposed.callbacks.XC_LoadPackage
import me.singleneuron.singleneuronui.BuildConfig
import me.singleneuron.singleneuronui.utils.ContextUtils
import java.io.PrintWriter
import java.io.StringWriter

@Keep
class DebugToolHook(val loadPackageParam: XC_LoadPackage.LoadPackageParam) {

    fun init() {

        XposedHelpers.findAndHookMethod("me.singleneuron.singleneuronui.debugtool.MainActivity", loadPackageParam.classLoader, "toHookOnInit", object : XC_MethodReplacement() {
            override fun replaceHookedMethod(param: MethodHookParam?): Any? {
                val classLoader = loadPackageParam.classLoader
                val context = ContextUtils.contextInXposed
                val xposedPrint = XposedPrint(param!!)

                try {
                    XposedBridge.log("已注入调试器")
                    Toast.makeText(context, "Xposed已经注入", Toast.LENGTH_SHORT).show()
                    val selinux = if (SELinuxHelper.isSELinuxEnabled()) if (SELinuxHelper.isSELinuxEnforced()) "Enforcing" else "Permissive" else "Disabled"
                    xposedPrint.print("singleNeuronUI已找到：\n版本名: " + BuildConfig.VERSION_NAME + "\n版本号：" + BuildConfig.VERSION_CODE)
                    xposedPrint.print("SeLinux (probably unreliable Xposed framework provided): $selinux")
                    xposedPrint.print("Context (got by Xposed): $context")
                    //xposedPrint.print("ModuleContext: " + GeneralUtils.getModuleContext(context))
                    xposedPrint.print("ClassLoader (from LoadPackageParam): $classLoader")
                    //xposedPrint.print("ModuleClassLoader: " + GeneralUtils.getModuleContext(context).classLoader)
                    try {
                        val systemContext: Context = XposedHelpers.callMethod(XposedHelpers.callStaticMethod(XposedHelpers.findClass("android.app.ActivityThread", loadPackageParam.classLoader), "currentActivityThread"), "getSystemContext") as Context
                        xposedPrint.print("SystemContext: $systemContext")
                        xposedPrint.print("SystemClassLoader: " + systemContext.classLoader)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        val stringWriter = StringWriter()
                        val printWriter = PrintWriter(stringWriter)
                        e.printStackTrace(printWriter)
                        xposedPrint.print(stringWriter.toString())
                    }
                    /*val jsonString: String = ContentProviderPreference(ContentProvider.CONTENT_PROVIDER_JSON, "me.singleneuron.originalmusicnotification_debugtool", context).getJSONString()
                    xposedPrint.print("JSONString: $jsonString")
                    val settingJsonString: String = ContentProviderPreference(ContentProvider.CONTENT_PROVIDER_PREFERENCE, null, context).getJSONString()
                    xposedPrint.print("ModuleSettings: $settingJsonString")
                    val deveceProtectedPreference = ContentProviderPreference(ContentProvider.CONTENT_PROVIDER_DEVICE_PROTECTED_PREFERENCE, null, context).getJSONString()
                    xposedPrint.print("DeviceProtectedPreference: $deveceProtectedPreference")
                    xposedPrint.print("---------------")*/
                } catch (e: Exception) {
                    e.printStackTrace()
                    val stringWriter = StringWriter()
                    val printWriter = PrintWriter(stringWriter)
                    e.printStackTrace(printWriter)
                    xposedPrint.print(stringWriter.toString())
                }
                //LogUtils.addLogByContentProvider(loadPackageParam.packageName, "", context)
                return null
            }
        })

        XposedHelpers.findAndHookMethod(Instrumentation::class.java, "callApplicationOnCreate", Application::class.java, object : XC_MethodHook() {
            override fun afterHookedMethod(paramInit: MethodHookParam?) {
                try {

                    /*val context = paramInit!!.args[0] as Context
                    val classLoader = context.classLoader*/

                    XposedHelpers.findAndHookMethod("me.singleneuron.originalmusicnotification_debugtool.MainActivity", loadPackageParam.classLoader, "toHook", object : XC_MethodReplacement() {

                        override fun replaceHookedMethod(param: MethodHookParam?): Any? {
                            val xposedPrint = XposedPrint(param!!)
                            try {
                                XposedBridge.log("已附加至Instrumentation")
                                xposedPrint.print("已附加至Instrumentation")
                                if (paramInit == null) {
                                    xposedPrint.print("MethodHookParam: null")
                                    return null
                                }
                                xposedPrint.print("MethodHookParam: $paramInit")
                                if (paramInit.args[0] == null) {
                                    xposedPrint.print("Context (got by Application）: null")
                                    return null
                                }
                                xposedPrint.print("Context (got by Application）: " + paramInit.args[0])
                                xposedPrint.print("ClassLoader (from Context): " + (paramInit.args[0] as Context).classLoader)
                            } catch (e: Exception) {
                                e.printStackTrace()
                                val stringWriter = StringWriter()
                                val printWriter = PrintWriter(stringWriter)
                                e.printStackTrace(printWriter)
                                xposedPrint.print(stringWriter.toString())
                            }
                            return null
                        }

                    })
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    class XposedPrint(val param: XC_MethodHook.MethodHookParam) {
        fun print(string: String) {
            XposedHelpers.callMethod(param.thisObject, "writeToTextview", string)
        }
    }

}