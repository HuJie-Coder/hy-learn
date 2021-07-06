package com.hy.spark.batch.source

import org.apache.spark.{SparkConf, SparkContext}

/**
  * HySource
  *
  * @author Jie.Hu
  * @date 6/23/21 10:52 PM
  */
object HySource {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setAppName("Hy")
    conf.setMaster("local[*]")
    val sc = SparkContext.getOrCreate(conf)
    val rdd = sc.parallelize(Array(1,2,3,4,5,6,7,1,2,3,1))
    val arrRDD = sc.makeRDD(Array(1,2,3,4,5,6,1))
    val count = rdd.count()

    println(count)
  }

}
