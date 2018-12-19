package poms.edu.colorcorrectionclient.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.view.View
import com.squareup.picasso.Callback
import poms.edu.colorcorrectionclient.fragments.FiltersFragment
import poms.edu.colorcorrectionclient.fragments.ImageFragment
import poms.edu.colorcorrectionclient.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_image.view.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast
import poms.edu.colorcorrectionclient.images.drawableToFile
import poms.edu.colorcorrectionclient.network.*
import java.lang.Exception


class MainActivity : FragmentActivity() {

    private val imageFragment: ImageFragment = ImageFragment()
    private lateinit var filtersFragment: FiltersFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        openImageFragment()

        downloadFilterNamesAsyncAndThen(
            onSuccessAction =  { _, response -> runOnUiThread {
                val itemNames = parseFilterNames(response)

                hideProgressBar()
                createAndOpenFiltersFragment(itemNames)
            }},
            onErrorAction = {_, e -> runOnUiThread {
                hideProgressBar()
                longToast("Something went wrong: ${e.message}")
            }})

    }

    private fun openFragmentInsideContainer(fragment: Fragment, containerId: Int) {
        supportFragmentManager
            .beginTransaction()
            .replace(containerId, fragment)
            .commit()
    }

    private fun openImageFragment() {
        openFragmentInsideContainer(imageFragment, R.id.image_fragment_container)
    }

    private fun createAndOpenFiltersFragment(items: List<String>) {

        filtersFragment = FiltersFragment
            .newInstance(items, onFilterChosenCallback = ::uploadCurrentImageAndGetProcessedImageAndShow)
            .also {
                openFragmentInsideContainer(it, R.id.filters_fragment_container)
            }
    }

    private fun hideProgressBar() {
        progress_circular.visibility = View.GONE
    }

    private fun downloadImageAndShow(imageToken: String, filterName: String) = runOnUiThread {

        downloadProcessedImage(imageToken, filterName)
            .placeholder(imageFragment.currentDrawable!!)
            .into(
                imageFragment.view!!.main_image, object: Callback {
                    override fun onSuccess() = runOnUiThread {
                        imageFragment.hideProgressBar()
                    }

                    override fun onError(e: Exception?) = runOnUiThread {
                        imageFragment.hideProgressBar()
                        longToast("Something went wrong")
                    }

                }
            )
    }

    private fun uploadCurrentImageAndGetProcessedImageAndShow(filterName: String) = with(imageFragment.view!!) {

        toast("Uploading your image...")
        main_image_progress_bar.visibility = View.VISIBLE

        val drawable = imageFragment.drawableNotProcessed
        val imgFile = drawableToFile(drawable, filesDir)
        uploadImageAndThen(imgFile,
            onSuccess =  { imageToken ->
                runOnUiThread {
                    toast("Getting processed image...")
                }
                downloadImageAndShow(imageToken, filterName)
            },
            onError = {
                runOnUiThread {
                    imageFragment.hideProgressBar()
                    longToast("Something went wrong")
                }
            }
        )
    }

}
