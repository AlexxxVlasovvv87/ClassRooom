package com.example.classrooom.fragments.admin

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.database.FirebaseDatabase
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.classrooom.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.example.classrooom.adapter.GroupsAdapter
import com.example.classrooom.models.Group
import java.util.ArrayList

class AdminGroupsFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(LOG_TAG, "onCreate")
    }

    lateinit var recyclerView: RecyclerView
    lateinit var backButton: Button
    lateinit var newGroupButton: Button
    lateinit var groupsRef: DatabaseReference
    lateinit var listener: ValueEventListener
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.groups_teacher_lay, container, false)
        Log.d(LOG_TAG, "onCreateView")
        newGroupButton = view.findViewById(R.id.sign_out_button4)
        backButton = view.findViewById(R.id.backButton2)
        recyclerView = view.findViewById(R.id.recyclerview_users)
        newGroupButton.setVisibility(View.GONE)
        backButton.setOnClickListener(View.OnClickListener { view1: View? ->
            NavHostFragment.findNavController(
                this
            ).navigateUp()
        })
        groupsRef = FirebaseDatabase.getInstance().getReference("Groups")
        recyclerView.setLayoutManager(LinearLayoutManager(context))
        groupsRef!!.get()
            .addOnSuccessListener { dataSnapshot: DataSnapshot -> loadInfo(dataSnapshot) }
        listener = getListener()
        groupsRef!!.addValueEventListener(listener!!)
        return view
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

    private fun loadInfo(dataSnapshot: DataSnapshot) {
        val groups = ArrayList<Group>()
        for (snaps in dataSnapshot.children) {
            for (snap in snaps.children) groups.add(
                snap.getValue(
                    Group::class.java
                )!!
            )
        }
        recyclerView!!.adapter = GroupsAdapter(context, groups, this)
    }

    override fun onDestroy() {
        super.onDestroy()
        groupsRef!!.removeEventListener(listener!!)
    }

    companion object {
        private val LOG_TAG = AdminGroupsFragment::class.java.simpleName
    }
}