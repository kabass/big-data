package sn.ka.spark_mysql_hbase_scala.examples

import com.iobeam.spark.streams.annotation.SparkRun
import com.iobeam.spark.streams.model.{OutputStreams, TimeRecord}
import com.iobeam.spark.streams.{AppContext, SparkApp}

/**
  * Converts Celsius to Fahrenheit, writes new stream
  * and generates trigger if temperature too high.
  */
@SparkRun("convertCelsius")
object TemperatureConversionApp extends SparkApp {

    /**
      * Simple example of a processing function.
      * Converts the temperature from Celsius
      * to Fahrenheit and writes the value to a new series.
      */
    def convertCelsiusToFahrenheit(timeRecord: TimeRecord): TimeRecord = {
        val temp_C = timeRecord.requireDouble("temp_C")
        val temp_F = (9.0 / 5 * temp_C) + 32

        // Create output series, make sure it uses a new series name
        new TimeRecord(timeRecord.time, Map("temp_F" -> temp_F))
    }
    override def main(appContext: AppContext):
    OutputStreams = {
        // Get raw input data
        val stream = appContext.getData("input")

        // Transform data
        val outStream = stream.map(convertCelsiusToFahrenheit)

        // Output streams
        OutputStreams(("temp_F", "device_id", outStream))
    }
}
