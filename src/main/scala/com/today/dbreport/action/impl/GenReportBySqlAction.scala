package com.today.dbreport.action.impl

import java.io.FileOutputStream

import com.today.dbreport.Main.logger
import com.today.dbreport.action.ReportAction
import com.today.dbreport.dto.GenReportParam
import com.today.dbreport.utils.DataGenerator
import com.today.dbreport.utils.TemplateReader
import org.jxls.builder.xml.XmlAreaBuilder
import org.jxls.common.CellRef
import org.jxls.transform.poi.PoiTransformer
import org.jxls.util.TransformerFactory

import scala.collection.JavaConverters._

/**
  * 根据SQL及默认模板生成报表
  *
  * @author BarryWang create at 2018/6/14 10:03
  * @version 0.0.1
  */
class GenReportBySqlAction(genReportParam: GenReportParam) extends ReportAction{
  /**
    * 前置条件检查：动作、状态等业务逻辑
    */
  override def preCheck: Boolean = {
    true
  }

  /**
    * 生成报表
    */
  override def genetateReport: Unit = {
    val headersAndRows = DataGenerator.getHeadsAndDataBySql(genReportParam.database, genReportParam.querySql)
    val headers = headersAndRows._1
    val rows = headersAndRows._2

    val is = TemplateReader.getTemplateInputStream("dynamic_columns_demo.xls")
    try {
      val os = new FileOutputStream(genReportParam.output)
      try {
        val transformer = TransformerFactory.createTransformer(is, os)
        val configInputStream = TemplateReader.getTemplateInputStream("dynamic_columns_demo.xml")
        val areaBuilder = new XmlAreaBuilder(configInputStream, transformer)
        val xlsAreaList = areaBuilder.build
        val xlsArea = xlsAreaList.get(0)
        // creating context
        val context = PoiTransformer.createInitialContext
        context.putVar("headers", headers.asJava)
        context.putVar("rows", rows.asJava)
        // applying transformation
        logger.info("Applying area " + xlsArea.getAreaRef + " at cell " + new CellRef("Template!A1"))
        xlsArea.applyAt(new CellRef("Template!A1"), context)
        // saving the results to file
        transformer.write()
        logger.info("Complete")
      } finally {
        if (os != null){
          os.close()
        }
      }
    } finally {
      if (is != null){
        is.close()
      }
    }
  }
}
