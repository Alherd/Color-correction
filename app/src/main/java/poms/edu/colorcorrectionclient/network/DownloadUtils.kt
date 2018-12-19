package poms.edu.colorcorrectionclient.network

import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import kotlinx.android.synthetic.main.fragment_image.view.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONArray
import java.io.IOException

fun parseFilterNames(response: Response): List<String> {
        val json = JSONArray(response.body()!!.string())
        return List(json.length()) {
            json[it].toString()
        }
    }


fun downloadFilterNamesAsyncAndThen(
    onSuccessAction: (Call, Response) -> Unit,
    onErrorAction: (Call, IOException) -> Unit) {

    ColorCorrectionHttpClient.get(
        ColorCorrectionHttpClient.getAbsoluteUrl("get_all_filters"),
        object: Callback {
            override fun onResponse(call: Call, response: Response) {
                onSuccessAction(call, response)
            }

            override fun onFailure(call: Call, e: IOException) {
                onErrorAction(call, e)
            }

        },
        withTimeOut = true
    )
}

private fun downloadImage(relativeUrl: String): RequestCreator {
    val absoluteUrl = ColorCorrectionHttpClient.getAbsoluteUrl(relativeUrl)
    return Picasso
        .get()
        .load(absoluteUrl)
        .networkPolicy(NetworkPolicy.NO_CACHE)
        .memoryPolicy(MemoryPolicy.NO_CACHE)
}

fun downloadProcessedImage(imageToken: String, filterName: String): RequestCreator =
        downloadImage("process_image?image_token=$imageToken&grid_name=$filterName")

fun downloadFilterIcon(filterName: String): RequestCreator =
        downloadImage("get_filter_img_by_name?name=$filterName")
