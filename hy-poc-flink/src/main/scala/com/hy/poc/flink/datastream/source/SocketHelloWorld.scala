package com.hy.poc.flink.datastream.source

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.slf4j.{Logger, LoggerFactory}

/**
  * SocketHelloWorld
  *
  * @author Jie.Hu
  * @date 7/12/21 12:57 PM
  */
object SocketHelloWorld {

  private val logger: Logger = LoggerFactory.getLogger(SocketHelloWorld.getClass)

  def main(args: Array[String]): Unit = {

    val streamEnv = StreamExecutionEnvironment.getExecutionEnvironment
    val socketStream = streamEnv.socketTextStream("120.26.84.112", 9200)

    streamEnv.execute("POC_Socket_Stream")
  }

}
