package com.sodexo.clustering

import com.beust.jcommander.JCommander
import com.sodexo.clustering.model.FormatType._
import com.sodexo.clustering.service.{ OutputWriter}
import com.sodexo.clustering.service.impl.{DefaultModelService, JsonInputReader, JsonOutputWriter}
import com.sodexo.clustering.service.InputReader
import com.sodexo.clustering.util.{ApplicationConfig, CommandLineArgs}
import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.{ SparkSession}
import org.slf4j.LoggerFactory
object Application extends App {

  private lazy val LOGGER = LoggerFactory.getLogger(this.getClass.getName)
  val config = getConfig(args)
  val sparkSession = SparkSession
    .builder()
    //.master("local[1]")
    .getOrCreate()

  val inputReader : InputReader = config.inputFormat match {
    case JSON => new JsonInputReader(sparkSession)
    case _ => {
             LOGGER.error(s"${config.inputFormat} is unknown input format")
             throw new RuntimeException
          }
  }
  val outputWriter : OutputWriter = config.outputFormat match {
    case JSON => new JsonOutputWriter(sparkSession)
    case _ => {
      LOGGER.error(s"${config.outputFormat} is unknown output format")
      throw new RuntimeException
    }
  }
  val inputDataFrame  = inputReader.read(config.inputPath)
  inputDataFrame.printSchema()
  val modelService = new DefaultModelService(config.modelDirPath, config.numCluster)
  val model = modelService.fit(inputDataFrame,config.percentageRetrain)
  val resultDataframe = modelService.cluster(inputDataFrame, model)
  outputWriter.write(resultDataframe,config.outputPath)



  def getConfig(args: Array[String]) : ApplicationConfig = {

    val jCommander = new JCommander(CommandLineArgs, null, args.toArray: _*)
    if (CommandLineArgs.help  ) {
      jCommander.usage()
      System.exit(0)
    }
    val config = new ApplicationConfig(ConfigFactory.load(s"application-${CommandLineArgs.environment}.properties"))
    LOGGER.info(s"config loaded : $config")

    return config

  }
}
