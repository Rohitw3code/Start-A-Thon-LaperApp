package com.lapperapp.laper.ui.dashboard

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lapperapp.laper.Model.RequestReceivedModel
import com.lapperapp.laper.R
import com.lapperapp.laper.ui.dashboard.AvailableClient.AvailableClientAdapter
import com.lapperapp.laper.ui.dashboard.AvailableClient.AvailableClientModel
import com.lapperapp.laper.ui.dashboard.AvailableExpert.AvailableRequestAdapter
import com.lapperapp.laper.ui.dashboard.AvailableExpert.AvailableRequestMode
import com.lapperapp.laper.ui.dashboard.Declined.DeclinedActivity
import com.lapperapp.laper.ui.dashboard.RequestReceived.ReceivedRequestsActivity
import com.lapperapp.laper.ui.dashboard.RequestReceived.RequestReceivedAdapter
import com.lapperapp.laper.ui.dashboard.RequestSent.RequestSentActivity
import com.lapperapp.laper.ui.dashboard.RequestSent.RequestSentAdapter
import com.lapperapp.laper.ui.dashboard.RequestSent.RequestSentModel
import com.lapperapp.laper.ui.dashboard.TaskComplete.TaskCompleteAdapter
import com.lapperapp.laper.ui.dashboard.TaskComplete.TaskCompleteModel
import com.lapperapp.laper.ui.dashboard.Users.UsersActivity

class DashboardFragment : Fragment() {
    var db = Firebase.firestore
    var userRef = db.collection("users")
    var auth = FirebaseAuth.getInstance()

    private lateinit var requestRecycler: RecyclerView
    private lateinit var reqReceivedModel: ArrayList<RequestReceivedModel>
    private lateinit var reqReceivedAdapter: RequestReceivedAdapter
    private lateinit var viewAllRequest: TextView
    private lateinit var totalRequests: TextView
    private lateinit var requestSentAll: TextView

    private lateinit var requestSentRecyclerView: RecyclerView
    private lateinit var reqSentAdapter: RequestSentAdapter
    private lateinit var reqSentModel: ArrayList<RequestSentModel>

    private lateinit var availableRecyclerView: RecyclerView
    private lateinit var avaReqAdapter: AvailableRequestAdapter
    private lateinit var avaReqModel: ArrayList<AvailableRequestMode>

    private lateinit var avaClientRecyclerView: RecyclerView
    private lateinit var avaClientReqAdapter: AvailableClientAdapter
    private lateinit var avaClientReqModel: ArrayList<AvailableClientModel>

    private lateinit var taskCompleteReqRecyclerView: RecyclerView
    private lateinit var taskCompleteReqAdapter: TaskCompleteAdapter
    private lateinit var taskCompleteReqModel: ArrayList<TaskCompleteModel>

    private lateinit var acLinearLayout: LinearLayout
    private lateinit var expertLinearLayout: CardView

    private lateinit var requestSentLinear: LinearLayout
    private lateinit var avaClientLinear: CardView
    private lateinit var taskCompleteCard: CardView
    private lateinit var reqReceivedCard: CardView
    private lateinit var usersCard: CardView

    lateinit var rrIds: List<String>
    lateinit var rsIds: List<String>
    lateinit var arIds: List<String>
    lateinit var acIds: List<String>
    lateinit var tcrIds: List<String>

    private lateinit var declinedRequests: CardView

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        viewAllRequest = view.findViewById(R.id.dashboard_view_all)
        totalRequests = view.findViewById(R.id.dashboard_total_request)

        requestSentRecyclerView = view.findViewById(R.id.dash_request_sent_recycler_view)
        requestSentLinear = view.findViewById(R.id.all_request_sent_dashboard_linear)
        requestRecycler = view.findViewById(R.id.request_recycler_view_dashboard)
        reqReceivedCard = view.findViewById(R.id.dash_relative_1)
        usersCard = view.findViewById(R.id.user_list_dash)

        requestSentAll = view.findViewById(R.id.all_request_sent_dashboard)
        requestRecycler.layoutManager = LinearLayoutManager(context)

        reqReceivedModel = ArrayList<RequestReceivedModel>()
        reqReceivedAdapter = RequestReceivedAdapter(reqReceivedModel)
        requestRecycler.adapter = reqReceivedAdapter
        reqReceivedAdapter.notifyDataSetChanged()

        taskCompleteReqRecyclerView =
            view.findViewById(R.id.dash_task_complete_request_recycler_view)
        taskCompleteCard = view.findViewById(R.id.dash_task_complete_request_linear)
        taskCompleteReqRecyclerView.layoutManager = LinearLayoutManager(context)
        taskCompleteReqModel = ArrayList<TaskCompleteModel>()
        taskCompleteReqAdapter = TaskCompleteAdapter(taskCompleteReqModel)
        taskCompleteReqRecyclerView.adapter = taskCompleteReqAdapter
        taskCompleteReqAdapter.notifyDataSetChanged()

        availableRecyclerView = view.findViewById(R.id.dash_expert_available_recycler_view)
        availableRecyclerView.layoutManager = LinearLayoutManager(context)
        avaReqModel = ArrayList<AvailableRequestMode>()
        avaReqAdapter = AvailableRequestAdapter(avaReqModel)
        availableRecyclerView.adapter = avaReqAdapter
        avaReqAdapter.notifyDataSetChanged()

        requestSentRecyclerView.layoutManager = LinearLayoutManager(context)
        reqSentModel = ArrayList()
        reqSentAdapter = RequestSentAdapter(reqSentModel)
        requestSentRecyclerView.adapter = reqSentAdapter
        reqSentAdapter.notifyDataSetChanged()

        avaClientLinear = view.findViewById(R.id.dash_available_client_linear)
        avaClientRecyclerView = view.findViewById(R.id.dash_available_client_recycler_view)
        avaClientRecyclerView.layoutManager = LinearLayoutManager(context)
        avaClientReqModel = ArrayList<AvailableClientModel>()
        avaClientReqAdapter = AvailableClientAdapter(avaClientReqModel)
        avaClientRecyclerView.adapter = avaClientReqAdapter
        avaClientReqAdapter.notifyDataSetChanged()

        acLinearLayout = view.findViewById(R.id.dash_relative_4)
        expertLinearLayout = view.findViewById(R.id.dash_relative_3)

        declinedRequests = view.findViewById(R.id.declined_request_Sent_received_dashboard)

        viewAllRequest.setOnClickListener {
            val intent = Intent(context, ReceivedRequestsActivity::class.java)
            startActivity(intent)
        }



        requestSentAll.setOnClickListener {
            val intent = Intent(context, RequestSentActivity::class.java)
            startActivity(intent)
        }

        declinedRequests.setOnClickListener {
            val intent = Intent(context, DeclinedActivity::class.java)
            startActivity(intent)
        }

        usersCard.setOnClickListener {
            val intent = Intent(context, UsersActivity::class.java)
            startActivity(intent)
        }

        rsIds = listOf()
        rrIds = listOf()
        acIds = listOf()
        arIds = listOf()
        tcrIds = listOf()

        fetchRequests()
        fetchAvailableClient()
        fetchAvailableReq()
        fetchRequestSent()
        fetchTaskComplete()

        return view

    }

    override fun onStart() {
        super.onStart()
        fetchRequests()
        fetchAvailableClient()
        fetchAvailableReq()
        fetchRequestSent()
        fetchTaskComplete()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun fetchTaskComplete() {
        userRef.document(auth.uid.toString())
            .collection("taskCompletedRequest")
            .whereEqualTo("completed", false)
            .get().addOnSuccessListener { docs ->
                if (docs.size() < 1) {
                    taskCompleteCard.visibility = View.GONE
                }

                for (doc in docs.documents) {
                    if (!tcrIds.contains(doc.id)) {
                        tcrIds = tcrIds + doc.id
                        val expertId = doc.getString("expertId").toString()
                        val ps = doc.getString("problemStatement").toString()
                        val techId = doc.getString("techId").toString()
                        val time = doc.getLong("time") as Long

                        userRef.document(expertId)
                            .get().addOnSuccessListener { documents ->
                                if (documents != null) {
                                    val uImageUrl = documents.get("userImageUrl").toString()
                                    val uName = documents.get("username").toString()
                                    taskCompleteReqModel.add(
                                        TaskCompleteModel(
                                            expertId,
                                            uName,
                                            uImageUrl,
                                            doc.id,
                                            ps,
                                            time,
                                            techId
                                        )
                                    )
                                    taskCompleteReqAdapter.notifyDataSetChanged()
                                }
                            }
                        taskCompleteReqAdapter.notifyDataSetChanged()
                    }
                }
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun fetchRequestSent() {
        userRef.document(auth.uid.toString())
            .collection("requestSent")
            .whereEqualTo("taskCompleted", false)
            .whereEqualTo("declined", false)
            .limit(3)
            .get()
            .addOnSuccessListener { docs ->
                if (docs.size() < 1) {
                    requestSentLinear.visibility = View.GONE
                }
                for (doc in docs) {
                    val reqSentDate = doc.getLong("requestSentDate") as Long
                    val expertId = doc.getString("expertId").toString()
                    val requestId = doc.id
                    val ps = doc.getString("desc").toString()
                    val cancel = doc.getBoolean("cancelled") as Boolean
                    val techId = doc.getString("techId").toString()
                    if (!rsIds.contains(requestId)) {
                        userRef.document(expertId)
                            .get().addOnSuccessListener { documents ->
                                if (documents != null) {
                                    val uImageUrl = documents.get("userImageUrl").toString()
                                    val uName = documents.get("username").toString()
                                    reqSentModel.add(
                                        RequestSentModel(
                                            reqSentDate,
                                            expertId,
                                            requestId,
                                            uName,
                                            uImageUrl,
                                            ps,
                                            cancel,
                                            techId
                                        )
                                    )
                                    reqSentAdapter.notifyDataSetChanged()
                                }
                                reqSentAdapter.notifyDataSetChanged()
                            }
                        rsIds = rsIds + requestId
                    }
                }
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun fetchAvailableClient() {
        userRef.document(auth.uid.toString())
            .collection("availableClient")
            .limit(3)
            .get().addOnSuccessListener { docu ->
                if (docu.size() < 1) {
                    avaClientLinear.visibility = View.GONE
                }
                for (doc in docu.documents) {
                    val requestId = doc.id
                    if (!acIds.contains(requestId)) {
                        val userId = doc.getString("userId").toString()
                        val ps = doc.getString("problemStatement").toString()
                        val techId = doc.getString("techId").toString()
                        userRef.document(userId)
                            .get().addOnSuccessListener { expertDoc ->
                                val expertName = expertDoc.getString("username").toString()
                                val expertImageURL = expertDoc.getString("userImageUrl").toString()
                                avaClientReqModel.add(
                                    AvailableClientModel(
                                        expertName,
                                        expertImageURL,
                                        0,
                                        userId,
                                        requestId,
                                        ps,
                                        techId
                                    )
                                )
                                avaClientReqAdapter.notifyDataSetChanged()
                            }
                        acIds = acIds + requestId
                    }
                }
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun fetchAvailableReq() {
        userRef.document(auth.uid.toString())
            .collection("availableExpert")
            .limit(3)
            .get().addOnSuccessListener { docu ->
                if (docu.size() < 1) {
                    expertLinearLayout.visibility = View.GONE
                }
                for (doc in docu.documents) {
                    val requestId = doc.id
                    if (!arIds.contains(requestId)) {
                        val expertId = doc.getString("expertId").toString()
                        val ps = doc.getString("problemStatement").toString()
                        userRef.document(expertId)
                            .get().addOnSuccessListener { expertDoc ->
                                val expertName = expertDoc.getString("username").toString()
                                val expertImageURL = expertDoc.getString("userImageUrl").toString()
                                avaReqModel.add(
                                    AvailableRequestMode(
                                        expertName,
                                        expertImageURL,
                                        ps,
                                        0,
                                        expertId,
                                        requestId,
                                        ps
                                    )
                                )
                                avaReqAdapter.notifyDataSetChanged()
                            }
                        arIds = arIds + requestId
                    }
                }
            }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun fetchRequests() {
        userRef.document(auth.uid.toString())
            .collection("requestReceived")
            .whereEqualTo("taskCompleted", false)
            .whereEqualTo("declined", false)
            .limit(3)
            .get().addOnSuccessListener { docu ->
                if (docu.size() < 1) {
                    reqReceivedCard.visibility = View.GONE
                }
                for (doc in docu.documents) {
                    val requestId = doc.id
                    if (!rrIds.contains(requestId)) {
                        val rUserId = doc.get("userId").toString()
                        val rDesc = doc.get("desc").toString()
                        val rTechId = doc.get("techId").toString()
                        val rDate = doc.getLong("requestDate") as Long
                        val accepted = doc.getBoolean("accepted") as Boolean
                        reqReceivedModel.add(
                            RequestReceivedModel(
                                rDesc,
                                doc.id,
                                rUserId,
                                rTechId,
                                rDate,
                                accepted
                            )
                        )
                        reqReceivedAdapter.notifyDataSetChanged()
                        rrIds = rrIds + requestId
                    }
                }
            }

    }

}