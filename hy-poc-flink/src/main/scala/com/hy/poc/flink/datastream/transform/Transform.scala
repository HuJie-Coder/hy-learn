package com.hy.poc.flink.datastream.transform

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment

/**
  * Transform
  *
  * @author Jie.Hu
  * @date 7/25/21 9:44 AM
  */
object Transform {

  def main(args: Array[String]): Unit = {

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val eleDataStream = env.fromElements("abc","bcd","def","abs","abh","abk")

    val mapDataStream = eleDataStream.map("map:" + _)

    mapDataStream.print()

    env.execute("Transformation_Demo")

  }

}
