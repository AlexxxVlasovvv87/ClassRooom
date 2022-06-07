package com.example.classrooom.fragments.student

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.classrooom.R
import androidx.navigation.fragment.NavHostFragment
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.classrooom.activities.SettingsActivity
import com.example.classrooom.game.MainGameActivity
import com.example.classrooom.game.MathActivity

class StudentMenuFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(LOG_TAG, "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.main_menu_lay, container, false)
        Log.d(LOG_TAG, "onCreateView")
        val cardsGame = view.findViewById<Button>(R.id.play_button)
        val profileButton = view.findViewById<Button>(R.id.info_button)
        val settings = view.findViewById<Button>(R.id.settings_button)
        val math = view.findViewById<Button>(R.id.challenge_button)
        cardsGame.setOnClickListener { view12: View? -> startCardsGame() }
        settings.setOnClickListener { view12: View? -> toSettings() }
        math.setOnClickListener { view12: View? -> startMathGame() }
        profileButton.setOnClickListener { view1: View? ->
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_studentMenuFragment_to_profileFragment)
        }
        return view
    }

    private fun toSettings() {
        val intent = Intent(requireActivity().application, SettingsActivity::class.java)
        startActivity(intent)
        requireActivity().overridePendingTransition(
            android.R.anim.slide_in_left,
            android.R.anim.slide_out_right
        )
    }

    private fun startCardsGame() {
        //Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.scale);
        //getActivity().startAnimation(myAnim);
        val intent = Intent(requireActivity().application, MainGameActivity::class.java)
        startActivity(intent)
        requireActivity().overridePendingTransition(
            android.R.anim.slide_in_left,
            android.R.anim.slide_out_right
        )
    }

    private fun startMathGame() {
        //Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.scale);
        //getActivity().startAnimation(myAnim);
        val intent = Intent(requireActivity().application, MathActivity::class.java)
        startActivity(intent)
        requireActivity().overridePendingTransition(
            android.R.anim.slide_in_left,
            android.R.anim.slide_out_right
        )
    }

    companion object {
        private val LOG_TAG = StudentMenuFragment::class.java.simpleName
    }
}