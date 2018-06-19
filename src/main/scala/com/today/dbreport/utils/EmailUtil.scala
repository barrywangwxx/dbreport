package com.today.dbreport.utils

import java.io.File
import java.util.Properties

import com.typesafe.config.ConfigFactory
import javax.activation.{DataHandler, FileDataSource}
import javax.mail._
import javax.mail.internet.{InternetAddress, MimeBodyPart, MimeMessage, MimeMultipart}


/**
  * 邮件发送工具类
  *
  * @author BarryWang create at 2018/6/19 11:15
  * @version 0.0.1
  */
object EmailUtil {
  private val EMAIL_CONFIG = ConfigFactory.load(EmailUtil.getClass().getClassLoader, "config.conf");
  private val SMTP_HOST = EMAIL_CONFIG.getString("mail.smtp.host")
  private val SMPT_USER = EMAIL_CONFIG.getString("mail.smtp.user")
  private val SMTP_PASSWORD = EMAIL_CONFIG.getString("mail.smtp.password")
  private val SMTP_AUTH = EMAIL_CONFIG.getString("mail.smtp.auth")
  private val FROM = EMAIL_CONFIG.getString("mail.from")

  /**
    * 发送邮件
    *
    * @param receiverEmail 收件邮箱
    * @param subject       邮件标题
    * @param content       邮件内容
    * @param filePath      邮件附件
    * @throws MessagingException
    */
  def sendEmail(receiverEmail: String, subject: String, content: String, filePath: String): Unit = {
    // 1.创建一个程序与邮件服务器会话对象 Session
    val props = new Properties
    props.setProperty("mail.transport.protocol", "smtp")
    props.setProperty("mail.smtp.host", SMTP_HOST)
    props.setProperty("mail.smtp.port", "25")
    // 指定验证为true
    props.setProperty("mail.smtp.auth", SMTP_AUTH)
    props.setProperty("mail.smtp.timeout", "1000")
    // 验证账号及密码，密码需要是第三方授权码
    val auth = new Authenticator() {
      override def getPasswordAuthentication = new PasswordAuthentication(SMPT_USER, SMTP_PASSWORD)
    }
    val session = Session.getInstance(props, auth)
    //2、通过session得到transport对象
    val ts = session.getTransport
    //3、连上邮件服务器
    ts.connect(SMTP_HOST, SMPT_USER, SMTP_PASSWORD)
    //4、创建邮件
    val message = createAttachMail(session, receiverEmail, subject, content, filePath)
    //5、发送邮件
    ts.sendMessage(message, message.getAllRecipients)
    ts.close()
  }

  /**
    * 创建一封带附件的邮件
    */
  private def createAttachMail(session: Session, receiverEmail: String, subject: String, content: String, filePath: String): MimeMessage = {
    val message = new MimeMessage(session)
    //设置邮件的基本信息
    //发件人
    message.setFrom(new InternetAddress(FROM))
    //收件人
    //message.setRecipient(Message.RecipientType.TO, new InternetAddress("980124639@qq.com"));
    message.setRecipients(Message.RecipientType.TO, receiverEmail.split(";").map(new InternetAddress(_)).toArray[Address])
    //邮件标题
    message.setSubject(subject)
    //创建邮件正文，为了避免邮件正文中文乱码问题，需要使用charset=UTF-8指明字符编码
    val text = new MimeBodyPart
    text.setContent(content, "text/html;charset=UTF-8")
    //创建容器描述数据关系
    val mp = new MimeMultipart
    mp.addBodyPart(text)
    mp.setSubType("mixed")
    //创建邮件附件
    if (filePath != null && new File(filePath).exists) {
      val attach = new MimeBodyPart
      val dh = new DataHandler(new FileDataSource(filePath))
      attach.setDataHandler(dh)
      attach.setFileName(dh.getName)
      mp.addBodyPart(attach)
    }
    message.setContent(mp)
    message.saveChanges()
    //将创建的Email写入到E盘存储
    // message.writeTo(new FileOutputStream("E:\\attachMail.eml"));
    //返回生成的邮件
    message
  }

  def main(args: Array[String]): Unit = {
    sendEmail("980124639@qq.com", "报表生成", "报表生成内容", "C:\\Users\\lhe\\Desktop\\test2.sc")
  }
}
