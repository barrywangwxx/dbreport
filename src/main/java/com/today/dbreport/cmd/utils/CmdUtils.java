package com.today.dbreport.cmd.utils;

import org.clamshellcli.api.Context;
import org.clamshellcli.api.IOConsole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CmdUtils {
    private static final Logger logger = LoggerFactory.getLogger(CmdUtils.class);

    public static void writeMsg(Context context, String content) {
        IOConsole ioc = context.getIoConsole();
        ioc.println(content);
    }

    public static void writeFormatMsg(Context context, String format, Object... var2) {
        IOConsole ioc = context.getIoConsole();
        String info = String.format(format, var2);
        ioc.println(info);
    }


    public static Map<String, String> getCmdArgs(Context context) {
        Map<String, String> args = new HashMap<>();
        String[] inputArgs = (String[]) context.getValue(Context.KEY_COMMAND_LINE_ARGS);

        if (inputArgs != null && inputArgs.length > 0) {
            for (int i = 0; i < inputArgs.length; i++) {
                String arg = inputArgs[i];
                switch (arg) {
                   /* case CmdProperties.KEY_ARGS_PATH:
                        if ((i + 1) <= inputArgs.length) {
                            args.put(CmdProperties.KEY_ARGS_PATH, inputArgs[i+1]);
                        }
                        break;*//*
                    case CmdProperties.KEY_ARGS_DATA:
                        if ((i + 1) <= inputArgs.length) {
                            args.put(CmdProperties.KEY_ARGS_DATA, inputArgs[i + 1]);
                        }
                        break;
                    case CmdProperties.KEY_ARGS_ROUTE:
                        if ((i + 1) <= inputArgs.length) {
                            args.put(CmdProperties.KEY_ARGS_ROUTE, inputArgs[i + 1]);
                        }
                        break;*/
                    default:
                        if ((i + 1) < inputArgs.length && arg.contains("-")) {
                            args.put(arg, inputArgs[i + 1]);
                        }else if((i + 1) == inputArgs.length && arg.contains("-")){
                            args.put(arg, arg);
                        }
                        break;
                }
            }
        }
        return args;
    }

    public static boolean isEmpty(String value) {
        return value == null || value.trim().length() <= 0;
    }

    public static Map<String, String> _getCmdArgs(Context ctx) {
        String cmdLine = (String) ctx.getValue(Context.KEY_COMMAND_LINE_INPUT);
        cmdLine+=" ";//不加" "  类似这种会报错 service -list
        logger.info("[_getCmdArgs] ==>cmdLine=[{}]",cmdLine);
        Map<String, String> args = new HashMap<>();
        int charIndex = 0;

        while (charIndex < cmdLine.length()) {
            int startIndex = cmdLine.indexOf('-', charIndex);
            int endIndex = cmdLine.indexOf(' ', startIndex);
            String key = cmdLine.substring(startIndex, endIndex);

            startIndex = cmdLine.indexOf(' ', endIndex);
            endIndex = cmdLine.indexOf('-', startIndex) > 0 ? cmdLine.indexOf('-', startIndex) : cmdLine.length();
            String value = cmdLine.substring(startIndex, endIndex);
            charIndex = endIndex;
            if(value.trim().isEmpty()){
                args.put(key.trim(), key.trim());
            }else{
                args.put(key.trim(), value.trim());
            }
        }
        return args;
    }

    public static String getResult(Object obj) {
        String empty_data = "the data is empty...";
        if (obj == null) return empty_data;

        if (obj instanceof List) {
            List<String> stringList = (List<String>) obj;
            if (stringList != null && !stringList.isEmpty()) {
                StringBuilder info = new StringBuilder();
                stringList.forEach(item -> {
                    info.append(item);
                    info.append("\r\n");
                });
                return info.toString();
            } else {
                return empty_data;
            }
        }

        if (obj instanceof String) {
            if (!((String) obj).isEmpty()) {
                return (String) obj;
            } else {
                return empty_data;
            }
        }
        return empty_data;
    }

    public static void handledStatus(Context context, boolean handled,String cmdUsage) {
        //没有处理  打印help info
        if (!handled) {
            CmdUtils.writeMsg(context, cmdUsage);
        }
    }

}
