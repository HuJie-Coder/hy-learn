package com.hy.spark


import scala.collection.mutable

/**
 * Create by jie.hu on 2021/7/22
 */
package object sql {

  case class TagInfo(one_id:String,tags:mutable.HashMap[String,String])
}
