package com.hy.sql.connector

import org.apache.flink.table.api.{EnvironmentSettings, Table, TableEnvironment}
import org.apache.flink.table.catalog.hive.HiveCatalog
import org.apache.flink.table.api.Expressions._
import org.apache.flink.api.scala._
import org.apache.flink.table.api._
import org.apache.flink.table.api.bridge.scala._
import org.apache.flink.types.Row


object HiveReadConnector {

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

    val table = tableEnv.from("flink_people").groupBy($"name").select($"name", $"age".count as "count")
    val value = table.toAppendStream[Row]
    value.print("stream")
    table.execute().print()


  }

}
