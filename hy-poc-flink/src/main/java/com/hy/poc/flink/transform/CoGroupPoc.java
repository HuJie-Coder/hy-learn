package com.hy.poc.flink.transform;

import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.CoGroupFunction;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.CheckpointingOptions;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.core.fs.Path;
import org.apache.flink.runtime.state.hashmap.HashMapStateBackend;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.functions.sink.SocketClientSink;
import org.apache.flink.streaming.api.functions.sink.filesystem.OutputFileConfig;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.DateTimeBucketAssigner;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy;
import org.apache.flink.streaming.api.windowing.assigners.SlidingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.Duration;
import java.util.Iterator;

/**
 * CoGroupPoc
 *
 * @author Jie.Hu
 * @date 8/6/21 5:34 PM
 */
public class CoGroupPoc {

//    private static String HASH_MAP_CHECKPOINT_DIR = "hdfs://120.26.84.112:9000/user/flink/flink-checkpoints";

    private static String SINK_DIR = "hdfs://120.26.84.112:9000/user/flink/output/flink-cogroup";
    public static String hostname = "jobmanager";
    public static Integer inputPort1 = 9500;
    public static Integer inputPort2 = 9501;
    public static String sinkHostanme = "120.26.84.112";
    public static Integer sinkPort = 9200;

    /**
     * cogroup demo
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        Configuration configuration = new Configuration();
        configuration.set(CheckpointingOptions.MAX_RETAINED_CHECKPOINTS, 10);

        StreamExecutionEnvironment streamEnv = StreamExecutionEnvironment.getExecutionEnvironment(configuration);

        ///////// checkpoint /////////
        streamEnv.enableCheckpointing(3 * 60 * 1000);
        CheckpointConfig checkpointConfig = streamEnv.getCheckpointConfig();
        checkpointConfig.enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
//        checkpointConfig.setCheckpointStorage(HASH_MAP_CHECKPOINT_DIR);
        checkpointConfig.setMinPauseBetweenCheckpoints(1 * 60 * 1000);
        checkpointConfig.setMaxConcurrentCheckpoints(1);
        checkpointConfig.setTolerableCheckpointFailureNumber(5);
        checkpointConfig.setCheckpointTimeout(50 * 1000);


        ///////// state backend /////////
        streamEnv.setStateBackend(new HashMapStateBackend());

        DataStreamSource<String> socketTextStream1 = streamEnv.socketTextStream(hostname, inputPort1);
        DataStreamSource<String> socketTextStream2 = streamEnv.socketTextStream(hostname, inputPort2);

        //////// watermark strategy ////////
        WatermarkStrategy<Tuple2<String, String>> watermarkStrategy = WatermarkStrategy
                .<Tuple2<String, String>>forBoundedOutOfOrderness(Duration.ofSeconds(0))
                .withTimestampAssigner((element, recordTimestamp) -> {
                    Long timestamp = Long.valueOf(element.f1);
                    return timestamp;
                }).withIdleness(Duration.ofSeconds(3));

        SingleOutputStreamOperator<Tuple2<String, String>> stream1 =
                socketTextStream1
                        .map(row -> {
                            String[] strings = row.split(",");
                            return Tuple2.of(strings[0], strings[1]);
                        })
                        .returns(Types.TUPLE(Types.STRING, Types.STRING))
                        .assignTimestampsAndWatermarks(watermarkStrategy);

        SingleOutputStreamOperator<Tuple2<String, String>> stream2 =
                socketTextStream2
                        .map(row -> {
                            String[] strings = row.split(",");
                            return Tuple2.of(strings[0], strings[1]);
                        })
                        .returns(Types.TUPLE(Types.STRING, Types.STRING))
                        .assignTimestampsAndWatermarks(watermarkStrategy);

        // filesink
        StreamingFileSink fileSink = StreamingFileSink
                .forRowFormat(new Path(SINK_DIR), new SimpleStringEncoder<String>())
                .withBucketAssigner(new DateTimeBucketAssigner())
                .withRollingPolicy(DefaultRollingPolicy
                        .builder()
                        .withRolloverInterval(5 * 60 * 1000)
                        .withMaxPartSize(128 * 1024 * 1024L)
                        .withInactivityInterval(50 * 1000)
                        .build())
                .withOutputFileConfig(OutputFileConfig.builder()
                        .withPartPrefix("ck_row")
                        .withPartSuffix(".csv")
                        .build())
                .build();



        // cogroup
        stream1.coGroup(stream2)
                .where(row1 -> row1.f0)
                .equalTo(row2 -> row2.f0)
                .window(SlidingEventTimeWindows.of(Time.seconds(5), Time.milliseconds(50)))
                .apply(new CoGroupFunction<Tuple2<String, String>, Tuple2<String, String>, Tuple2<String, Integer>>() {
                    @Override
                    public void coGroup(Iterable<Tuple2<String, String>> first,
                                        Iterable<Tuple2<String, String>> second,
                                        Collector<Tuple2<String, Integer>> out) throws Exception {
                        Iterator<Tuple2<String, String>> iterator1 = first.iterator();
                        Iterator<Tuple2<String, String>> iterator2 = second.iterator();
                        int count = 0;
                        String key = null;
                        while (iterator1.hasNext()) {
                            key = iterator1.next().f0;
                            count++;
                        }
                        while (iterator2.hasNext()) {
                            key = iterator2.next().f0;
                            count++;
                        }
                        out.collect(Tuple2.of(key, count));
                    }
                })
                .map(tuple -> tuple.toString() + "\n")
                .setParallelism(2)
                // socket sink
                .addSink(fileSink)
        ;


        streamEnv.execute();


    }
}
