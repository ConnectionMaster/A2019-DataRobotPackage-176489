package com.automationanywhere.commands.DataRobotIntegration;

import Utils.CustomFileUtils;
import Utils.DataRobotServer;
import Utils.HTTPRequest;
import Utils.OutputTransform;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.ListValue;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.data.impl.TableValue;
import com.automationanywhere.botcommand.data.model.table.Row;
import com.automationanywhere.botcommand.data.model.table.Table;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.i18n.Messages;
import com.automationanywhere.commandsdk.i18n.MessagesFactory;
import com.automationanywhere.commandsdk.model.AttributeType;
import com.automationanywhere.commandsdk.model.DataType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import shadow.org.apache.commons.io.FileUtils;
import shadow.org.apache.tools.ant.taskdefs.Input;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.automationanywhere.commandsdk.model.AttributeType.TEXT;
import static com.automationanywhere.commandsdk.model.DataType.STRING;

//BotCommand makes a class eligible for being considered as an action.
@BotCommand

//CommandPks adds required information to be displayable on GUI.
@CommandPkg(
        //Unique name inside a package and label to display.
        name = "Get Best Predictions", label = "Get Best Predictions",
        node_label = "Get Best Predictions for Data Set", description = "Get Best Predictions for Data Set", icon = "DRicon.svg",
        comment = true ,  text_color = "#7B848B" , background_color =  "#3196D3",
        //Return type information. return_type ensures only the right kind of variable is provided on the UI.
        return_label = "Returns a list of Dictionaries with keys 'score', 'label', 'rowId'", return_type = DataType.LIST, return_sub_type = DataType.ANY, return_required = true)

public class GetBestPrediction {

    @Sessions
    private Map<String, Object> sessions;

    private static final Messages MESSAGES = MessagesFactory.getMessages("com.automationanywhere.botcommand.demo.messages");
    private static final Logger LOGGER = LogManager.getLogger(GetBestPrediction.class);

    @GlobalSessionContext
    private com.automationanywhere.bot.service.GlobalSessionContext globalSessionContext;

    public void setGlobalSessionContext(com.automationanywhere.bot.service.GlobalSessionContext globalSessionContext) {
        this.globalSessionContext = globalSessionContext;
    }

    //Identify the entry point for the action. Returns a Value<String> because the return type is String.
    @Execute
    public ListValue<?> action(
            @Idx(index = "1", type = TEXT) @Pkg(label = "Session name", default_value_type = STRING,  default_value = "Default") @NotEmpty String sessionName,
            @Idx(index = "2", type = TEXT) @Pkg(label = "DataRobot Key") @NotEmpty String DRKey,
            @Idx(index = "3", type = TEXT) @Pkg(label = "Deployment ID") @NotEmpty String ModelID,
            @Idx(index = "4", type = AttributeType.SELECT, options = {
                    @Idx.Option(index = "4.1", pkg = @Pkg(label = "Multiclass", value = "Multiclass")),
                    @Idx.Option(index = "4.2", pkg = @Pkg(label = "Binary", value = "Binary")),
                    @Idx.Option(index = "4.3", pkg = @Pkg(label = "Binary with Explanations", value = "BinaryExpl"))
            })
            @Pkg(label = "Prediction Type", default_value = "Multiclass", default_value_type = STRING) @NotEmpty String PredictionType,
            @Idx(index = "5", type = AttributeType.FILE) @Pkg(label = "Data Set File (.csv (with headers) or .json)", default_value_type = DataType.FILE) @NotEmpty String InputFile
            ) throws IOException, ParseException {

        //Internal Checks on inputs
        if ("".equals(InputFile.trim())) { throw new BotCommandException(MESSAGES.getString("emptyInputString", "Input File"));}
        if ("".equals(ModelID.trim())) { throw new BotCommandException(MESSAGES.getString("emptyInputString", "Model ID"));}
        if ("".equals(DRKey.trim())) { throw new BotCommandException(MESSAGES.getString("emptyInputString", "DataRobot Key"));}

        File fInputFile = new File(InputFile);
        if(!fInputFile.exists()){ LOGGER.error("Input File Does Not Exist: "+InputFile);throw new BotCommandException(MESSAGES.getString("inputFileAbsent", InputFile));}
        if(fInputFile.isDirectory()){ LOGGER.error("Input File Is a Directory: "+InputFile);throw new BotCommandException(MESSAGES.getString("inputFileIsFolder", InputFile));}
        String JSON = "";
        try{
            JSON = CustomFileUtils.GetFileAsString(fInputFile);
        }catch(IOException e){
            LOGGER.error("Could Not Process File: "+e.getMessage());throw new BotCommandException(MESSAGES.getString("inputFileError", e.getMessage()));
        }

        //Retrieve APIKey String that is passed as Session Object
        DataRobotServer drs = (DataRobotServer) this.sessions.get(sessionName);
        String APIKey = drs.getAPIKey();
        String URL = drs.getBaseURL();

        //Call predictionExplanations endpoint if the prediction type is Binary
        String PostUrl = URL + "/predApi/v1.0/deployments/" + ModelID;
        if(PredictionType!="BinaryExpl"){ PostUrl = PostUrl + "/predictions";}
        else {PostUrl = PostUrl + "/predictionExplanations";}

        LOGGER.info("Post URL: "+PostUrl);
        String response = HTTPRequest.POST(APIKey, PostUrl, JSON, DRKey);
        LOGGER.info("API Response: "+response);
        //System.out.println("API Response: "+response);

        ListValue allBestPredictions = OutputTransform.MultiCastJsonToList(response);

        return allBestPredictions;
        }

    public void setSessions(Map<String, Object> sessions) {
        this.sessions = sessions;
    }
}
