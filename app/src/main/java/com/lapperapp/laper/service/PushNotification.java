package com.lapperapp.laper.service;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.trenzlr.firebasenotificationhelper.FirebaseNotificationHelper;

import org.json.JSONException;
import org.json.JSONObject;


public class PushNotification {
    private static final String KEY_TITLE = "title";
    private static final String KEY_TEXT = "text";
    private static final String KEY_ACTIVITY = "activityType";
    private static final String KEY_USER_ID = "userId";

    private final DatabaseReference tokenRef = FirebaseDatabase.getInstance().getReference();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference adminRef = db.collection("admin");
    private final CollectionReference expertRef = db.collection("experts");
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final String authId = FirebaseAuth.getInstance().getUid();
    Context context;

    public PushNotification(Context context) {
        this.context = context;
    }

    private String getJsonBody(String title, String text, String activityType) {
        JSONObject jsonObjectData = new JSONObject();
        try {
            jsonObjectData.put(KEY_TITLE, title);
            jsonObjectData.put(KEY_TEXT, text);
            jsonObjectData.put(KEY_ACTIVITY, activityType);
            jsonObjectData.put(KEY_USER_ID, authId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObjectData.toString();
    }


    public void sendNotification(String userId, String title, String text, String activityType) {
        adminRef.document("server")
                .get().addOnSuccessListener(documentSnapshot -> {
                    String serverKey = documentSnapshot.getString("serverKey");
                    expertRef.document(userId).get().addOnSuccessListener(documentSnapshot1 -> {
                        String token = documentSnapshot1.getString("token");
                        FirebaseNotificationHelper.initialize(serverKey)
                                .defaultJson(false, getJsonBody(title, text, activityType))
                                .receiverFirebaseToken(token)
                                .send();
                    });

//                    tokenRef.child("token").child(userId)
//                            .get().addOnSuccessListener(chi -> {
//                                String token = Objects.requireNonNull(chi.child("token").getValue()).toString();
//                                Log.d("token","toke : "+chi);
//                                Toast.makeText(context, "token : "+token, Toast.LENGTH_SHORT).show();
//                                FirebaseNotificationHelper.initialize(serverKey)
//                                        .defaultJson(false, getJsonBody(title, text, activityType))
//                                        .receiverFirebaseToken(token)
//                                        .send();
//                            });
                });
    }


}