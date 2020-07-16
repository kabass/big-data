import java.text.SimpleDateFormat
import java.util.concurrent.{Future, TimeUnit}
import java.util.{Date, Properties}

import scala.io.Source
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, RecordMetadata}



object SparkStreamingProducer extends App {
  val brokers = "localhost:9092,localhost:9093,localhost:9094";
  val topic = "test-partition-4";
  val props = new Properties
  props.put("bootstrap.servers", brokers)
  props.put("client.id", "Producer")
  props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  val producer = new KafkaProducer[String, String](props)
  val filename = "src/main/resources/IP_LOG.log"
  for (line <- Source.fromFile(filename).getLines) {
    val ipData = new ProducerRecord[String, String]("test-partition-3", line)
    println(line)
    val recordMetadata = producer.send(ipData)
  }
}
