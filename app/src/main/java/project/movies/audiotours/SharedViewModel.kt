package project.movies.audiotours

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import project.movies.audiotours.data.Step

class SharedViewModel : ViewModel() {

    private val _currentStepLiveDate = MutableLiveData<Step>()
    val currentStepLiveDate: LiveData<Step>
        get() = _currentStepLiveDate

    fun getCurrentStep(step: Step) {
        _currentStepLiveDate.value = step
    }
}