package com.hy.poc.flink.datastream.sink

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.api.functions.sink.SocketClientSink
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.util.Collector

/**
  * StreamSocketSink
  *
  * @author Jie.Hu
  * @date 7/31/21 9:17 PM
  */
object StreamSocketSink {

  var host = "127.0.0.1"

  var sourcePort = 8989

  var sinkPort = 9898

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val socketStream = env.socketTextStream(host, sourcePort)
    socketStream.print()
    env.execute();
  }

}
