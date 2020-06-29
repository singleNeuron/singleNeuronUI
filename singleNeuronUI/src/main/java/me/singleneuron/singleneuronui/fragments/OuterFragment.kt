package me.singleneuron.singleneuronui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import me.singleneuron.singleneuronui.R
import me.singleneuron.singleneuronui.databinding.OuterFragmentBinding
import me.singleneuron.singleneuronui.interfaces.RunBeforeAndAfterAble

abstract class OuterFragment : Fragment(), RunBeforeAndAfterAble {

    protected lateinit var binding: OuterFragmentBinding
    private lateinit var floatingActionButton: FloatingActionButton
    abstract var fragment: Fragment?

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        runBefore()
        binding = OuterFragmentBinding.inflate(inflater)
        //val view = inflater.inflate(R.layout.config_fragment,container,false)
        childFragmentManager.beginTransaction().replace(R.id.fragment_outer, fragment!!).addToBackStack(fragment!!::class.java.simpleName).commit()
        floatingActionButton = binding.floatingActionButton
        floatingActionButton.setColorFilter(Color.WHITE)
        floatingActionButton.rippleColor = ContextCompat.getColor(requireContext(), R.color.colorPrimaryVariant)
        runAfter()
        return binding.root
    }

}