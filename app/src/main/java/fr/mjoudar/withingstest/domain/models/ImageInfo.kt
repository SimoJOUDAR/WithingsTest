package fr.mjoudar.withingstest.domain.models

import android.os.Parcelable
import androidx.databinding.BaseObservable
import kotlinx.android.parcel.Parcelize
import java.net.URL

@Parcelize
data class ImageInfo(
    val url: String? = null,
    val views: Long? = null,
    val likes: Long? = null,
    val downloads: Long? = null,
    val tags: String? = null
) : Parcelable, BaseObservable()