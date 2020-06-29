package me.singleneuron.singleneuronui.demo

import androidx.fragment.app.Fragment
import me.singleneuron.singleneuronui.activities.AbstractMainActivity
import me.singleneuron.singleneuronui.fragments.OuterSettingFragment
import me.singleneuron.singleneuronui.views.XposedAlcatrazInteractiveCardView

class MainActivity : AbstractMainActivity() {

    override var innerFragment: Fragment? = SettingFragment()

    override fun runAfter() {
        OuterSettingFragment.addFragmentTask({
            it.binding.settingFragmentLinearLayout.addView(XposedAlcatrazInteractiveCardView(this).apply {
                firstLine = "First Line"
                secondLine = "Second Line"
            })
        })
    }

}