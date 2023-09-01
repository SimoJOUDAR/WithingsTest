package fr.mjoudar.withingstest.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.mjoudar.withingstest.domain.models.ImageInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailsViewModel @Inject constructor(): ViewModel() {


    private val _imageLot = MutableStateFlow<List<ImageInfo>>(listOf())
    val imageLot = _imageLot.asStateFlow().stateIn(
        scope = viewModelScope,
        initialValue = listOf(),
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000)
    )

    fun setData(images: List<ImageInfo>) = viewModelScope.launch(Dispatchers.IO){
        _imageLot.emit(images)
    }
}