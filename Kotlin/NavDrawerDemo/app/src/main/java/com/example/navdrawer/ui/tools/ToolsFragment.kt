package com.example.navdrawer.ui.tools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.navdrawer.R

class ToolsFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_tools, container, false)
        val textView = root.findViewById<TextView>(R.id.text_tools)
        textView.text = "This is a tools fragment"
        return root
    }
}