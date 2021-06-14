package com.hy.hudi.sql


import java.util.UUID

import org.apache.commons.io.{FileUtils, IOUtils}
import org.apache.hudi.{DataSourceReadOptions, DataSourceWriteOptions}
import org.apache.hudi.common.model.HoodieTableType
import org.apache.hudi.config.HoodieWriteConfig
import org.apache.spark.sql.{SQLContext, SaveMode, SparkSession}

/**
  * SqlDemo
  *
  * @author Jie.Hu
  * @date 5/27/21 5:23 PM
  */
object SqlDemo {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .master("local[*]").getOrCreate()

    import spark.implicits._

    val context = spark.sparkContext
    val arrayRdd = context.parallelize(
      Array(
        (1, "test1", "20210101"),
        (2, "test2", "20210101"),
        (3, "test3", "20210101"),
        (4, "test4", "20210102")
      ))


    val arrayDF = arrayRdd
      .map(row => (UUID.randomUUID.toString, row._1, row._2, row._3, System.currentTimeMillis()))
      .toDF("uuid", "age", "name", "date", "ts")
    val tableName = "hudi_test"
    val databaseName = "hy"
    val basePath = s"/Users/hujie/IdeaProjects/hy-learn/hy-hudi/data/${databaseName}.db/${tableName}"
    val basePathWithSchema = s"file://${basePath}"

    def generateData(): Unit = {
      arrayDF
        .repartition(1)
        .write
        .format("org.apache.hudi")
        .option(DataSourceWriteOptions.RECORDKEY_FIELD_OPT_KEY, "uuid")
        .option(DataSourceWriteOptions.PARTITIONPATH_FIELD_OPT_KEY, "date")
        .option(DataSourceWriteOptions.PRECOMBINE_FIELD_OPT_KEY, "ts")
        .option(DataSourceReadOptions.QUERY_TYPE_OPT_KEY,DataSourceReadOptions.QUERY_TYPE_INCREMENTAL_OPT_VAL)
        .option(HoodieWriteConfig.INSERT_PARALLELISM, "1")
        .option(HoodieWriteConfig.UPSERT_PARALLELISM, "1")
        .option(HoodieWriteConfig.TABLE_NAME, tableName)
        .option(DataSourceWriteOptions.TABLE_TYPE_OPT_KEY, HoodieTableType.MERGE_ON_READ.name())
        .mode(SaveMode.Append)
        .save(basePathWithSchema)
    }

    generateData()

  }


}
