import org.apache.spark.{SparkConf, SparkContext}

//两个rdd的计算操作
object compute {
  def main(args: Array[String]): Unit = {
    val config=new SparkConf().setMaster("local[*]").setAppName("compute")
    val sc=new SparkContext(config)
//    union求并集
    val rdd1=sc.parallelize(1 to  5)
    rdd1.collect().foreach(println)
    val rdd2=sc.parallelize(5 to 10)
    val rdd3=rdd1.union(rdd2)
    rdd3.collect().foreach(println)
//    subtract求差集
    val rdd4=sc.parallelize(3 to 8)
    val rdd5=sc.parallelize(1 to 5)
    val rdd6=rdd4.subtract(rdd5)
    rdd6.collect().foreach(println)
//    intersection计算两个rdd的交集
    val rdd7 = sc.parallelize(1 to 7)
    val rdd8 = sc.parallelize(5 to 10)
    val rdd9 = rdd7.intersection(rdd8)
    rdd9.collect().foreach(println)
//    计算两个RDD的笛卡尔积并打印
    val rdd10 = sc.parallelize(1 to 3)
    val rdd11 = sc.parallelize(2 to 5)
//    val rdd12 = rdd10.cartesian(rdd11)
//    rdd12.collect().foreach(println)
    //zip将两个RDD组合成Key/Value形式的RDD,这里默认两个RDD的partition数量以及元素数量都相同，否则会抛出异常。
    val rdd12 = sc.parallelize(Array(1,2,3),3)
    val rdd13 = sc.parallelize(Array("a","b","c"),3)
    val collectrdd12: Array[(Int, String)] = rdd12.zip(rdd13).collect()
    val collectrdd13: Array[(String, Int)] = rdd13.zip(rdd12).collect()
    collectrdd12.foreach(println)
    println("**********")
    collectrdd13.foreach(println)
  }
}
