import scala.sys.process._

name := "spark-streaming-kafka"

version := "0.1"

scalaVersion := "2.11.12"

libraryDependencies += "org.apache.spark" % "spark-core_2.11" % "2.3.0"

libraryDependencies += "org.apache.spark" % "spark-sql_2.11" % "2.3.0"

libraryDependencies += "org.apache.spark" % "spark-hive_2.11" % "2.3.0"

libraryDependencies += "org.apache.spark" % "spark-streaming_2.11" % "2.3.0"

libraryDependencies += "org.apache.spark" % "spark-streaming-kafka-0-10_2.11" % "2.3.2"

libraryDependencies += "org.apache.spark" % "spark-mllib_2.11" % "2.3.0"

libraryDependencies += "org.apache.spark" % "spark-graphx_2.11" % "2.3.0"

libraryDependencies += "graphframes" % "graphframes" % "0.5.0-spark2.1-s_2.11"


resolvers+= "hhh" at "https://dl.bintray.com/spark-packages/maven/"

lazy val moveFile = taskKey[Unit]("move file task")
moveFile := {
  println(s"move file task ${name.value}")
  s"cp target/scala-2.11/${name.value}_2.11-${version.value}.jar /home/bka/logiciel/docker/partage-data/"!
}

