package com.hy.scala.akka

import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorSystem, Props}
import com.hy.scala.akka.ReceiveActor.sender

/**
  * AkkaConmunication
  *
  * @author Jie.Hu
  * @date 6/15/21 11:30 AM
  */
object SenderActor extends Actor {

  // 从 actor 系统中获取 receiverActor 的地址
  val receiveActor = this.context.actorSelection("/user/stupid_1")

  override def receive: Receive = {
    case "start" => println("开始发送消息给 receiver")
      receiveActor ! "start"
    case _ =>
      println("%s : 我吃了，你吃了没".format(sender.path))
      receiveActor ! "吃了没"

  }
}

object ReceiveActor extends Actor {
  // 从 actor 系统中获取 receiverActor 的地址
  val senderActor = this.context.actorSelection("/user/stupid_2")

  override def receive: Receive = {
    case _ =>
      println("%s : 我吃了，你吃了没".format(sender.path))
      Thread.sleep(1000)
      senderActor ! "吃了没"
  }
}

object AkkaConmunication {
  def main(args: Array[String]): Unit = {
    val actorSystem = ActorSystem("user")
    val senderActor = actorSystem.actorOf(Props(SenderActor),"stupid_2")
    actorSystem.actorOf(Props(ReceiveActor),"stupid_1")
    senderActor ! "start"
  }
}
