package com.hy.datastream.sink

import org.apache.flink.core.fs.Path
import org.apache.flink.formats.parquet.avro.ParquetAvroWriters
import org.apache.flink.runtime.state.filesystem.FsStateBackend
import org.apache.flink.streaming.api.environment.CheckpointConfig.ExternalizedCheckpointCleanup
import org.apache.flink.streaming.api.functions.sink.filesystem.{BucketAssigner, OutputFileConfig, StreamingFileSink}
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.{DefaultRollingPolicy, OnCheckpointRollingPolicy}
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.api.scala._

/**
  * ParquetSink
  *
  * @author Jie.Hu
  * @date 6/1/21 1:39 PM
  */
object ParquetSink {

  case class Person(name: String, age: Int)


  def main(args: Array[String]): Unit = {


    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStateBackend(new FsStateBackend("file:///Users/hujie/IdeaProjects/hy-learn/hy-flink/output/checkpoint"))
    val config = env.getCheckpointConfig
    config.enableExternalizedCheckpoints(ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION)
    env.enableCheckpointing(3000L)
    val source = env.socketTextStream("127.0.0.1", 8989).map(row => {
      val strings = row.split(",")
      Person(strings(0), strings(1).toInt)
    })

    source.addSink(StreamingFileSink
      .forBulkFormat(new Path("file:///Users/hujie/IdeaProjects/hy-learn/hy-flink/output/sink/flink_bulk_sink"), ParquetAvroWriters.forReflectRecord(classOf[Person]))
      .withBucketCheckInterval(1000L)
        .withOutputFileConfig(new OutputFileConfig("demo",".parquet"))
      .withRollingPolicy(OnCheckpointRollingPolicy.build())
      .build())

    env.execute()
  }

}
