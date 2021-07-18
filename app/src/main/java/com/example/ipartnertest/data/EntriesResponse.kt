package com.example.ipartnertest.data


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

// Response class for the list of entries.

data class EntriesResponse(
    @SerializedName("data")
    val `data`: List<List<Entry?>?>?,
    @SerializedName("status")
    val status: Int?
)

@Parcelize
data class Entry(
    @SerializedName("body")
    val body: String?,
    @SerializedName("da")
    val da: String?,
    @SerializedName("dm")
    val dm: String?,
    @SerializedName("id")
    val id: String?
) : Parcelable