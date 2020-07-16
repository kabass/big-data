package com.sodexo.clustering.service.impl

import com.sodexo.clustering.service.ModelService
import org.apache.spark.ml.clustering.KMeans
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.array
import org.apache.spark.sql.types.{ArrayType, DoubleType, StructField, StructType}

class DefaultModelService(modelDirectoryPath : String, numCluster:Int) extends ModelService {

  val MODEL_FILE_NAME : String = "clustering"
  val NUM_ITERATIONS = 20
  val modelFileAbsolutePath  =  s"$modelDirectoryPath/$MODEL_FILE_NAME"
  /**
    * To fit the model for generating and saving new model
    *
    * @param inputDataFrame the training dataframe
    * @param percentageTraining the percentage of data to train
    *
    * @return the model
    */
  override def fit(inputDataFrame: DataFrame, percentageTraining : Double): PipelineModel = {
    if(percentageTraining <= 0 ){
      return PipelineModel.load(modelFileAbsolutePath)
    }
    val vectorAssembler = new VectorAssembler()
      .setInputCols(Array("latitudeComputed", "longitudeComputed"))
      .setOutputCol("features")
    val kmeans = new KMeans().setK(numCluster).setSeed(1L).setFeaturesCol("features").setPredictionCol("cluster")
    val pipeline = new Pipeline().setStages(Array(vectorAssembler, kmeans))
    val model  = pipeline.fit(inputDataFrame
       .setNullableStateOfColumn("latitudeComputed", true)
       .setNullableStateOfColumn("longitudeComputed", true)
        .where("latitudeComputed is not null and longitudeComputed is not null")
       .sample(percentageTraining/100))
    model.write.overwrite().save(modelFileAbsolutePath)
    return model
  }

  /**
    * To cluster a new input basing on the model
    *
    * @param inputDataFrame the input dataframe
    * @return the dataframe
    */
  override def cluster(inputDataFrame: DataFrame, model: PipelineModel ): DataFrame = {
      model.transform(inputDataFrame)
  }



  implicit class EnrichedDataFrame(dataFrame: DataFrame ) {
    /**
      * Set nullable property of column.
      *
      * @param cn       is the column name to change
      * @param nullable is the flag to set, such that the column is  either nullable or not
      */
    def setNullableStateOfColumn( cn: String, nullable: Boolean): DataFrame = {

      def lowerRemoveAllWhitespace(s: String): String = {
        s.toLowerCase().replaceAll("\\s", "")
      }

      // get schema
      val schema = dataFrame.schema
      // modify [[StructField] with name `cn`
      val newSchema = StructType(schema.map {
        case StructField(c, ArrayType(_, _), _, m) if c.equals(cn) => StructField(c, new ArrayType(DoubleType, nullable), nullable = nullable, m)
        case StructField(c, t, _, m) if c.equals(cn) => StructField(c, t, nullable = nullable, m)
        case y: StructField => y
      })
      // apply new schema
      dataFrame.sqlContext.createDataFrame(dataFrame.rdd, newSchema)
    }
  }
}
