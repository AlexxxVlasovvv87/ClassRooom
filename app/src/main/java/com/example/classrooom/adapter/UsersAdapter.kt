package com.example.classrooom.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.classrooom.R
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import android.view.View.OnLongClickListener
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import android.widget.TextView
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.ImageView
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.classrooom.models.User
import java.util.*

class UsersAdapter(
    context: Context?,
    private val usersIds: ArrayList<String?>,
    inviteCode: String,
    fragment: Fragment
) : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {
    private val inflater: LayoutInflater
    private val fragment: Fragment
    private val inviteCode: String
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.user_list_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val id = usersIds[position]
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        if (id != null) {
            ref.child(id).get().addOnSuccessListener { dataSnapshot: DataSnapshot ->
                val user = dataSnapshot.getValue(
                    User::class.java
                )!!
                holder.num.text = "" + (position + 1)
                holder.text.text = user.name + " " + user.surname
                holder.itemView.setOnLongClickListener { view: View ->
                    showDialogToDeleteUser(view.context, user.uid)
                    false
                }
                holder.itemView.setOnClickListener { view: View? ->
                    val bundle = Bundle()
                    bundle.putString("userId", user.uid)
                    bundle.putInt("place", position + 1)
                    NavHostFragment.findNavController(fragment)
                        .navigate(R.id.action_teacherChildrenFragment_to_statsFragment, bundle)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return usersIds.size
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
            val groupsRef = FirebaseDatabase.getInstance().getReference("Groups")
            val uid = FirebaseAuth.getInstance().currentUser!!.uid
            groupsRef.child(uid).child(inviteCode).child("members").child(studentid!!)
                .setValue(null).addOnSuccessListener { task: Void? ->
                Toast.makeText(context, "Пользователь удален из группы", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    init {
        inflater = LayoutInflater.from(context)
        this.inviteCode = inviteCode
        this.fragment = fragment
    }
}