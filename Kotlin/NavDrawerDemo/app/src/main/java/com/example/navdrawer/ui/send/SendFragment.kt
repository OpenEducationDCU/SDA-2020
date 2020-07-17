package com.example.navdrawer.ui.send

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.navdrawer.R

class SendFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_send, container, false)
        val textView = root.findViewById<TextView>(R.id.text_send)
        textView.text = "This is a send fragment"
        return root
    }
}