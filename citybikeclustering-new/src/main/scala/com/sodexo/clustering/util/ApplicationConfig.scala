package com.sodexo.clustering.util

import com.sodexo.clustering.model.FormatType
import com.sodexo.clustering.model.FormatType.FormatType
import com.typesafe.config.Config


class ApplicationConfig  {
   var inputPath: String = _
  var outputPath: String = _
  var inputFormat: FormatType = _
  var outputFormat: FormatType = _
  var percentageRetrain: Double = _
  var modelDirPath: String = _
  var numCluster: Int = _

   def this(config: Config) = {
     this()
     this.inputPath = config.getString("sodexo.clustering.inputPah")
     this.outputPath = config.getString("sodexo.clustering.outputPath")
     this.inputFormat = FormatType.withName(config.getString("sodexo.clustering.inputFormat").toUpperCase)
     this.outputFormat = FormatType.withName(config.getString("sodexo.clustering.outputFormat").toUpperCase)
     this.percentageRetrain = config.getDouble("sodexo.clustering.percentageRetrain")
     this.modelDirPath = config.getString("sodexo.clustering.modelDirPath")
     this.numCluster = config.getInt("sodexo.clustering.numCluster")

  }

  override def toString = s"ApplicationConfig(inputPath=$inputPath, outputPath=$outputPath, inputFormat=$inputFormat, outputFormat=$outputFormat, percentageRetrain=$percentageRetrain, modelDirPath=$modelDirPath)"
}


