import sbt._
import Keys._

object Sirc extends Build {
  lazy val sirc = Project(id = "sirc", base = file(".")) aggregate(model, connection)
  lazy val model = Project(id = "sirc-model", base = file("model"))
  lazy val connection = Project(id = "sirc-connection", base = file("connection"))
}
