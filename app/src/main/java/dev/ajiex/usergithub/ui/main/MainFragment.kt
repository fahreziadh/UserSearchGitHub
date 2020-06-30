package dev.ajiex.usergithub.ui.main

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import dev.ajiex.usergithub.R
import dev.ajiex.usergithub.model.ErrorResponse
import dev.ajiex.usergithub.model.Item
import dev.ajiex.usergithub.model.SearchUserResponse
import dev.ajiex.usergithub.ui.adapter.ItemUserAdapter
import dev.ajiex.usergithub.util.EndlessRecyclerViewScrollListener
import kotlinx.android.synthetic.main.main_fragment.*


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    private lateinit var scrollListener: EndlessRecyclerViewScrollListener

    private var item_list = ArrayList<Item>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        var layoutManager = LinearLayoutManager(context)
        layoutManager.isSmoothScrollbarEnabled = true
        list_user.layoutManager = layoutManager
        list_user.adapter = ItemUserAdapter(item_list)
        scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                progress_circular.visibility = View.VISIBLE
                println("on scrolling $page")
                loadNextDataFromApi(page + 1)
            }
        }

        list_user.addOnScrollListener(scrollListener)

        search_box.setOnKeyListener { view, i, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                item_list.clear()
                progress_circular.visibility = View.VISIBLE
                (list_user.adapter as ItemUserAdapter).notifyDataSetChanged()
                scrollListener.resetState()
                loadNextDataFromApi(1)
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
    }

    fun showMessage(type: String) {
        var alertBuilder = AlertDialog.Builder(context!!)
        var alertLayout = LayoutInflater.from(context!!).inflate(R.layout.message_dialog, null)
        var lottie = alertLayout.findViewById<LottieAnimationView>(R.id.lottie)
        var message = alertLayout.findViewById<TextView>(R.id.message)
        var btn = alertLayout.findViewById<TextView>(R.id.btn)

        when(type){
            "empty"->{
                lottie.setAnimation(R.raw.empty)
                lottie.repeatCount=10
                lottie.loop(true)
                lottie.playAnimation()
                message.text=search_box.text.toString()+" Not Found"
                btn.text="Back"
            }
            "limit"->{
                lottie.setAnimation(R.raw.limit)
                lottie.repeatCount=10
                lottie.loop(true)
                lottie.playAnimation()
                message.text="API rate limit exceeded"
                btn.text="Back"
            }
        }
        alertBuilder.setView(alertLayout)
        var alertDialog = alertBuilder.create()
        alertDialog.show()
        btn.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    fun loadNextDataFromApi(page: Int) {
        if (search_box.text.toString().isNotEmpty()) {
            viewModel.searchUser(search_box.text.toString(), page, 20)
                .observe(viewLifecycleOwner,
                    Observer {
                        progress_circular.visibility = View.GONE
                        when (it) {
                            is ErrorResponse -> {
                               showMessage("limit")
                            }
                            is SearchUserResponse -> {
                                if (it.total_count==0){
                                    showMessage("empty")
                                }
                                it.items?.let { item -> item_list.addAll(item) }
                                (list_user.adapter as ItemUserAdapter).notifyDataSetChanged()
                            }
                        }
                    })
        }
    }
}