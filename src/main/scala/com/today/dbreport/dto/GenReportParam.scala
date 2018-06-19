package com.today.dbreport.dto

/**
  * 报表生成参数
  *
  * @author BarryWang create at 2018/6/15 11:53
  * @version 0.0.1
  */
case class GenReportParam(
                           /**
                             * 数据库名
                             */
                           database: String,
                           /**
                             *查询SQL
                             */
                           querySql: String,
                           /**
                             * 报表输出路径
                             */
                           output: String,
                           /**
                             * 模板
                             */
                           template: Option[String],
                           /**
                             * Scala脚本
                             */
                           script: Option[String],
                           /**
                             * 发送邮箱
                             */
                           mailto: Option[String])
