package com.regularoddity.status.shared

import java.net.URL
import java.time.LocalDate

object Role extends Enumeration {
  type Role = Value
  val employee, editor, admid = Value
}

import Role._

object Status extends Enumeration {
  type Status = Value
  val off, available, availableForUrgent, unavailable = Value
}

import Status._

case class Employee(
                firstName: String,
                lastName: String,
                displayName: String,
                jobTitle: String,
                photoUrl: URL,
                email: String,
                phone: String,
                division: String,
                hireDate: LocalDate,
                mapLocation: (Double, Double),
                status: Status,
                active: Boolean,
                message: String,
                nickname: String,
                role: Role,
                visible: Boolean,
            )
