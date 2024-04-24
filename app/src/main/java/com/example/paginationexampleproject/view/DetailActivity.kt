package com.example.paginationexampleproject.view

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.example.paginationexampleproject.R
import com.example.paginationexampleproject.viewmodal.AppViewModal

class DetailActivity : AppCompatActivity() {
    private val idPlaceHolderTV by lazy { findViewById<TextView>(R.id.idPlaceHolderTV) }
    private val idTV by lazy { findViewById<TextView>(R.id.idTV) }
    private val titlePlaceHolderTV by lazy { findViewById<TextView>(R.id.titlePlaceHolderTV) }
    private val titleTV by lazy { findViewById<TextView>(R.id.titleTV) }
    private val bodyPlaceHolderTV by lazy { findViewById<TextView>(R.id.bodyPlaceHolderTV) }
    private val bodyTV by lazy { findViewById<TextView>(R.id.bodyTV) }
    private val toolbar: Toolbar by lazy { findViewById(R.id.toolbar) }
    private val toolbarTitle: TextView by lazy { findViewById(R.id.toolbarTitle) }
    private val backArrow: ImageView by lazy { findViewById(R.id.backArrow) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = this.resources.getColor(R.color.black)
        setContentView(R.layout.detail_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowTitleEnabled(false)
        }
        toolbar.elevation = 10f
        toolbarTitle.text = this.getString(R.string.detail)
        backArrow.visibility = View.VISIBLE
        if(intent != null && intent.extras != null){
            if(intent.hasExtra("id") && !intent.getStringExtra("id").isNullOrEmpty()){
                idTV.text = intent.getStringExtra("id")
                idPlaceHolderTV.visibility = View.VISIBLE
                idTV.visibility = View.VISIBLE
            }

            if(intent.hasExtra("title") && !intent.getStringExtra("title").isNullOrEmpty()){
                titleTV.text = intent.getStringExtra("title")
                titlePlaceHolderTV.visibility = View.VISIBLE
                titleTV.visibility = View.VISIBLE
            }

            if(intent.hasExtra("body") && !intent.getStringExtra("body").isNullOrEmpty()){
                bodyTV.text = intent.getStringExtra("body")
                bodyPlaceHolderTV.visibility = View.VISIBLE
                bodyTV.visibility = View.VISIBLE
            }

            backArrow.setOnClickListener { onBackPressed() }
        }
    }
}