package com.project.moviedigger.views

import android.content.Context
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Toast
import com.project.moviedigger.R

abstract class PopupList(

) : PopupMenu.OnMenuItemClickListener {
    private var popup: PopupMenu? = null
    protected lateinit var view: View

    protected var result: String = ""
    protected val dropdownArrow: String = " ▼ "

    fun show(view: View, layoutId: Int) {
        this.view = view
        popup = PopupMenu(view.context, view)
        popup!!.setOnMenuItemClickListener(this)
        popup!!.inflate(layoutId)
        popup!!.show()
    }
}
