package com.sodexo.clustering.service

import org.apache.spark.ml.PipelineModel
import org.apache.spark.sql.DataFrame

trait ModelService {

  /**
    * To fit the model for generating and saving new model
    *
    * @param trainingDataFrame the training dataframe
    * @param percentageTraining the percentage of data to train
    *
    * @return the model
    */
  def fit(trainingDataFrame: DataFrame, percentageTraining : Double): PipelineModel

  /**
    *  To cluster a new input basing on the model
    * @param inputDataFrame the input dataframe
    * @param model the model
    *
    */
  def cluster(inputDataFrame : DataFrame, model: PipelineModel) : DataFrame
}
