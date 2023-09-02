package fr.mjoudar.withingstest.presentation.homepage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import fr.mjoudar.withingstest.databinding.FragmentHomepageBinding
import fr.mjoudar.withingstest.domain.models.ImageInfo
import fr.mjoudar.withingstest.utils.columnNumberCalculator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import fr.mjoudar.withingstest.presentation.homepage.HomepageViewModel.ImagesUiState.Loading as Loading
import fr.mjoudar.withingstest.presentation.homepage.HomepageViewModel.ImagesUiState.Error as Error
import fr.mjoudar.withingstest.presentation.homepage.HomepageViewModel.ImagesUiState.Success as Success

@AndroidEntryPoint
class HomepageFragment : Fragment() {

    private var _binding: FragmentHomepageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomepageViewModel by viewModels()
    private lateinit var adapter: HomepageGridAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomepageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        setListeners()
        collectData()
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
        binding.recyclerview.layoutManager = with(requireContext()) {
            GridLayoutManager(this, this.columnNumberCalculator())
        }
    }

    private fun setListeners() {
        binding.searchBtn.setOnClickListener {
            binding.searchEditText.text?.let { editable ->
                with(editable.toString()) {
                    if (!isNullOrEmpty())
                        viewModel.getResults(this)
                }
            }
        }
        binding.detailsBtn.setOnClickListener {
            navigateToDetails()
        }
    }

    private fun collectData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.imageLot.collectLatest {
                    when (it) {
                        is Loading -> displayIsLoading()
                        is Error -> displayError(it.error)
                        is Success -> displayData(it.images)
                    }
                }
            }
        }
    }

    private fun navigateToDetails() {
        viewModel.getSelectedItems().let {
            if (it.size >= 2)
                findNavController().navigate(
                    HomepageFragmentDirections.actionHomepageFragmentToDetailsFragment(
                        it
                    )
                )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun displayIsLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerview.visibility = View.INVISIBLE
        binding.errorLayout.errorPage.visibility = View.GONE
    }

    private fun displayError(e: Exception) {
        binding.errorLayout.errorText.text = e.toString()
        binding.progressBar.visibility = View.GONE
        binding.recyclerview.visibility = View.GONE
        binding.errorLayout.errorPage.visibility = View.VISIBLE
    }

    private fun displayData(data: List<ImageInfo>) {
        binding.progressBar.visibility = View.GONE
        binding.recyclerview.visibility = View.VISIBLE
        binding.errorLayout.errorPage.visibility = View.GONE
        ((binding.recyclerview.adapter) as HomepageGridAdapter).setData(data)
    }

}