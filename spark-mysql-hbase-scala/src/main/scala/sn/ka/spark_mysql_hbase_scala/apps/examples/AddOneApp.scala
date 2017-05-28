package sn.ka.spark_mysql_hbase_scala.examples

import com.iobeam.spark.streams.annotation.SparkRun
import com.iobeam.spark.streams.model.{OutputStreams, TimeRecord, TriggerEvent}
import com.iobeam.spark.streams.{AppContext, SparkApp}

/**
  * Simple app used to demonstrate unit testing.
  */
@SparkRun("addOne")
object AddOneApp extends SparkApp {
    /**
      * Simple example of processing function. Adds 1 to the field "value" and writes it to
      * the value-new series.
      */
    def add1(timeRecord: TimeRecord): TimeRecord = {

        val newValue = timeRecord.requireDouble("value") + 1

        // Create output series, make sure it uses a new series name
        new TimeRecord(timeRecord.time, Map("value-new" -> newValue))
    }

    /**
      * Simple trigger function. Returning empty Seq means no triggers. If more
      * than one field cause triggers, the Seq can contain multiple triggers.
      *
      * @param deviceAndRecord record to check
      * @return Seq of trigger events
      */
    def checkTrigger(deviceAndRecord: (String, TimeRecord)): Seq[TriggerEvent] = {
        val (deviceId, timeRecord) = deviceAndRecord
        val myThreshold = 5.0
        val value = timeRecord.requireDouble("value")

        if (value > myThreshold) {
            return Seq(new TriggerEvent("myEventName",
                new TimeRecord(timeRecord.time, Map("triggeredValue" -> value, "deviceId" ->
                    deviceId))))
        }

        // Not a trigger in this record
        Seq()
    }

    /**
      * Sets up stream processing for project.
      *
      * @param appContext interface to iobeam backend
      * @return Set of outputStreams
      */

    override def main(appContext: AppContext):
    OutputStreams = {
        val stream = appContext.getData("input")
        val outStream = stream.map(add1)
        OutputStreams(("output_namespace", "device_id", outStream))
    }
}
