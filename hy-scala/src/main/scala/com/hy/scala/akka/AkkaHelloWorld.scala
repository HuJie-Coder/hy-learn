package com.hy.scala.akka

import akka.actor.{Actor, ActorSystem, Props}

/**
  * AkkaHelloWorld
  *
  * @author Jie.Hu
  * @date 6/15/21 11:20 AM
  */
class HyActor(name:String) extends Actor{

  // 接收到消息之后的处理
  override def receive: Receive = {
    case "hello" => println("Hello from %s".format(name))
    case _ => println("sender: %s ......".format(sender.path))

  }

}

object Main {
  def main(args: Array[String]): Unit = {
    // 创建一个 Akka 系统
    val system = ActorSystem("Hello_System")
    // 创建一个新的 Actor
    val actor1 = system.actorOf(Props(new HyActor("Jayden")),name = "test")
    // 向 actor1 发送一个新的消息 "hello"
    actor1 ! "hello"
    // 向 actor2 发送一个新的消息 "test"
    actor1 ! "test"
    // 关闭系统
    system.terminate()
  }
}
