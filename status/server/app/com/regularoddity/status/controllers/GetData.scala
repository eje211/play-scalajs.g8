package com.regularoddity.status.controllers

import java.net.URI
import java.time.LocalDate

import javax.inject._
import com.regularoddity.status.shared.{Employee, Role, Status => EmployeeStatus}
import play.api.libs.json._
import play.api.mvc._
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONArray, BSONDocument, BSONObjectID}
import reactivemongo.play.json.collection.JSONCollection
import reactivemongo.play.json._
import reactivemongo.play.json.collection._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

@Singleton
class GetData @Inject()(
  cc: ControllerComponents,
  val reactiveMongoApi: ReactiveMongoApi
) extends AbstractController(cc) with MongoController with ReactiveMongoComponents {
  implicit def EmployeeWriter: EmployeeSerialization.EmployeeWriter.type = EmployeeSerialization.EmployeeWriter
  implicit def EmployeeReader: EmployeeSerialization.EmployeeReader.type = EmployeeSerialization.EmployeeReader

  def get: Action[AnyContent] = Action.async {
    database.flatMap(_.collection[JSONCollection]("status").find(JsObject.empty)
      .cursor[JsObject]().fold[JsArray](JsArray.empty)(_ :+ _)).map(Ok(_))
  }

  def makeNew: Action[AnyContent] = Action {
    val employee = Employee(
      "Bernard",
      "Bernoulli",
      None,
      "Exeperimenter",
      new URI("http://WWW.example.com"),
      "bernard@fake.com",
      "212 555 5679",
      "Things",
      LocalDate.of(2014, 5, 1),
      (10.0, 14.0),
      EmployeeStatus.available,
      active = true,
      None,
      None,
      Role.employee,
      visible = true,
      None
    )
    database.foreach(_.collection[BSONCollection]("status")
        .insert(employee)(EmployeeWriter, cc.executionContext).onComplete {
      case Success(value) => println(value)
      case Failure(exception) => exception.printStackTrace()
    })
    Ok("Done")
  }

  def updatePin: Action[AnyContent] = Action.async { request => {
    val data = request.body.asJson
    val location = data.map(_("location").as[List[Double]])
    data.map(_("_id").as[String]).flatMap(id => BSONObjectID.parse(id).toOption)
      .map(id => database.flatMap(_.collection[BSONCollection]("status")
        .update(BSONDocument(
          "_id" -> id
        ), BSONDocument(
          "$set" -> BSONDocument(
            "mapLocation" -> location
          )
        ))
        .map(result =>
          Ok(Json.obj("status" -> JsString(result.flatten.errmsg.map(_.toString).getOrElse("OK")))))
    )).getOrElse(Future(Ok(Json.obj("status" -> JsString("Problem")))))

  }}
}
