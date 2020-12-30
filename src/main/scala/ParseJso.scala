import org.json4s._
import org.json4s.jackson.JsonMethods._

object ParseJso {
  def main(args: Array[String]): Unit = {
    val json = parse(
      """
         { "name": "joe",
           "children": [
             {
               "name": "Mary",
               "age": 5
             },
             {
               "name": "Mazy",
               "age": 3
             }
           ]
         }
      """)

    // 嵌套返回值
    for (JArray(child) <- json) println(child)
//    res0: List(JObject(List((name,JString(Mary)), (age,JInt(5)))), JObject(List((name,JString(Mazy)), (age,JInt(3)))))

    // 嵌套取数组中某个字段值
    for {
      JObject(child) <- json
      JField("age", JInt(age)) <- child
    } yield age

    // 嵌套取数组中某个字段值，并添加过滤
    for {
      JObject(child) <- json
      JField("name", JString(name)) <- child
      JField("age", JInt(age)) <- child
      if age > 4
    } println (name, age)
  }
}
