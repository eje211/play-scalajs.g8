package com.cibo.leaflet

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal


@JSGlobal("L")
@js.native
class CRS extends js.Object {

  def latLngToPoint(latLng: LatLng, zoon: Number): Point = js.native

  def pointToLatLng(point: Point, zoom: Number): LatLng = js.native

  def project(latLng: LatLng): Point = js.native

  def unproject(point: Point): LatLng = js.native

  def scale(zoom: Number): Number = js.native

  def zoom(scale: Number): Number = js.native

  def getProjectedBounds(zoom: Number): Bounds = js.native

  def distance(latLng1: LatLng, latLng2: LatLng): Number = js.native

  def wrapLatLng(latLng: LatLng): LatLng = js.native

  def wrapLatLngBounds(latLngBounds: LatLngBounds): LatLngBounds = js.native

  def code: String = js.native

  def wrapLng: Array[Number] = js.native

  def wrapLat: Array[Number] = js.native

  def infinite: Boolean = js.native
}


@JSGlobal("L.CRS")
@js.native
object CRS extends js.Object {
  def Earth: CRS = js.native
  def EPSG3395: CRS = js.native
  def EPSG3857: CRS = js.native
  def EPSG4326: CRS = js.native
  def Base: CRS = js.native
  def Simple: CRS = js.native
}