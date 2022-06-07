package com.example.classrooom.fragments.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.classrooom.R
import com.google.firebase.auth.FirebaseAuth
import androidx.navigation.fragment.NavHostFragment
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.classrooom.activities.SignInActivity

class AdminMenuFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(LOG_TAG, "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.main_menu_admin_lay, container, false)
        Log.d(LOG_TAG, "onCreateView")
        val logOut = view.findViewById<Button>(R.id.info_button)
        val teachersButton = view.findViewById<Button>(R.id.play_button)
        val studentsButton = view.findViewById<Button>(R.id.challenge_button)
        val groupsButton = view.findViewById<Button>(R.id.challenge_button2)
        logOut.setOnClickListener { view12: View? ->
            FirebaseAuth.getInstance().signOut()
            openSignInView()
        }
        teachersButton.setOnClickListener { view1: View? ->
            val bundle = Bundle()
            bundle.putBoolean("isTeacherList", true)
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_adminMenuFragment_to_adminUsersFragment, bundle)
        }
        studentsButton.setOnClickListener { view1: View? ->
            val bundle = Bundle()
            bundle.putBoolean("isTeacherList", false)
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_adminMenuFragment_to_adminUsersFragment, bundle)
        }
        groupsButton.setOnClickListener { view1: View? ->
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_adminMenuFragment_to_adminGroupsFragment)
        }
        return view
    }

    fun openSignInView() {
        val intent = Intent(requireActivity().application, SignInActivity::class.java)
        startActivity(intent)
    }

    companion object {
        private val LOG_TAG = AdminMenuFragment::class.java.simpleName
    }
}