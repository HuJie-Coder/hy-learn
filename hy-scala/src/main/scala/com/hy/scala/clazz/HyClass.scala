package com.hy.scala.clazz

/**
  * HyClass
  *
  * @author Jie.Hu
  * @date 6/15/21 10:12 AM
  */
class HyClass private(val name: String, var age: Int) {

  def print(message: String): Unit = {
    println(message)
  }

}

object HyClass {

  private var instance: HyClass = null

  /**
    * 单例的实现
    *
    * @param name
    * @param age
    * @return
    */
  def apply(name: String, age: Int): HyClass = {
    if (instance == null) {
      HyClass.synchronized {}
      if (instance == null) {
        instance = new HyClass(name, age)
      }
    }
    instance
  }
}

