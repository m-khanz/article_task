package com.task.nytimesarticles.data.repository

import com.google.gson.annotations.SerializedName

class ArticlesDTO {
    @SerializedName("results")
    var articlesList: List<ArticleRes>? = null

    class ArticleRes {
        var id: Long = 0
        var byline: String? = null
        var title: String? = null

        @SerializedName("abstract")
        var description: String? = null

        @SerializedName("published_date")
        var publishedDate: String? = null

        @SerializedName("url")
        var url: String? = null

        var media: List<MediaBean>? = null

        class MediaBean {
            @SerializedName("media-metadata")
            var mediaMetaData: List<MediaMetaDataBean>? = null

            class MediaMetaDataBean {
                var url: String? = null
            }
        }
    }
}