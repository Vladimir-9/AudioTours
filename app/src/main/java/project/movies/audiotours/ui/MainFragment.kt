package project.movies.audiotours.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import project.movies.audiotours.R
import project.movies.audiotours.SharedViewModel
import project.movies.audiotours.data.Excursion
import project.movies.audiotours.databinding.FragmentMainBinding
import project.movies.audiotours.ui.adapter.PageToursAdapter
import project.movies.audiotours.ui.dialog.PlayerScreenDialog

class MainFragment : Fragment(R.layout.fragment_main) {

    private var viewBinding: FragmentMainBinding? = null
    private val excursion = Excursion("Русский музей: импрессионисты")
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewBinding = FragmentMainBinding.bind(view)
        observeStep()

        // triggered when the application is launched for the first time
        savedInstanceState ?: run {
            sharedViewModel.getCurrentStep(excursion.stepOne)
            openFragment()
        }
    }

    private fun openFragment() {
        childFragmentManager.beginTransaction()
            .replace(R.id.container_main, PlayerScreenDialog.newInstance(excursion))
            .commit()
    }

    private fun settingValuesInTheTW(name: String, description: String) {
        viewBinding!!.include.twHeading.text = name
        viewBinding!!.include.twDescription.text = description
    }

    // subscription to the choice of a new stage of the excursion
    private fun observeStep() {
        sharedViewModel.currentStepLiveDate.observe(viewLifecycleOwner) { step ->
            initViewPager(step.listImageUrl)
            settingValuesInTheTW(getString(step.name), getString(step.description))
        }
    }

    private fun initViewPager(imageToursList: List<String>) {
        val adapter = PageToursAdapter(imageToursList, this)
        viewBinding!!.include.viewPager.adapter = adapter
        indicatorViewPager()
    }

    private fun indicatorViewPager() {
        viewBinding!!.include.indicator.setViewPager2(viewBinding!!.include.viewPager)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }
}