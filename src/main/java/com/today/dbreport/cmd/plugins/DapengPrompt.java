package com.today.dbreport.cmd.plugins;

import org.clamshellcli.api.Context;
import org.clamshellcli.api.Prompt;

public class DapengPrompt implements Prompt {

    private static final String PROMPT = "dbreport > ";

    @Override
    public String getValue(Context context) {
        return PROMPT;
    }

    @Override
    public void plug(Context context) {

    }

    @Override
    public void unplug(Context context) {

    }
}
