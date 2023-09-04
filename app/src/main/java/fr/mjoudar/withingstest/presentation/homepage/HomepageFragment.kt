package fr.mjoudar.withingstest.presentation.homepage

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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
import fr.mjoudar.withingstest.utils.hideSoftInput
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
            (itemView.tag as Int).let { position ->
                viewModel.itemClicked(position)
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
            requireContext().hideSoftInput(it.windowToken)
        }
        binding.detailsBtn.setOnClickListener {
            navigateToDetails()
        }
    }

    private fun collectData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.imageLot.collectLatest {
                    Log.d("Panda_test", "collected")
                    when (it) {
                        is Loading -> displayIsLoading()
                        is Error -> displayError(it.error)
                        is Success -> displayData(it.images, it.position)
                    }
                }
            }
        }
    }

    private fun navigateToDetails() {
        viewModel.getSelectedItems().let {
            if (it.size >= 2)
                findNavController().navigate(
                    HomepageFragmentDirections.actionHomepageFragmentToDetailsFragment(it)
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

    private fun displayData(data: List<ImageInfo>, position: Int?) {
        binding.progressBar.visibility = View.GONE
        binding.recyclerview.visibility = View.VISIBLE
        binding.errorLayout.errorPage.visibility = View.GONE
        ((binding.recyclerview.adapter) as HomepageGridAdapter).setData(data, position)
    }

}