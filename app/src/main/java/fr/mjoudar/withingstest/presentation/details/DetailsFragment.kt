package fr.mjoudar.withingstest.presentation.details

import android.Manifest
import android.graphics.ImageDecoder
import android.graphics.drawable.AnimatedImageDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.work.WorkInfo
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint
import fr.mjoudar.withingstest.R
import fr.mjoudar.withingstest.databinding.FragmentDetailsBinding
import fr.mjoudar.withingstest.presentation.homepage.HomepageViewModel
import fr.mjoudar.withingstest.utils.Constants.Companion.GIF_FILE_NAME
import fr.mjoudar.withingstest.utils.Constants.Companion.URLS
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest
import java.io.File


@AndroidEntryPoint
class DetailsFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailsViewModel by viewModels()
    private val requestCodePermissions = 111

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestStoragePermissions()
    }

    private fun loadData() {
        arguments?.let { bundle ->
            (bundle.getStringArray(URLS))?.let {
                if (it.isNotEmpty() && it.size >= 2) {
                    viewModel.createGif(requireContext(), it)
                    collectData()
                }
            }
        }
    }

    private fun collectData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.processingState.collectLatest {
                    when (it) {
                        DetailsViewModel.GifUiState.Loading -> displayProcessingLayout()
                        is DetailsViewModel.GifUiState.Success -> displayGif(it.fileName)
                        is DetailsViewModel.GifUiState.Error -> displayErrorLayout()
                    }
                }
            }
        }
    }

    private fun displayGif(fileName: String) {
        var path: String
        val externalFilesDir = requireContext().getExternalFilesDir(null)
        with(File(externalFilesDir, fileName)) {
            if (exists())
                path = absolutePath
            else {
                displayErrorLayout()
                return
            }
        }
        binding.processingLayout.processingLayout.visibility = View.GONE
        binding.gif.visibility = View.VISIBLE
        Glide.with(this)
            .asGif()
            .load(path)
            .error(R.drawable.ic_placeholder)
            .apply(RequestOptions().fitCenter())
            .into(binding.gif)
    }

    private fun displayProcessingLayout() {
        val source: ImageDecoder.Source =
            ImageDecoder.createSource(resources, R.drawable.ic_processing)
        val drawable = ImageDecoder.decodeDrawable(source)
        binding.processingLayout.processingLayout.visibility = View.VISIBLE
        binding.processingLayout.processingGif.setImageDrawable(drawable)
        (drawable as? AnimatedImageDrawable)?.start()
    }

    private fun displayErrorLayout() {
        binding.processingLayout.processingLayout.visibility = View.GONE
        binding.errorLayout.errorPage.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun requestStoragePermissions() {
        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (EasyPermissions.hasPermissions(requireContext(), *permissions)) {
            loadData()
        } else {
            EasyPermissions.requestPermissions(
                PermissionRequest.Builder(
                    this,
                    requestCodePermissions,
                    *permissions
                )
                    .setRationale(getString(R.string.permission_rationale))
                    .setPositiveButtonText(getString(R.string.permission_allow))
                    .setNegativeButtonText(getString(R.string.permission_deny))
                    .build()
            )
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        loadData()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        AppSettingsDialog.Builder(this).build().show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}