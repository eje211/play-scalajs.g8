package com.regularoddity.status

import com.regularoddity.status.shared.Employee
import com.regularoddity.status.SerializableEmployee.toJS
import monix.execution.Scheduler.Implicits.global
import fr.hmil.roshttp.HttpRequest
import io.circe.Json
import io.circe.parser.parse
import org.scalajs.dom

import scala.collection.mutable
import scala.concurrent.Future
import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import scala.util.{Failure, Success}


@JSExportTopLevel("DataHolder")
object DataHolder {
  import SerializableEmployee._

  @JSExport
  val employees = mutable.Map.empty[String, Employee]

  @JSExport
  val employeesJs = js.Dictionary[Any]()

  private val request = HttpRequest(s"//${dom.document.location.host}/data")

  def requestToEmployees(request: HttpRequest): Future[List[Employee]] =
    request.send().map(response => {
      parse(response.body).map(json =>
        json.as[List[Json]]
          .map(_.map(_.as[Employee])
            .flatMap(_.toOption)).toOption.getOrElse(Nil)
      ).getOrElse(Nil)
    })

  def getEmployeeData(): Unit = {
    requestToEmployees(request)
      .onComplete({
        case employeesResult: Success[List[Employee]] =>
          employees.empty
          employeesResult.value.filter(_.id.isDefined) foreach { employee => {
            employees(employee.id.get) = employee
            Map addPin employee
          }}
          employeesJs.empty
          try {
            employees.filter(employee => employee._2.id.isDefined).map(employee => toJS(employee._2)).map(e =>
              e.get("id").map(_.asInstanceOf[String]).foreach(key => employeesJs.update(key, e)))
          } catch {
            case e: Exception => dom.console.error(e.getMessage)
          }
        case _: Failure[List[Employee]] => "Something went wrong."
        case _ => println("Something else")
      })
  }

}
