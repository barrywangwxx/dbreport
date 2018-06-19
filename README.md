## 1.使用方式:  
    > cd dist  
    > java -jar cli.jar 

## 2. 命令使用说明:    

###    2.1. help 命令使用说明 
      2.1.1 help     查看所有指令的用法
###    2.2. exit 命令使用说明 
      2.2.1 exit     退出命令行
###    2.3. sysinfo 命令使用说明 
      2.3.1 sysinfo  查看当前运行环境信息
      2.3.2 sysinfo -cp  查看当前JVM classpath
      2.3.3 sysinfo -props  使用JVM配置. Usage -props.
      2.3.3 sysinfo -mem  当前JVM的内存使用情况
###    2.4. genreport 命令使用说明 
      2.4.1 genreport  根据SQL动态生成报表
      2.4.2 help genreport  展示genreport命令完整使用说明
      2.4.3 参数说明：
             -db      [required] 数据库名称
          -query      [required] 生成报表的查询SQL，可以添加多个SQL（没有模板只能传一个SQL格式：-query SQL），格式：object1Name:SQL1;object2Name:SQL2
         -output      [required] 生成报表绝对路径
       -template      [optional] 生成报表JXLS Excel模板绝对路径， 请参考：http://jxls.sourceforge.net/reference/simple_exporter.html
         -script      [optional] 生成报表Scala脚本, 请参考：http://ammonite.io/#ScalaScripts
         -mailto      [optional] 生成报表后发送的邮箱, 多个请用英文分号“;”隔开
      2.4.4 实例展示：
       dbreport > genreport -db member -query select * from member -output C:\Users\lhe\Desktop\output1.xlsx -mailto 980124639@qq.com
       dbreport > genreport -db member -query select * from member -output C:\Users\lhe\Desktop\output2.xlsx -mailto 980124639@qq.com
       dbreport > genreport -db member -query members:select * from member;memberCoupons:select * from member_coupon -template C:\Users\lhe\Desktop\my_template.xls -output C:\Users\lhe\Desktop\output3.xlsx -mailto 980124639@qq.com
       dbreport > genreport -db member -template C:\Users\lhe\Desktop\my_template.xls -script C:\Users\lhe\Desktop\genreportnosql.sc -output C:\Users\lhe\Desktop\output4.xlsx -mailto 980124639@qq.com
       dbreport > genreport -db member -query members:select * from member;memberCoupons:select * from member_coupon -template C:\Users\lhe\Desktop\my_template.xls -script C:\Users\lhe\Desktop\genreport.sc -output C:\Users\lhe\Desktop\output5.xlsx -mailto 980124639@qq.com