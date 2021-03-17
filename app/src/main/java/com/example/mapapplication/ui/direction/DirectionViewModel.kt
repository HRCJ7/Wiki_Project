package com.example.mapapplication.ui.direction

import android.os.Build
import android.text.Html
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.mapapplication.BuildConfig
import com.example.mapapplication.data.model.others.Route
import com.example.mapapplication.data.repository.MapRepository
import com.example.mapapplication.ui.base.BaseViewModel
import com.example.mapapplication.utils.log.Logger
import com.example.mapapplication.utils.network.NetworkHelper
import com.example.mapapplication.utils.rx.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable


class DirectionViewModel(
        schedulerProvider: SchedulerProvider,
        compositeDisposable: CompositeDisposable,
        networkHelper: NetworkHelper,
        private val mapRepository: MapRepository
) : BaseViewModel(schedulerProvider, compositeDisposable, networkHelper) {

    val routes: MutableLiveData<Route> = MutableLiveData()
    val routeInfoData: MutableLiveData<String> = MutableLiveData()

    companion object {
        private const val GOOGLE_BASE_URL = "https://maps.googleapis.com/maps/api/directions/json"
    }

    override fun onCreate() {

    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun fetchRoute(origin: String, destination: String) {
        if (checkInternetConnectionWithMessage()) {
            compositeDisposable.addAll(
                    mapRepository.fetchDirection(
                            GOOGLE_BASE_URL,
                            origin,
                            destination,
                            BuildConfig.API_KEY
                    )
                            .subscribeOn(schedulerProvider.io())
                            .subscribe(
                                    {
                                        val routeInfo = StringBuilder()
                                        if (it.status.equals("OK")) {
                                            val legs = it.routes[0].legs[0]
                                            val route = Route(
                                                    origin,
                                                    destination,
                                                    legs.startLocation.lat,
                                                    legs.startLocation.lng,
                                                    legs.endLocation.lat,
                                                    legs.endLocation.lng,
                                                    it.routes[0].overviewPolyline.points
                                            )
                                            routes.postValue(route)

                                            legs.steps?.forEach {
                                                routeInfo.append(
                                                        Html.fromHtml(
                                                                it.htmlInstructions,
                                                                Html.FROM_HTML_MODE_COMPACT
                                                        )
                                                ).append("\n")
                                            }

                                            routeInfoData.postValue(routeInfo.toString())

                                        } else {
                                            Logger.d("Status", it.status)
                                        }
                                    },
                                    {
                                        handleNetworkError(it)
                                    }
                            ))
        }
    }
}