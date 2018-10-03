package com.regularoddity.status

import java.net.URI
import java.time.LocalDate

import com.regularoddity.status.shared.Role.Role
import com.regularoddity.status.shared.Status.Status
import com.regularoddity.status.shared.{Employee, Role, Status}
import io.circe.Decoder.Result
import io.circe.{CursorOp, Decoder, DecodingFailure, HCursor}

import scala.util.Try


object SerializableEmployee {
  private val MillisecondsInDay = 1000 * 60 * 60 * 24

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

  implicit val decode: Decoder[Employee] = new Decoder[Employee] {
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
          visible)
      }
  }
}