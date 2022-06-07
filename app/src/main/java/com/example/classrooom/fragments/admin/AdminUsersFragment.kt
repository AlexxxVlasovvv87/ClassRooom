package com.example.classrooom.fragments.admin

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DatabaseReference
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.classrooom.R
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.database.FirebaseDatabase
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.example.classrooom.adapter.AdminUsersAdapter
import com.example.classrooom.models.User
import com.google.firebase.database.DatabaseError
import java.util.ArrayList

class AdminUsersFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(LOG_TAG, "onCreate")
    }

    lateinit var newMemberButton: Button
    lateinit var backButton: Button
    lateinit var recyclerView: RecyclerView
    lateinit var title: TextView
    var isTeacherList: Boolean = false
    lateinit var listener: ValueEventListener
    lateinit var usersRef: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.groups_child_lay, container, false)
        Log.d(LOG_TAG, "onCreateView")
        isTeacherList = requireArguments().getBoolean("isTeacherList")
        newMemberButton = view.findViewById(R.id.sign_out_button2)
        backButton = view.findViewById(R.id.backButton2)
        title = view.findViewById(R.id.title_res)
        recyclerView = view.findViewById(R.id.recyclerview_users)
        if (isTeacherList!!) title.setText("Список учителей")
        backButton.setOnClickListener(View.OnClickListener { view1: View? ->
            NavHostFragment.findNavController(
                this
            ).navigateUp()
        })
        newMemberButton.setVisibility(View.GONE)
        usersRef = FirebaseDatabase.getInstance().getReference("Users")
        recyclerView.setLayoutManager(LinearLayoutManager(context))
        usersRef!!.get()
            .addOnSuccessListener { dataSnapshot: DataSnapshot -> loadInfo(dataSnapshot) }
        listener = getListener()
        usersRef!!.addValueEventListener(listener!!)
        return view
    }

    private fun loadInfo(dataSnapshot: DataSnapshot) {
        val users = ArrayList<User>()
        for (snap in dataSnapshot.children) {
            val user = snap.getValue(
                User::class.java
            )!!
            if (user.isTeacher == isTeacherList) {
                users.add(user)
            }
        }
        recyclerView!!.adapter = AdminUsersAdapter(context, users, this)
    }

    @JvmName("getListener1")
    fun getListener(): ValueEventListener {
        return object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                loadInfo(dataSnapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(LOG_TAG, "Failed to read value.", error.toException())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        usersRef!!.removeEventListener(listener!!)
    }

    companion object {
        private val LOG_TAG = AdminUsersFragment::class.java.simpleName
    }
}