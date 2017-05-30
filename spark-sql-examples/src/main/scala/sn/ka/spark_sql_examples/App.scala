package sn.ka.spark_sql_examples

import org.apache.spark.sql.SparkSession

/**
 * @author ${user.name}
 */
object App {
  
  def foo(x : Array[String]) = x.foldLeft("")((a,b) => a + b)
  
  def main(args : Array[String]) {
    println( "Hello World!" )
    println("concat arguments = " + foo(args))
    
    val sparkSession = SparkSession
    .builder()
    .appName("app")
    .master("local")
    .getOrCreate();
    
    // For implicit conversions like converting RDDs to DataFrames
    import sparkSession.implicits._
  }

}
