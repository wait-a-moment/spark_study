import org.apache.spark.{SparkConf, SparkContext}

import scala.util.parsing.json.JSON
object ReadJson {
  def main(args: Array[String]): Unit = {
    val config=new SparkConf().setMaster("local[*]").setAppName("Sample")
    val sc=new SparkContext(config)
    val file=sc.textFile("input/user.json")
    val result = file.map(JSON.parseFull)
//    val str = "{\"host\":\"td_test\",\"ts\":1486979192345,\"device\":{\"tid\":\"a123456\",\"os\":\"android\",\"sdk\":\"1.0.3\"},\"time\":1501469230058}"
//    val result=JSON.parseFull(str)
    result.foreach(println)
  }
}
