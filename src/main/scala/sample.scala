import org.apache.spark.{SparkConf, SparkContext}

object sample {
  def main(args: Array[String]): Unit = {
    val config=new SparkConf().setMaster("local[*]").setAppName("Sample")
    val sc=new SparkContext(config)
    val rdd = sc.makeRDD(Array("hello1","hello2","hello3","hello4","hello5","hello6"))
    //false表示元素不可以多次抽样
    val sampleRdd=rdd.sample(false,0.7)
    sampleRdd.foreach(println)

  }
}
