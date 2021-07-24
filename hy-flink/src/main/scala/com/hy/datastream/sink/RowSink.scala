package com.hy.datastream.sink


import org.apache.flink.api.common.serialization.SimpleStringEncoder
import org.apache.flink.core.fs.Path
import org.apache.flink.runtime.state.filesystem.FsStateBackend
import org.apache.flink.streaming.api.environment.CheckpointConfig
import org.apache.flink.streaming.api.functions.sink.filesystem.{OutputFileConfig, StreamingFileSink}
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.DateTimeBucketAssigner
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.table.filesystem.SerializationSchemaAdapter

/**
  * RowSink
  *
  * @author Jie.Hu
  * @date 7/10/21 12:44 PM
  */
object RowSink {

  val rowFilePath = new Path("hdfs://120.26.84.112:9000/user/flink/sink/row")
  val bulkFilePath = "hdfs://120.26.84.112:9000/user/flink/sink/bulk"

  def main(args: Array[String]): Unit = {

    System.setProperty("HADOOP_USER_NAME", "flink")

    val streamEnv = StreamExecutionEnvironment.getExecutionEnvironment

    val socketStream: DataStream[String] = streamEnv.socketTextStream("127.0.0.1", 8989)

    val rowFileSink = StreamingFileSink.forRowFormat(rowFilePath, new SimpleStringEncoder[(Int, String)]())
      .withBucketAssigner(new DateTimeBucketAssigner("yyyyMMddHH"))
      .withOutputFileConfig(OutputFileConfig.builder()
        .withPartPrefix("row")
        .withPartSuffix(".csv")
        .build())
      .withRollingPolicy(DefaultRollingPolicy.builder()
        .withRolloverInterval(60 * 1000L)
        .withInactivityInterval(30 * 1000L)
        .build())
      .build()

    import org.apache.flink.api.scala._

    val rowStream: DataStream[(Int, String)] = socketStream.map(row => {
      println(row)
      (row.length, row)
    }).setParallelism(1)
    rowStream.addSink(rowFileSink).setParallelism(1)

    streamEnv.execute("Socket")
  }

}
