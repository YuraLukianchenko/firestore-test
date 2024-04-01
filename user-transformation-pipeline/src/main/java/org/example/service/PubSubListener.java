package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.PubsubMessage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.example.model.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.pubsub.v1.ProjectSubscriptionName;

@Service
public class PubSubListener {

  @Value("${cloud.gcp.projectId}")
  private String projectId;

  @Value("${cloud.gcp.services.pubSub.subscriptionId}")
  private String subscriptionId;

  private ApacheBeamPipeline pipeline;

  private ObjectMapper objectMapper;

  public PubSubListener(ApacheBeamPipeline pipeline, ObjectMapper objectMapper) {
    this.pipeline = pipeline;
    this.objectMapper = objectMapper;
  }

  public void listenToPubSub() {

    ObjectMapper objectMapper = new ObjectMapper();
    subscribeAsyncExample(projectId, subscriptionId);
  }

  public void subscribeAsyncExample(String projectId, String subscriptionId) {
    ProjectSubscriptionName subscriptionName =
        ProjectSubscriptionName.of(projectId, subscriptionId);

    // Instantiate an asynchronous message receiver.
    MessageReceiver receiver =
        (PubsubMessage message, AckReplyConsumer consumer) -> {
          // Handle incoming message, then ack the received message.
          System.out.println("Id: " + message.getMessageId());
          System.out.println("Data: " + message.getData().toStringUtf8());

          String userId = null;
          try {
            userId = objectMapper.readValue(message.getData().toStringUtf8(), Message.class).userId();
          } catch (IOException e) {
            throw new RuntimeException(e);
          }

          pipeline.startPipeline(userId);
          consumer.ack();
        };

    Subscriber subscriber = null;
    try {
      subscriber = Subscriber.newBuilder(subscriptionName, receiver).build();
      // Start the subscriber.
      subscriber.startAsync().awaitRunning();
      System.out.printf("Listening for messages on %s:\n", subscriptionName.toString());
      // Allow the subscriber to run for 30s unless an unrecoverable error occurs.
      subscriber.awaitTerminated(3000, TimeUnit.SECONDS);
    } catch (TimeoutException timeoutException) {
      // Shut down the subscriber after 30s. Stop receiving messages.
      subscriber.stopAsync();
    }
  }

}


