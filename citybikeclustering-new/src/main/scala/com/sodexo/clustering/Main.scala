package com.sodexo.clustering

import com.beust.jcommander.JCommander
import com.sodexo.clustering.util.CommandLineArgs
import org.apache.spark.ml.clustering.KMeans
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.mllib.linalg.{Vector, Vectors}
import org.apache.spark.sql.types._
import org.slf4j.LoggerFactory


object Main extends App {
  lazy val LOGGER = LoggerFactory.getLogger(this.getClass.getName)
  val spark = SparkSession
    .builder()
    .master("local[1]")
    .getOrCreate()

  /**
    * Set nullable property of column.
    * @param df source DataFrame
    * @param cn is the column name to change
    * @param nullable is the flag to set, such that the column is  either nullable or not
    */
  def setNullableStateOfColumn( df: DataFrame, cn: String, nullable: Boolean) : DataFrame = {

    def lowerRemoveAllWhitespace(s: String): String = {
      s.toLowerCase().replaceAll("\\s", "")
    }
    // get schema
    val schema = df.schema
    // modify [[StructField] with name `cn`
    val newSchema = StructType(schema.map {
      case StructField( c, ArrayType(_,_), _, m) if c.equals(cn) => StructField( c, new ArrayType(DoubleType, nullable), nullable = nullable, m)
      case StructField( c, t, _, m) if c.equals(cn) => StructField( c, t, nullable = nullable, m)
      case y: StructField => y
    })
    // apply new schema
    df.sqlContext.createDataFrame( df.rdd, newSchema )
  }

  val cityBikeDataFrame = spark.read.option("multiline", "true").json("src/main/resources/Brisbane_CityBike.json")
    .withColumn("latitudeComputed", coalesce(col("coordinates.latitude"),col("latitude"),lit(0.0))*1000000 )
    .withColumn("longitudeComputed", coalesce(col("coordinates.longitude"),col("longitude"),lit(0.0))*1000000)
    .filter("longitudeComputed != 0 AND latitudeComputed != 0")
    .withColumn("feature",array("latitudeComputed","longitudeComputed"))
 // val cityBikeDataFrame2  = setNullableStateOfColumn(cityBikeDataFrame,"latitudeComputed",false)
  val cityBikeDataFrame2  = setNullableStateOfColumn(cityBikeDataFrame,"feature",false)
  cityBikeDataFrame2.printSchema()
  val numClusters = 5
  val numIterations = 20
  val kmeans = new KMeans().setK(numClusters).setSeed(1L).setFeaturesCol("feature")
  val model  = kmeans.fit(cityBikeDataFrame2)
  val predictions = model.transform(cityBikeDataFrame2).groupBy("prediction").count().show(6)




}
