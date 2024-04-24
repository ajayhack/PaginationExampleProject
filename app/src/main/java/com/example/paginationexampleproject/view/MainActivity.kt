package com.example.paginationexampleproject.view

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.paginationexampleproject.R
import com.example.paginationexampleproject.modal.PlaceHolderModal
import com.example.paginationexampleproject.utils.AppUtils
import com.example.paginationexampleproject.viewmodal.AppViewModal
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private var viewModal : AppViewModal? = null
    private val toolbar: Toolbar by lazy { findViewById(R.id.toolbar) }
    private val placeHolderRV: RecyclerView by lazy { findViewById(R.id.placeHolderRV) }
    private var postsList : MutableList<PlaceHolderModal>? = mutableListOf()
    private var paginationList : List<List<PlaceHolderModal>>? = mutableListOf()
    private var pageNumber = 0
    private var progressDialog : Dialog? = null
    private val placeHolderAdapter by lazy { PlaceHolderAdapter(postsList ,  ::onItemClick) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = this.resources.getColor(R.color.black)
        viewModal = ViewModelProvider(this)[AppViewModal ::class.java]
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowTitleEnabled(false)
        }
        toolbar.elevation = 10f
        progressDialog = AppUtils.setProgressDialog(this@MainActivity)

        loadFirstPageItem(page = pageNumber)

        //recyclerview onScrollStateChanges:-
        placeHolderRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = LinearLayoutManager::class.java.cast(recyclerView.layoutManager)
                val totalItemCount = layoutManager?.getItemCount()?:0
                val lastVisible = layoutManager?.findLastVisibleItemPosition()
                if (dx == RecyclerView.SCROLL_STATE_IDLE && totalItemCount > 0 && (lastVisible?.plus(1) ?: 0) == totalItemCount) {
                    pageNumber += 1
                    loadMoreItems(page = pageNumber)
                }
            }
        })
    }

    private fun loadFirstPageItem(page: Int? = null){
            progressDialog?.show()
            lifecycleScope.launch(Dispatchers.IO) {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModal?.getPlaceHolderPageData()
                        viewModal?.placeHolderPageFlowData?.collect {
                            if(it.isNotEmpty()){
                                paginationList = it.chunked(it.size.div(10) ?: 1)
                                postsList = paginationList?.get(page ?: 0)?.toMutableList()
                                Log.d("DataSourcePage:- ", postsList?.size.toString())
                                withContext(Dispatchers.Main) {
                                    refreshRVData(page)
                                }
                            }
                        }
                    }
            }
    }

    private fun loadMoreItems(page: Int? = null){
        if(pageNumber > (paginationList?.size?.minus(1) ?: 0)) return
        lifecycleScope.launch(Dispatchers.Default) {
            postsList?.addAll(paginationList?.get(page ?: 0)?.toMutableList() ?: mutableListOf())
            Log.d("DataSourcePage:- ", postsList?.size.toString())
            withContext(Dispatchers.Main){
                refreshRVData(page)
            }
        }
    }

    private fun refreshRVData(page: Int? = 0){
        println("HolderCount:- " + placeHolderAdapter.itemCount)
        if(progressDialog?.isShowing == true) progressDialog?.dismiss()
        if(!postsList.isNullOrEmpty()){
            if(page == 0){
                placeHolderRV.apply {
                    adapter = placeHolderAdapter
                    layoutManager = LinearLayoutManager(this@MainActivity)
                    itemAnimator = DefaultItemAnimator()
                }
            }else{
                val startIndex = placeHolderAdapter.itemCount
                placeHolderAdapter.notifyItemRangeInserted(startIndex , postsList?.size ?: 0)
            }
        }
    }

    //region=================On Item click event Handle:-
    private fun onItemClick(position : Int){
        if(position > -1){
            val intent = Intent(this , DetailActivity :: class.java)
            intent.putExtra("id" , postsList?.get(position)?.id)
            intent.putExtra("title" , postsList?.get(position)?.title)
            intent.putExtra("body" , postsList?.get(position)?.body)
            startActivity(intent)
        }
    }
    //endregion

    override fun onBackPressed() {
        super.onBackPressed()
        this.finishAffinity()
    }
}

class PlaceHolderAdapter(private var contentList : MutableList<PlaceHolderModal>?,
                            private var onItemClickEvent: (Int) -> Unit) :
    RecyclerView.Adapter<PlaceHolderAdapter.RomanticViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RomanticViewHolder {
        return RomanticViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false))
    }

    override fun onBindViewHolder(holder: RomanticViewHolder, position: Int) {
        holder.bindData(position)
    }

    override fun getItemCount(): Int = contentList?.size ?: 0

    inner class RomanticViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val idTV = itemView.findViewById<TextView>(R.id.idTV)
        val idPlaceHolderTV = itemView.findViewById<TextView>(R.id.idPlaceHolderTV)
        val titleTV = itemView.findViewById<TextView>(R.id.titleTV)
        val titlePlaceHolderTV = itemView.findViewById<TextView>(R.id.titlePlaceHolderTV)
        val container = itemView.findViewById<ConstraintLayout>(R.id.container)

        fun bindData(position: Int){
            val model = contentList?.get(position)
            if(!model?.id.isNullOrEmpty()){
                idTV.text = model?.id
                idTV.visibility = View.VISIBLE
                idPlaceHolderTV.visibility = View.VISIBLE
            }else{
                idTV.visibility = View.GONE
                idPlaceHolderTV.visibility = View.GONE
            }
            if(!model?.title.isNullOrEmpty()){
                titleTV.text = model?.title
                titleTV.visibility = View.VISIBLE
                titlePlaceHolderTV.visibility = View.VISIBLE
            }else{
                titleTV.visibility = View.GONE
                titlePlaceHolderTV.visibility = View.GONE
            }

        }

        init {
            container.setOnClickListener { onItemClickEvent(adapterPosition) }
        }
    }
}