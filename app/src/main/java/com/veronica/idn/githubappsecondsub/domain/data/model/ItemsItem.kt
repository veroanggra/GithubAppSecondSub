package com.veronica.idn.githubappsecondsub.domain.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ItemsItem(

    @field:SerializedName("login")
    val login: String,

    @field:SerializedName("type")
    val type: String,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,
    ) : Parcelable