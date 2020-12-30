import com.alibaba.fastjson.JSON

object FastJson {
  def main(args: Array[String]): Unit = {
    //解析json格式的字符串，对于jsonarray形式的字符串，可以组装成json格式的字符串再使用json.getJSONArray(key).get(0)方法（小白目前还不知道其他方法）
    val jsonString = "{\"et\":\"kanqiu_client_join\",\"vtm\":1435898329434,\"body\":{\"client\":\"866963024862254\",\"" +
      "client_type\":\"android\",\"room\":\"NBA_HOME\",\"gid\":\"\",\"type\":\"\",\"roomid\":\"\"}," +
      "\"time\":[{\"arrayKey\":\"arrayVal\"},{\"key2\":\"val2\"}]}"
    println("Json String:--------")
    println(jsonString)
    println(jsonString.getClass)

    val json=JSON.parseObject(jsonString)
    println("Parse Json:--------")
    println(json)
    println(json.getClass)
    println(json.getInteger("vtm"))
    println(json.getString("et"))
    println(json.getJSONObject("body"))
    println(json.getJSONObject("body").getClass)
    println(json.getJSONObject("body").getString("client"))

    println("---------Array------------")
    println(json.get("time"))
    println(json.get("time").getClass)
    println(json.getJSONArray("time"))
    println(json.getJSONArray("time").getClass)

    println(json.getJSONArray("time").get(0))
    println(json.getJSONArray("time").get(0).getClass)
    println(json.getJSONArray("time").getJSONObject(0))
    println(json.getJSONArray("time").getJSONObject(0).getClass)

    println(json.getJSONArray("time").get(0))  //虽然都是JSONObject，但这个没有 get 方法
    //Error:(36, 46) value get is not a member of Object
    //    println(json.getJSONArray("time").get(0).get("arrayKey"))  //虽然都是JSONObject，但这个没有 get 方法
    println(json.getJSONArray("time").get(0).getClass)
    println(json.getJSONArray("time").getJSONObject(0).get("arrayKey"))   //虽然都是JSONObject，有 get 方法
    println(json.getJSONArray("time").getJSONObject(0).get("arrayKey").getClass)
  }
}
