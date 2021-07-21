package com.hy.sql.connector

import org.apache.flink.table.api.{EnvironmentSettings, TableEnvironment}

/**
  * HudiConnector
  *
  * @author Jie.Hu
  * @date 5/25/21 4:46 PM
  */
object HudiConnector {

  def main(args: Array[String]): Unit = {
    System.setProperty("HADOOP_USER_NAME", "hadoop")

    val settings: EnvironmentSettings = EnvironmentSettings.newInstance().useBlinkPlanner().inBatchMode().build()
    val tableEnv: TableEnvironment = TableEnvironment.create(settings)

    val hudiTableDDL =
      """
        |CREATE TABLE IF NOT EXISTS hudi_test(
        | id VARCHAR(20),
        | name VARCHAR(20),
        | age  INT,
        | `time` TIMESTAMP(3),
        | `date` VARCHAR(8)
        |)PARTITIONED BY(
        | `date`
        |)
        |WITH(
        | 'connector' = 'hudi',
        | 'path' = 'hdfs://120.26.84.112:9000/user/hive/warehouse/hy.db/hudi_test',
        | 'write.tasks' = '2',
        | 'write.batch.size.MB' = '128',
        | 'compaction.task' = '2',
        | 'table.type' = 'COPY_ON_WRITE',
        | 'hoodie.datasource.write.recordkey.field' = 'id',
        | 'write.precombine.field' = 'time',
        | 'read.streaming.enabled' = 'true',
        | 'read.streaming.check-interval' = '1'
        |)
      """.stripMargin

    val insertSql =
      """
        | INSERT INTO hudi_test VALUES
        |  ('id3','Danny',24,TIMESTAMP '1970-01-01 00:00:01','20210101'),
        |  ('id1','Danny',25,TIMESTAMP '1970-01-01 00:00:01','20210101'),
        |  ('id4','Stephen',34,TIMESTAMP '1970-01-01 00:00:02','20210102')
      """.stripMargin

    tableEnv.executeSql(hudiTableDDL)
    tableEnv.executeSql(insertSql)

  }

}
