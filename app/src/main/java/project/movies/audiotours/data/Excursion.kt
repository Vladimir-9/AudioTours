package project.movies.audiotours.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import project.movies.audiotours.R

@Parcelize
data class Excursion(val name: String) : Parcelable {

    val stepOne = Step(
        R.string.step_one, R.string.description_one, listOf(
            ("https://i.pinimg.com/736x/ac/6a/0e/ac6a0e327a403add4f9eb147ddd84ef4.jpg"),
            ("https://i.pinimg.com/736x/ac/6a/0e/ac6a0e327a403add4f9eb147ddd84ef4.jpg"),
            ("https://i.pinimg.com/736x/ac/6a/0e/ac6a0e327a403add4f9eb147ddd84ef4.jpg")
        ), R.raw.music, "1/10"
    )

    val stepTwo = Step(
        R.string.step_two, R.string.description_two, listOf(
            ("https://i.pinimg.com/originals/12/8c/96/128c96c47b763efc896a973b68aa48f1.jpg"),
            ("https://i.pinimg.com/originals/12/8c/96/128c96c47b763efc896a973b68aa48f1.jpg"),
            ("https://i.pinimg.com/originals/12/8c/96/128c96c47b763efc896a973b68aa48f1.jpg"),
            ("https://i.pinimg.com/originals/12/8c/96/128c96c47b763efc896a973b68aa48f1.jpg")
        ), R.raw.music_2, "2/10"
    )
}