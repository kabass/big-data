package sn.ka.spark_sql_examples

import org.apache.spark.sql.SparkSession

/**
 * @author ${user.name}
 */
object JonExamples {

  def foo(x: Array[String]) = x.foldLeft("")((a, b) => a + b)

  def main(args: Array[String]) {
    println("Hello World!")
    println("concat arguments = " + foo(args))
    val sparkSession = SparkSession
      .builder()
      .appName("app json")
      .master("local")
      .getOrCreate()

    // For implicit conversions like converting RDDs to DataFrames
    import sparkSession.implicits._

    val df = sparkSession.read.json("src/main/resources/people.json")

    // Select only the "name" column
    df.select("name").show()

    // Select everybody, but increment the age by 1
    df.select($"name", $"age" + 1).show()

    // Displays the content of the DataFrame to stdout
    df.show()

    // Print the schema in a tree format
    df.printSchema()
    // Select everybody, but increment the age by 1
    df.select($"name", $"age" + 1).show()
    // +-------+---------+
    // |   name|(age + 1)|
    // +-------+---------+
    // |Michael|     null|
    // |   Andy|       31|
    // | Justin|       20|
    // +-------+---------+

    // Select people older than 21
    df.filter($"age" > 21).show()
    // +---+----+
    // |age|name|
    // +---+----+
    // | 30|Andy|
    // +---+----+

    // Count people by age
    df.groupBy("age").count().show()
    // +----+-----+
    // | age|count|
    // +----+-----+
    // |  19|    1|
    // |null|    1|
    // |  30|    1|
    // +----+-----+  

    val sqlDF = sparkSession.sql("SELECT * FROM people")
    sqlDF.show()
  }

}
