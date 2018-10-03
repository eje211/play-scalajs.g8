package com.regularoddity.status.shared

import java.net.URL
import java.time.LocalDate

object Role extends Enumeration {
  type Role = Value
  val employee, editor, admid = Value
}

import Role.Role

object Status extends Enumeration {
  type Status = Value
  val off, available, availableForUrgent, unavailable = Value
}

import Status.Status

abstract class Employee(
    firstName: String,
    lastName: String,
    displayName: Option[String],
    jobTitle: String,
    photoUrl: URL,
    email: String,
    phone: String,
    division: String,
    hireDate: LocalDate,
    mapLocation: (Double, Double),
    status: Status,
    active: Boolean,
    message: Option[String],
    nickname: String,
    role: Role,
    visible: Boolean,
)
