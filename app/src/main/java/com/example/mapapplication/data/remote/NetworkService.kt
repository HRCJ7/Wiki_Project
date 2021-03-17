package com.example.mapapplication.data.remote

import com.example.mapapplication.data.model.google.directions.Directions
import com.example.mapapplication.data.remote.response.ArticleDetailResponse
import com.example.mapapplication.data.remote.response.ArticleListResponse
import com.example.mapapplication.data.remote.response.ImageUrlResponse
import com.example.mapapplication.data.remote.response.PageInfoResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url
import javax.inject.Singleton

@Singleton
interface NetworkService {


    @GET(Endpoints.ARTICLE_LIST)
    fun doArticleListCall(
            @Query("gscoord") gsCoord: String?
    ): Single<ArticleListResponse>

    @GET(Endpoints.ARTICLE_DETAIL)
    fun doArticleDetailCall(
            @Query("pageids") pageId: Int
    ): Single<ArticleDetailResponse>

    @GET(Endpoints.PAGE_URL)
    fun doPageUrlCall(
            @Query("pageids") pageId: Int
    ): Single<PageInfoResponse>

    @GET(Endpoints.IMAGE_URL)
    fun doAImageUrlCall(
            @Query("titles") title: String
    ): Single<ImageUrlResponse>

    @GET
    fun getDirections(
            @Url url: String,
            @Query("origin") origin: String,
            @Query("destination") destination: String,
            @Query("key") key: String
    ): Single<Directions>
}