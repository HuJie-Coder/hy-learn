package com.hy.scala.func

/**
  * HyScalaFunction
  *
  * @author Jie.Hu
  * @date 6/23/21 11:19 PM
  */
object HyScalaFunction {

  def main(args: Array[String]): Unit = {

    val value = Some("scala")

    println(value.getOrElse("java"))

    println(System.nanoTime()/1e9)

    println(9e6.toInt)

    case class Person(name:String,age:Int)

    val person = Person("Jayden",1)
    val option = Option(person)

    option match {
      case Some(person) => println(s"Person:${person.name}" )
      case None => println("Nothing")
    }


  }

}
