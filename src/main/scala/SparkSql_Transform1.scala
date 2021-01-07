import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object SparkSql_Transform1 {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("UDAF")
    val session = SparkSession.builder.config(conf).getOrCreate()
    // 注意这里的session只是和上面的变量名一致
    import session.implicits._
    //创建rdd
    val rdd=session.sparkContext.makeRDD(List((1,"zhangsan",20),(2,"lisi",10)))
    //转为df
    val df=rdd.map{
      case (id,name,age)=>
        User(id,name,age)
    }
    print(df)

    //转为ds
    val dataset = df.toDS()
    dataset.show()
    //dataset和rdd互相转换
    val rdd1 = dataset.rdd
    rdd1.foreach(println)
  }
}
case class User(id: Int, name: String, age: Int)