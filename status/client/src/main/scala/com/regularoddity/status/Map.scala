package com.regularoddity.status

import com.cibo.leaflet._
import com.felstar.scalajs.vue.{ExtendedVueFunction1, Vue}
import com.regularoddity.status.shared.Employee
import fr.hmil.roshttp.{HttpRequest, body}
import fr.hmil.roshttp.body.JSONBody
import fr.hmil.roshttp.body.JSONBody.{JSONArray, JSONObject, JSONValue}
import org.scalajs.dom
import org.scalajs.dom.raw.{Attr, HTMLElement, Node}

import scala.collection.mutable.ListBuffer
import scala.scalajs.js
import scala.util.{Failure, Success}
import io.circe.generic.auto._
import io.circe.syntax._
import scala.scalajs.js.timers._

import scala.scalajs.js.annotation.JSExport


object Map {

  import monix.execution.Scheduler.Implicits.global
  import SerializableEmployee._

  private val request = HttpRequest(s"//${dom.document.location.host}/updatePin")
    .withHeader("Content-Type", "application/json")

  var myVue: Option[Vue] = None

  private val leafletJS = Leaflet.map("map", LMapOptions
    .jsOpt("crs", CRS.Simple)
    .jsOpt("maxZoom", 2)
    .jsOpt("zoomSnap", 0.25)
    .jsOpt("zoomDelta", 0.25)
    .jsOpt("maxBounds", LatLngBounds(LatLng(-20, -20), LatLng(601, 880))).build)

  val employeeMarker = ListBuffer.empty[Marker]

  def configureMap(): Unit = {
    Leaflet.imageOverlay(s"${dom.document.location.protocol}//${dom.document.location.host}"
      + "/assets/images/sample-office-plan.jpg",
      LatLngBounds(LatLng(0, 0), LatLng(581, 860))).addTo(leafletJS)
    leafletJS.setView(LatLng(290, 430), 1)
  }

  def addPin(employee: Employee): Marker = {
    val marker = Leaflet.marker(
      LatLng(employee.mapLocation._1, employee.mapLocation._2),
      MarkerOptions.draggable(true).build)
    val jsEmployee = js.eval(s"(${employee.asJson.spaces2})")
    // val markerConstructor = Vue.extend(pinLabel).asInstanceOf[ExtendedVueFunction1]
    // val content = s"""</div>""".asInstanceOf[HTMLElement]
    marker.bindPopup(s"""<div id="_${employee.id.get}"><pin-label eid="${employee.id.get}"><pin-label><div>""".asInstanceOf[HTMLElement])
    marker.on("moveend", e => {
      employee.id.foreach(id =>
        request.post(
          body.JSONBody(JSONObject(
            "_id" -> (new JSONBody.JSONString(id): JSONValue),
            "location" ->
              JSONArray((marker.getLatLng().lat :: marker.getLatLng().lng :: Nil)
                .map(new JSONBody.JSONNumber(_): JSONValue):_*)
          ))
        )
          .onComplete({
            case Success(value) => println(value)
            case Failure(exception) => println(exception)
          }))
      e
    })
      .on("popupopen", e => {
          myVue = Some(new Vue(js.Dictionary(
            "el" -> s"#_${employee.id.get}"
          )))
        e
      })
        .on("popupclose", e => {
          myVue = None
          e
        })
    marker.addTo(leafletJS)
    marker
  }
}
