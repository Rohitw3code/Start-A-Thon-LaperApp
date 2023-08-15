package com.lapperapp.laper.Notification

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.lapperapp.laper.service.PushNotification

class NotificationSender constructor(val context: Context, val userId: String) {
    private val db = Firebase.firestore
    private val auth = FirebaseAuth.getInstance()
    var userRef = db.collection("users")

    fun sendNotification(ps: String, title: String) {
        val type = "0"
        val id = System.currentTimeMillis().toString()
        val hash = hashMapOf(
            "id" to id,
            "type" to type,
            "title" to title,
            "text" to ps,
            "userId" to auth.uid,
            "time" to System.currentTimeMillis()
        )

        userRef.document(userId)
            .collection("notification")
            .document(id)
            .set(hash)

        userRef.document(userId)
            .update("notificationCount", FieldValue.increment(1))

        val pn = PushNotification(context)
        pn.sendNotification(userId, "New Request", ps, type)
    }

    fun sendRequestSentNotification(ps: String) {
        val type = "0"
        val id = userRef.document().id
        val hash = hashMapOf(
            "id" to id,
            "type" to type,
            "title" to "New Request",
            "text" to ps,
            "userId" to auth.uid,
            "time" to System.currentTimeMillis()
        )

        userRef.document(userId)
            .collection("notification")
            .document(id)
            .set(hash)

        userRef.document(userId)
            .update("notificationCount", FieldValue.increment(1))

        val pn = PushNotification(context)
        pn.sendNotification(userId, "New Request", ps, type)
    }

    fun sendRequestAccept(ps: String) {
        val type = "1"
        val id = userRef.document().id
        val hash = hashMapOf(
            "id" to id,
            "type" to type,
            "title" to "Request Accepted",
            "text" to ps,
            "userId" to auth.uid,
            "time" to System.currentTimeMillis()
        )

        userRef.document(userId)
            .collection("notification")
            .document(id)
            .set(hash)

        userRef.document(userId)
            .update("notificationCount", FieldValue.increment(1))

        val pn = PushNotification(context)
        pn.sendNotification(userId, "Request Accepted", ps, type)
    }


    fun sendTaskCompleteRequestNotification(techId: String, requestId: String, ps: String) {
        val id = userRef.document().id
        val taskHash = hashMapOf(
            "time" to System.currentTimeMillis(),
            "expertId" to auth.uid.toString(),
            "userId" to userId,
            "techId" to techId,
            "requestId" to requestId,
            "problemStatement" to ps,
            "completed" to false
        )

        val hash = hashMapOf(
            "id" to id,
            "type" to "3",
            "title" to "Task Completed Request",
            "text" to "Accept the Request if you agree your work is done : " + ps,
            "userId" to auth.uid,
            "time" to System.currentTimeMillis()
        )

        userRef.document(userId)
            .collection("notification")
            .document(id)
            .set(hash)

        userRef.document(userId)
            .collection("taskCompletedRequest")
            .document(requestId)
            .set(taskHash)
        userRef.document(userId)
            .update("notificationCount", FieldValue.increment(1))
        val obj = PushNotification(context)
        obj.sendNotification(
            userId,
            "Task Completed Request",
            "Accept the Request if you agree your work is done : " + ps,
            "2"
        )
    }

    fun sendTaskCompletedNotification(techId: String, requestId: String, ps: String) {
        val id = userRef.document().id
        val type = "4"
        val taskHash = hashMapOf(
            "time" to System.currentTimeMillis(),
            "expertId" to userId,
            "userId" to auth.uid.toString(),
            "techId" to techId,
            "requestId" to requestId,
            "problemStatement" to ps,
            "taskCompletedTime" to System.currentTimeMillis()
        )
        val title = "Congrats Task is Completed"
        val text = "You have successfully completed you task : " + ps

        val hash = hashMapOf(
            "id" to id,
            "type" to type,
            "title" to title,
            "text" to text,
            "userId" to userId,
            "time" to System.currentTimeMillis()
        )

        userRef.document(userId)
            .collection("notification")
            .document(id)
            .set(hash)

        userRef.document(userId)
            .collection("taskCompletedRequest")
            .document(requestId)
            .update("completed", true)

        userRef.document(userId)
            .collection("taskCompleted")
            .document(requestId)
            .set(taskHash)

        userRef.document(userId)
            .update("notificationCount", FieldValue.increment(1))

        val obj = PushNotification(context)
        obj.sendNotification(userId, title, text, type)

    }

    fun requestDeclined(ps: String) {
        val type = "5"
        val id = userRef.document().id
        val hash = hashMapOf(
            "id" to id,
            "type" to type,
            "title" to "Request Decline",
            "text" to ps,
            "userId" to auth.uid,
            "time" to System.currentTimeMillis()
        )

        userRef.document(userId)
            .collection("notification")
            .document(id)
            .set(hash)

        userRef.document(userId)
            .update("notificationCount", FieldValue.increment(1))

        val pn = PushNotification(context)
        pn.sendNotification(userId, "Request Declined", ps, "0")

    }

    fun requestCancelled(ps: String) {
        val type = "6"
        val id = userRef.document().id
        val hash = hashMapOf(
            "id" to id,
            "type" to type,
            "title" to "Request Cancelled",
            "text" to ps,
            "userId" to auth.uid,
            "time" to System.currentTimeMillis()
        )

        userRef.document(userId)
            .collection("notification")
            .document(id)
            .set(hash)

        userRef.document(userId)
            .update("notificationCount", FieldValue.increment(1))

        val pn = PushNotification(context)
        pn.sendNotification(userId, "Request Cancelled", ps, "0")

    }


}