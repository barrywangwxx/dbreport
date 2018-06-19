import $ivy.`mysql:mysql-connector-java:5.1.36`
import $ivy.`com.github.wangzaixiang::scala-sql:2.0.3`

import wangzx.scala_commons.sql._
import ammonite.ops._

@main
def main(sql: String, driver: String, url: String, username: String, password: String) = {
	Class.forName(driver)
	val conn = java.sql.DriverManager.getConnection(url, username, password)
	val rows = conn.rows[Row](sql);
	rows.foreach(println)
}