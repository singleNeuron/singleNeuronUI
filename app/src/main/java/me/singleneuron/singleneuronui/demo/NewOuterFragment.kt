package me.singleneuron.singleneuronui.demo

import androidx.fragment.app.Fragment
import me.singleneuron.singleneuronui.fragments.OuterFragment

class NewOuterFragment : OuterFragment() {

    override var fragment: Fragment? = NewFragment()

}