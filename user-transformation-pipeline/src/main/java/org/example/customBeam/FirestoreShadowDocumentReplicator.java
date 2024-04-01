package org.example.customBeam;

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
import org.apache.beam.sdk.transforms.SimpleFunction;
import org.example.model.ShadowUser;
import org.example.model.User;

public class FirestoreShadowDocumentReplicator extends SimpleFunction<User, ShadowUser> {

  @Override
  public ShadowUser apply(final User user) {

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

    DocumentReference shadowDocRef = db.collection("newUsers").document(user.userId());


    Map<String, Object> data = new HashMap<>();

    try {
//      System.out.println("born: " + result.get().get("born"));
      data.put("name",  user.first() + " " + user.last());
      data.put("born", user.last());
      ApiFuture<WriteResult> shadowResult = shadowDocRef.set(data);
      System.out.println("Update time : " + shadowResult.get().getUpdateTime());
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    }

    return new ShadowUser("", "", 3l);
  }
}
