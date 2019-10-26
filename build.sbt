name := "streaming-ucu-week3"

version := "0.1"

scalaVersion := "2.12.7"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"
libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.13.+" % "test"
libraryDependencies += "com.storm-enroute" %% "scalameter-core" % "0.10.1"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}
