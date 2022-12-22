package com.marioioannou.cryptopal.domain.model.coins


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SparklineIn7d(
    @SerializedName("price")
    val price: List<Double>?
): Parcelable