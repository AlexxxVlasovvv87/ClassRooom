package com.example.classrooom.fragments.teacher

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DatabaseReference
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.classrooom.R
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.database.FirebaseDatabase
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.classrooom.adapter.UsersAdapter
import com.example.classrooom.models.Score
import com.example.classrooom.models.User
import com.example.classrooom.utils.MyUtils
import com.google.firebase.database.DatabaseError
import java.util.*

class TeacherChildrenFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(LOG_TAG, "onCreate")
    }

    lateinit var newMemberButton: Button
    lateinit var backButton: Button
    lateinit var recyclerView: RecyclerView
    lateinit var groupId: String
    lateinit var creatorId: String
    lateinit var listener: ValueEventListener
    lateinit var groupsRef: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.groups_child_lay, container, false)
        Log.d(LOG_TAG, "onCreateView")
        creatorId = requireArguments().getString("creatorId").toString()
        groupId = requireArguments().getString("groupId").toString()
        newMemberButton = view.findViewById(R.id.sign_out_button2)
        backButton = view.findViewById(R.id.backButton2)
        recyclerView = view.findViewById(R.id.recyclerview_users)
        backButton.setOnClickListener(View.OnClickListener { view1: View? ->
            NavHostFragment.findNavController(
                this
            ).navigateUp()
        })
        newMemberButton.setOnClickListener(View.OnClickListener { view1: View? -> NewMemberDialog() })
        groupsRef = FirebaseDatabase.getInstance().getReference("Groups")
            .child(creatorId!!).child(groupId!!).child("members")
        recyclerView.setLayoutManager(LinearLayoutManager(context))
        groupsRef!!.get()
            .addOnSuccessListener { dataSnapshot: DataSnapshot -> loadInfo(dataSnapshot) }
        listener = getListener()
        groupsRef!!.addValueEventListener(listener!!)
        return view
    }

    private fun NewMemberDialog() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_child_lay)
        Objects.requireNonNull(dialog.window)
            ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.findViewById<View>(R.id.deleteButton).setOnClickListener { v: View? ->
            val email = dialog.findViewById<TextView>(R.id.groupEditText)
            if (!email.text.toString().isEmpty()) {
                val usersRef = FirebaseDatabase.getInstance().getReference("Users")
                usersRef.get().addOnSuccessListener { dataSnapshot: DataSnapshot ->
                    var uid: String? = null
                    for (snap in dataSnapshot.children) {
                        val user = snap.getValue(
                            User::class.java
                        )!!
                        if (user.email == email.text.toString() && !user.isTeacher) {
                            uid = user.uid
                        }
                    }
                    if (uid != null) {
                        groupsRef!!.child(uid).setValue("member")
                            .addOnSuccessListener { aVoid: Void? ->
                                Toast.makeText(
                                    context,
                                    "Пользователь добавлен",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        dialog.dismiss()
                    } else {
                        Toast.makeText(context, "Пользователь не найден", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(context, "Введите почту пользователя", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }

    private fun loadInfo(dataSnapshot: DataSnapshot) {
        val users = ArrayList<String?>()
        for (snap in dataSnapshot.children) {
            Log.w(LOG_TAG, snap.key!!)
            users.add(snap.key)
        }
        sortUsers(users)
    }

    private fun sortUsers(users: ArrayList<String?>) {
        val scoresRef = FirebaseDatabase.getInstance().getReference("UserScores")
        val fragment: Fragment = this
        scoresRef.get().addOnSuccessListener { dataSnapshot: DataSnapshot ->
            val scores = HashMap<String?, Int>()
            for (snap in dataSnapshot.children) {
                if (users.contains(snap.key)) {
                    val score = snap.getValue(Score::class.java)!!
                    scores[snap.key] = score.points
                    users.remove(snap.key)
                }
            }
            val newUsers = ArrayList(MyUtils.sortByValue(scores).keys)
            newUsers.addAll(users)
            Collections.reverse(newUsers)
            recyclerView!!.adapter = UsersAdapter(context, newUsers, groupId!!, fragment)
        }
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
        groupsRef!!.removeEventListener(listener!!)
    }

    companion object {
        private val LOG_TAG = TeacherChildrenFragment::class.java.simpleName
    }
}