package project.movies.audiotours.ui.dialog

import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.annotation.IdRes
import androidx.annotation.RawRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Constraints
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import project.movies.audiotours.R
import project.movies.audiotours.SharedViewModel
import project.movies.audiotours.data.Excursion
import project.movies.audiotours.databinding.DialogPlayerScreenBinding
import project.movies.audiotours.fromDpToPixels
import project.movies.audiotours.withArguments
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

class PlayerScreenDialog : BottomSheetDialogFragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var viewBinding: DialogPlayerScreenBinding? = null
    private var mediaPlayer: MediaPlayer? = null
    private var runnable: Runnable? = null
    private var executor: ScheduledExecutorService? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = DialogPlayerScreenBinding.inflate(layoutInflater, container, false)
        return viewBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arg = requireArguments().getParcelable<Excursion>(KEY_EXCURSION_INSTANCE)
        val bottomSheet = viewBinding!!.mainPlayer
        val behavior = BottomSheetBehavior.from(bottomSheet)
        viewBinding!!.twNameExcursion.text = arg!!.name
        mediaPlayer = instanceMediaPlayer(arg.stepOne.audio)

        if (savedInstanceState != null) {
            val position = savedInstanceState.getInt(KEY_POSITION_SEEK_BAR)
            restoreStateMediaPlayer(position)
        }

        initSeekBar()
        statePlayerScreen(behavior)
        observeStageSelect()
        reactionToTheChangeSeekBar()

        viewBinding!!.twInformation.setOnClickListener {
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        viewBinding!!.btStartPause.setOnClickListener {
            playSound()
        }
        viewBinding!!.btStartPauseMain.setOnClickListener {
            playSound()
        }
        viewBinding!!.btStageView.setOnClickListener {
            ListOfStagesDialog.newInstance(arg).show(parentFragmentManager, null)
        }
        viewBinding!!.btBack.setOnClickListener {
            rewind()
        }
        viewBinding!!.btBackMain.setOnClickListener {
            rewind()
        }
        viewBinding!!.btForward.setOnClickListener {
            fastForward()
        }
        viewBinding!!.btForwardMain.setOnClickListener {
            fastForward()
        }
    }

    private fun restoreStateMediaPlayer(position: Int) {
        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_RESUME) {
                    mediaPlayer!!.seekTo(position)
                    playSound()
                }
            }
        })
    }

    private fun reactionToTheChangeSeekBar() {
        viewBinding!!.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser)
                    mediaPlayer!!.seekTo(progress * 1000)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun rewind() {
        val rewind = mediaPlayer!!.currentPosition - 5000
        mediaPlayer!!.seekTo(rewind)
    }

    private fun fastForward() {
        val fastForward = mediaPlayer!!.currentPosition + 5000
        mediaPlayer!!.seekTo(fastForward)
    }

    private fun observeStageSelect() {
        sharedViewModel.currentStepLiveDate.observe(viewLifecycleOwner) { step ->
            mediaPlayer!!.release()
            setIconButtonPlay(true)
            mediaPlayer = instanceMediaPlayer(step.audio)
            initSeekBar()
            viewBinding!!.twInformation.text = getString(step.description)
            viewBinding!!.twDescriptions.text = getString(step.description)
        }
    }

    private fun statePlayerScreen(behavior: BottomSheetBehavior<ConstraintLayout>) {
        behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {}

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                miniPlayerChangeAlfaOfTheButtons(slideOffset)
                miniPlayerVisibilityOfTheButtons(slideOffset)
                mainPlayerVisibilityOfTheButtons(slideOffset)
                locationSeekBar(slideOffset)
            }
        })
    }

    private fun miniPlayerChangeAlfaOfTheButtons(slideOffset: Float) {
        viewBinding!!.btBack.alpha = 1 - slideOffset
        viewBinding!!.btForward.alpha = 1 - slideOffset
        viewBinding!!.btStartPause.alpha = 1 - slideOffset
        viewBinding!!.twInformation.alpha = 1 - slideOffset
    }

    private fun miniPlayerVisibilityOfTheButtons(slideOffset: Float) {
        val isVisible = slideOffset < 0.9
        viewBinding!!.btBack.isVisible = isVisible
        viewBinding!!.btForward.isVisible = isVisible
        viewBinding!!.btStartPause.isVisible = isVisible
    }

    private fun mainPlayerVisibilityOfTheButtons(slideOffset: Float) {
        val isVisible = slideOffset > 0.7
        viewBinding!!.btStageView.isVisible = isVisible
        viewBinding!!.btBackMain.isVisible = isVisible
        viewBinding!!.btForwardMain.isVisible = isVisible
        viewBinding!!.btStartPauseMain.isVisible = isVisible
        viewBinding!!.twDescriptions.isVisible = isVisible
        viewBinding!!.twNameExcursion.isVisible = isVisible
        viewBinding!!.textView.isVisible = isVisible
    }

    private fun locationSeekBar(slideOffset: Float) {
        if (slideOffset > 0.7)
            paramsLocationSeekBar(R.id.bt_start_pause_main)
        else
            paramsLocationSeekBar(R.id.tw_information)
    }

    private fun paramsLocationSeekBar(@IdRes id: Int) {
        viewBinding!!.seekBar.layoutParams =
            Constraints.LayoutParams(0, 20.fromDpToPixels(requireContext())).apply {
                startToStart = Constraints.LayoutParams.PARENT_ID
                endToEnd = Constraints.LayoutParams.PARENT_ID
                bottomToTop = id
            }
    }

    private fun setIconButtonPlay(isIconPlay: Boolean) {
        val icon = if (isIconPlay)
            R.drawable.ic_play
        else
            R.drawable.ic_pause

        viewBinding!!.btStartPause.setImageDrawable(
            ResourcesCompat.getDrawable(resources, icon, null)
        )
        viewBinding!!.btStartPauseMain.setImageDrawable(
            ResourcesCompat.getDrawable(resources, icon, null)
        )
    }

    private fun playSound() {
        if (mediaPlayer!!.isPlaying) {
            mediaPlayer!!.pause()
            setIconButtonPlay(true)
        } else {
            mediaPlayer!!.start()
            setIconButtonPlay(false)
        }
    }

    private fun instanceMediaPlayer(@RawRes raw: Int): MediaPlayer {
        val player = MediaPlayer.create(requireContext(), raw)
        player.setOnCompletionListener {
            setIconButtonPlay(true)
            mediaPlayer!!.reset()
            executor!!.shutdown()
            executor = null
            runnable = null
            viewBinding!!.seekBar.progress = 0
            mediaPlayer = instanceMediaPlayer(raw)
            initSeekBar()
        }
        return player
    }

    private fun initSeekBar() {
        executor = Executors.newSingleThreadScheduledExecutor()
        viewBinding!!.seekBar.max = mediaPlayer!!.duration / 1000
        runnable = Runnable {
            viewBinding!!.seekBar.progress = mediaPlayer!!.currentPosition / 1000
        }
        executor!!.scheduleAtFixedRate(runnable, 0, 1000, TimeUnit.MILLISECONDS)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mediaPlayer!!.release()
        mediaPlayer = null
        viewBinding = null
        executor = null
        runnable = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_POSITION_SEEK_BAR, mediaPlayer!!.currentPosition)
    }

    companion object {
        private const val KEY_POSITION_SEEK_BAR = "positionSeekBar"
        private const val KEY_EXCURSION_INSTANCE = "excursionInstance"

        fun newInstance(instance: Excursion): PlayerScreenDialog {
            return PlayerScreenDialog().withArguments {
                putParcelable(KEY_EXCURSION_INSTANCE, instance)
            }
        }
    }
}