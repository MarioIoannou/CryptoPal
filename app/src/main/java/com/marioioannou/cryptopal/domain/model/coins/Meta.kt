package com.marioioannou.cryptopal.domain.model.coins


import com.google.gson.annotations.SerializedName

data class Meta(
    @SerializedName("hasNextPage")
    val hasNextPage: Boolean,
    @SerializedName("hasPreviousPage")
    val hasPreviousPage: Boolean,
    @SerializedName("itemCount")
    val itemCount: Int,
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("pageCount")
    val pageCount: Int
)