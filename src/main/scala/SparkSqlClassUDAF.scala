import org.apache.spark.SparkConf
import org.apache.spark.sql.expressions.Aggregator
import org.apache.spark.sql.{Encoder, Encoders, SparkSession}

object SparkSqlClassUDAF {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("UDAF")
    val session = SparkSession.builder.config(conf).getOrCreate()
    // 注意这里的session只是和上面的变量名一致
    import session.implicits._
    //自定义聚合函数
    //创建聚合函数对象
    val udaf = new MyAgeAvgClassFunc
    //将聚合函数转为查询列
    val avgCol = udaf.toColumn.name("avgAge")

    val dataframe = session.read.json("input/user.json")

    val userDs = dataframe.as[UserBean]

    userDs.select(avgCol).show()

    session.stop()
  }
}

case class UserBean(name: String, var age: Long)

//不加var声明的话，sum是常量 不支持+=操作
case class AvgBuffer( var sum: Long, var count: Long)

class MyAgeAvgClassFunc extends Aggregator[UserBean, AvgBuffer, Double] {
  //初始化buffer
  override def zero: AvgBuffer = AvgBuffer(0L, 0L)

  //处理一条新数据
  override def reduce(b: AvgBuffer, a: UserBean): AvgBuffer = {
    b.sum += + a.age
    b.count += 1L
    //需要返回一个AvgBuffer
    b
  }

  //缓冲区合并操作
  override def merge(b1: AvgBuffer, b2: AvgBuffer): AvgBuffer = {
    b1.sum += b2.sum
    b1.count += b2.count
    b1
  }

  //完成计算
  override def finish(reduction: AvgBuffer): Double = {
    reduction.sum.toDouble / reduction.count
  }

  //以下两条基本不变
  override def bufferEncoder: Encoder[AvgBuffer] = Encoders.product

  override def outputEncoder: Encoder[Double] = Encoders.scalaDouble
}