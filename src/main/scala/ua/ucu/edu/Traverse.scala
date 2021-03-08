package ua.ucu.edu

import scala.io.Source

case class Employee(
  id: Int,
  firstName: String,
  lastName: String,
  email: String,
  jobId: String,
  salary: Int,
  managerId: Int,
  departmentId: Int)

case class Department(
  departmentId: Int,
  departmentName: String,
  managerId: Int,
  locationId: Int)

object Traverse extends App {

  for {
    line <- Source.fromResource("employees.csv").getLines().toList
    fields = line.split(",")
  } println(fields(0) + "--" + fields(1) + "--" + fields(2) + "--" + fields(10))

  def parseEmployees: List[Employee] = ???

  def parseDepartments: List[Employee] = ???
}
