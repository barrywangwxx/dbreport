package com.today.dbreport.cmd.plugins;

import com.today.dbreport.action.impl.GenReportByScriptWithSqlAction;
import com.today.dbreport.action.impl.GenReportByScriptWithoutSqlAction;
import com.today.dbreport.action.impl.GenReportBySqlAction;
import com.today.dbreport.action.impl.GenReportByTemplateBySqlAction;
import com.today.dbreport.cmd.utils.CmdProperties;
import com.today.dbreport.cmd.utils.CmdUtils;
import com.today.dbreport.dto.GenReportParam;
import com.today.dbreport.utils.EmailUtil;
import org.clamshellcli.api.Command;
import org.clamshellcli.api.Configurator;
import org.clamshellcli.api.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Option;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 生成报表服务
 *
 * @author BarryWang create at 2018/6/14 16:38
 * @version 0.0.1
 */
public class GenReportCmd implements Command {
    private static final Logger logger = LoggerFactory.getLogger(HelpCmd.class);
    private static final String NAMESPACE = "syscmd";
    private static final String ACTION_NAME = "genreport";

    @Override
    public Descriptor getDescriptor() {
        return new Descriptor() {
            @Override
            public String getNamespace() {
                return NAMESPACE;
            }
            @Override
            public String getName() {
                return ACTION_NAME;
            }
            @Override
            public String getDescription() {
                return " 数据库生成报表";
            }
            @Override
            public String getUsage() {
                StringBuilder sb = new StringBuilder();
                sb.append(Configurator.VALUE_LINE_SEP)
                        .append(" genreport -db member -query select * from member -output /data/report/output/20180614.xlsx")
                        .append(Configurator.VALUE_LINE_SEP)
                        .append(" genreport -db member -query select * from member -output /data/report/output/20180614.xlsx -mailto XXX.today36524.com.cn")
                        .append(Configurator.VALUE_LINE_SEP)
                        .append(" genreport -db member -query members:select * from member;memberCoupons:select * from member_coupon -template /data/report/template/member.xlsx -output /data/report/output/20180614.xlsx")
                        .append(Configurator.VALUE_LINE_SEP)
                        .append(" genreport -db member -template /data/report/template/member.xlsx -script /data/report/script/memeber.sc -output /data/report/output/20180614.xlsx")
                        .append(Configurator.VALUE_LINE_SEP)
                        .append(" genreport -db member -query members:select * from member;memberCoupons:select * from member_coupon -template /data/report/template/member.xlsx -script /data/report/script/memeber.sc -output /data/report/output/20180614.xlsx")
                        .append(Configurator.VALUE_LINE_SEP);
                return sb.toString();
            }

            Map<String,String> args = null;
            @Override
            public Map<String, String> getArguments() {
                if(args != null) {
                    return args;
                }
                args = new LinkedHashMap<>();
                args.put(CmdProperties.KEY_ARGS_DATABASE , "[required] 数据库名称");
                args.put(CmdProperties.KEY_ARGS_SQL , "[required] 生成报表的查询SQL，可以添加多个SQL（没有模板只能传一个SQL格式：-query SQL），格式：object1Name:SQL1;object2Name:SQL2");
                args.put(CmdProperties.KEY_ARGS_OUTPUT , "[required] 生成报表绝对路径");
                args.put(CmdProperties.KEY_ARGS_TEMPLATE, "[optional] 生成报表JXLS Excel模板绝对路径， 请参考：http://jxls.sourceforge.net/reference/simple_exporter.html");
                args.put(CmdProperties.KEY_ARGS_SCRIPT, "[optional] 生成报表Scala脚本, 请参考：http://ammonite.io/#ScalaScripts");
                args.put(CmdProperties.KEY_ARGS_EMAIL_TO, "[optional] 生成报表后发送的邮箱, 多个请用英文分号“;”隔开");
                return args;
            }
        };
    }

    @Override
    public Object execute(Context context) {
        Map<String, String> inputArgs = CmdUtils._getCmdArgs(context);
        logger.info("[execute] ==> inputArgs=[{}]", inputArgs);
        String database = inputArgs.get(CmdProperties.KEY_ARGS_DATABASE);
        String querySql = inputArgs.get(CmdProperties.KEY_ARGS_SQL);
        String output = inputArgs.get(CmdProperties.KEY_ARGS_OUTPUT);
        String template = inputArgs.get(CmdProperties.KEY_ARGS_TEMPLATE);
        String script = inputArgs.get(CmdProperties.KEY_ARGS_SCRIPT);
        String mailto = inputArgs.get(CmdProperties.KEY_ARGS_EMAIL_TO);
        /**
        1. no template + sql -> normal excel
        2. sql* + jxls template
        3. script + jxls tempalte
        4. sql + script + jxls template
        5. email support
        6. task publish standard. docker*/
        if(!CmdUtils.isEmpty(script)){
            Option templateOptional = Option.empty();
            if(!CmdUtils.isEmpty(template) ){
                templateOptional = Option.apply(template);
            }
            Option scriptOptional = Option.apply(script);
            Option mailtoOptional = Option.empty();
            if(!CmdUtils.isEmpty(mailto) ){
                mailtoOptional = Option.apply(mailto);
            }
            GenReportParam genReportParam = new GenReportParam(database, querySql, output,
                    templateOptional, scriptOptional, mailtoOptional);
            if(CmdUtils.isEmpty(querySql)){//sql + script + jxls template
                new GenReportByScriptWithoutSqlAction(genReportParam).execute();
            } else {//script + jxls tempalte
                new GenReportByScriptWithSqlAction(genReportParam).execute();
            }
        } else if (CmdUtils.isEmpty(database) || CmdUtils.isEmpty(querySql) || CmdUtils.isEmpty(output)) {
            CmdUtils.writeMsg(context, " request format is invalid.. please check your input.....");
            String usage = getDescriptor().getUsage();
            CmdUtils.writeMsg(context, usage);
        } else {
            Option templateOptional = Option.empty();
            if(!CmdUtils.isEmpty(template) ){
                templateOptional = Option.apply(template);
            }
            Option scriptOptional = Option.empty();
            if(!CmdUtils.isEmpty(script) ){
                scriptOptional = Option.apply(script);
            }
            Option mailtoOptional = Option.empty();
            if(!CmdUtils.isEmpty(mailto) ){
                mailtoOptional = Option.apply(mailto);
            }
            GenReportParam genReportParam = new GenReportParam(database, querySql, output,
                                                templateOptional, scriptOptional, mailtoOptional);
            if(!CmdUtils.isEmpty(template) ){//sql* + jxls template
                new GenReportByTemplateBySqlAction(genReportParam).execute();
            } else {//no template + sql
                new GenReportBySqlAction(genReportParam).execute();
            }
        }

        CmdUtils.writeMsg(context, " report generated, please find it in [" + output + "]");
        //发送邮件
        if(!CmdUtils.isEmpty(mailto) ){
            EmailUtil.sendEmail(mailto.trim(), "生成报表", "生成报表请参考附件", output);
            CmdUtils.writeMsg(context, " please find attached email [" + mailto.trim() + "]");
        }
        return null;
    }

    @Override
    public void plug(Context context) {

    }

    @Override
    public void unplug(Context context) {

    }
}
