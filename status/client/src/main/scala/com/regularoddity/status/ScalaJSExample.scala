package com.regularoddity.status

// import com.regularoddity.status.shared.SharedMessages
import org.scalajs._
import scala.scalajs.js

import com.felstar.scalajs.vue._
import com.cibo.leaflet._


object ScalaJSExample {
  private val material = Vue.component("material", js.Dictionary(
    "template" ->
      """
        |  <div class="page-container">
        |    <md-app>
        |      <md-app-toolbar class="md-primary">
        |        <span class="md-title">My Title</span>
        |      </md-app-toolbar>
        |    </md-app>
        |  </div>
      """.stripMargin
  ))

  private val app = new Vue(js.Dictionary(
    "el" -> "#app",
  ))

  def main(args: Array[String]): Unit = {
    scala.scalajs.js.eval("L.Icon.Default.imagePath = '/assets/images/';")
    Map.configureMap()
    DataHolder.getEmployeeData()
  }

}


