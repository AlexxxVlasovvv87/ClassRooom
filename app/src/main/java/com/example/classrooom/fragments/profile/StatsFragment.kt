package com.example.classrooom.fragments.profile

import android.os.Bundle
import android.util.Log
import com.example.classrooom.fragments.profile.StatsFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.classrooom.R
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import com.example.classrooom.models.Score

class StatsFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(LOG_TAG, "onCreate")
    }

    var userId: String? = null
    var place = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.statistic_child_lay, container, false)
        Log.d(LOG_TAG, "onCreateView")
        userId = requireArguments().getString("userId")
        place = requireArguments().getInt("place")
        val progress = view.findViewById<TextView>(R.id.text8)
        val pointsTV = view.findViewById<TextView>(R.id.text9)
        val placeTV = view.findViewById<TextView>(R.id.text11)
        val backButton = view.findViewById<Button>(R.id.backButton3)
        backButton.setOnClickListener { view1: View? ->
            NavHostFragment.findNavController(this).navigateUp()
        }
        val scoreRef = FirebaseDatabase.getInstance().getReference("UserScores").child(
            userId!!
        )
        scoreRef.get().addOnSuccessListener { dataSnapshot: DataSnapshot ->
            val score = dataSnapshot.getValue(Score::class.java)
            if (score != null) {
                progress.text = java.lang.String.valueOf(score.progress)
                placeTV.text = place.toString()
                pointsTV.text = java.lang.String.valueOf(score.points)
            } else {
                progress.text = "1"
                placeTV.text = "-"
                pointsTV.text = "0"
            }
        }
        return view
    }

    companion object {
        private val LOG_TAG = StatsFragment::class.java.simpleName
    }
}