import org.apache.spark.{SparkConf, SparkContext}

//统计每个省份广告被点击次数的top3
object Top3Ad {
  def main(args: Array[String]): Unit = {
    // 1. 创建 SparkConf对象, 并设置 App名字
    val conf=new SparkConf().setAppName("WordCount").setMaster("local[*]")
    // 2. 创建SparkContext对象
    val sc=new SparkContext(conf)
    val line=sc.textFile("input/agent.log")
    //4.计算每个省中每个广告被点击的总数：((Province,AD),sum)
    val provinceAdToOne=line.map{x=>
      val key=x.split(" ")
      ((key(1),key(4)),1)
    }
    // 转化为（（省份，广告），次数）
    val provinceAdToSum= provinceAdToOne.reduceByKey(_+_)
    // 将省份作为key,(广告，次数)作为value 再进行聚合(Province,List((AD1,sum1),(AD2,sum2)...))
    val provinceGroup= provinceAdToSum.map(x=>
      (x._1._1,(x._1._2,x._2))
    ).groupByKey()
    //对同一个省份所有广告的集合进行排序并取前3条，排序规则为广告点击总数
    val provinceAdTop3 = provinceGroup.mapValues { x =>
      x.toList.sortWith((x, y) => x._2 > y._2).take(3)
    }
    //8.将数据拉取到Driver端并打印
    provinceAdTop3.collect().foreach(println)
  }
}
