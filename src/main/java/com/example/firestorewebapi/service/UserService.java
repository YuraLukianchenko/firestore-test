package com.example.firestorewebapi.service;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.WriteResult;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Value("${cloud.gcp.projectId}")
  private String projectId;

  @Value("${cloud.gcp.services.firestore.collectionName}")
  private String collectionName;

  @SneakyThrows
  public String addRandomUser() {
    FirestoreOptions firestoreOptions;

    firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder()
        .setProjectId(projectId)
        .setCredentials(GoogleCredentials.getApplicationDefault())
        .build();

    Firestore db = firestoreOptions.getService();

    Random random = new Random();
    int number = random.nextInt();
    String userId = "USERNEW" + number;
    DocumentReference docRef = db.collection(collectionName).document(userId);

    // Add document data  with id "alovelace" using a hashmap
    Map<String, Object> data = new HashMap<>();
    data.put("first", "Bibineto");
    data.put("last", "John5");
    data.put("born", 1965);

    //asynchronously write data
    ApiFuture<WriteResult> result = docRef.set(data);

    // ...
    // result.get() blocks on response
    System.out.println("Update time : " + result.get().getUpdateTime());

    return userId;
  }
}
