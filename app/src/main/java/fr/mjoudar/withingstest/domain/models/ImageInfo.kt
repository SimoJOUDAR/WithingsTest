package fr.mjoudar.withingstest.domain.models

import android.os.Parcelable
import androidx.databinding.BaseObservable
import kotlinx.android.parcel.Parcelize
import java.net.URL

@Parcelize
data class ImageInfo(
    val id: Int,
    val previewURL: String,
    val url: String,
    val views: Int,
    val likes: Int,
    val downloads: Int,
    val tags: String,
    val isChecked: Boolean = false
) : Parcelable, BaseObservable()