package com.regularoddity.status.controllers

import javax.inject._
import com.regularoddity.status.shared.SharedMessages
import play.api.mvc._

  @Singleton
  class Application @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

    def index = Action {
      Ok(views.html.index(SharedMessages.itWorks))
    }
  }
// target/scala-2.12/client-jsdeps.js