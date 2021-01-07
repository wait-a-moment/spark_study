import org.apache.spark.SparkConf
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, DoubleType, LongType, StructType}

object SparkSqlUDAF {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[*]").setAppName("UDAF")
    val session = SparkSession.builder.config(conf).getOrCreate()
    // 注意这里的session只是和上面的变量名一致
    import session.implicits._
    //自定义聚合函数
    //创建聚合函数对象
    val udaf=new MyAvgFunt
    //注册聚合函数
    session.udf.register("avgage",udaf)
    val dataframe=session.read.json("input/user.json")
    dataframe.createOrReplaceTempView("user")
    session.sql("select avgage(age) from user").show()
  }
}

//自定义聚合函数
//1.继承
class MyAvgFunt extends UserDefinedAggregateFunction {
  //聚合函数的输入数据结构
  override def inputSchema: StructType = {
    new StructType().add("age", LongType)
  }

  //缓存区数据结构
  override def bufferSchema: StructType = {
    new StructType().add("sum", LongType).add("count", LongType)
  }

  //聚合函数返回值数据结构
  override def dataType: DataType = DoubleType

  //聚合函数是否是幂等的，即相同输入是否总是能得到相同的输出
  override def deterministic: Boolean = true

  //初始化缓冲区
  override def initialize(buffer: MutableAggregationBuffer): Unit = {
    buffer(0) = 0L
    buffer(1) = 0L
  }

  //给聚合函数传入一条新数据进行处理
  override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
    buffer(0) = buffer.getLong(0) + input.getLong(0)
    buffer(1) = buffer.getLong(1) + 1
  }

  //合并聚合函数缓冲区
  override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
    buffer1(0) = buffer1.getLong(0) + buffer2.getLong(0)
    buffer1(1) = buffer1.getLong(1) + buffer2.getLong(1)
  }

  //计算最终结果
  override def evaluate(buffer: Row): Any = {
    buffer.getLong(0).toDouble / buffer.getLong(1).toDouble
  }
}