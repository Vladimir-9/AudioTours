package project.movies.audiotours.ui.dialog

import android.app.Activity
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import project.movies.audiotours.R
import project.movies.audiotours.SharedViewModel
import project.movies.audiotours.data.Excursion
import project.movies.audiotours.databinding.DialogListOfStagesBinding
import project.movies.audiotours.withArguments

class ListOfStagesDialog : BottomSheetDialogFragment() {

    private var viewBinding: DialogListOfStagesBinding? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = DialogListOfStagesBinding.inflate(layoutInflater, container, false)
        return viewBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arg = requireArguments().getParcelable<Excursion>(KEY_EXCURSION_STAGE)

        viewBinding!!.twStepOne.text = getString(arg!!.stepOne.name)
        viewBinding!!.twStepTwo.text = getString(arg.stepTwo.name)
        viewBinding!!.twNameExcursion.text = arg.name
        viewBinding!!.twStepOness.text = arg.stepOne.numberStep
        viewBinding!!.twStepTwsso.text = arg.stepTwo.numberStep

        viewBinding!!.twStepOne.setOnClickListener {
            sharedViewModel.getCurrentStep(arg.stepOne)
            this.dismiss()
        }

        viewBinding!!.twStepTwo.setOnClickListener {
            sharedViewModel.getCurrentStep(arg.stepTwo)
            this.dismiss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            setupFullHeight(bottomSheetDialog)
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout
        val behavior = BottomSheetBehavior.from(bottomSheet)
        val layoutParams = bottomSheet.layoutParams
        val windowHeight = getWindowHeight()
        if (layoutParams != null) {
            layoutParams.height = windowHeight
        }
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getWindowHeight(): Int {
        val displayMetrics = DisplayMetrics()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            (context as Activity?)!!.display?.getRealMetrics(displayMetrics)
        else
            @Suppress("DEPRECATION")
            (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }

    companion object {
        private const val KEY_EXCURSION_STAGE = "excursionStage"

        fun newInstance(instance: Excursion): ListOfStagesDialog {
            return ListOfStagesDialog().withArguments {
                putParcelable(KEY_EXCURSION_STAGE, instance)
            }
        }
    }
}