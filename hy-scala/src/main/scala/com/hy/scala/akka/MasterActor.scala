package com.hy.scala.akka

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

/**
  * MasterActor
  *
  * @author Jie.Hu
  * @date 6/15/21 2:08 PM
  */
object MasterActor extends Actor{
  override def receive: Receive = {
    case "connect" => {
      println("MasterActor: 接收到 connect 的消息")
      sender ! "success"
    }
  }
}

object Master{
  def main(args: Array[String]): Unit = {
    val actorSystem = ActorSystem("actorSystem",ConfigFactory.load())
    val masterActor = actorSystem.actorOf(Props(MasterActor),"masterActor")
  }
}
