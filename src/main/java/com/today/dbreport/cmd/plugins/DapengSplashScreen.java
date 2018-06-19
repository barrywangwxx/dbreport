package com.today.dbreport.cmd.plugins;

import org.clamshellcli.api.Configurator;
import org.clamshellcli.api.Context;
import org.clamshellcli.api.IOConsole;
import org.clamshellcli.api.SplashScreen;

public class DapengSplashScreen implements SplashScreen {

    private StringBuilder screen;

    @Override
    public void render(Context ctx) {
        IOConsole console = ctx.getIoConsole();
        console.println(screen.toString());
        //console.writeOutput(screen.toString());
    }

    @Override
    public void plug(Context context) {
        screen = new StringBuilder(128);
        screen
        .append(Configurator.VALUE_LINE_SEP)
        .append(Configurator.VALUE_LINE_SEP)
        .append("                                                                                        " ).append(Configurator.VALUE_LINE_SEP)
        .append(" 88888      88888    888888     88888888  888888         888       888888    88888888888" ).append(Configurator.VALUE_LINE_SEP)
        .append(" 88   888   88   88  88   888   88        88    88    88     88    88    88      88     ").append(Configurator.VALUE_LINE_SEP)
        .append(" 88     88  88888    88   888   88        88    88  88         88  88    88      88     ").append(Configurator.VALUE_LINE_SEP)
        .append(" 88     88  88       888888     88888888  888888    8           8  888888        88     ").append(Configurator.VALUE_LINE_SEP)
        .append(" 88     88  88888    88   88    88        88        88         88  88   88       88     ").append(Configurator.VALUE_LINE_SEP)
        .append(" 88   888   88   88  88    88   88        88          88     88    88    88      88     " ).append(Configurator.VALUE_LINE_SEP)
        .append(" 88888      88888    88     88  88888888  88             888       88    88      88     ").append(Configurator.VALUE_LINE_SEP)
        .append(Configurator.VALUE_LINE_SEP)
        .append(Configurator.VALUE_LINE_SEP)
        .append("Java version: ").append(System.getProperty("java.version")).append(Configurator.VALUE_LINE_SEP)
        .append("Java Home: ").append(System.getProperty("java.home")).append(Configurator.VALUE_LINE_SEP)
        .append("OS: ").append(System.getProperty("os.name")).append(", Version: ").append(System.getProperty("os.version"))
        .append(Configurator.VALUE_LINE_SEP)
        .append(Configurator.VALUE_LINE_SEP);
        setScreen(screen);
    }

    @Override
    public void unplug(Context context) {

    }


    public StringBuilder getScreen() {
        return screen;
    }

    public void setScreen(StringBuilder screen) {
        this.screen = screen;
    }
}
