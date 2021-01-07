import org.apache.spark
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object SparkSql_Transform {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("transform")
    val session = SparkSession.builder.config(conf).getOrCreate()
    // 注意这里的session只是和上面的变量名一致
    import session.implicits._
    //创建rdd，并转为df，再转化为ds，再转化为df，再转为rdd
    val rdd = session.sparkContext.makeRDD(List((1, "zhangsan", 20), (2, "lisi", 10)))

    //rdd转为df
    val df = rdd.toDF("id", "name", "age")
    df.show()

    //rdd转为ds
    val ds = df.as[User]
    ds.show()

    //ds转df
    val df1=ds.toDF()
    df1.show()

    //df转rdd
    val rdd1=df1.rdd
    rdd1.foreach(row=>println(row.getString(1)))
  }
}

case class User(id: Int, name: String, age: Int)