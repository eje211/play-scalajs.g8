package com.regularoddity.status

import com.cibo.leaflet._
import com.regularoddity.status.shared.Employee
import fr.hmil
import fr.hmil.roshttp
import fr.hmil.roshttp.{HttpRequest, body}
import fr.hmil.roshttp.body.JSONBody
import fr.hmil.roshttp.body.JSONBody.{JSONArray, JSONObject, JSONValue}
import io.circe.JsonNumber
import org.scalajs.dom

import scala.collection.mutable.ListBuffer
import scala.util.{Failure, Success}

object Map {

  import monix.execution.Scheduler.Implicits.global

  private val request = HttpRequest(s"//${dom.document.location.host}/updatePin")
    .withHeader("Content-Type", "application/json")

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

  def addPin(employee: Employee): Unit = {
    val marker = Leaflet.marker(
      LatLng(employee.mapLocation._1, employee.mapLocation._2),
        MarkerOptions.draggable(true).build)
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
      println(marker.getLatLng())
      e
    })
    marker.addTo(leafletJS)
  }
}
