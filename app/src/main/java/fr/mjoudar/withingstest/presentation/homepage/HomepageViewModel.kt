package fr.mjoudar.withingstest.presentation.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.mjoudar.withingstest.domain.models.ImageInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class HomepageViewModel @Inject constructor(): ViewModel() {

    private var selectedItems = mutableListOf<ImageInfo>()

    //

    private val _imageLot = MutableStateFlow<List<ImageInfo>>(listOf())
    val imageLot = _imageLot.asStateFlow().stateIn(
        scope = viewModelScope,
        initialValue = listOf(),
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000)
    )

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
}