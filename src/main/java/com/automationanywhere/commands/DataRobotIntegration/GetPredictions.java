package com.automationanywhere.commands.DataRobotIntegration;
import Utils.HTTPRequest;
import com.automationanywhere.botcommand.data.Value;
import static com.automationanywhere.commandsdk.model.AttributeType.*;
import com.automationanywhere.botcommand.data.impl.AbstractValue;
import com.automationanywhere.botcommand.data.impl.DictionaryValue;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.data.impl.TableValue;
import com.automationanywhere.botcommand.data.model.Schema;
import com.automationanywhere.botcommand.data.model.table.Row;
import com.automationanywhere.botcommand.data.model.table.Table;
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
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.FileReader;
import java.io.File;
import java.io.FileWriter;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.dataformat.csv.*;
import shadow.org.apache.commons.io.FileUtils;
import shadow.org.apache.tools.ant.taskdefs.Input;

import com.automationanywhere.commandsdk.model.DataType;
import com.automationanywhere.core.security.SecureString;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import java.util.ArrayList;
import java.util.List;

import static com.automationanywhere.commandsdk.model.AttributeType.*;
import static com.automationanywhere.commandsdk.model.DataType.STRING;

//BotCommand makes a class eligible for being considered as an action.
//@BotCommand

//CommandPks adds required information to be displayable on GUI.
@CommandPkg(
        //Unique name inside a package and label to display.
        name = "Get Predictions", label = "Get Predictions",
        node_label = "Get Predictions for Data Set", description = "Returns prediction outputs in a table variable for a provided data set", icon = "DRicon.svg",
        comment = true ,  text_color = "#7B848B" , background_color =  "#3196D3",
        //Return type information. return_type ensures only the right kind of variable is provided on the UI.
        return_label = "Assign output to a table variable", return_type = DataType.TABLE, return_required = true)

public class GetPredictions {

    @Sessions
    private Map<String, Object> sessions;

    private static final Messages MESSAGES = MessagesFactory.getMessages("com.automationanywhere.botcommand.demo.messages");

    @com.automationanywhere.commandsdk.annotations.GlobalSessionContext
    private com.automationanywhere.bot.service.GlobalSessionContext globalSessionContext;

    public void setGlobalSessionContext(com.automationanywhere.bot.service.GlobalSessionContext globalSessionContext) {
        this.globalSessionContext = globalSessionContext;
    }

    //Identify the entry point for the action. Returns a Value<String> because the return type is String.
    @Execute
    public TableValue action(
            @Idx(index = "1", type = TEXT) @Pkg(label = "Session name", default_value_type = STRING,  default_value = "Default") @NotEmpty String sessionName,
            @Idx(index = "2", type = AttributeType.TEXT) @Pkg(label = "URL") @NotEmpty String URL,
            @Idx(index = "3", type = AttributeType.TEXT) @Pkg(label = "DataRobot Key") @NotEmpty String DRKey,
            @Idx(index = "4", type = AttributeType.TEXT) @Pkg(label = "Deployment ID") @NotEmpty String ModelID,
            @Idx(index = "5", type = AttributeType.SELECT, options = {
                    @Idx.Option(index = "5.1", pkg = @Pkg(label = "Multiclass", value = "Multiclass")),
                    @Idx.Option(index = "5.2", pkg = @Pkg(label = "Binary", value = "Binary")),
                    @Idx.Option(index = "5.3", pkg = @Pkg(label = "Binary with Explanations", value = "BinaryExpl"))
            })
            @Pkg(label = "Prediction Type", default_value = "Multiclass", default_value_type = DataType.STRING) @NotEmpty String PredictionType,
            @Idx(index = "6", type = AttributeType.FILE) @Pkg(label = "Data Set File (.csv or .json)", default_value_type = DataType.FILE) @NotEmpty String InputFile
            ) throws IOException, ParseException {

        //Internal Checks on inputs
        if ("".equals(InputFile.trim())) { throw new BotCommandException(MESSAGES.getString("emptyInputString", "Input File"));}
        if ("".equals(ModelID.trim())) { throw new BotCommandException(MESSAGES.getString("emptyInputString", "Model ID"));}
        if ("".equals(DRKey.trim())) { throw new BotCommandException(MESSAGES.getString("emptyInputString", "DataRobot Key"));}
        if ("".equals(URL.trim())) { throw new BotCommandException(MESSAGES.getString("emptyInputString", "URL"));}

        //Convert CSV  or JSON File to JSON String
        String JSON = "";
        if(InputFile.contains(".csv")) {
            File input = new File(InputFile);
            CsvSchema csvSchema = CsvSchema.builder().setUseHeader(true).build();
            CsvMapper csvMapper = new CsvMapper();
            List<Object> readAll = csvMapper.readerFor(Map.class).with(csvSchema).readValues(input).readAll();
            ObjectMapper mapper = new ObjectMapper();
            JSON = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(readAll);
        } else if (InputFile.contains(".json")){
            JSON = FileUtils.readFileToString(new File(InputFile), StandardCharsets.UTF_8);
        }

        //Retrieve APIKey String that is passed as Session Object
        String APIKey = (String) this.sessions.get(sessionName);
        String PostUrl;

        //Call predictionExplanations endpoint if the prediction type is Binary
        if(PredictionType!="BinaryExpl"){
        PostUrl = URL + "/predApi/v1.0/deployments/" + ModelID + "/predictions";}
        else {PostUrl = URL + "/predApi/v1.0/deployments/" + ModelID + "/predictionExplanations";}

        //System.out.println("DEBUG:["+PostUrl+"]");
        String response = HTTPRequest.POST(APIKey, PostUrl, JSON, DRKey);
            //Create Table for output
            Table table = new Table();
            //Create List of type "Row" objects - this will be used to store the list of Row objects
            List<Row> tableRows = new ArrayList<Row>();
            //Parse JSON to get JSON array at the key called "data"
        //System.out.println("DEBUG:["+response+"]");
            Object obj = new JSONParser().parse(response);
            JSONObject jsonObj = (JSONObject) obj;
            //Get JSON Array that occurs at key "data"
            JSONArray dataArray = (JSONArray)jsonObj.get("data");
            
            //Get the first JSON object in the array from dataArray - to be used for header values
            JSONObject predData = (JSONObject) dataArray.get(0);
            //Get the JSON Array that exists at key value "predictionValues"
            JSONArray predValues = (JSONArray)predData.get("predictionValues");
            //List of type Value for the headers to be used
            List<Value> headerValues = new ArrayList<>();
            headerValues.add(new StringValue("row ID"));//add "row ID" as the first header
            String label = null;

            //Set headers for first row of table by looping through the
            //predValues array and getting the string at "label" key
            //then add the string at label key as a StringValue to rowValues List
            for (int i=0; i<predValues.size(); i++){
                JSONObject predObj = (JSONObject) predValues.get(i);
                headerValues.add(new StringValue(predObj.get("label").toString()));
            }
            headerValues.add(new StringValue("prediction"));
            //Add column headers for prediction explanations
            if(PredictionType=="BinaryExpl") {
                JSONArray predExpl = (JSONArray)predData.get("predictionExplanations");
                for (int l = 0; l < predExpl.size(); l++) {
                    headerValues.add(new StringValue("Feature Value"));
                    headerValues.add(new StringValue("Strength"));
                    headerValues.add(new StringValue("Feature"));
                    headerValues.add(new StringValue("Qualitative Strength"));
                    headerValues.add(new StringValue("Label"));
                }
            }
            //Create Row object to use for header row
            Row headerRow = new Row();
            //Set header row values with the List of StringValues in rowValues
            headerRow.setValues(headerValues);
            //add Row object headerRow to the List of type Row
            tableRows.add(0,headerRow);

            //Create List of Lists of type Value to use for table of data
            List<List<Value>> predictionData = new ArrayList<List<Value>>();
            String rowID=null;

            for (int i=0; i<dataArray.size(); i++){
                List<Value> rowValues = new ArrayList<>();
                JSONObject dataArrJSON = (JSONObject) dataArray.get(i);
                rowID = dataArrJSON.get("rowId").toString();
                StringValue sRowId = new StringValue (rowID);
                rowValues.add(sRowId);
                JSONArray predValuesIndex = (JSONArray) dataArrJSON.get("predictionValues");
                for (int j = 0; j < predValuesIndex.size(); j++) {
                    JSONObject predElement = (JSONObject) predValuesIndex.get(j);
                    rowValues.add(new StringValue(predElement.get("value").toString()));
                }
                rowValues.add(new StringValue(dataArrJSON.get("prediction").toString()));
                if (PredictionType=="BinaryExpl") {
                    //Loop for Prediction Explanations
                    JSONArray predExplIndex = (JSONArray) dataArrJSON.get("predictionExplanations");
                    for (int k = 0; k < predExplIndex.size(); k++) {
                        JSONObject predExplElement = (JSONObject) predExplIndex.get(k);
                        Object featureValObj = predExplElement.get("featureValue");
                        Object strengthObj = predExplElement.get("strength");
                        Object featureObj = predExplElement.get("feature");
                        Object qualStrengthObj = predExplElement.get("qualitativeStrength");
                        Object labelObj = predExplElement.get("label");

                        if (featureValObj!=null){
                            rowValues.add(new StringValue(featureValObj.toString()));
                        } else{rowValues.add(new StringValue("no value"));}

                        if (strengthObj!=null) {
                            rowValues.add(new StringValue(strengthObj.toString()));
                        }else{rowValues.add(new StringValue("no value"));}

                        if (featureObj!=null) {
                            rowValues.add(new StringValue(featureObj.toString()));
                        }else{rowValues.add(new StringValue("no value"));}

                        if (qualStrengthObj!=null) {
                            rowValues.add(new StringValue(qualStrengthObj.toString()));
                        }else{rowValues.add(new StringValue("no value"));}

                        if (labelObj!=null) {
                            rowValues.add(new StringValue(labelObj.toString()));
                        }else{rowValues.add(new StringValue("no value"));}

                    }
                }
                predictionData.add(rowValues);
            }
            //Now loop through list of lists of type Value, predictionData and set each List as Row object, add to table
            for (int i=0; i<predictionData.size(); i++){
                Row row = new Row();
                row.setValues(predictionData.get(i));
                tableRows.add(i + 1, row);
                table.setRows(tableRows);
            }
            return new TableValue(table);
        }
    public void setSessions(Map<String, Object> sessions) {
        this.sessions = sessions;
    }
}
