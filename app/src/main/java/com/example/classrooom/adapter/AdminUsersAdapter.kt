package com.example.classrooom.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.View.OnLongClickListener
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import android.widget.TextView
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.ImageView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.android.gms.tasks.OnSuccessListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.classrooom.R
import com.example.classrooom.models.User
import com.google.firebase.database.DataSnapshot
import java.util.*

class AdminUsersAdapter(context: Context?, private val users: ArrayList<User>, fragment: Fragment) :
    RecyclerView.Adapter<AdminUsersAdapter.ViewHolder>() {
    private val inflater: LayoutInflater
    private val fragment: Fragment
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.user_list_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        holder.num.text = "" + (position + 1)
        holder.text.text = user.name + " " + user.surname
        holder.itemView.setOnLongClickListener { view: View ->
            showDialogToDeleteUser(view.context, user.uid)
            false
        }
        holder.itemView.setOnClickListener { view: View? ->
            val bundle = Bundle()
            if (user.isTeacher) {
                bundle.putString("uid", user.uid)
                NavHostFragment.findNavController(fragment)
                    .navigate(R.id.action_adminUsersFragment_to_teacherGroupsFragment, bundle)
            } else {
                bundle.putString("userId", user.uid)
                bundle.putInt("place", position + 1)
                NavHostFragment.findNavController(fragment)
                    .navigate(R.id.action_adminUsersFragment_to_statsFragment, bundle)
            }
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    class ViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        val action: ImageView
        val num: TextView
        val text: TextView

        init {
            action = view.findViewById<View>(R.id.look_button) as ImageView
            num = view.findViewById<View>(R.id.increment_num_txtView) as TextView
            text = view.findViewById<View>(R.id.username_txtView) as TextView
        }
    }

    private fun showDialogToDeleteUser(context: Context, studentid: String?) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_redactor_lay)
        Objects.requireNonNull(dialog.window)
            ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.findViewById<View>(R.id.deleteButton).setOnClickListener { v: View? ->
            val usersRef = FirebaseDatabase.getInstance().getReference("Users")
            usersRef.child(studentid!!).setValue(null).addOnSuccessListener { task: Void? ->
                deleteInGroups(studentid)
                Toast.makeText(context, "Пользователь удален", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun deleteInGroups(studentId: String?) {
        val scoreRef = FirebaseDatabase.getInstance().getReference("UserScores")
        scoreRef.child(studentId!!).setValue(null)
        val groupsRef = FirebaseDatabase.getInstance().getReference("Groups")
        groupsRef.get().addOnSuccessListener { dataSnapshot: DataSnapshot ->
            for (user in dataSnapshot.children) {
                for (group in user.children) {
                    group.child("members").child(studentId).ref.setValue(null)
                }
            }
        }
    }

    init {
        inflater = LayoutInflater.from(context)
        this.fragment = fragment
    }
}