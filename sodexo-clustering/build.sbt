name := "sodexo-clustering"

version := "0.1"

scalaVersion := "2.12.8"


libraryDependencies += "org.apache.spark" %% "spark-core" % "2.4.0"

libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.0"

libraryDependencies += "org.apache.spark" %% "spark-mllib" % "2.4.0"

libraryDependencies += "com.beust" % "jcommander" % "1.72"

libraryDependencies += "com.typesafe" % "config" % "1.2.1"

resolvers+= "jcommander repo" at "https://mvnrepository.com/artifact/com.beust/jcommander"

assemblyShadeRules in assembly := Seq(
  ShadeRule.keep("*").inAll
)
/*
lazy val moveFile = taskKey[Unit]("move file task")
moveFile := {
  println(s"move file task ${name.value}")
  s"cp target/scala-2.12/${name.value}_2.11-${version.value}.jar /home/bka/logiciel/docker/partage-data/"!
}*/

