package com.automationanywhere.commands.DataRobotIntegration;


import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;

import java.util.Map;

import static com.automationanywhere.commandsdk.model.AttributeType.TEXT;
import static com.automationanywhere.commandsdk.model.DataType.STRING;

@BotCommand
@CommandPkg(label = "End Session", name = "End Session", description = "Session End", icon = "DRicon.svg", node_label = "session end {{sessionName}}",
        comment = true ,  text_color = "#7B848B" , background_color =  "#3196D3")

public class EndSession {

    @Sessions
    private Map<String, Object> sessions;

    @Execute
    public void end(@Idx(index = "1", type = TEXT) @Pkg(label = "Session name", default_value_type = STRING, default_value = "Default") @NotEmpty String sessionName){

       // String APIKey = (String) this.sessions.get(sessionName);
        sessions.remove(sessionName);
    }
    public void setSessions(Map<String, Object> sessions) {
        this.sessions = sessions;
    }

}
