import org.apache.spark._
import org.apache.spark.streaming._
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe


object SparkStreamingConsumer extends App{

  val  conf = new SparkConf()
    .setMaster("local[3]")
    .set("spark.eventLog.dir","/home/bka/logiciel/hadoop-spark/log/")
    .setAppName("spark streaming")
    .set("spark.driver.allowMultipleContexts" , "true")


  val sparkStreaming =  new StreamingContext(conf, Seconds(10))

  val kafkaParams = Map[String, Object](
    "bootstrap.servers" -> "localhost:9093,localhost:9094,localhost:9092",
    "key.deserializer" -> classOf[StringDeserializer],
    "value.deserializer" -> classOf[StringDeserializer],
    "group.id" -> "test-group",
    "auto.offset.reset" -> "earliest",
    "enable.auto.commit" -> (false: java.lang.Boolean)
  )
  val l:java.lang.Long = new java.lang.Long(21);

  val topics= Array("test", "test-partition-3")
  val stream = KafkaUtils.createDirectStream[String, String](
    sparkStreaming,
    PreferConsistent,
    Subscribe[String, String](topics, kafkaParams)
  )


  stream.map(record => (record.key, record.value)).print(5)

  sparkStreaming.start()

  sparkStreaming.awaitTermination()
}
