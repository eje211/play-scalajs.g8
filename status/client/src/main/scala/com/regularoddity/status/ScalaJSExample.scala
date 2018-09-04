package com.regularoddity.status

import com.regularoddity.status.shared.SharedMessages
import org.scalajs._
import com.felstar.scalajs.vue._



object ScalaJSExample {

  def main(args: Array[String]): Unit = {
    dom.document.getElementById("scalajsShoutOut").textContent = SharedMessages.itWorks
    val v = new Vue()
  }
}
