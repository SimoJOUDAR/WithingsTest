package fr.mjoudar.withingstest.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import fr.mjoudar.withingstest.R
import fr.mjoudar.withingstest.domain.models.ImageInfo

class CustomViewBinding {

    companion object {

        @JvmStatic
        @BindingAdapter("cover")
        fun bindCover(imgView: ImageView, imageInfo: ImageInfo) {
            with(imageInfo.previewURL) {
                if (isNotEmpty()) {
                    imgView.load(this) {
                        placeholder(R.drawable.ic_placeholder)
                        crossfade(true)
                        error(R.drawable.ic_placeholder)
                    }
                } else imgView.load(R.drawable.ic_placeholder)
            }
        }

    }
}