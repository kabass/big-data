package com.sodexo.clustering.service

import org.apache.spark.sql.DataFrame

trait OutputWriter{
  /**
    * This method write datarame to a path
    * @param dataFrame th dataFrame
    * @param path the path ( can be a directory, fileName or table name )
    */
   def write ( dataFrame : DataFrame,  path : String)
}
