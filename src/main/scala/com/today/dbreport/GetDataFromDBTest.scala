package com.today.dbreport

import ammonite.ops.Path
import ammonite.util.Res
import com.today.dbreport.utils.DbReportConnection
import com.typesafe.config.ConfigFactory

/**
  * 类功能描述: Ammonite 测试
  */
object GetDataFromDBTest {
  def main(args: Array[String]): Unit = {
    val database = "finance"
    val databaseConfig = ConfigFactory.load(DbReportConnection.getClass().getClassLoader, "config.conf");
    val driver = databaseConfig.getString(s"${database}.driverClassName")
    val url = databaseConfig.getString(s"${database}.url")
    val username = databaseConfig.getString(s"${database}.username")
    val password = databaseConfig.getString(s"${database}.password")
    val sql = "select * from t_oi limit 10"

    val args = Seq( ("sql", Some(sql)),
                    ("driver", Some(driver)),
                    ("url", Some(url)),
                    ("username", Some(username)),
                    ("password", Some(password)))
    val script = GetDataFromDBTest.getClass.getClassLoader.getResource("scripts/getDataFromDB.sc").getPath.substring(1)
    val result: (Res[Any], Seq[(Path, Long)]) = ammonite.Main().runScript(Path(script), args)
    println("----------" + result._1)
  }
}
