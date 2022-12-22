package com.marioioannou.cryptopal.adapters

import com.marioioannou.cryptopal.domain.model.coins.SparklineIn7d
import com.robinhood.spark.SparkAdapter

class SparkLineAdapter(private val data: List<Double>) : SparkAdapter() {

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(index: Int): Any {
        return data[index]
    }

    override fun getY(index: Int): Float {
        return data[index].toFloat()
    }
}