package com.task.nytimesarticles.ui.main.viewholder

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.task.nytimesarticles.R
import coil.load
import com.task.nytimesarticles.databinding.ItemArticleBinding
import com.task.nytimesarticles.model.Article
import kotlin.coroutines.coroutineContext

class ArticleViewHolder(private val binding: ItemArticleBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(article: Article, onItemClicked: (Article, ImageView) -> Unit) {
        binding.articleTitle.text = article.title
        binding.articleAuthor.text = article.byline
        binding.articlePublishDate.text = "Published Date: "+article.publishedDate
        binding.ivArticle.load(article.imageUrl) {
            placeholder(R.drawable.ic_placeholder)
            error(R.drawable.image_not_found)
        }

        binding.root.setOnClickListener {
            onItemClicked(article, binding.ivArticle)
        }
    }
}
