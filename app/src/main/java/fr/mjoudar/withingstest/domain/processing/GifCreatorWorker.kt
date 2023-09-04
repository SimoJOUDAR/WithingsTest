package fr.mjoudar.withingstest.domain.processing

import android.content.Context
import android.graphics.Bitmap
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.bumptech.glide.Glide
import com.bumptech.glide.request.FutureTarget
import fr.mjoudar.withingstest.utils.Constants.Companion.GIF_FILE_NAME
import fr.mjoudar.withingstest.utils.Constants.Companion.IMAGE_URLS
import fr.mjoudar.withingstest.utils.getGifFileNaming
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.ExecutionException

class GifCreatorWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    val fileName = getGifFileNaming()

    override fun doWork(): Result {
        val imageUrls = inputData.getStringArray(IMAGE_URLS) ?: return Result.failure()

        return try {
            with(createGifFromUrls(applicationContext, imageUrls)) {
                if (this) {
                    val outputData = Data.Builder()
                        .putString(GIF_FILE_NAME, fileName)
                        .build()
                    Result.success(outputData)
                }
                else
                    Result.failure()
            }
        } catch (e: ExecutionException) {
            e.printStackTrace()
            Result.failure()
        } catch (e: InterruptedException) {
            e.printStackTrace()
            Result.failure()
        }
    }

    private fun createGifFromUrls(context: Context, imageUrls: Array<String>): Boolean {
        val outputGifFile = File(context.getExternalFilesDir(null), fileName)
        val bitmapList = mutableListOf<Bitmap>()

        for (url in imageUrls) {
            val futureTarget: FutureTarget<Bitmap> = Glide.with(context)
                .asBitmap()
                .load(url)
                .submit()

            val bitmap = futureTarget.get()
            bitmapList.add(bitmap)
        }

        val outputStream = FileOutputStream(outputGifFile)

        with(AnimatedGifEncoder()) {
            start(outputStream)
            setDelay(900)
            for (bitmap in bitmapList) {
                addFrame(bitmap)
            }
            finish()
        }
        outputStream.close()

        return true
    }

}