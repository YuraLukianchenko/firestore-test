package org.example.service;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.MapElements;
import org.apache.beam.sdk.values.TypeDescriptor;
import org.example.customBeam.FirestoreDocumentGetter;
import org.example.customBeam.FirestoreShadowDocumentReplicator;
import org.example.model.User;
import org.springframework.stereotype.Service;

import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;

@Service
public class ApacheBeamPipeline {

  public void startPipeline(String userId) {
    System.out.println("pipeline " + userId);
    PipelineOptions options =
        PipelineOptionsFactory.fromArgs("--runner=direct").withValidation().create();
    Pipeline p = Pipeline.create(options);
//
    p.apply(Create.of(userId))
        .apply(MapElements.into(TypeDescriptor.of(User.class)).via(new FirestoreDocumentGetter()));
//        .apply(MapElements.via(new FirestoreShadowDocumentReplicator()));

//
//
    p.run().waitUntilFinish();

  }
}
