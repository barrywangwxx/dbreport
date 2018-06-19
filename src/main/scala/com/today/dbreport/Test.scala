package com.today.dbreport

import java.io.{FileInputStream, FileOutputStream, InputStream, OutputStream}
import java.util.{List => JavaList}

import com.today.dbreport.utils.DataGenerator
import org.jxls.area.Area
import org.jxls.builder.AreaBuilder
import org.jxls.builder.xls.XlsCommentAreaBuilder
import org.jxls.common.{CellRef, Context}
import org.jxls.transform.Transformer
import org.jxls.transform.poi.PoiTransformer
import org.jxls.util.TransformerFactory
import org.slf4j.{Logger, LoggerFactory}

/**
  * TODO the class description
  *
  * @author BarryWang create at 2018/6/2 14:24
  * @version 0.0.1
  */
object Test {
  val logger: Logger = LoggerFactory.getLogger(Test.getClass)

  val template = "C:\\Users\\lhe\\Desktop\\my_template.xls"
  val output = "C:\\Users\\lhe\\Desktop\\my_output.xlsx"

  def main(args: Array[String]): Unit ={
   /* val list = List(1,2,3,4)
    for(i <- 0 to list.size-1){
      println(list(i))
    }*/

    /*val map = Map(2->23)
    if(map.contains(3)){
      println(map(3))
    }*/

    /*val vv:Integer = null
    println(vv)*/
    execute()
  }

  def execute(): Unit = {
    val objNameSqlMappingArray = "members:select * from member;memberCoupons:select * from member_coupon".split(";")
    if(objNameSqlMappingArray.size < 1){
      logger.error("请输入正确的查询SQL")
      return
    }
    val context: Context = PoiTransformer.createInitialContext
    objNameSqlMappingArray.foreach{x =>
      val objNameSql = x.split(":")
      if(objNameSql.size < 1){
        logger.error("请输入正确的查询SQL")
        return
      }
      val dataObj: JavaList[Object] = DataGenerator.getObjectDataList("member", objNameSql(1))
      context.putVar(objNameSql(0), dataObj)
    }

//    val departments: JavaList[Object] = DataGenerator.getObjectDataList("finance", "select * from employee")
//    println("==============="+departments.get(0))
//    logger.info("Opening input stream")
    val is: InputStream = new FileInputStream(template)
    try {
      val os: OutputStream = new FileOutputStream(output)
      try {
        val transformer: Transformer = TransformerFactory.createTransformer(is, os)
        val areaBuilder: AreaBuilder = new XlsCommentAreaBuilder(transformer, false)
        val xlsAreaList: JavaList[Area] = areaBuilder.build
        val xlsArea: Area = xlsAreaList.get(0)
//        val context: Context = PoiTransformer.createInitialContext
//        context.putVar("employees", departments)
        logger.info("Applying area " + xlsArea.getAreaRef + " at cell " + new CellRef("Template!A1"))
        xlsArea.applyAt(new CellRef("Template!A1"), context)
        xlsArea.processFormulas()
        logger.info("Complete")
        transformer.write()
        logger.info("written to file")
      } finally if (os != null) os.close()
    }
    finally if (is != null) is.close()
  }
}
