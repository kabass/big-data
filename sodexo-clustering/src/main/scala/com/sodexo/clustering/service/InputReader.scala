package com.sodexo.clustering.service

import org.apache.spark.sql.DataFrame

trait InputReader{
  /**
    * This method read a source basing on the path parameter
    * @param path the path ( can be a directory, fileName or table name )
    * @return a dataFrame
    */
  def read ( path : String) : DataFrame
}
