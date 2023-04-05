package com.example.githubapi.ui.xml

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.githubapi.R

//class LoaderStateAdapter(private val retry: () -> Unit) :
//    LoadStateAdapter<LoaderStateAdapter.LoaderViewHolder>() {
//
//    override fun onBindViewHolder(holder: LoaderViewHolder, loadState: LoadState) {
//        holder.bind(loadState)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoaderViewHolder {
//        return LoaderViewHolder.getInstance(parent, retry)
//    }
//
//    /**
//     * view holder class for footer loader and error state handling
//     */
//    class LoaderViewHolder(view: View, retry: () -> Unit) : RecyclerView.ViewHolder(view) {
//
//        companion object {
//            //get instance of the DoggoImageViewHolder
//            fun getInstance(parent: ViewGroup, retry: () -> Unit): LoaderViewHolder {
//                val inflater = LayoutInflater.from(parent.context)
////                val view = inflater.inflate(R.layout.item_doggo_loader, parent, false)
//                val textView = TextView()
//                return LoaderViewHolder(view, retry)
//            }
//        }
//
////        val motionLayout: MotionLayout = view.findViewById(R.id.mlLoader)
//
////        init {
////            view.findViewById<Button>(R.id.btnRetry).setOnClickListener {
////                retry()
////            }
////        }
//
//        fun bind(loadState: LoadState) {
////            if (loadState is LoadState.Loading) {
////                motionLayout.transitionToEnd()
////            } else {
////                motionLayout.transitionToStart()
////            }
//        }
//    }
//}

class PostsLoadStateAdapter(
//    private val adapter: MyPagingAdapter
) : LoadStateAdapter<NetworkStateItemViewHolder>() {
    override fun onBindViewHolder(holder: NetworkStateItemViewHolder, loadState: LoadState) {
        holder.bindTo(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): NetworkStateItemViewHolder {
        return NetworkStateItemViewHolder(parent)
    }
}

class NetworkStateItemViewHolder(
    parent: ViewGroup,
//    private val retryCallback: () -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.network_state_item, parent, false)
) {
    private val progressBar = itemView.findViewById<ProgressBar>(R.id.progress_bar)
    private val errorMsg = itemView.findViewById<TextView>(R.id.error_msg)
//    private val retry = itemView.findViewById<Button>(R.id.retry_button)
//        .also {
//            it.setOnClickListener { retryCallback() }
//        }

    fun bindTo(loadState: LoadState) {
        progressBar.isVisible = loadState is LoadState.Loading
//        retry.isVisible = loadState is Error
        errorMsg.isVisible = loadState is LoadState.Error
        if (loadState is LoadState.Error) {
            errorMsg.text = loadState.error.message
        }
//        errorMsg.isVisible = !(loadState as? Error)?.error?.message.isNullOrBlank()
//        errorMsg.text = (loadState as? Error)?.error?.message
    }
}