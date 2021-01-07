import java.sql.DriverManager

import org.apache.spark.rdd.JdbcRDD
import org.apache.spark.{SparkConf, SparkContext}

object SparkMysql {
  def main(args: Array[String]): Unit = {
    // 1. 创建 SparkConf对象, 并设置 App名字
    val conf = new SparkConf().setAppName("WordCount").setMaster("local[*]")
    // 2. 创建SparkContext对象
    val sc = new SparkContext(conf)
    // 3. 定义连接MySQL的参数
    val driver = "com.mysql.jdbc.Driver"
    val url = "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC"
    val userName = "root"
    val passWd = "123456"
    //    val sql = "select * from user where id >= ? and id <= ?"
    //    val jdbcRDD = new JdbcRDD(
    //      sc, () => {
    //        //        获取数据库连接对象,注意pom导入的依赖与MySQL版本是否一致
    //        Class.forName(driver)
    //        DriverManager.getConnection(url, userName, passWd)
    //      }, sql, 1, 3, 2, (rs) => {
    //        println(rs.getString(1) + "," + rs.getString(2))
    //      }
    //    )

    //保存数据
    //多条数据写入数据库时顺序不一样是因为不同的数据发给了不同的节点去操作
    //这里有几条数据就创建了几次连接，性能较差
    //如果把数据库连接放在外面会报错；因为数据库连接运行再driver端，而foreach运行在executor端，而connection无法序列化
    //    val dataRDD = sc.makeRDD(List(("x1", "20"), ("x2", "30")))
    //    dataRDD.foreach { case (username, age) =>
    //      Class.forName(driver)
    //      val connection = DriverManager.getConnection(url, userName, passWd)
    //      val sql="insert into user (username,password) values (?,?)"
    //      val statement=connection.prepareStatement(sql)
    //      statement.setString(1,username)
    //      statement.setString(2,age)
    //      statement.execute()
    //      statement.close()
    //      connection.close()
    //    }

    //foreachRDD操作，对每个分区进行一次数据库连接
    //缺点时可能出现内存溢出 和mappartition一样
    val rdd = sc.parallelize(Array(("y1", "police"), ("y2", "fire")))
    // 对每个分区执行 参数函数
    rdd.foreachPartition(it => {
      Class.forName(driver)
      val conn = DriverManager.getConnection(url, userName, passWd)
      it.foreach(x => {
        val statement = conn.prepareStatement("insert into user (username,password) values(?, ?)")
        statement.setString(1,x._1)
        statement.setString(2, x._2)
        statement.executeUpdate()
      })
    })

    // 释放资源
    sc.stop()
  }
}
