package com.automationanywhere.commands.tests;

import Utils.DataRobotServer;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.DictionaryValue;
import com.automationanywhere.botcommand.data.impl.ListValue;
import com.automationanywhere.botcommand.data.impl.TableValue;
import com.automationanywhere.commands.DataRobotIntegration.GetBestPrediction;
import com.automationanywhere.commands.DataRobotIntegration.GetPredictions;
import com.automationanywhere.commands.DataRobotIntegration.StartSession;
import com.automationanywhere.core.security.SecureString;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class GetBestPredictionTests {

    public static void aTest(String InputFilePath) throws IOException, ParseException {

        StartSession session = new StartSession();

        String APIKey = "NWYyOTYwMzBkNmUxM2YwZmI2ZDEyZDNjOnFlNVUxd0ZGN3dqSERCb1U3eE4vL2cxdFJLYlUrSnFhWWVReFBxcWtMU1E9";
       // char[] chAPIKey = new char[APIKey.length()];
        //for (int i = 0; i < APIKey.length(); i++) { chAPIKey[i] = APIKey.charAt(i); }
        //SecureString sAPIKey = new SecureString(chAPIKey);

       // char[] password = new char[0];
        //SecureString sPassword = new SecureString(password);

        //session.action("Default","https://automationanywhere.orm.datarobot.com",sAPIKey);

        Map<String, Object> mockSession = new HashMap<String,Object>();
        DataRobotServer drs = new DataRobotServer("https://automationanywhere.orm.datarobot.com","https://app.datarobot.com",APIKey);
        mockSession.put("Default",drs);

        GetBestPrediction pred = new GetBestPrediction();
        pred.setSessions(mockSession);

        ListValue ListV = pred.action("Default","8daeefc6-bb42-c288-a5cd-e3127ecb7e85","5f295fbbce243f1116ec5d9f","Multiclass",InputFilePath);

        List<Value> allValues = ListV.get();
        for(Value v : allValues){
            DictionaryValue dv = (DictionaryValue) v;
            String RowID = (String) dv.get("rowId").get();
            String Label = (String) dv.get("label").get();
            String Score = (String) dv.get("score").get();
            System.out.println(RowID+"|"+Label+"|"+Score);
        }

    }

    public static void main(String[] args) throws IOException, ParseException {
        aTest("C:\\iqbot\\DataRobot\\data.csv");
        //aTest("C:\\iqbot\\DataRobot\\data_err.csv");
    }
}
