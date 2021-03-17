package com.example.mapapplication.utils.map

import android.content.Context

object DisplayUtility {
    /**
     * px to dp
     *
     * @param pxValue
     * @return
     */
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }
}