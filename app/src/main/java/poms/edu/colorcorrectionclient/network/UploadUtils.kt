package poms.edu.colorcorrectionclient.network

import okhttp3.*
import org.json.JSONObject
import java.io.File
import java.io.IOException

fun uploadImageAndThen(
    imgFile: File,
    onSuccess: (String) -> Unit,
    onError: () -> Unit) {
    val url = ColorCorrectionHttpClient.getAbsoluteUrl("" +
            "send_image")


    val requestBody = MultipartBody.Builder()
        .setType(MultipartBody.FORM)
        .addFormDataPart("file", "a",
            RequestBody.create(MediaType.parse("image/jpg"), imgFile)
        )
        .build()

    ColorCorrectionHttpClient.post(
        url,
        requestBody,
        object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                onError()
            }

            override fun onResponse(call: Call, response: Response) {
                val responseJson = response.body()?.string()
                val imageToken = JSONObject(responseJson).getString("image_token")
                onSuccess(imageToken)
            }

        }
    )
}