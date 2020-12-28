import org.apache.spark.{SparkConf, SparkContext}

object SortByKey {
  def main(args: Array[String]): Unit = {
    val config=new SparkConf().setMaster("local[*]").setAppName("sortbykey")
    val sc=new SparkContext(config)
//    val rdd=sc.parallelize(Array((3,"a"),(6,"c"),(2,"b"),(1,"d")))
//    根据key排序
//    rdd.sortByKey(false).collect().foreach(println)
//    mapValue算子针对于(K,V)形式的类型只对V进行操作,不改变key 只改变value
//    rdd.mapValues(_+"|||").collect().foreach(println)
//    join算子，在类型为(K,V)和(K,W)的RDD上调用，返回一个相同key对应的所有元素对在一起的(K,(V,W))的RDD,只有两个rdd都存在这个key才会返回
    val rdd = sc.parallelize(Array((1,"a"),(2,"b"),(3,"c"),(4,"d")))
    val rdd1 = sc.parallelize(Array((1,4),(2,5),(3,6),(5,8)))
//    rdd.join(rdd1).collect().foreach(println)

    rdd.cogroup(rdd1).collect().foreach(println)
//    join和congroup的区别
    /*
    congroup会返回所有 join只会返回两个rdd都存在的key
    (1,(CompactBuffer(a),CompactBuffer(4)))
    (2,(CompactBuffer(b),CompactBuffer(5)))
    (3,(CompactBuffer(c),CompactBuffer(6)))
    (4,(CompactBuffer(d),CompactBuffer()))
    (5,(CompactBuffer(),CompactBuffer(8)))*/

  }
}
