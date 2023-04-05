package com.example.githubapi.ui.xml

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubapi.R
import com.example.githubapi.data.remote.github.search.repositories.Item
import com.example.githubapi.ui.component.extractDate


// 這邊真的太趕，一天寫完還是第一次寫...
class MyPagingAdapter : PagingDataAdapter<Item, ViewHolder>(REPO_COMPARATOR) {

    companion object {
        val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean =
                oldItem == newItem


            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean =
                oldItem == newItem
        }
    }

    class PagingViewHolder(itemView: View) : ViewHolder(itemView) {

        val repoCard: CardView = itemView.findViewById(R.id.repo_card)

        val repoName: TextView = itemView.findViewById(R.id.repo_name)
        val owner: TextView = itemView.findViewById(R.id.owner_text)
        val description: TextView = itemView.findViewById(R.id.description)
        val stars: TextView = itemView.findViewById(R.id.stars)
        val forks: TextView = itemView.findViewById(R.id.forks)
        val language: TextView = itemView.findViewById(R.id.language)
        val update: TextView = itemView.findViewById(R.id.update)

        val image: ImageView = itemView.findViewById(R.id.owner_image)


    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        (holder as PagingViewHolder).repoName.text = item?.name ?: "???"
        holder.owner.text = item?.owner?.login  ?: "???"
        holder.description.text = item?.description  ?: "???"
        holder.stars.text = "⭐  ${item?.stargazers_count.toString()}"
        holder.forks.text = "\uD83C\uDF74  ${item?.forks_count.toString()}"
        holder.language.text = "⌨  ${item?.language}"
        holder.update.text = "✨  ${item?.updated_at?.extractDate() ?: "???"}"

        val requestOptions: RequestOptions = RequestOptions()
            .placeholder(R.drawable.iconmonstr_github_1)
            .error(R.drawable.iconmonstr_github_1)

        Glide.with(holder.itemView).load(item?.owner?.avatar_url ?: "")
            .apply(requestOptions).into(holder.image)

        holder.repoCard.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            if (item != null) {
                i.data = Uri.parse(item.html_url)
                holder.itemView.context.startActivity(i)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(android.R.layout.simple_list_item_1, parent, false)

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.repo_card, parent, false)


        return PagingViewHolder(view)
    }

}