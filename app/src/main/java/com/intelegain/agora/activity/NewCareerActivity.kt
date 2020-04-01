package com.intelegain.agora.activity

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.intelegain.agora.R
import com.intelegain.agora.fragmments.CareersFragment

class NewCareerActivity : AppCompatActivity() {
    private val TAG = javaClass.simpleName
    private var toolbar: Toolbar? = null
    private var toolbar_title: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_career)
        InitializeWidget()
        setToolbar()
        setEventClickListener()
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.containerView, CareersFragment())
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun InitializeWidget() {
        toolbar = findViewById<View>(R.id.custom_toolbar) as Toolbar
        toolbar_title = findViewById<View>(R.id.toolbar_title) as TextView
    }

    /**
     * Set the event click listener for views
     */
    private fun setEventClickListener() {
        toolbar!!.setNavigationOnClickListener { onBackPressed() }
    }

    /**
     * Set toolbar properties
     */
    private fun setToolbar() {
        setSupportActionBar(toolbar)
        val strTitleArray = resources.getStringArray(R.array.menu_drawer)
        val iIndexForContactUs = 10 // Index no of contact us in string.xml file
        val strScreenTitle = strTitleArray[iIndexForContactUs]
        supportActionBar!!.title = strScreenTitle
        toolbar_title!!.text = strScreenTitle
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}