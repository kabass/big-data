package sn.ka.spark_mysql_hbase_scala.examples

import com.iobeam.spark.streams.annotation.SparkRun
import com.iobeam.spark.streams.model.OutputStreams
import com.iobeam.spark.streams.transforms.{DeviceTimeoutTrigger, Ewma, ThresholdTimeoutTrigger, ThresholdTrigger}
import com.iobeam.spark.streams.{AppContext, DeviceOps, DeviceOpsConfig, SparkApp}
import org.apache.spark.streaming.{Minutes, Seconds}

/**
  * Builds a Device Ops app using iobeam library.
  */
@SparkRun("deviceOps")
object DeviceOpsApp extends SparkApp {

    override def main(appContext: AppContext):
    OutputStreams = {
        // Get raw input data
        val stream = appContext.getData("input")

        // Build device ops config
        val config = new DeviceOpsConfig()
            // smooth the input series cpu with a EWMA filter with alpha 0.2
            .addFieldTransform("cpu", "cpu_smoothed", new Ewma(0.2))
            // Add a threshold trigger to the series
            // where device_ops is the namespace and cpu_smoothed is the field to output.
            // Write the trigger events to the same namespace in the field cpu_triggers
            .addFieldTransform("cpu_smoothed","cpu_triggers", new ThresholdTrigger(90.0,
            "cpu_over_90", 70,
            "cpu_below_70"))
            // Add a threshold timeout trigger
            .addFieldTransform("cpu", "cpu_triggers",
            new ThresholdTimeoutTrigger(85.0, 70.0, Seconds(5), "cpu_over_85_for_5_s",
                "cpu_restored"))
            // Add check of when devices go offline
            .addNamespaceTransform("timeouts", new DeviceTimeoutTrigger
        (Minutes(1), "device_offline"))

        val outStream = DeviceOps.getDeviceOpsOutput(stream, config)
        OutputStreams(outStream)
    }
}