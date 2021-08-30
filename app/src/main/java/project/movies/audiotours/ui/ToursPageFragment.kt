package project.movies.audiotours.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import project.movies.audiotours.R
import project.movies.audiotours.databinding.FragmentToursPageBinding
import project.movies.audiotours.withArguments

class ToursPageFragment : Fragment(R.layout.fragment_tours_page) {

    private var viewBinding: FragmentToursPageBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentToursPageBinding.bind(view)
        loadImageTours(view, requireArguments().getString(KEY_URI_IMAGE)!!)
    }

    private fun loadImageTours(view: View, uri: String) {
        Glide.with(view)
            .load(uri)
            .placeholder(R.drawable.ic_no_internet)
            .into(viewBinding?.ivTours!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }

    companion object {
        private const val KEY_URI_IMAGE = "uriImage"

        fun newInstance(uri: String): ToursPageFragment {
            return ToursPageFragment().withArguments {
                putString(KEY_URI_IMAGE, uri)
            }
        }
    }
}