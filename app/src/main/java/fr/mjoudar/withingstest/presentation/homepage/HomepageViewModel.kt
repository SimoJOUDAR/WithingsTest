package fr.mjoudar.withingstest.presentation.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.mjoudar.withingstest.data.repository.ImageRepository
import fr.mjoudar.withingstest.domain.models.ImageInfo
import fr.mjoudar.withingstest.utils.Constants.Companion.UNKNOWN_EXCEPTION
import fr.mjoudar.withingstest.utils.toImageInfo
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

    private var items = mutableListOf<ImageInfo>()

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
        val response = repository.getData(input)
        if (response.succeeded) {
            items = response.body.toImageInfo()
            _imageLot.emit(ImagesUiState.Success(items))
        } else {
            val e = response.exception ?: UNKNOWN_EXCEPTION
            _imageLot.emit(ImagesUiState.Error(e))
        }
    }

    fun itemClicked(position: Int) {
        items[position] = items[position].copy(isChecked = !items[position].isChecked)
            viewModelScope.launch(Dispatchers.IO) {
                _imageLot.emit(ImagesUiState.Success(items, position))
            }
    }

    fun getSelectedItems(): Array<String> {
        return items.filter { it.isChecked }.map { it.url }.toTypedArray()
    }

    sealed class ImagesUiState {
        object Loading : ImagesUiState()
        data class Success(val images: List<ImageInfo>, val position: Int? = null) : ImagesUiState()
        data class Error(val error: Exception) : ImagesUiState()
    }
}