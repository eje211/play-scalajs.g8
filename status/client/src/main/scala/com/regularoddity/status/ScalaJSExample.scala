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

  private val leafletJS = Leaflet.map("map", LMapOptions
    .jsOpt("crs", CRS.Simple)
    .jsOpt("maxZoom", 2)
    .jsOpt("zoomSnap", 0.25)
    .jsOpt("zoomDelta", 0.25)
    .jsOpt("maxBounds", LatLngBounds(LatLng(-20, -20), LatLng(601, 880))).build)

  def main(args: Array[String]): Unit = {
    Leaflet.imageOverlay( s"${dom.document.location.protocol}//${dom.document.location.host}"
      + "/assets/images/sample-office-plan.jpg",
      LatLngBounds(LatLng(0, 0), LatLng(581, 860))).addTo(leafletJS)
    leafletJS.setView(LatLng(290, 430), 1)

    DataHolder.getEmployeeData()
  }

}


