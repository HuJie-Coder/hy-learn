package com.hy.spark.sql

import java.util

import org.apache.spark.sql.{DataFrame, Encoder, Encoders, SparkSession}
import org.apache.spark.sql.expressions.Aggregator

import scala.collection.mutable

/**
 * Create by jie.hu on 2021/7/21
 */
object HyUdaf {

  case class Tag(one_id: String, tag_id: String, tag_value:String, source_table: String)


  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().master("local").appName("spark").getOrCreate()

    import spark.implicits._
    val frame: DataFrame = spark.sparkContext.makeRDD(
      Array(Tag("one1", "T1", "", "source"),
        Tag("one1", "T2", "", "source"),
        Tag("one1", "T3", "", "source"),
        Tag("one2", "T1", "", "source"),
        Tag("one2", "T2", "", "source"))).toDF()

    val tagDF = frame.as[Tag].groupByKey(row => row.one_id).mapGroups((key, iter) => {
      val hashMap = new mutable.HashMap[String, String]()
      while (iter.hasNext) {
        val tag = iter.next()
        hashMap.put(tag.tag_id, tag.tag_value)
      }
      TagInfo(key, hashMap)
    })

    tagDF.show(100,false)
  }
}
