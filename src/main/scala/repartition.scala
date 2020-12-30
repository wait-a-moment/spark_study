import org.apache.spark.{SparkConf, SparkContext}

object repartition {
  def main(args: Array[String]): Unit = {
    val config=new SparkConf().setMaster("local[*]").setAppName("Sample")
    val sc=new SparkContext(config)
    val parallelize=sc.parallelize(1 to 16,4)
    println(parallelize.partitions.size)

    val rerdd=parallelize.repartition(2)
    print(rerdd.partitions.size)

    val glom=rerdd.glom()
    glom.collect().foreach(array=>{
      println(array.mkString(",")) //mkString将原字符串使用特定的字符串seq分割。
    })
  }
}
