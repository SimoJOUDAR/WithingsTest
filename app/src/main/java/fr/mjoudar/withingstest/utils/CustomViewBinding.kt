package fr.mjoudar.withingstest.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import fr.mjoudar.withingstest.R
import fr.mjoudar.withingstest.domain.models.ImageInfo

class CustomViewBinding {

    companion object {

        @JvmStatic
        @BindingAdapter("cover")
        fun bindCover(imgView: ImageView, imageInfo: ImageInfo) {
            with(imageInfo.previewURL) {
                Glide.with(imgView.context)
                    .load(this)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_placeholder)
                    .into(imgView)
            }
        }

    }
}