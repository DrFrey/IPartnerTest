package com.example.ipartnertest.data


import com.google.gson.annotations.SerializedName

data class EntriesResponse(
    @SerializedName("data")
    val `data`: List<List<Entry?>?>?,
    @SerializedName("status")
    val status: Int?
) {
    data class Entry(
        @SerializedName("body")
        val body: String?,
        @SerializedName("da")
        val da: String?,
        @SerializedName("dm")
        val dm: String?,
        @SerializedName("id")
        val id: String?
    )
}