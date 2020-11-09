package com.automationanywhere.commands.DataRobotIntegration;
import static com.automationanywhere.commandsdk.model.AttributeType.CREDENTIAL;
import static com.automationanywhere.commandsdk.model.AttributeType.TEXT;
import static com.automationanywhere.commandsdk.model.DataType.STRING;

import Utils.DataRobotServer;
import Utils.OutputTransform;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.DictionaryValue;
import com.automationanywhere.botcommand.data.impl.ListValue;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.i18n.Messages;
import com.automationanywhere.commandsdk.i18n.MessagesFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import com.automationanywhere.commandsdk.model.DataType;
import com.automationanywhere.core.security.SecureString;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import Utils.HTTPRequest;


@BotCommand

@CommandPkg(
        //Unique name inside a package and label to display.
        name = "Get Deployments", label = "Get Deployments",
        node_label = "Get Deployments", description = "Retrieves Deployment Names, IDs, DataRobot Key and URL for generating predictions", icon = "DRicon.svg",
        comment = true ,  text_color = "#7B848B" , background_color =  "#3196D3",
        return_label = "List of Dictionaries with keys: 'modelLabel', 'modelId', 'modelArchType', 'modelType', 'projectId', 'projectName', 'modelStatus', 'modelUrl', 'modelServerId', 'modelDatarobotKey'", return_type = DataType.LIST, return_required = true)

public class GetDeployments {

    @Sessions
    private Map<String, Object> sessions;

    @com.automationanywhere.commandsdk.annotations.GlobalSessionContext
    private com.automationanywhere.bot.service.GlobalSessionContext globalSessionContext;

    public void setGlobalSessionContext(com.automationanywhere.bot.service.GlobalSessionContext globalSessionContext) {
        this.globalSessionContext = globalSessionContext;
    }

    private static final Messages MESSAGES = MessagesFactory.getMessages("com.automationanywhere.botcommand.demo.messages");

    @Execute
    public ListValue<?> action(
            @Idx(index = "1", type = TEXT) @Pkg(label = "Session name", default_value_type = STRING,  default_value = "Default") @NotEmpty String sessionName

    ) throws IOException, ParseException {

        DataRobotServer drs = (DataRobotServer) this.sessions.get(sessionName);
        String APIKey = drs.getAPIKey();
        String URL = drs.getAccountURL();

        // APIKey = (String) this.sessions.get(sessionName);

        //Create Map for Dictionary Output
        Map<String,Value> ResMap = new LinkedHashMap();

        //First API call to get deployments - returns 20 per page
        String response = HTTPRequest.GET(APIKey, URL+"/api/v2/deployments/");

        ListValue allModelInfo =OutputTransform.GetDeploymentModelsInfoToList(response);

        return allModelInfo;

    }
    public void setSessions(Map<String, Object> sessions) {this.sessions = sessions;}

}



