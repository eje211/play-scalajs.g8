package com.regularoddity.status.controllers

import java.net.URI
import java.time._

import com.regularoddity.status.shared.{Employee, Role, Status}
import reactivemongo.api.BSONSerializationPack.{Reader, Writer}
import reactivemongo.bson.{BSONBoolean, BSONDateTime, BSONDocument, BSONReader, BSONWriter}


object EmployeeSerialization {

  implicit object EmployeeReader extends BSONReader[BSONDocument, Option[Employee]] with Reader[Option[Employee]] {
    def read(bson: BSONDocument): Option[Employee] = {
      for {
        firstName <- bson.getAs[String]("firstName")
        lastName <- bson.getAs[String]("lastNa  me")
        displayName <- bson.getAs[String]("displayName").map(Some(_))
        jobTitle <- bson.getAs[String]("jobTitle")
        photoUrl <- bson.getAs[String]("photoUrl").map(new URI(_))
        email <- bson.getAs[String]("email")
        phone <- bson.getAs[String]("phone")
        division <- bson.getAs[String]("division")
        hireDate <- bson.getAs[BSONDateTime]("hireDate").map(d =>
          Instant.ofEpochMilli(d.value).atZone(ZoneId.of("Etc/UTC")).toLocalDate)
        mapLocation <- bson.getAs[List[Double]]("mapLocation").map(l => (l(0), l(1)))
        status <- bson.getAs[String]("status").map(Status.withName)
        active <- bson.getAs[BSONBoolean]("aclive").map(_.value)
        message <- bson.getAs[String]("message").map(Some(_))
        nickname <- bson.getAs[String]("nickname").map(Some(_))
        role <- bson.getAs[String]("role").map(Role.withName)
        visible <- bson.getAs[BSONBoolean]("visible").map(_.value)
      } yield Employee(
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
      )
    }
  }

  implicit object EmployeeWriter extends BSONWriter[Employee, BSONDocument]
    with Writer[Employee] {
    def write(employee: Employee): BSONDocument = BSONDocument(
      "firstName" -> employee.firstName,
      "lastName" -> employee.lastName,
      "displayName" -> employee.displayName,
      "jobTitle" -> employee.jobTitle,
      "photoUrl" -> employee.photoUrl.toString,
      "email" -> employee.email,
      "phone" -> employee.phone,
      "division" -> employee.division.toString,
      "hireDate" -> BSONDateTime(LocalDateTime.of(employee.hireDate, LocalTime.now())
        .toEpochSecond(ZoneOffset.UTC) * 1000),
      "mapLocation" -> employee.mapLocation.productIterator.toList.map(d => d.asInstanceOf[Double]),
      "status" -> employee.status.toString,
      "active" -> employee.active,
      "message" -> employee.message,
      "nickname" -> employee.nickname,
      "role" -> employee.role.toString,
      "visible" -> employee.visible
    )
  }

}