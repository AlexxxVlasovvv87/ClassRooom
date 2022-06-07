package com.example.classrooom.info

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.example.classrooom.R
import android.widget.TextView
import android.widget.ImageButton

class RecyclerView_Config {
    private var mContext: Context? = null
    private var mUsersAdapter: UsersAdapter? = null
    fun setConfig(
        recyclerView: RecyclerView,
        context: Context?,
        users: List<Post?>?,
        keys: List<String?>?,
        positions: Map<Post?, Int?>?
    ) {
        mContext = context
        mUsersAdapter = UsersAdapter(users, keys, positions)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = mUsersAdapter
    }

    internal inner class UserItemView(parent: ViewGroup?) : RecyclerView.ViewHolder(
        LayoutInflater.from(mContext).inflate(R.layout.user_list_item, parent, false)
    ) {
        private val mIncrement: TextView
        private val mUsername: TextView
        private val actionButton: ImageButton
        private var key: String? = null
        fun bind(post: Post, key: String?, increment: Int) {
            mIncrement.text = increment.toString()
            mUsername.text = post.username
            actionButton.setOnClickListener { view: View? -> }
            this.key = key
        }

        init {
            mIncrement = itemView.findViewById(R.id.increment_num_txtView)
            mUsername = itemView.findViewById(R.id.username_txtView)
            actionButton = itemView.findViewById(R.id.look_button)
        }
    }

    internal inner class UsersAdapter(
        private val mUserList: List<Post?>?,
        private val mKeys: List<String?>?,
        private val mPositions: Map<Post?, Int?>?
    ) : RecyclerView.Adapter<UserItemView>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserItemView {
            return UserItemView(parent)
        }

        override fun onBindViewHolder(holder: UserItemView, position: Int) {
            mUserList?.get(position)?.let { holder.bind(it, mKeys?.get(position), mPositions?.get(mUserList[position])!!) }
        }

        override fun getItemCount(): Int {
            return mUserList!!.size
        }
    }
}