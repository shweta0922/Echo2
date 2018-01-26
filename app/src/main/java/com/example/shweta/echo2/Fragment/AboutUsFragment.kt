package com.example.shweta.echo2.Fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView

import com.example.shweta.echo2.R


/**
 * A simple [Fragment] subclass.
 */
class AboutUsFragment : Fragment() {

    var name : TextView?=null
    var email: TextView?=null
    var phone :TextView?=null
    var profile : RelativeLayout?=null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater!!.inflate(R.layout.fragment_about_us, container, false)
        activity.title="About Us"
        name = view?.findViewById(R.id.name)
        email=view?.findViewById(R.id.email)
        phone =view?.findViewById(R.id.contact)
        profile=view?.findViewById(R.id.pro)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        var item = menu?.findItem(R.id.action_sort)
        item?.isVisible = false
    }

}// Required empty public constructor
