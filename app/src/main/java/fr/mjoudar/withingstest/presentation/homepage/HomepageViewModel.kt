package fr.mjoudar.withingstest.presentation.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.mjoudar.withingstest.data.repository.ImageRepository
import fr.mjoudar.withingstest.domain.models.ImageInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomepageViewModel @Inject constructor(
    private val repository: ImageRepository
) : ViewModel() {

    private var selectedItems = mutableListOf<ImageInfo>()

    private val _imageLot = MutableStateFlow<ImagesUiState>(ImagesUiState.Loading)
    val imageLot = _imageLot.asStateFlow().stateIn(
        scope = viewModelScope,
        initialValue = ImagesUiState.Loading,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000)
    )

    init {
        getResults("panda")
    }

    fun getResults(input: String) = viewModelScope.launch(Dispatchers.IO) {
        val defaultException =
            Exception("An unidentified error occurred. We couldn't load the data. Please, check your internet connection.")
        val response = repository.getData(input)
        if (response.isSuccessful) {
            _imageLot.emit(ImagesUiState.Success(response.body.hits?.map { it.toImageInfo() }
                ?: listOf()))
        } else {
            val e = response.exception ?: defaultException
            _imageLot.emit(ImagesUiState.Error(e))
        }
    }

    //
    fun itemClicked(image: ImageInfo) {
        if (selectedItems.contains(image))
            selectedItems.remove(image)
        else
            selectedItems.add(image)
    }

    fun getSelectedItems(): Array<ImageInfo> {
        return selectedItems.toTypedArray()
    }

    sealed class ImagesUiState {
        object Loading : ImagesUiState()
        data class Success(val images: List<ImageInfo>) : ImagesUiState()
        data class Error(val error: Exception) : ImagesUiState()
    }
}