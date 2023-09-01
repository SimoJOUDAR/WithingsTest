package fr.mjoudar.withingstest.presentation.homepage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.android.volley.RequestQueue
import dagger.hilt.android.AndroidEntryPoint
import fr.mjoudar.withingstest.databinding.FragmentHomepageBinding
import fr.mjoudar.withingstest.domain.models.ImageInfo


@AndroidEntryPoint
class HomepageFragment : Fragment() {

    private var _binding: FragmentHomepageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomepageViewModel by viewModels()
    private lateinit var adapter: HomepageGridAdapter
    lateinit var request: RequestQueue

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomepageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        setListeners()
    }

    private fun setRecyclerView() {
        val onItemClickListener = View.OnClickListener { itemView ->
            val imageInfo = itemView.tag as ImageInfo?
            imageInfo?.let {
                viewModel.itemClicked(it)
            }
        }
        val onContextClickListener = View.OnContextClickListener { true }
        adapter = HomepageGridAdapter(onItemClickListener, onContextClickListener)
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = GridLayoutManager(requireContext(), columnNumberCalculator()) // For screen size adaptability
    }

    private fun setListeners() {
        binding.detailsBtn.setOnClickListener {
            navigateToDetails()
        }
    }

    private fun navigateToDetails() {
        val images = viewModel.getSelectedItems()
        if (images.size >= 2)
            findNavController().navigate(HomepageFragmentDirections.actionHomepageFragmentToDetailsFragment(images))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fetchData() {

    }

    /**
     * Return the number of columns that can fit in a Grid depending on the screen dimensions
     */
    private fun columnNumberCalculator() : Int {
        val recyclerViewItemWidth = 176
        val displayMetrics = requireContext().resources.displayMetrics
        val dpWidth = displayMetrics.widthPixels / displayMetrics.density
        return (dpWidth / recyclerViewItemWidth).toInt()
    }

}