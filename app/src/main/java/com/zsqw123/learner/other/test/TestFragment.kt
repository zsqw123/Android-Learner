package com.zsqw123.learner.other.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.zsqw123.learner.databinding.FragTestBinding

/**
 * Author zsqw123
 * Create by damyjy
 * Date 2021/10/14 21:26
 */
class TestFragment : Fragment() {
    private lateinit var binding: FragTestBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        println("--->FragCreateView<---")
        binding = FragTestBinding.inflate(layoutInflater, container, false)
        binding.fragTestTv.setOnClickListener { parentFragmentManager.beginTransaction().remove(this).commitAllowingStateLoss() }
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("--->FragCreate<---")
    }

    override fun onResume() {
        super.onResume()
        println("--->FragResume<---")
        binding.fragTestTv.text = "666"
    }

    override fun onDestroy() {
        super.onDestroy()
        println("--->FragDestroy<---")
    }
}