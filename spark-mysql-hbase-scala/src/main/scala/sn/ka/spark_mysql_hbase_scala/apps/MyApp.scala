package sn.ka.spark_mysql_hbase_scala

import com.iobeam.spark.streams.model.OutputStreams
import com.iobeam.spark.streams.{AppContext, SparkApp}
import com.iobeam.spark.streams.annotation.SparkRun


/**
  * Template for custom analysis code.
  */
@SparkRun("spark-mysql-hbase-scala")
object MyApp extends SparkApp {
    /**
      * Setup for your spark app
      * @param appContext iobeam interface
      * @return output series and triggers
      */

    override def main(appContext: AppContext): OutputStreams = {
        // This is where you read the input data.
        val stream = appContext.getData("input")
        //
        // This is where you put your custom analysis.
        //

        // Change this line to fit your project. (Example below just drops all data.)
        val output = stream.filter(a => false)
        OutputStreams(("output","device_id", output))
    }
}
