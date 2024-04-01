package org.example.customBeam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import org.apache.beam.sdk.transforms.ProcessFunction;
import org.apache.beam.sdk.transforms.SerializableFunction;
import org.apache.beam.sdk.transforms.SerializableFunctions;
import org.apache.beam.sdk.transforms.SimpleFunction;
import org.example.model.User;

public class FirestoreDocumentGetter  extends SimpleFunction<String, String> {





  @Override
  public String apply(final String userId) {

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
    String first;
    String last;
    Long born;
    try {
      System.out.println("born: " + result.get().get("born"));
      first = (String) result.get().get("first");
      last = (String) result.get().get("last");
      born = (Long) result.get().get("born");
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }

    System.out.println("firestore doc");

    ObjectMapper om = new ObjectMapper();

    User user = new User(userId, first, last, born);
    String userfinal;
    try {
      userfinal = om.writeValueAsString(user);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    return userfinal;
  }

}
