package com.today.dbreport.action.impl

import ammonite.ops.Path
import ammonite.util.Res
import com.today.dbreport.action.ReportAction
import com.today.dbreport.dto.GenReportParam
import com.today.dbreport.utils.DbReportConnection
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

/**
  * 根据Scala脚本及模板生成报表
  *
  * @author BarryWang create at 2018/6/19 15:59
  * @version 0.0.1
  */
class GenReportByScriptWithoutSqlAction(genReportParam: GenReportParam) extends ReportAction {
  val logger= LoggerFactory.getLogger(classOf[GenReportByScriptWithoutSqlAction])

  /**
    * 前置条件检查：动作、状态等业务逻辑
    */
  override def preCheck: Boolean = {
    genReportParam.template match {
      case Some(x) => return true
      case None => {
        logger.error("请输入模板！")
        false
      }
    }
  }

  /**
    * 生成报表
    */
  override def genetateReport: Unit = {
    val database = genReportParam.database
    val databaseConfig = ConfigFactory.load(DbReportConnection.getClass().getClassLoader, "config.conf");
    val driver = databaseConfig.getString(s"${database}.driverClassName")
    val url = databaseConfig.getString(s"${database}.url")
    val username = databaseConfig.getString(s"${database}.username")
    val password = databaseConfig.getString(s"${database}.password")

    val args = Seq( ("driver", Some(driver)),
                    ("url", Some(url)),
                    ("username", Some(username)),
                    ("password", Some(password)),
                    ("tempalte", Some(genReportParam.template.getOrElse(""))),
                    ("output", Some(genReportParam.output)))
    val result: (Res[Any], Seq[(Path, Long)]) = ammonite.Main().runScript(Path(genReportParam.script.getOrElse("")), args)
    println("----------" + result._1)
  }
}
