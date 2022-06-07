package com.example.classrooom.fragments.profile

import android.os.Bundle
import com.example.classrooom.fragments.profile.ProfileFragment
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.classrooom.R
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import androidx.navigation.Navigation
import android.widget.TextView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.classrooom.activities.SignInActivity
import com.example.classrooom.models.User

class ProfileFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(LOG_TAG, "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(LOG_TAG, "onCreateView")
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val signOutButton = view.findViewById<Button>(R.id.sign_out_button)
        val changeButton = view.findViewById<Button>(R.id.change_name_button)
        val backButton = view.findViewById<Button>(R.id.backBut4)
        backButton.setOnClickListener { view1: View? ->
            NavHostFragment.findNavController(this).navigateUp()
        }
        signOutButton.setOnClickListener { view1: View? ->
            FirebaseAuth.getInstance().signOut()
            checkLogin()
        }
        changeButton.setOnClickListener { view1: View? ->
            Navigation.findNavController(view)
                .navigate(R.id.action_profileFragment_to_profileChangeFragment)
        }
        val name = view.findViewById<TextView>(R.id.user_name_edit)
        val surname = view.findViewById<TextView>(R.id.user_surname_edit)
        val email = view.findViewById<TextView>(R.id.user_key_edit)
        val uid: String?
        uid = if (arguments != null && requireArguments().containsKey("userId")) {
            requireArguments().getString("userId")
        } else {
            FirebaseAuth.getInstance().currentUser!!.uid
        }
        val userRef = FirebaseDatabase.getInstance().getReference("Users").child(
            uid!!
        )
        userRef.get().addOnSuccessListener { dataSnapshot: DataSnapshot ->
            val user = dataSnapshot.getValue(
                User::class.java
            )
            name.text = user!!.name
            surname.text = user.surname
            email.text = user.email
        }
        return view
    }

    private fun checkLogin() {
        val auth = FirebaseAuth.getInstance()
        if (auth.currentUser == null) {
            openSignInView()
        }
    }

    fun openSignInView() {
        val intent = Intent(requireActivity().application, SignInActivity::class.java)
        startActivity(intent)
        requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    companion object {
        private val LOG_TAG = ProfileFragment::class.java.simpleName
    }
}