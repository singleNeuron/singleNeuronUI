package me.singleneuron.singleneuronui.demo

import android.os.Bundle
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import me.singleneuron.singleneuronui.utils.LogUtils

class NewFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.new_fragment)
        findPreference<EditTextPreference>("addLog")!!.setOnPreferenceChangeListener { _, newValue ->
            LogUtils.addLog(newValue.toString(),requireContext())
            true
        }
    }
}