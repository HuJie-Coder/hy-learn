package com.hy.sql.connector

import org.apache.flink.api.scala._
import org.apache.flink.table.api.bridge.scala._
import org.apache.flink.table.api.{EnvironmentSettings, TableEnvironment, _}
import org.apache.flink.table.catalog.hive.HiveCatalog
import org.apache.flink.types.Row


object HiveWriteConnector {

  def main(args: Array[String]): Unit = {
    System.setProperty("HADOOP_USER_NAME", "root")

    val settings: EnvironmentSettings = EnvironmentSettings.newInstance().useBlinkPlanner().inBatchMode().build()
    val tableEnv: TableEnvironment = TableEnvironment.create(settings)

    val catalogName = "hive"
    val defaultDB = "hy"

    var path: String = HiveReadConnector.getClass.getClassLoader.getResource("hive-site.xml").getPath
    path = path.substring(0, path.lastIndexOf("/"))

    val catalog = new HiveCatalog(catalogName, defaultDB, path)
    tableEnv.registerCatalog(catalogName, catalog)
    tableEnv.useCatalog(catalogName)
    tableEnv.useDatabase(defaultDB)
    tableEnv.getConfig.setSqlDialect(SqlDialect.HIVE)

    val table = tableEnv.fromValues(
      DataTypes.ROW(
        DataTypes.FIELD("name", DataTypes.STRING()),
        DataTypes.FIELD("age", DataTypes.INT()),
        DataTypes.FIELD("etl_date", DataTypes.STRING())
      ),
      row("test1", 1, "20210101"),
      row("test2", 2, "20210101"),
      row("test3", 3, "20210102")
    )
    tableEnv.createTemporaryView("temp_table", table)

    // hive 中创建表，也可以在 hive 中提前创建好表
    tableEnv.executeSql(
      """
        | CREATE TABLE IF NOT EXISTS flink_write_test
        | (
        |   name STRING,
        |   age  INT
        | ) PARTITIONED BY (
        |   etl_date STRING
        | ) STORED AS PARQUET
      """.stripMargin)

    // 数据插入 方法1
    tableEnv.executeSql(
      """
        | INSERT INTO flink_write_test
        | SELECT name,age,etl_date
        | FROM temp_table
      """.stripMargin)

    // 数据插入 方法2 推荐使用
    table.executeInsert("flink_write_test")

    // 数据插入 方法3 不推荐使用
    tableEnv.insertInto("",table)
  }

}
