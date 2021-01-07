import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object SparkSql_Demo {
  def main(args: Array[String]): Unit = {
    val conf=new SparkConf().setMaster("local[*]").setAppName("Demo")
    val session=SparkSession.builder.config(conf).getOrCreate()
    val dataframe=session.read.json("input/user.json")
    dataframe.show()
//    将dataframe转换为表
    val view = dataframe.createTempView("table")
    session.sql("select * from table").show()
    session.stop()
}}
