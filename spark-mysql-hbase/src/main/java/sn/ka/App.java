package sn.ka;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;

import it.nerdammer.spark.hbase.HBaseSaltedRDD;
import jersey.repackaged.com.google.common.collect.Lists;
import scala.Tuple3;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws InterruptedException {
		System.out.println("Hello World!");

		// SparkSession spark = SparkSession.builder().appName("Java Spark SQL data
		// sources example")
		// // .config("spark.submit.deployMode", "cluster")
		// .config("spark.eventLog.enabled", "true")
		// .config("spark.eventLog.dir", "hdfs://Hadoop:9000/shared/spark-logs")
		// .config("spark.master", "yarn-client")
		// .config("spark.yarn.jars",
		// "/home/bka/Documents/project/big-data/spark-mysql-hbase/target/spark-mysql-hbase-0.0.1-SNAPSHOT.jar")
		// // .master("yarn")
		// .getOrCreate();

		SparkSession spark = SparkSession.builder().master("local").config("spark.hbase.host", "localhost")
				.appName("JavaSparkPi").getOrCreate();

		JavaSparkContext jsc = new JavaSparkContext(spark.sparkContext());

		Configuration conf = HBaseConfiguration.create();
		String tableName = "emp";

		System.setProperty("user.name", "hdfs");
		System.setProperty("HADOOP_USER_NAME", "hdfs");
		conf.set("hbase.master", "localhost:60000");
		conf.setInt("timeout", 120000);
		conf.set("hbase.zookeeper.quorum", "localhost");
		conf.set("zookeeper.znode.parent", "/hbase-unsecure");
		;
		conf.set(TableInputFormat.INPUT_TABLE, tableName);

		HBaseAdmin admin = new HBaseAdmin(conf);
		if (!admin.isTableAvailable(tableName)) {
			HTableDescriptor tableDesc = new HTableDescriptor(tableName);
			admin.createTable(tableDesc);
		}
           
		JavaPairRDD hBaseRDD = jsc.newAPIHadoopRDD(conf, TableInputFormat.class, ImmutableBytesWritable.class, Object.class);
		println("Number of Records found : " + hBaseRDD.count());
		jsc.stop();

		// int slices = (args.length == 1) ? Integer.parseInt(args[0]) : 2;
		// int n = 100000 * slices;
		// List<Integer> l = new ArrayList<>(n);
		// for (int i = 0; i < n; i++) {
		// l.add(i);
		// }
		//
		// JavaRDD<Integer> dataSet = jsc.parallelize(l, slices);
		//
		// int count = (Integer) dataSet.map(integer -> {
		// double x = Math.random() * 2 - 1;
		// double y = Math.random() * 2 - 1;
		// return (x * x + y * y <= 1) ? 1 : 0;
		// }).reduce((integer, integer2) -> integer + integer2);
		//
		// System.out.println("Pi is roughly " + 4.0 * count / n);
		// spark.stop();
		// spark.close();
	}
}
