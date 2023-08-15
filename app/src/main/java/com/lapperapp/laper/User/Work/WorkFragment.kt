package com.lapperapp.laper.User.Work

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lapperapp.laper.Adapter.ProjectWorkAdapter
import com.lapperapp.laper.Model.ProjectWorkModel
import com.lapperapp.laper.R

class WorkFragment(var userId: String) : Fragment() {
    val db = Firebase.firestore
    var userRef = db.collection("experts")
    var techRef = db.collection("tech")
    val auth = FirebaseAuth.getInstance()

    lateinit var workData: ArrayList<ProjectWorkModel>
    lateinit var workAdapter: ProjectWorkAdapter

    lateinit var projectWorkRecyclerView: RecyclerView


    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_work, container, false)

        projectWorkRecyclerView =
            view.findViewById<RecyclerView>(R.id.project_work_profile_recycler_view)
        projectWorkRecyclerView.layoutManager = LinearLayoutManager(context)

        workData = ArrayList<ProjectWorkModel>()
        var l1 = listOf<String>("fluter", "react")
        var l2 = listOf<String>("design", "app", "xml")

        workAdapter = ProjectWorkAdapter(workData)
        projectWorkRecyclerView.adapter = workAdapter
        workAdapter.notifyDataSetChanged()

        fetchTaskDone()

        return view

    }

    @SuppressLint("NotifyDataSetChanged")
    fun fetchTaskDone() {
        userRef.document(userId.trim())
            .collection("taskCompleted")
            .get().addOnSuccessListener { docs ->
                for (doc in docs.documents) {
                    val ps = doc.getString("problemStatement").toString()
                    val time = doc.getLong("taskCompletedTime") as Long
                    val techId = doc.getString("techId").toString().trim()
                    techRef.document(techId)
                        .get().addOnSuccessListener { tdoc ->
                            val techName = tdoc.getString("name").toString()
                            var l1 = listOf<String>("fluter", "react")
                            var l2 = listOf<String>("design", "app", "xml")
                            workData.add(ProjectWorkModel(techName, ps, l1, l2))
                            workAdapter.notifyDataSetChanged()
                        }
                }
            }
    }

}