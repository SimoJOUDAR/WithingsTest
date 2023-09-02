package fr.mjoudar.withingstest.presentation.homepage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.mjoudar.withingstest.databinding.LayoutImageItemBinding
import fr.mjoudar.withingstest.domain.models.ImageInfo

class HomepageGridAdapter(
    private val onItemClickListener: View.OnClickListener,
    private val onContextClickListener: View.OnContextClickListener
) : RecyclerView.Adapter<HomepageGridAdapter.ImageViewHolder>() {

    private var images = listOf<ImageInfo>()

    class ImageViewHolder(val binding: LayoutImageItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = LayoutImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.binding.imageInfo = images[position]
        with(holder.itemView) {
            tag = images[position]
            setOnClickListener(onItemClickListener)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                setOnContextClickListener(onContextClickListener)
            }
            //TODO highlight image frame
        }
    }

    // Set Adapter's data from outside the class
    fun setData(data: List<ImageInfo>) {
        images = data
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return images.size
    }
}