package com.today.dbreport

import com.today.dbreport.action.impl.GenReportBySqlAction
import com.today.dbreport.dto.GenReportParam
import org.slf4j.LoggerFactory

/**
  * 生成报表入口
  *
  * @author BarryWang create at 2018/6/1 11:02
  * @version 0.0.1
  */
object Main {
  val logger= LoggerFactory.getLogger(Main.getClass)

  def main(args: Array[String]): Unit = {
    if(args.length < 3){
      logger.info("请输入正确的参数：java -jar dbreport.jar 数据库 \"SQL\" \"输出文件\"")
      logger.info("正确参数格式如下：java -jar dbreport.jar member \"select * from memeber\" \"/tmp/report/output.xls\"")
      return
    }
    new GenReportBySqlAction(GenReportParam(args(0), args(1), args(2), None, None, None)).execute
  }
}
