package com.sodexo.clustering.service.impl

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import com.sodexo.clustering.service.OutputWriter
import com.sodexo.clustering.service.OutputWriter
import org.apache.hadoop.fs.{FileSystem, FileUtil, Path}
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import org.slf4j.LoggerFactory

class JsonOutputWriter (sparkSession : SparkSession) extends OutputWriter {
  private val DATE: LocalDateTime = LocalDateTime.now()
  val DATE_STRING: String = DATE.format(DateTimeFormatter.ofPattern("YYYYMMddHHmmss"))

  val FILESYSTEM =
    FileSystem.get(sparkSession.sparkContext.hadoopConfiguration)
  private lazy val LOGGER = LoggerFactory.getLogger(this.getClass.getName)
  /**
    * This method write datarame to a path
    *
    * @param dataFrame th dataFrame
    * @param filePath      the path ( can be a directory, fileName or table name )
    */
  override def write(dataFrame: DataFrame, filePath: String): Unit ={
    val pathWithDate = s"${filePath}_${DATE_STRING}"
    val tempPath = s"${pathWithDate}_temp"
    dataFrame
      .coalesce(1)
      .write
      .mode(SaveMode.Overwrite)
      .option("multiline", "true")
      .json(tempPath)
    FILESYSTEM.deleteOnExit(new Path(pathWithDate))
    FileUtil.copyMerge(FILESYSTEM,
      new Path(tempPath),
      FILESYSTEM,
      new Path(pathWithDate),
      false,
      sparkSession.sparkContext.hadoopConfiguration,
      null)
    FILESYSTEM.delete(new Path(tempPath), true)
    LOGGER.info(s"$pathWithDate saved")



  }
}
