package project.movies.audiotours.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import project.movies.audiotours.ui.ToursPageFragment

class PageToursAdapter(private val toursList: List<String>, fragment: Fragment) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount() = toursList.size

    override fun createFragment(position: Int): Fragment {
        val tours: String = toursList[position]
        return ToursPageFragment.newInstance(tours)
    }
}