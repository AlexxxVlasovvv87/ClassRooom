package com.example.classrooom.fragments.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.classrooom.R
import com.google.firebase.auth.FirebaseAuth
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.classrooom.models.User

class ProfileChangeFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(LOG_TAG, "onCreate")
    }

    var uid: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(LOG_TAG, "onCreateView")
        val view = inflater.inflate(R.layout.redactor_lay, container, false)
        uid = if (arguments != null && requireArguments().containsKey("userId")) {
            requireArguments().getString("userId")
        } else {
            FirebaseAuth.getInstance().currentUser!!.uid
        }
        val saveButton = view.findViewById<Button>(R.id.save_button)
        val name = view.findViewById<TextView>(R.id.new_name)
        val surname = view.findViewById<TextView>(R.id.new_surname)
        val backButton = view.findViewById<Button>(R.id.backButton3)
        backButton.setOnClickListener { view1: View? ->
            NavHostFragment.findNavController(this).navigateUp()
        }
        val userRef = FirebaseDatabase.getInstance().getReference("Users").child(
            uid!!
        )
        userRef.get().addOnSuccessListener { dataSnapshot: DataSnapshot ->
            val user = dataSnapshot.getValue(
                User::class.java
            )!!
            name.text = user.name
            surname.text = user.surname
        }
        saveButton.setOnClickListener { view1: View? ->
            userRef.get().addOnSuccessListener { dataSnapshot: DataSnapshot ->
                val user = dataSnapshot.getValue(
                    User::class.java
                )!!
                val newName = name.text.toString()
                val newSurname = surname.text.toString()
                if (newName.isEmpty()) {
                    Toast.makeText(context, "Имя не может быть пустым", Toast.LENGTH_LONG).show()
                } else if (newSurname.isEmpty()) {
                    Toast.makeText(context, "Фамилия не может быть пустой", Toast.LENGTH_LONG)
                        .show()
                } else {
                    user.name = newName
                    user.surname = newSurname
                    userRef.setValue(user)
                    Navigation.findNavController(view).navigateUp()
                }
            }
        }
        return view
    }

    companion object {
        private val LOG_TAG = ProfileChangeFragment::class.java.simpleName
    }
}