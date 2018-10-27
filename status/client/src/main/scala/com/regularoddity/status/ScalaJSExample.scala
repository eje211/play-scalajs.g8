package com.regularoddity.status

import org.scalajs._

import scala.scalajs.js
import com.felstar.scalajs.vue._

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

@JSExportTopLevel("ScalaJSExample")
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


  val pinLabbel = Vue.component("pin-label", js.Dictionary(
    "props" -> js.Array("eid"),
    "data" -> js.eval("""(function () {
      return {employees: DataHolder.employeesJs};
    })"""),
    "template" -> """
                    |<div>
                    |  <h1>{{employees[eid].displayName}}</h1>
                    |  <div class="circled employee-card">
                    |      <img src="{{employees[eid].photoUrl}}" />
                    |  </div>
                    |  <div class="info employee-card">
                    |      <p v-if="employees[eid].nickname">A.K.A.: “{{employees[eid].nickname}}”</p>
                    |      <p>
                    |          {{employees[eid].jobTitle}}
                    |      </p>
                    |      <cite v-if="employees[eid].message">{{employees[eid].message}}</cite>
                    |      <p>&#x2709; <a v-if="employees[eid].workEmail" href="mailto:{{employees[eid].workEmail}}">
                    |          {{employees[eid].workEmail}}</a></p>
                    |      <p v-if="employees[eid].workPhone">Extension: {{employees[eid].workPhone }}</p>
                    |      <p v-if="employees[eid].mobile">{{employees[eid].mobile}}</p>
                    |      <p>Department: {{employees[eid].division}}</p>
                    |  </div>
                    |</div>
                  """.stripMargin))


  @JSExport("app")
  var app: Vue = new Vue(js.Dictionary(
    "el" -> "#app"
  ))

  def main(args: Array[String]): Unit = {
    DataHolder.getEmployeeData()

    scala.scalajs.js.eval("L.Icon.Default.imagePath = '/assets/images/';")
    Map.configureMap()
  }

}


