package com.regularoddity.status

import java.net.URI
import java.time.LocalDate

import com.regularoddity.status.shared.Role.Role
import com.regularoddity.status.shared.Status.Status
import com.regularoddity.status.shared.{Employee, Role, Status}
import io.circe.Decoder.Result
import io.circe._

import scala.scalajs.js
import scala.util.Try


object SerializableEmployee {
  val MillisecondsInDay = 1000 * 60 * 60 * 24

  implicit val decodeDate: Decoder[LocalDate] = new Decoder[LocalDate] {
    final def apply(c: HCursor): Result[LocalDate] =
      c.downField("$date").as[Long].map(d => LocalDate.ofEpochDay(d / MillisecondsInDay))
  }

  implicit val decodeRole: Decoder[Role] = new Decoder[Role] {
    final def apply(c: HCursor): Result[Role] =
      c.value.as[String].map(Role.withName)
  }

  implicit val decodeStatus: Decoder[Status] = new Decoder[Status] {
    final def apply(c: HCursor): Result[Status] =
      c.value.as[String].map(Status.withName)
  }

  implicit val decodeUrl: Decoder[URI] = new Decoder[URI] {
    final def apply(c: HCursor): Result[URI] =
      c.value.as[String].flatMap(s => Try(new URI(s))
        .toEither.left.map(_ => DecodingFailure("Bad URI", CursorOp.MoveRight :: Nil)))
  }

  implicit val decodeEmployee: Decoder[Employee] = new Decoder[Employee] {
    final def apply(c: HCursor): Decoder.Result[Employee] =
      for {
        firstName <- c.downField("firstName").as[String]
        lastName <- c.downField("lastName").as[String]
        displayName <- c.downField("displayName").as[Option[String]]
        jobTitle <- c.downField("jobTitle").as[String]
        photoUrl <- c.downField("photoUrl").as[URI]
        email <- c.downField("email").as[String]
        phone <- c.downField("phone").as[String]
        division <- c.downField("division").as[String]
        hireDate <- c.downField("hireDate").as[LocalDate]
        mapLocation <- c.downField("mapLocation").as[(Double, Double)]
        status <- c.downField("status").as[Status]
        active <- c.downField("active").as[Boolean]
        message <- c.downField("message").as[Option[String]]
        nickname <- c.downField("nickname").as[Option[String]]
        role <- c.downField("role").as[Role]
        visible <- c.downField("visible").as[Boolean]
        id <- c.downField("_id").downField("$oid").as[Option[String]]
      } yield {
        Employee(
          firstName,
          lastName,
          displayName,
          jobTitle,
          photoUrl,
          email,
          phone,
          division,
          hireDate,
          mapLocation,
          status,
          active,
          message,
          nickname,
          role,
          visible,
          id,
        )
      }
  }

  implicit val encodeEmployee: Encoder[Employee] = new Encoder[Employee] {
    def content(employee: Employee): List[(String, Json)] =  List(
      Some("firstName" -> Json.fromString(employee.firstName)),
      Some("lastName" -> Json.fromString(employee.lastName)),
      Some("displayName" -> Json.fromString(employee.displayName)).filter(_ => employee.displayNameIsDefined),
      Some("jobTitle" -> Json.fromString(employee.jobTitle)),
      Some("photoUrl" -> Json.fromString(employee.photoUrl.toString)),
      Some("email" -> Json.fromString(employee.email)),
      Some("phone" -> Json.fromString(employee.phone)),
      Some("division" -> Json.fromString(employee.division)),
      Some("hireDate" -> Json.fromLong(employee.hireDate.toEpochDay * MillisecondsInDay)),
      Some("mapLocation" -> Json.fromValues(List(employee.mapLocation._1, employee.mapLocation._2)
        .flatMap(coordinate => Json.fromDouble(coordinate)))),
      Some("status" -> Json.fromString(employee.status.toString)),
      Some("active" -> Json.fromBoolean(employee.active)),
      employee.message.map(message => "message" -> Json.fromString(message)),
      Some("nickname" -> Json.fromString(employee.nickname)).filter(_ => employee.nicknameIsDefined),
      Some("role" -> Json.fromString(employee.role.toString)),
      Some("visible" -> Json.fromBoolean(employee.visible)),
      employee.id.map(id => "_id" -> Json.fromString(id))
    ).flatten

    final def apply(employee: Employee): Json = Json.obj(content(employee):_*)
  }

  implicit def toJS(employee: Employee): js.Dictionary[js.Any] = js.Dictionary[js.Any](
    "firstName" -> employee.firstName,
    "lastName" -> employee.lastName,
    "displayName" -> employee.displayNameIsDefined,
    "jobTitle" -> employee.jobTitle,
    "photoUrl" -> employee.photoUrl.toString,
    "email" -> employee.email,
    "phone" -> employee.phone,
    "division" -> employee.division,
    "hireDate" -> new js.Date(employee.hireDate.toEpochDay * MillisecondsInDay),
    "mapLocation" -> js.Array(employee.mapLocation._1, employee.mapLocation._2),
    "status" -> employee.status.toString,
    "active" -> employee.active,
    "message" -> (employee.message.map(_.asInstanceOf[js.Any]) getOrElse js.undefined),
    "nickname" -> employee.nickname,
    "role" -> employee.role.toString,
    "visible" -> employee.visible,
    "id" -> employee.id.map(_.toString.asInstanceOf[js.Any]).getOrElse(js.undefined)
  )
}