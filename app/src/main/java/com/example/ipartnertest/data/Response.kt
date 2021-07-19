package com.example.ipartnertest.data


import com.google.gson.annotations.SerializedName

// Common response class for various types of responses, except for the list for entries.
// Using EntriesResponse for that.

data class Response(
    @SerializedName("status")
    val status: Int?,
    @SerializedName("data")
    val `data`: Data?,
    @SerializedName("error")
    val error: String?
)

data class Data(
    @SerializedName("id")
    val id: String?,
    @SerializedName("result")
    val result: Boolean?,
    @SerializedName("session")
    val session: String
)