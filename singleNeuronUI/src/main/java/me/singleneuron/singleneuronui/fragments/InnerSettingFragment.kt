package me.singleneuron.singleneuronui.fragments

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.annotation.Keep
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.topjohnwu.superuser.Shell
import de.psdev.licensesdialog.LicensesDialog
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20
import de.psdev.licensesdialog.licenses.GnuLesserGeneralPublicLicense3
import de.psdev.licensesdialog.licenses.MITLicense
import de.psdev.licensesdialog.model.Notice
import de.psdev.licensesdialog.model.Notices
import me.singleneuron.singleneuronui.R
import me.singleneuron.singleneuronui.interfaces.RunBeforeAndAfterAble
import me.singleneuron.singleneuronui.utils.ContextUtils.Companion.getSharedPreferenceOnUI
import me.singleneuron.singleneuronui.utils.HookStatue
import me.singleneuron.singleneuronui.utils.HookStatue.getStatue
import java.io.ByteArrayInputStream
import java.util.*
import kotlin.collections.ArrayList

@Keep
abstract class InnerSettingFragment : PreferenceFragmentCompat(), RunBeforeAndAfterAble {

    abstract val version: String
    abstract val githubUrl: String
    abstract val googlePlayUrl: String
    open var licensesAbove: ArrayList<out Notice> = ArrayList()
    open var licensesUnder: ArrayList<out Notice> = ArrayList()

    final override fun onCreatePreferences(savedInstanceState: Bundle?, rootkey: String?) {
        runBefore()
        addPreferencesFromResource(R.xml.inner_setting)
        val statue: HookStatue.Statue = requireContext().getStatue()
        val debugMode = requireContext().getSharedPreferenceOnUI().getBoolean("debugMode", false)
        if (debugMode || statue.name.contains("taichi")) {
            val taichiProblemPreference: Preference = findPreference("taichiProblem")!!
            taichiProblemPreference.isVisible = true
            taichiProblemPreference.setOnPreferenceClickListener {
                val builder = MaterialAlertDialogBuilder(requireContext())
                val imageView = ImageView(requireContext())
                imageView.setImageResource(R.drawable.taichi)
                imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
                imageView.adjustViewBounds = true
                builder.setView(imageView).setPositiveButton(R.string.OK, null).create().show()
                true
            }
        }
        if (debugMode || statue.name.contains("Edxp")) {
            val edxpProblemPreference: Preference = findPreference("edxpProblem")!!
            edxpProblemPreference.isVisible = true
            edxpProblemPreference.setOnPreferenceClickListener {
                val builder = MaterialAlertDialogBuilder(requireContext())
                val imageView = ImageView(requireContext())
                imageView.setImageResource(R.drawable.edxp)
                imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
                imageView.adjustViewBounds = true
                builder.setView(imageView).setPositiveButton(R.string.OK, null).create().show()
                true
            }
        }
        if (debugMode || isMIUI) {
            val miuiProblemPreference: Preference = findPreference("miuiProblem")!!
            miuiProblemPreference.isVisible = true
            miuiProblemPreference.setOnPreferenceClickListener {
                val builder = MaterialAlertDialogBuilder(requireContext())
                val imageView = ImageView(requireContext())
                imageView.setImageResource(R.drawable.miui)
                imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
                imageView.adjustViewBounds = true
                builder.setView(imageView).setPositiveButton(R.string.OK, null).create().show()
                true
            }
        }
        findPreference<Preference>("version")!!.summary = version
        findPreference<Preference>("qqqun")!!.setOnPreferenceClickListener {
            /*
                 *
                 * 发起添加群流程。群号：某些不靠谱插件交流群(951343825) 的 key 为： AjOW9zYQyaV9LQhyqIQrjo21bXnu3JRC
                 * 调用 joinQQGroup(AjOW9zYQyaV9LQhyqIQrjo21bXnu3JRC) 即可发起手Q客户端申请加群 某些不靠谱插件交流群(951343825)
                 *
                 * @param key 由官网生成的key
                 * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
                 ******************/
            val key = "AjOW9zYQyaV9LQhyqIQrjo21bXnu3JRC"
            val intent = Intent()
            intent.data = Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D$key")
            // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            try {
                startActivity(intent)
            } catch (e: Exception) {
                val cmb = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val mClipData = ClipData.newPlainText(getString(R.string.qq_group), "951343825")
                cmb.setPrimaryClip(mClipData)
                Toast.makeText(requireContext(), R.string.already_copy_to_clipbroad, Toast.LENGTH_SHORT).show()
            }
            true
        }
        findPreference<Preference>("connect")!!.setOnPreferenceClickListener {
            startActivity(Intent.createChooser(Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:liziyuan0720@gmail.com")), getString(R.string.send_email)))
            true
        }
        findPreference<Preference>("openSource")!!.setOnPreferenceClickListener {
            val notices = Notices()
            licensesAbove.forEach {
                notices.addNotice(it)
            }
            notices.addNotice(Notice("XposedMusicNotify", "https://github.com/singleNeuron/XposedMusicNotify", "Copyright 2019 神经元", GnuLesserGeneralPublicLicense3()))
            notices.addNotice(Notice("Android", "https://source.android.com/license", "The Android Open Source Project", ApacheSoftwareLicense20()))
            notices.addNotice(Notice("XposedBridge", "https://github.com/rovo89/XposedBridge", "Copyright 2013 rovo89, Tungstwenty", ApacheSoftwareLicense20()))
            notices.addNotice(Notice("Kotlin", "https://github.com/JetBrains/kotlin", "Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.", ApacheSoftwareLicense20()))
            notices.addNotice(Notice("libsu", "https://github.com/topjohnwu/libsu", "topjohnwu", ApacheSoftwareLicense20()))
            notices.addNotice(Notice("suspension-fab", "https://github.com/userwangjf/MindLock/tree/master/suspension-fab", "Copyright [2016-09-21] [阿钟]", ApacheSoftwareLicense20()))
            notices.addNotice(Notice("gson", "https://github.com/google/gson", "Copyright 2008 Google Inc.", ApacheSoftwareLicense20()))
            notices.addNotice(Notice("alcatrazui", "https://github.com/Alcatraz323/alcatrazui", "Alcatraz323", ApacheSoftwareLicense20()))
            notices.addNotice(Notice("audiohq_md2", "https://github.com/Alcatraz323/audiohq_md2", "Alcatraz323", MITLicense()))
            licensesUnder.forEach {
                notices.addNotice(it)
            }
            LicensesDialog.Builder(requireContext()).setNotices(notices).setIncludeOwnLicense(true).build().show()
            true
        }
        findPreference<Preference>("forceNight")!!.setOnPreferenceChangeListener { _, _ ->
            requireActivity().recreate()
            true
        }
        findPreference<Preference>("autoStart")!!.setOnPreferenceClickListener {
            try {
                val intent = getAutostartSettingIntent(requireContext())
                startActivity(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            true
        }
        findPreference<Preference>("github")!!.summary = githubUrl
        findPreference<Preference>("github")!!.setOnPreferenceClickListener {
            startActivity(Intent(Intent.ACTION_VIEW,Uri.parse(githubUrl)))
            true
        }
        findPreference<Preference>("version")!!.setOnPreferenceClickListener {
            startActivity(Intent(Intent.ACTION_VIEW,Uri.parse(googlePlayUrl)))
            true
        }
        runAfter()
    }

    override fun onResume() {
        super.onResume()
        listView.overScrollMode = ListView.OVER_SCROLL_NEVER
    }

    override fun onCreateAnimator(transit: Int, enter: Boolean, nextAnim: Int): Animator? {
        //condition
        val objectAnimator: ObjectAnimator = ObjectAnimator.ofFloat(view, "alpha", 1f, 1f)
        objectAnimator.duration = requireContext().resources.getInteger(android.R.integer.config_mediumAnimTime).toLong() //time same with parent fragment's animation
        return objectAnimator
    }

    fun addPreferencesInSetting(vararg preference: Preference) {
        preference.forEach {
            findPreference<PreferenceCategory>("settings")!!.addPreference(it)
        }
    }

    companion object {
        private fun getAutostartSettingIntent(context: Context): Intent {
            var componentName: ComponentName? = null
            val brand = Build.MANUFACTURER
            val intent = Intent()
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            when (brand.toLowerCase(Locale.ROOT)) {
                "samsung" -> componentName = ComponentName("com.samsung.android.sm", "com.samsung.android.sm.app.dashboard.SmartManagerDashBoardActivity")
                "huawei" ->                 //荣耀V8，EMUI 8.0.0，Android 8.0上，以下两者效果一样
                    componentName = ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity")
                "xiaomi" -> componentName = ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")
                "vivo" -> //            componentName = new ComponentName("com.iqoo.secure", "com.iqoo.secure.safaguard.PurviewTabActivity");
                    componentName = ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")
                "oppo" -> //            componentName = new ComponentName("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity");
                    componentName = ComponentName("com.coloros.oppoguardelf", "com.coloros.powermanager.fuelgaue.PowerUsageModelActivity")
                "yulong", "360" -> componentName = ComponentName("com.yulong.android.coolsafe", "com.yulong.android.coolsafe.ui.activity.autorun.AutoRunListActivity")
                "meizu" -> componentName = ComponentName("com.meizu.safe", "com.meizu.safe.permission.SmartBGActivity")
                "oneplus" -> componentName = ComponentName("com.oneplus.security", "com.oneplus.security.chainlaunch.view.ChainLaunchAppListActivity")
                "letv" -> {
                    intent.action = "com.letv.android.permissionautoboot"
                    intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                    intent.data = Uri.fromParts("package", context.packageName, null)
                }
                else -> {
                    intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                    intent.data = Uri.fromParts("package", context.packageName, null)
                }
            }
            intent.component = componentName
            return intent
        }

        private const val KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code"
        private const val KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name"
        private const val KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage"
        val isMIUI: Boolean
            get() {
                val prop = Properties()
                val isMIUI: Boolean
                try {
                    val result = Shell.su("cat /system/build.prop").exec()
                    prop.load(ByteArrayInputStream(result.out[0].toByteArray()))
                } catch (e: Exception) {
                    e.printStackTrace()
                    return false
                }
                isMIUI = prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null
                return isMIUI
            }
    }
}