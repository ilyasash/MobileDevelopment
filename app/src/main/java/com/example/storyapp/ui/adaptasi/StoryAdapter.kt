package com.example.storyapp.ui.adaptasi

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.storyapp.data.reponse.ListStoryItem
import com.example.storyapp.databinding.ItemStoryBinding
import com.example.storyapp.ui.detail.DetailStoryActivity

class StoryAdapter: ListAdapter<ListStoryItem, StoryAdapter.MyViewHolder>(CALLBACK) {
    class MyViewHolder(private val binding: ItemStoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(listStories: ListStoryItem) {
            binding.apply {
                photoUrl.load(listStories.photoUrl)
                textName.text = "${listStories.name}"
                textDescriptions.text = "${listStories.description}"

                root.setOnClickListener {
                    Intent(root.context, DetailStoryActivity::class.java).also {
                        it.putExtra(DetailStoryActivity.EXTRA_PHOTO_URL, listStories.photoUrl)
                        it.putExtra(DetailStoryActivity.EXTRA_NAME, listStories.name)
                        it.putExtra(DetailStoryActivity.EXTRA_DESC, listStories.description)

                        val optionsCompat: ActivityOptionsCompat =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(
                                itemView.context as Activity,
                                Pair(photoUrl, "photo"),
                                Pair(textName, "name"),
                                Pair(textDescriptions, "description"),
                            )

                        root.context.startActivity(it, optionsCompat.toBundle())
                    }
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val items = getItem(position)
        holder.bind(items)
    }

    /*companion object {
        val CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }*/
    companion object {
        val CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return areContentsTheSame(oldItem, newItem)
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }

}