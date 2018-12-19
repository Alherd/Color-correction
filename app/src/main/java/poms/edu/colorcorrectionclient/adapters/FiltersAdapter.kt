package poms.edu.colorcorrectionclient.adapters

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.squareup.picasso.Callback
import poms.edu.colorcorrectionclient.R
import kotlinx.android.synthetic.main.item_filter.view.*
import poms.edu.colorcorrectionclient.network.downloadFilterIcon
import java.lang.Exception


class FiltersAdapter(
    private val filterNames: List<String>,
    private val clickListener: (String) -> Unit)
    : RecyclerView.Adapter<FiltersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_filter, parent, false) as LinearLayout
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = filterNames.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentFilter = filterNames[position]

        fun downloadAndDisplayImage() = with(holder.linearLayout) {
            filter_name.text = currentFilter
            downloadFilterIcon(currentFilter)
                .into(filter_item_image, object : Callback {
                    override fun onError(e: Exception?) {
                        Log.e("ColorCorrectionClient", "Error loading filter image $currentFilter")
                    }

                    override fun onSuccess() {
                        filter_img_progress.visibility = View.GONE
                    }

                })

            setOnClickListener {
                clickListener(filterNames[position])
            }

        }
        downloadAndDisplayImage()
    }

    inner class ViewHolder(val linearLayout: LinearLayout) : RecyclerView.ViewHolder(linearLayout)
}