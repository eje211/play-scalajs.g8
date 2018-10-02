package com.regularoddity.status.controllers

import java.net.URL
import java.time.LocalDate

import javax.inject._
import com.regularoddity.status.controllers.{Employee, Role, Status => EmployeeStatus}
import play.api.http.Writeable
import play.api.libs.json._
import play.api.mvc._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.play.json.collection.JSONCollection
import reactivemongo.play.json._
import reactivemongo.play.json.collection._



import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

@Singleton
class GetData @Inject()(
  cc: ControllerComponents,
  val reactiveMongoApi: ReactiveMongoApi
) extends AbstractController(cc) with MongoController with ReactiveMongoComponents {
  implicit def EmployeeWriter = Employee.EmployeeWriter

  def get: Action[AnyContent] = Action.async {
    database.flatMap(_.collection[JSONCollection]("status").find(JsObject.empty)
      .cursor[JsObject]().fold[String]("")(_ + _.toString())).map(Ok(_))
  }

  def makeNew: Action[AnyContent] = Action {
    val employee = new Employee.Employee(
      "Annie",
      "Aardvark",
      Some("Ann"),
      "Organiser of things",
       new URL("http://"),
      "aaardvark@fake.com",
      "212 555 1234",
      "Things",
      LocalDate.of(2015, 5, 1),
      (10.0, 12.0),
      EmployeeStatus.available,
      true,
      None,
      "Annie",
      Role.employee,
      true
    )
    database.foreach(_.collection[BSONCollection]("status").insert(employee)(EmployeeWriter, cc.executionContext).onComplete {
      case Success(value) => println(value)
      case Failure(exception) => exception.printStackTrace()
    })

    Ok("Done")
  }
}
