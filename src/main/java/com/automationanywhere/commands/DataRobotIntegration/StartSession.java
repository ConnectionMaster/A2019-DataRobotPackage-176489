package com.automationanywhere.commands.DataRobotIntegration;
import static com.automationanywhere.commandsdk.model.AttributeType.CREDENTIAL;
import static com.automationanywhere.commandsdk.model.AttributeType.TEXT;
import static com.automationanywhere.commandsdk.model.DataType.STRING;

import Utils.DataRobotServer;
import Utils.HTTPRequest;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.i18n.Messages;
import com.automationanywhere.commandsdk.i18n.MessagesFactory;
import com.automationanywhere.commandsdk.model.AttributeType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.*;
import java.io.FileReader;

import com.automationanywhere.core.security.SecureString;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

@BotCommand

@CommandPkg(
        name = "Start Session", label = "Start Session",
        node_label = "Start Session", description = "Enter Username/PW or API Key to authenticate with DataRobot", icon = "DRicon.svg",
        comment = true ,  text_color = "#7B848B" , background_color =  "#3196D3"
        )

public class StartSession {

    @GlobalSessionContext
    public com.automationanywhere.bot.service.GlobalSessionContext globalSessionContext;

    public void setGlobalSessionContext(com.automationanywhere.bot.service.GlobalSessionContext globalSessionContext) {
        this.globalSessionContext = globalSessionContext;
    }

    @Sessions
    private Map<String, Object> sessions = new HashMap<String,Object>();

    private static final Messages MESSAGES = MessagesFactory.getMessages("com.automationanywhere.botcommand.demo.messages");

    @Execute
    public void action(
            @Idx(index = "1", type = TEXT) @Pkg(label = "Session name",  default_value_type = STRING, default_value = "Default") @NotEmpty String sessionName,
            @Idx(index = "2", type = TEXT) @Pkg(label = "Inference Base URL",  default_value_type = STRING, default_value = "https://automationanywhere.orm.datarobot.com") @NotEmpty String InferenceURL,
            @Idx(index = "3", type = TEXT) @Pkg(label = "Account Base URL",  default_value_type = STRING, default_value = "https://app.datarobot.com") @NotEmpty String AccountBaseURL,
            @Idx(index = "4", type = CREDENTIAL) @Pkg(label = "API Key") SecureString APIKeyInput
           // @Idx(index = "3", type = AttributeType.TEXT) @Pkg(label = "Username") String UserName,
           // @Idx(index = "4", type = CREDENTIAL) @Pkg(label = "Password") SecureString Password,


    ) throws IOException, ParseException {

        String apiKey="";

        if (this.sessions.containsKey(sessionName)){
            throw new BotCommandException(MESSAGES.getString("Session name in use"));
        }
        if (APIKeyInput==null){
            throw new BotCommandException(MESSAGES.getString("APIKeyMissing"));
        }
        if (InferenceURL==null){
            throw new BotCommandException(MESSAGES.getString("URLEmpty","InferenceURL"));
        }
        if (AccountBaseURL==null){
            throw new BotCommandException(MESSAGES.getString("URLEmpty","Account URL"));
        }
        //else if (Password==null && APIKeyInput==null){
       //     throw new BotCommandException(MESSAGES.getString("emptyInputString", "Enter either user credentials or API Key"));
       // }

        /*
        if(!"".equals(UserName.trim()) && !"".equals(Password.getInsecureString().trim())){

            String pw = Password.getInsecureString();

            //Credentials for Basic Auth in API call
            String usernameColonPassword = UserName + ":" + pw;
            String basicAuthPayload = "Basic " + Base64.getEncoder().encodeToString(usernameColonPassword.getBytes());

            String response = HTTPRequest.GET(basicAuthPayload, "https://app.datarobot.com/api/v2/account/apiKeys/");
            // return result and parse nested JSON to get API key
            Object obj = new JSONParser().parse(response);
            JSONObject jsonObj = (JSONObject) obj;
            JSONArray result = (JSONArray) jsonObj.get("data");
            JSONObject result1 = (JSONObject) result.get(0);
            apiKey = (String) result1.get("key");
            }
          */

       // else
           // if (!"".equals(APIKeyInput.getInsecureString().trim())){ apiKey = APIKeyInput.getInsecureString();}

        apiKey = APIKeyInput.getInsecureString();

        DataRobotServer drs = new DataRobotServer(InferenceURL,AccountBaseURL,apiKey);

        this.sessions.put(sessionName, drs);
    }
    public void setSessions(Map<String, Object> sessions) {this.sessions = sessions;}
}
