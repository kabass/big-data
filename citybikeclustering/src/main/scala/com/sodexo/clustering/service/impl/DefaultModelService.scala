package com.sodexo.clustering.service.impl

import com.sodexo.clustering.service.ModelService
import org.apache.spark.ml.clustering.KMeans
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.apache.spark.sql.DataFrame

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
      .setHandleInvalid("skip")
    val kmeans = new KMeans().setK(numCluster).setSeed(1L).setFeaturesCol("features").setPredictionCol("cluster")
    val pipeline = new Pipeline().setStages(Array(vectorAssembler, kmeans))
    val model  = pipeline.fit(inputDataFrame.sample(percentageTraining/100))
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
}
