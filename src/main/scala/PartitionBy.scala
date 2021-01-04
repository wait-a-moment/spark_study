import org.apache.spark.{Partitioner, SparkConf, SparkContext}

object PartitionBy {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("Coalesce").set("spark.testing.memory", "2147480000")
    val sc = new SparkContext(conf)
    //val listRDD: RDD[Int] = sc.makeRDD(List(1,2,3,4,5,3,21,1),4)
    val listRDD = sc.makeRDD(List(("a", 1), ("b", 2), ("c", 3), ("a", 4), ("b", 5), ("c", 6), ("d", 7)))
    val partitionBy = listRDD.partitionBy(new Mypari(3))
    partitionBy.saveAsTextFile("output")
  }

  //声明分区器
  class Mypari(partitions: Int) extends Partitioner {
    override def numPartitions: Int = {
      partitions
    }

    override def getPartition(key: Any): Int = {
      //这里可以根据key做判断放到不同的partition里面，这里写1表示不管是什么key，都放到1号partition里面
      //1
      if (key == "a") {
        1
      } else if (key == "b") {
        2
      } else {
        0
      }
    }
  }

}
