package com.lapperapp.laper.ui.home;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import ru.nikartm.support.ImageBadgeView;

public class NotificationCounter {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference userRef = db.collection("users");
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final ImageBadgeView imageBadgeView;

    public NotificationCounter(ImageBadgeView imageBadgeView) {
        this.imageBadgeView = imageBadgeView;
    }

    public void fetchNotificationCount() {
//        userRef.document(auth.getUid())
//                .get().addOnSuccessListener(doc -> {
//                    long count = doc.getLong("notificationCount");
//                    imageBadgeView.setShowCounter(true);
//                    imageBadgeView.setBadgeValue((int) count);
//                });
//
        userRef.document(auth.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    // Check for changes to the "name" field in the document
                    long count = snapshot.getLong("notificationCount");
                    imageBadgeView.setShowCounter(true);
                    imageBadgeView.setBadgeValue((int) count);
                } else {
                    Log.d(TAG, "Document does not exist");
                }
            }
        });

    }
}





