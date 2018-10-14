package com.regularoddity.status.shared

import java.net.URI
import java.time.LocalDate

object Role extends Enumeration {
  type Role = Value
  val employee, editor, admin = Value
}

import Role.Role

object Status extends Enumeration {
  type Status = Value
  val off, available, availableForUrgent, unavailable = Value
}

import Status.Status

case class Employee(
                   firstName: String,
                   lastName: String,
                   private val _displayName: Option[String],
                   jobTitle: String,
                   photoUrl: URI,
                   email: String,
                   phone: String,
                   division: String,
                   hireDate: LocalDate,
                   mapLocation: (Double, Double),
                   status: Status,
                   active: Boolean,
                   message: Option[String],
                   private val _nickname: Option[String],
                   role: Role,
                   visible: Boolean,
                   id: Option[String]
) {
  def displayName: String = _displayName getOrElse s"$firstName $lastName"

  def nickname: String = _nickname getOrElse firstName

  /**
    * Used for serialization.
    * @return whether the private member _displayName is defined.
    */
  def displayNameIsDefined = _displayName.isDefined

  /**
    * Used for serialization.
    * @return whether the private member _nickname is defined.
    */
  def nicknameIsDefined = _nickname.isDefined

  def canDrag = role >= Employee.MininumDraggableRole

  def mapLocationL = mapLocation._1 :: mapLocation._2 :: Nil

  def mapLocationA = Array(mapLocation._1, mapLocation._2)
}

object Employee {
  val MininumDraggableRole = Role.editor
}