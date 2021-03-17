package com.example.mapapplication.data.remote

object Endpoints {
    const val ARTICLE_LIST = "w/api.php?action=query&list=geosearch&gsradius=10000&gslimit=50&format=json"
    const val ARTICLE_DETAIL= "w/api.php?action=query&prop=info|extracts|description|images&format=json&exintro=1&explaintext=1&exsectionformat=plain"
    const val IMAGE_URL= "w/api.php?action=query&prop=pageimages&format=json&pithumbsize=100"
    const val PAGE_URL= "w/api.php?action=query&prop=info&inprop=url&format=json"
}