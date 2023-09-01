package fr.mjoudar.withingstest.presentation.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import fr.mjoudar.withingstest.R
import fr.mjoudar.withingstest.databinding.FragmentDetailsBinding
import fr.mjoudar.withingstest.domain.models.ImageInfo
import fr.mjoudar.withingstest.presentation.homepage.HomepageViewModel

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailsViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
    }

    private fun loadData() {
        arguments?.let { bundle ->
            val images = bundle.getParcelableArray(IMAGES) as Array<ImageInfo>
            if (images.isNotEmpty() && images.size >= 2)
                viewModel.setData(images.toList())
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val IMAGES = "images"
    }
}