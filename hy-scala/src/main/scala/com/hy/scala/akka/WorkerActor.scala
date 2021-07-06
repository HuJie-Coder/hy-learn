package com.hy.scala.akka

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

/**
  * WorkerActor
  *
  * @author Jie.Hu
  * @date 6/15/21 2:03 PM
  */
object WorkerActor extends Actor{
  override def receive: Receive = {
    case "setup" => {
      println("WorkerActor:接收到消息 setup")
      val masterActor = context.actorSelection("akka.tcp://actorSystem@127.0.0.1:8888/user/masterActor")
      masterActor ! "connect"
    }
    case "success" => {
      println("WorkerActor:接收到 success 消息，sender:%s".format(sender()))
    }
  }
}

object Worker {
  def main(args: Array[String]): Unit = {
    //  创建 actorSystem
    val actorSystem = ActorSystem("actorSystem",ConfigFactory.load())

    // 加载 Actor
    val workerActor = actorSystem.actorOf(Props(WorkerActor),"workerActor")

    // 发送消息
    workerActor ! "setup"
  }
}
