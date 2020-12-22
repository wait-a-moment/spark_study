
import org.apache.spark.{SparkConf, SparkContext}

object WordCount {
  def main(args: Array[String]): Unit = {
    // 1. 创建 SparkConf对象, 并设置 App名字
    val conf=new SparkConf().setAppName("WordCount").setMaster("local[*]")
    // 2. 创建SparkContext对象
    val sc=new SparkContext(conf)

    // 3.读取文件创建RDD并统计
//    print(sc.textFile("input/wordcount.txt").flatMap(_.split(" ")))
    sc.textFile("input/").flatMap(_.split(" "))
      .map((_, 1))
      .reduceByKey(_ + _)
      .saveAsTextFile("output/")

  }

}
