package com.marioioannou.cryptopal.domain.model.news


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class New(
    @SerializedName("bigImg")
    val bigImg: Boolean?,
    @SerializedName("content")
    val content: Boolean?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("feedDate")
    val feedDate: Long?,
    @SerializedName("icon")
    val icon: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("imgURL")
    val imgURL: String?,
    @SerializedName("link")
    val link: String?,
    @SerializedName("shareURL")
    val shareURL: String?,
    @SerializedName("source")
    val source: String?,
    @SerializedName("title")
    val title: String?
) : Parcelable