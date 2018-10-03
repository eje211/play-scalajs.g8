package com.regularoddity.status

import com.regularoddity.status.shared.Employee
import monix.execution.Scheduler.Implicits.global
import fr.hmil.roshttp.HttpRequest
import io.circe.Json
import io.circe.parser.parse
import org.scalajs.dom

import scala.concurrent.Future
import scala.util.{Failure, Success}

object DataHolder {
  import SerializableEmployee._

  private var _employees = Vector.empty[Employee]

  def employees = _employees

  def employees_=(employees: Vector[Employee]) = _employees = employees

  def employees_=(employees: List[Employee]) = _employees = employees.toVector

  private val request = HttpRequest(s"//${dom.document.location.host}/data")

  def requestToEmployees(request: HttpRequest): Future[List[Employee]] =
    request.send().map(response => {
      parse(response.body).map(json =>
        json.as[List[Json]]
          .map(_.map(_.as[Employee])
            .flatMap(_.toOption)).toOption.getOrElse(Nil)
      ).getOrElse(Nil)
    })

  def getEmployeeData() = {
    requestToEmployees(request)
      .onComplete({
        case employeesResult: Success[List[Employee]] =>
          employees = employeesResult.value
        case _: Failure[List[Employee]] => "Something went wrong."
        case _ => println("Something else")
      })
  }

}
