package org.example;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.FileBasedSink;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.values.PCollection;

public class ApacheBeamApp {

  public static void main(String[] args) {
    PipelineOptions options =
        PipelineOptionsFactory.fromArgs(args).withValidation().create();

    Pipeline p = Pipeline.create(options);

    PCollection<String> input = p.apply("Read", TextIO.read()
        .from("fileIn.txt"));

    input.apply("Write", TextIO.write().to("fileOut"));
//          .withSuffix(".txt")
//          .withWritableByteChannelFactory(FileBasedSink.CompressionType.GZIP));

    p.run();
  }
}
