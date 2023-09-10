package fr.mjoudar.withingstest.presentation.details

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import fr.mjoudar.withingstest.domain.processing.GifCreatorWorker
import fr.mjoudar.withingstest.utils.Constants.Companion.GIF_FILE_NAME
import fr.mjoudar.withingstest.utils.Constants.Companion.IMAGE_URLS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailsViewModel @Inject constructor() : ViewModel() {

    private val _processingState = MutableStateFlow<GifUiState>(GifUiState.Loading)
    val processingState = _processingState.asStateFlow().stateIn(
        scope = viewModelScope,
        initialValue = GifUiState.Loading,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000)
    )

    fun createGif(context: Context, urls: Array<String>) {

        val inputData = Data.Builder()
            .putStringArray(IMAGE_URLS, urls)
            .build()

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<GifCreatorWorker>()
            .setConstraints(constraints)
            .setInputData(inputData)
            .build()

        with(WorkManager.getInstance(context)) {
            enqueue(workRequest)
            getWorkInfoByIdLiveData(workRequest.id)
                .observeForever { workInfo ->
                    when (workInfo.state) {
                        WorkInfo.State.SUCCEEDED -> {
                            workInfo.outputData.getString(GIF_FILE_NAME)?.let {
                                emitData(it)
                            } ?: emitError()
                        }

                        WorkInfo.State.FAILED -> emitError()

                        else -> {}
                    }
                }
        }
    }

    private fun emitData(fileName: String) = viewModelScope.launch(Dispatchers.IO) {
        Log.d("Panda_test", "emitSuccess - fileName : $fileName")
        _processingState.emit(GifUiState.Success(fileName))
    }

    private fun emitError() = viewModelScope.launch(Dispatchers.IO) {
        Log.d("Panda_test", "emitError")
        _processingState.emit(GifUiState.Error(null))
    }

    sealed class GifUiState {
        object Loading : GifUiState()
        data class Success(val fileName: String) : GifUiState()
        data class Error(val error: Exception?) : GifUiState()
    }
}