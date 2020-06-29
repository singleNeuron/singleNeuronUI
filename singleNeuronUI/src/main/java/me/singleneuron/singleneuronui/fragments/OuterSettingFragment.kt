package me.singleneuron.singleneuronui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import me.singleneuron.singleneuronui.R
import me.singleneuron.singleneuronui.databinding.OuterSettingFragmentBinding

object OuterSettingFragment : Fragment() {

    lateinit var binding : OuterSettingFragmentBinding
    lateinit var innerSettingFragment: Fragment
    val runAfterTasks: MutableList<(OuterSettingFragment)->Unit> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = OuterSettingFragmentBinding.inflate(inflater)
        childFragmentManager.beginTransaction().setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out).replace(R.id.setting_fragment_inner, innerSettingFragment).addToBackStack(innerSettingFragment::class.java.simpleName).commit()
        runAfterTasks.forEach {
            it(this)
        }
        runAfterTasks.clear()
        return binding.root
    }

    fun isInnerSettingFragmentInitialized() : Boolean = this::innerSettingFragment.isInitialized

    fun addFragmentTask(vararg task: (OuterSettingFragment)->Unit) {
        task.forEach {
            runAfterTasks.add(it)
        }
    }
}