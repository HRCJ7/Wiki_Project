package com.example.mapapplication.data.repository


import com.example.mapapplication.data.model.ArticleQuery
import com.example.mapapplication.data.model.ImageQuery
import com.example.mapapplication.data.model.PageQuery
import com.example.mapapplication.data.model.Query
import com.example.mapapplication.data.model.google.directions.Directions
import com.example.mapapplication.data.remote.NetworkService
import io.reactivex.Single
import javax.inject.Inject

class MapRepository @Inject constructor(
        private val networkService: NetworkService
) {
    fun fetchArticleList(gsCoord: String): Single<Query> =
            networkService.doArticleListCall(gsCoord).map { it.query }

    fun fetchArticleDetail(pageId: Int): Single<ArticleQuery> =
            networkService.doArticleDetailCall(pageId).map { it.query }

    fun fetchPageUrl(pageId: Int): Single<PageQuery> =
            networkService.doPageUrlCall(pageId).map { it.query }

    fun fetchImageUrl(title: String): Single<ImageQuery> =
            networkService.doAImageUrlCall(title).map { it.query }

    fun fetchDirection(
            url: String,
            origin: String,
            destination: String,
            key: String
    ): Single<Directions> =
            networkService.getDirections(url, origin, destination, key).map { it }
}