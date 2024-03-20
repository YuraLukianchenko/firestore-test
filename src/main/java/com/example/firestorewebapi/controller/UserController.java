package com.example.firestorewebapi.controller;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.WriteResult;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  @GetMapping("users")
  public String getAllUsers() {
    FirestoreOptions firestoreOptions =
        null;
    try {
      firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder()
          .setProjectId("app-fire-project")
          .setCredentials(GoogleCredentials.getApplicationDefault())
          .build();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    Firestore db = firestoreOptions.getService();

    DocumentReference docRef = db.collection("users").document("alovelace");
// Add document data  with id "alovelace" using a hashmap
    Map<String, Object> data = new HashMap<>();
    data.put("first", "Ada");
    data.put("last", "Lovelace");
    data.put("born", 1815);
//asynchronously write data
    ApiFuture<WriteResult> result = docRef.set(data);
// ...
// result.get() blocks on response
    try {
      System.out.println("Update time : " + result.get().getUpdateTime());
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }
  return "added user";
  }
}