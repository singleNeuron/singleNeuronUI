package me.singleneuron.singleneuronui.demo

import androidx.preference.Preference
import me.singleneuron.singleneuronui.fragments.InnerSettingFragment
import me.singleneuron.singleneuronui.fragments.LogFragment

class SettingFragment : InnerSettingFragment()  {

    override val githubUrl: String = "https://github.com/singleNeuron"
    override val googlePlayUrl: String = "https://play.google.com/store"
    override val version: String = BuildConfig.VERSION_NAME

    override fun runAfter() {
        addPreferencesInSetting(
            Preference(requireContext()).apply {
                title = "Simple Preference"
                summary = "Simple Preference Summary"
                onPreferenceClickListener = Preference.OnPreferenceClickListener {
                    (requireActivity() as MainActivity).switchFragment(NewFragment::class.java)
                    true
                }
            },
            Preference(requireContext()).apply {
                title = "Floating Button Preference"
                summary = "Floating Button Preference Summary"
                onPreferenceClickListener = Preference.OnPreferenceClickListener {
                    (requireActivity() as MainActivity).switchFragment(NewOuterFragment::class.java)
                    true
                }
            },
            Preference(requireContext()).apply {
                title = "Log Preference"
                summary = "Log Preference Summary"
                onPreferenceClickListener = Preference.OnPreferenceClickListener {
                    (requireActivity() as MainActivity).switchFragment(LogFragment::class.java)
                    true
                }
            }
        )
    }

}