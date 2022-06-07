package com.example.classrooom.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.classrooom.R
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
import com.example.classrooom.models.Group
import java.util.*

class GroupsAdapter(context: Context?, private val groups: ArrayList<Group>, fragment: Fragment) :
    RecyclerView.Adapter<GroupsAdapter.ViewHolder>() {
    private val inflater: LayoutInflater
    private val fragment: Fragment
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.user_list_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val group = groups[position]
        holder.num.text = "" + (position + 1)
        holder.text.text = group.groupName
        holder.itemView.setOnLongClickListener { view: View ->
            showDialogToDeleteGroup(view.context, group)
            false
        }
        holder.itemView.setOnClickListener { view: View? ->
            val bundle = Bundle()
            bundle.putString("creatorId", group.creatorId)
            bundle.putString("groupId", group.inviteCode)
            NavHostFragment.findNavController(fragment).navigate(
                R.id.action_teacherGroupsFragment_to_teacherChildrenFragment, bundle
            )
        }
    }

    override fun getItemCount(): Int {
        return groups.size
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

    private fun showDialogToDeleteGroup(context: Context, group: Group) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_redactor_lay)
        Objects.requireNonNull(dialog.window)
            ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.findViewById<View>(R.id.deleteButton).setOnClickListener { v: View? ->
            val groupsRef = FirebaseDatabase.getInstance().getReference("Groups")
            groupsRef.child(group.creatorId!!).child(group.inviteCode!!).setValue(null)
                .addOnSuccessListener { task: Void? ->
                    Toast.makeText(
                        context,
                        "Группа \"" + group.groupName + "\" удалена",
                        Toast.LENGTH_SHORT
                    ).show()
                    dialog.dismiss()
                }
        }
        dialog.show()
    }

    init {
        inflater = LayoutInflater.from(context)
        this.fragment = fragment
    }
}