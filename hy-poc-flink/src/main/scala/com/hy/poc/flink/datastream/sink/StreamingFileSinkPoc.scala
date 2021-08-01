package com.hy.poc.flink.datastream.sink

import org.apache.flink.api.common.serialization.SimpleStringEncoder
import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.core.fs.Path
import org.apache.flink.runtime.state.hashmap.HashMapStateBackend
import org.apache.flink.streaming.api.environment.{CheckpointConfig, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.functions.sink.filesystem.{OutputFileConfig, StreamingFileSink}
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.DateTimeBucketAssigner
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy
import org.slf4j.{Logger, LoggerFactory}
import org.apache.flink.streaming.api.datastream.DataStreamSource

/**
  * SocketHelloWorld
  *
  * @author Jie.Hu
  * @date 7/12/21 12:57 PM
  */
object StreamingFileSinkPoc {

  private val logger: Logger = LoggerFactory.getLogger(StreamingFileSinkPoc.getClass)

  def main(args: Array[String]): Unit = {

    val tool = ParameterTool.fromArgs(args)
    val streamEnv = StreamExecutionEnvironment.getExecutionEnvironment
    val socketStream: DataStreamSource[String] = streamEnv.socketTextStream(tool.get("host"), tool.getInt("port"))

    val config = streamEnv.getConfig
    val checkpointCfg = streamEnv.getCheckpointConfig
    checkpointCfg.enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION)
    streamEnv.enableCheckpointing(tool.getLong("checkpoint.interval"))
    streamEnv.setStateBackend(new HashMapStateBackend())
    checkpointCfg.setCheckpointStorage(tool.get("checkpoint.dir"))

    val fileSink = StreamingFileSink
      .forRowFormat(new Path(tool.get("output")), new SimpleStringEncoder[String]())
      // bucket 分配器，即如何分配 bucket，默认根据小时分 bucket
      .withBucketAssigner(new DateTimeBucketAssigner())
      .withBucketCheckInterval(1000L)
      .withOutputFileConfig(
        // 输出文件配置
        OutputFileConfig.builder()
          .withPartPrefix("poc_row")
          .withPartSuffix(".csv")
          .build())
      .withRollingPolicy(
        // note that:
        //    只有打开了 checkpoint 才会进行回滚
        //    如果 checkpoint 被禁用了，part 文件将永远处于 in-progress 或 pending 状态，下游无法安全使用数据
        // 默认的回滚策略如下
        //    1. 如果 当前时间 - 文件的创建时间 > RolloverInterval 就关闭 inprogress 文件
        //    2. 如果 当前的时间 - 文件的最后修改时间 > InactivityInterval 就关闭 inprogress 文件
        //    3. 如果 文件的大小 > max part size 就关闭 inprogress 文件
        // 关闭 inprogress 文件后，就将文件改成 pending 状态，等到下一次 checkpoint 成功之后，将文件改为 finished 状态
        // 请注意，修改状态需要开启 checkpoint ，否则不会修改文件状态，因为只有在 checkpoint 时才会调用方法
        // 正确的文件回滚策略应该是
        //    1. 确认多久生成一个文件，即 RolloverInterval，这个时间应该小于等于 checkpoint
        //    2. 确认如果没有新数据写入的时候应该多久关闭文件，即InactivityInterval，这个时间应该小于 RolloverInterval
        //    3. 确认文件的大小，默认为 128M
        DefaultRollingPolicy.builder()
          // 文件不活动时间
          .withInactivityInterval(60 * 1000L)
          // 每隔多久进行文件回滚
          .withRolloverInterval(60 * 1000L)
          // 文件大小
          .withMaxPartSize(128 * 1024 * 1024L)
          .build())
      .build()

    socketStream.map(row => row)
      .addSink(fileSink)

    streamEnv.execute("POC_Socket_Stream")
  }

}
