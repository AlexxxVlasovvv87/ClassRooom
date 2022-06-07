package com.example.classrooom.fragments.teacher

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import com.example.classrooom.fragments.teacher.TeacherMenuFragment
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.classrooom.R
import androidx.navigation.fragment.NavHostFragment
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.tasks.OnSuccessListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.classrooom.models.Group
import java.util.*

class TeacherMenuFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(LOG_TAG, "onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.main_menu_teacher_lay, container, false)
        Log.d(LOG_TAG, "onCreateView")
        val newGroupButton = view.findViewById<Button>(R.id.play_button)
        newGroupButton.setOnClickListener { view12: View? -> showDialogToCreateGroup() }
        val groupsButton = view.findViewById<Button>(R.id.challenge_button)
        val profileButton = view.findViewById<Button>(R.id.info_button)
        groupsButton.setOnClickListener { view1: View? ->
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_teacherMenuFragment_to_teacherGroupsFragment)
        }
        profileButton.setOnClickListener { view1: View? ->
            NavHostFragment.findNavController(this)
                .navigate(R.id.action_teacherMenuFragment_to_profileFragment)
        }
        return view
    }

    private fun showDialogToCreateGroup() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_group_lay)
        Objects.requireNonNull(dialog.window)
            ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.findViewById<View>(R.id.deleteButton).setOnClickListener { v: View? ->
            val groupName = dialog.findViewById<TextView>(R.id.groupEditText)
            if (!groupName.text.toString().isEmpty()) {
                val groupsRef = FirebaseDatabase.getInstance().getReference("Groups")
                val uid = FirebaseAuth.getInstance().currentUser!!.uid
                val newGroup = Group(uid, groupName.text.toString())
                groupsRef.child(uid).child(newGroup.inviteCode!!).setValue(newGroup)
                    .addOnSuccessListener { task: Void? ->
                        Toast.makeText(
                            context,
                            "Группа \"" + newGroup.groupName + "\" создана",
                            Toast.LENGTH_SHORT
                        ).show()
                        dialog.dismiss()
                    }
            } else {
                Toast.makeText(context, "Введите название группы", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }

    companion object {
        private val LOG_TAG = TeacherMenuFragment::class.java.simpleName
    }
}