package org.example.customBeam;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.WriteResult;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import org.apache.beam.sdk.transforms.SimpleFunction;

public class FirestoreDocumentGetter  extends SimpleFunction<String, Integer> {





  @Override
  public Integer apply(final String userId) {

    FirestoreOptions firestoreOptions;
    Firestore db;
    try {

      firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder()
          .setProjectId("app-fire-project")
          .setCredentials(GoogleCredentials.getApplicationDefault())
          .build();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    db = firestoreOptions.getService();

    DocumentReference docRef = db.collection("users").document(userId);
    ApiFuture<DocumentSnapshot> result = docRef.get();
    try {
      System.out.println("born: " + result.get().get("born"));
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }

    System.out.println("firestore doc");
    return 5;
  }

}
