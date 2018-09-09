package com.regularoddity.status

import com.regularoddity.status.shared.SharedMessages
import org.scalajs._
import com.felstar.scalajs.vue._

import scala.scalajs.js
import com.cibo.leaflet._

object ScalaJSExample {

  val app = new Vue(js.Dictionary(
    "el" -> "#app",
    "data" -> js.Dictionary(
      "message" -> "Hello Vue!"
    )
  ))

  val leafletJS = Leaflet.map("map", LMapOptions
    .jsOpt("crs", CRS.Simple)
    .jsOpt("maxZoom", 2).build)
  Leaflet.imageOverlay( s"${dom.document.location.protocol}//${dom.document.location.host}/assets/images/sample-office-plan.jpg",
//    LatLngBounds(LatLng(0, 0), LatLng(100, 100)))
    LatLngBounds(LatLng(0, 0), LatLng(581, 860)))
    .addTo(leafletJS)
  leafletJS.setView(LatLng(290, 430), 1)


  def main(args: Array[String]): Unit = {
    dom.document.getElementById("scalajsShoutOut").textContent = SharedMessages.itWorks
    dom.console.log(app)
  }
}
