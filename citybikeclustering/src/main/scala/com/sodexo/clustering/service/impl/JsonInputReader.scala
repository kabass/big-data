package com.sodexo.clustering.service.impl

import com.sodexo.clustering.service.InputReader
import com.sodexo.clustering.service.InputReader
import org.apache.spark.sql.functions.{coalesce, col, lit}
import org.apache.spark.sql.{DataFrame, SparkSession}

class JsonInputReader (sparkSession : SparkSession) extends InputReader {
  /**
    * This method read a source basing on the path parameter
    *
    * @param path the path ( can be a directory, fileName or table name )
    * @return a dataFrame
    */
  override def read(path: String): DataFrame = {
    sparkSession.read.option("multiline", "true").json(path)
      .withColumn("latitudeComputed", coalesce(col("coordinates.latitude"),col("latitude"),lit(0.0)).cast("double") )
      .withColumn("longitudeComputed", coalesce(col("coordinates.longitude"),col("longitude"),lit(0.0)).cast("double"))

  }
}
