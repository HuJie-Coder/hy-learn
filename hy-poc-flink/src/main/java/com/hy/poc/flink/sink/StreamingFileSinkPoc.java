package com.hy.poc.flink.sink;

import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.core.fs.Path;
import org.apache.flink.runtime.state.hashmap.HashMapStateBackend;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.OutputFileConfig;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.DateTimeBucketAssigner;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * StreamingFileSinkPoc
 *
 * @author Jie.Hu
 * @date 8/6/21 2:10 PM
 */
public class StreamingFileSinkPoc {

    private static String HASH_MAP_CHECKPOINT_DIR = "file:///Users/hujie/IdeaProjects/hy-learn/hy-poc-flink/out/hashmap_checkpoint";

    private static String SINK_DIR = "file:///Users/hujie/IdeaProjects/hy-learn/hy-poc-flink/out/streamfilesink";

    public static void mock() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    ServerSocket serverSocket = new ServerSocket(8989, 3);
                    while (true) {
                        Socket socket = serverSocket.accept();
                        System.out.println(socket.getInetAddress().toString().substring(1) + ":" + socket.getPort() + " Connected");
                        OutputStream output = socket.getOutputStream();
                        int count = 0;
                        while (true) {
                            output.write(String.join(",", "number", String.valueOf(count++), "jayden\n").getBytes());
                            Thread.sleep(50);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };
        thread.start();
    }

    public static void main(String[] args) throws Exception {
        // mock data
        mock();

        StreamExecutionEnvironment streamEnv = StreamExecutionEnvironment.getExecutionEnvironment();
        ///////// checkpoint /////////
        // ??? 6 min ???????????? checkpoint
        streamEnv.enableCheckpointing(6 * 60 * 1000);
        CheckpointConfig checkpointConfig = streamEnv.getCheckpointConfig();
        // ??????????????????????????????????????? checkpoint ?????????????????????????????????
        // ???????????????????????????????????????????????? checkpoint ?????????
        checkpointConfig.enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        checkpointConfig.setCheckpointStorage(HASH_MAP_CHECKPOINT_DIR);
        // checkpoint ??????????????????????????? MinPauseBetweenCheckpoints ??? checkpoint ????????????????????????checkpoint ???????????????????????? 1
        checkpointConfig.setMaxConcurrentCheckpoints(1);
        // checkpoint ??????????????????????????????
        checkpointConfig.setMinPauseBetweenCheckpoints(2 * 60 * 1000);
        // checkpoint ???????????????????????? checkpoint ???????????? times ?????????????????????
        checkpointConfig.setTolerableCheckpointFailureNumber(5);
        // checkpoint ????????????
        checkpointConfig.setCheckpointTimeout(2 * 60 * 1000);


        ///////// state backend /////////
        streamEnv.setStateBackend(new HashMapStateBackend());

        //////// file sink ////////
        DataStreamSource<String> socketTextStream = streamEnv.socketTextStream("127.0.0.1", 8989);
        socketTextStream.name("source");

        StreamingFileSink fileSink = StreamingFileSink
                .forRowFormat(new Path(SINK_DIR), new SimpleStringEncoder<String>())
                .withBucketAssigner(new DateTimeBucketAssigner())
                .withRollingPolicy(DefaultRollingPolicy
                        .builder()
                        .withRolloverInterval(1 * 60 * 1000)
                        .withMaxPartSize(128 * 1024 * 1024L)
                        .withInactivityInterval(50 * 1000)
                        .build())
                .withOutputFileConfig(OutputFileConfig.builder()
                        .withPartPrefix("ck_row")
                        .withPartSuffix(".csv")
                        .build())
                .build();

        socketTextStream.rebalance();
        socketTextStream
                .addSink(fileSink)
                .name("sink")
                .setParallelism(4);

        streamEnv.execute();
    }
}
