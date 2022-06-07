package com.example.classrooom.fragments.login

import android.os.Bundle
import android.util.Log
import com.example.classrooom.fragments.login.SignInMenuFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.classrooom.R
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

class SignInMenuFragment : Fragment() {
    private var numberOfClicks = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(LOG_TAG, "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_in_menu, container, false)
        Log.d(LOG_TAG, "onCreateView")
        val signInButton = view.findViewById<Button>(R.id.play_button)
        val registerButton = view.findViewById<Button>(R.id.challenge_button)
        val admin = view.findViewById<View>(R.id.admin_view)
        signInButton.setOnClickListener { view1: View? ->
            val navHostFragment = NavHostFragment.findNavController(this)
            navHostFragment.navigate(R.id.action_signInMenuFragment_to_signInFragment)
        }
        registerButton.setOnClickListener { view1: View? ->
            val navHostFragment = NavHostFragment.findNavController(this)
            navHostFragment.navigate(R.id.action_signInMenuFragment_to_registerFragment)
        }
        admin.setOnClickListener { view1: View? ->
            numberOfClicks++
            if (numberOfClicks > 4) {
                val navHostFragment = NavHostFragment.findNavController(this)
                navHostFragment.navigate(R.id.action_signInMenuFragment_to_adminSignInFragment)
                numberOfClicks = 0
            }
        }
        return view
    }

    companion object {
        private val LOG_TAG = SignInMenuFragment::class.java.simpleName
    }
}