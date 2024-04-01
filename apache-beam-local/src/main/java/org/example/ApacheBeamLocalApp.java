package org.example;


import java.util.Arrays;
import java.util.List;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.coders.StringUtf8Coder;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.Combine;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.Sum;
import org.apache.beam.sdk.values.PCollection;

public class ApacheBeamLocalApp {


  public static void main(String[] args) {

//    final List<String> LINES = Arrays.asList(
//        "To be, or not to be: that is the question: ",
//        "Whether 'tis nobler in the mind to suffer ",
//        "The slings and arrows of outrageous fortune, ",
//        "Or to take arms against a sea of troubles, ");
//
//
    PipelineOptions options =
        PipelineOptionsFactory.fromArgs(args).withValidation().create();
    Pipeline p = Pipeline.create(options);
        p.apply(Create.of(1, 2, 3, 4, 5))
        .apply(Combine.globally(Sum.ofIntegers()));




//    p.apply(Create.of(LINES))
//        .apply(Combine.globally())
//        .apply(TextIO.write().to("output"));
//
    p.run().waitUntilFinish();

  }
}