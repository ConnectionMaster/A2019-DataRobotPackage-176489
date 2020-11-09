package com.automationanywhere.commands.tests;

import Utils.DataRobotServer;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.DictionaryValue;
import com.automationanywhere.botcommand.data.impl.ListValue;
import com.automationanywhere.commands.DataRobotIntegration.GetBestPrediction;
import com.automationanywhere.commands.DataRobotIntegration.GetDeployments;
import com.automationanywhere.commands.DataRobotIntegration.StartSession;
import com.automationanywhere.core.security.SecureString;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetDeploymentsTests {

    public static void aTest() throws IOException, ParseException {

        //StartSession session = new StartSession();

        String APIKey = "NWYyOTYwMzBkNmUxM2YwZmI2ZDEyZDNjOnFlNVUxd0ZGN3dqSERCb1U3eE4vL2cxdFJLYlUrSnFhWWVReFBxcWtMU1E9";
        //char[] chAPIKey = new char[APIKey.length()];
       // for (int i = 0; i < APIKey.length(); i++) { chAPIKey[i] = APIKey.charAt(i); }
       // SecureString sAPIKey = new SecureString(chAPIKey);

       // char[] password = new char[0];
        //SecureString sPassword = new SecureString(password);

        //session.action("Default","",sPassword,sAPIKey);

        Map<String, Object> mockSession = new HashMap<String,Object>();
        DataRobotServer drs = new DataRobotServer("https://automationanywhere.orm.datarobot.com","https://app.datarobot.com",APIKey);
        mockSession.put("Default",drs);


        //Map<String,Object> mockSession = new HashMap<String,Object>();
        //mockSession.put("Default",APIKey);

        GetDeployments pred = new GetDeployments();
        pred.setSessions(mockSession);

        ListValue ListV = pred.action("Default");

        List<Value> allValues = ListV.get();
        for(Value v : allValues){
            DictionaryValue dv = (DictionaryValue) v;

            String v1 = (String) dv.get("modelLabel").get();
            String v2 = (String) dv.get("modelId").get();
            String v3 = (String) dv.get("modelArchType").get();
            String v4 = (String) dv.get("modelType").get();
            String v5 = (String) dv.get("projectId").get();
            String v6 = (String) dv.get("projectName").get();
            String v7 = (String) dv.get("modelStatus").get();
            String v8 = (String) dv.get("modelUrl").get();
            String v9 = (String) dv.get("modelServerId").get();
            String v10 = (String) dv.get("modelDatarobotKey").get();
            System.out.println(v1+"|"+v2+"|"+v3+"|"+v4+"|"+v5+"|"+v6+"|"+v7+"|"+v8+"|"+v9+"|"+v10);
        }

    }

    public static void main(String[] args) throws IOException, ParseException {
        aTest();
        //aTest("C:\\iqbot\\DataRobot\\data_err.csv");
    }
}
