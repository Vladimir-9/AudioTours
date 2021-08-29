package project.movies.audiotours.data

import android.os.Parcelable
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class Step(
    @StringRes val name: Int,
    @StringRes val description: Int,
    val listImageUrl: List<String>,
    @RawRes val audio: Int,
    val numberStep: String
) : Parcelable