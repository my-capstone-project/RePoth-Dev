package com.capstone.repoth

import com.capstone.repoth.data.response.RepothDetailResponse

object DataDummy {

    fun generateDummyRepothResponse(): List<RepothDetailResponse> {
        val items: MutableList<RepothDetailResponse> = arrayListOf()
        for (i in 0..100) {
            val quote = RepothDetailResponse(
                i.toString(),
                "createdAt + $i",
                "description $i",
                0.0,
                0.0,
                "photoUrl $i",
            )
            items.add(quote)
        }
        return items
    }
}