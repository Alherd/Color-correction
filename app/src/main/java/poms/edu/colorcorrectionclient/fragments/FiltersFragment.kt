package poms.edu.colorcorrectionclient.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.fragment_filters.view.*
import poms.edu.colorcorrectionclient.adapters.FiltersAdapter
import poms.edu.colorcorrectionclient.R


class FiltersFragment : Fragment() {

    private var onFilterChosenCallback: ((String) -> Unit)? = null
    private lateinit var filterNames: List<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val layout = inflater.inflate(R.layout.fragment_filters, container, false)
        with(layout.recycler_view) {
            layoutManager = LinearLayoutManager(
                activity,
                LinearLayout.HORIZONTAL,
                false
            )
            adapter = FiltersAdapter(filterNames) { item -> onFilterChosenCallback?.invoke(item) }
        }
        return layout
    }

    override fun onDetach() {
        super.onDetach()
        onFilterChosenCallback = null
    }

    companion object {
        fun newInstance(
            filterNames: List<String>,
            onFilterChosenCallback: (String) -> Unit)
                : FiltersFragment =

                FiltersFragment().apply {
                    this.filterNames = filterNames
                    this.onFilterChosenCallback = onFilterChosenCallback
                }
    }
}
