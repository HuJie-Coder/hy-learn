package com.hy.scala.clazz

/**
  * Main
  *
  * @author Jie.Hu
  * @date 6/15/21 10:17 AM
  */
object Main {

  def main(args: Array[String]): Unit = {

    val jayden = HyClass("Jayden", 1)

    jayden.print(jayden.name)

    val clazz = HyClass("Jayden1",2)

    clazz.print(clazz.name)

  }

}
