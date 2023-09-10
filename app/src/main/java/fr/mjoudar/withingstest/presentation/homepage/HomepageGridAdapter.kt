package fr.mjoudar.withingstest.presentation.homepage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.mjoudar.withingstest.databinding.LayoutImageItemBinding
import fr.mjoudar.withingstest.domain.models.ImageInfo

class HomepageGridAdapter(
    private val onItemClickListener: View.OnClickListener
) : RecyclerView.Adapter<HomepageGridAdapter.ImageViewHolder>() {

    private var images = listOf<ImageInfo>()

    class ImageViewHolder(val binding: LayoutImageItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = LayoutImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        with(images[position]) {
            holder.binding.imageInfo = this
            holder.binding.frame.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
        with(holder.itemView) {
            tag = position
            setOnClickListener(onItemClickListener)
        }
    }

    fun setData(data: List<ImageInfo>, position: Int? = null) {
        images = data
        position?.let { notifyItemChanged(it) } ?: notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return images.size
    }
}