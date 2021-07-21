package com.hy.sql.schema

import org.apache.flink.table.api.{EnvironmentSettings, TableEnvironment}

/**
  * DDL
  *
  * @author Jie.Hu
  * @date 5/28/21 11:06 AM
  */
object DDL {

  def main(args: Array[String]): Unit = {

    val settings = EnvironmentSettings.newInstance().inBatchMode().useBlinkPlanner().build()

    val tableEnvironment = TableEnvironment.create(settings)

    tableEnvironment.executeSql(
      """
        |CREATE TABLE ddl_demo(
        | name STRING,
        | age INT,
        | age_plus AS age * 5
        |) with (
        | 'connector' = 'filesystem',
        | 'path' = 'file:///Users/hujie/IdeaProjects/hy-learn/hy-flink/output/table',
        | 'format' = 'csv'
        |)
      """.stripMargin)

    val result = tableEnvironment.executeSql("SELECT * FROM ddl_demo")

    result.print()
  }

}
