package fr.mjoudar.withingstest.presentation.details

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.work.*
import fr.mjoudar.withingstest.domain.processing.GifCreatorWorker
import fr.mjoudar.withingstest.utils.Constants.Companion.IMAGE_URLS
import javax.inject.Inject

class DetailsViewModel @Inject constructor() : ViewModel() {

    fun createGif(context: Context, urls: Array<String>): LiveData<WorkInfo> {

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

            return getWorkInfoByIdLiveData(workRequest.id)
        }
    }
}