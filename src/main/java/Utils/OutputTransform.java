package Utils;

import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.DictionaryValue;
import com.automationanywhere.botcommand.data.impl.ListValue;
import com.automationanywhere.botcommand.data.impl.StringValue;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OutputTransform {

    public static Double NormalizeScore(Double Score){
        return Score*100;
    }


    public static ListValue GetDeploymentModelsInfoToList(String JsonResponse) throws ParseException {
        Object obj = new JSONParser().parse(JsonResponse);
        JSONObject jsonObj = (JSONObject) obj;
        JSONArray dataArray = (JSONArray)jsonObj.get("data");

        ListValue classificationOutput = new ListValue<>();
        List<DictionaryValue> pageClassificationResults = new ArrayList<>();

        for(int i=0;i<dataArray.size();i++){

            JSONObject ModelInfo = (JSONObject) dataArray.get(i);

            JSONObject ModelPredServer = (JSONObject) ModelInfo.get("defaultPredictionServer");
            JSONObject ModelOwners = (JSONObject) ModelInfo.get("owners");
            JSONObject ModelDetailedInfo = (JSONObject) ModelInfo.get("model");

            String ModelLabel = (String) ModelInfo.get("label");
            String ModelID = (String) ModelInfo.get("id");

            String ModelTargetType = (String) ModelDetailedInfo.get("targetType");
            String ModelType = (String) ModelDetailedInfo.get("type");
            String ModelProjectID = (String) ModelDetailedInfo.get("projectId");
            String ModelProjectName = (String) ModelDetailedInfo.get("projectName");

            String ModelStatus = (String)ModelInfo.get("status");
            String ModelURL = (String) ModelPredServer.get("url");
            String ModelPredSServerID = (String) ModelPredServer.get("id");
            String ModelDRKey = (String) ModelPredServer.get("datarobot-key");

            DictionaryValue dictionaryValue = new DictionaryValue();
            Map<String, Value> ClassificationMap = new LinkedHashMap();
            ClassificationMap.put("modelLabel", new StringValue(ModelLabel));
            ClassificationMap.put("modelId", new StringValue(ModelID));
            ClassificationMap.put("modelArchType", new StringValue(ModelTargetType));
            ClassificationMap.put("modelType", new StringValue(ModelType));
            ClassificationMap.put("projectId", new StringValue(ModelProjectID));
            ClassificationMap.put("projectName", new StringValue(ModelProjectName));
            ClassificationMap.put("modelStatus", new StringValue(ModelStatus));
            ClassificationMap.put("modelUrl", new StringValue(ModelURL));
            ClassificationMap.put("modelServerId", new StringValue(ModelPredSServerID));
            ClassificationMap.put("modelDatarobotKey", new StringValue(ModelDRKey));

            dictionaryValue.set(ClassificationMap);
            pageClassificationResults.add(dictionaryValue);
            //System.out.println("DEBUG:"+MainPrediction+"|"+MainScore);
        }

        classificationOutput.set(pageClassificationResults);
        return classificationOutput;
    }

    public static ListValue MultiCastJsonToList(String JsonResponse) throws ParseException {
        //{"data":[
        // {"predictionValues":[{"value":0.0013888945,"label":"equipment_&_supplies"},{"value":0.002466151,"label":"facilities_access"},{"value":0.8454556624,"label":"server_access"},{"value":0.1506892921,"label":"system_&_database_access"}],"prediction":"server_access","rowId":0},
        // {"predictionValues":[{"value":0.9220922374,"label":"equipment_&_supplies"},{"value":0.0010011223,"label":"facilities_access"},{"value":0.068818435,"label":"server_access"},{"value":0.0080882053,"label":"system_&_database_access"}],"prediction":"equipment_&_supplies","rowId":1}
        // ]}
        Object obj = new JSONParser().parse(JsonResponse);
        JSONObject jsonObj = (JSONObject) obj;
        JSONArray dataArray = (JSONArray)jsonObj.get("data");

        ListValue classificationOutput = new ListValue<>();
        List<DictionaryValue> pageClassificationResults = new ArrayList<>();

        for(int i=0;i<dataArray.size();i++){

            JSONObject Predictions = (JSONObject) dataArray.get(i);
            JSONArray PredictionValues = (JSONArray)Predictions.get("predictionValues"); // [{"value":0.0013888945,"label":"equipment_&_supplies"},{"value":0.002466151,"label":"facilities_access"},{"value":0.8454556624,"label":"server_access"},{"value":0.1506892921,"label":"system_&_database_access"}]
            String MainPrediction = (String)Predictions.get("prediction");
            Long RowID = (Long)Predictions.get("rowId");
            Double MainScore = 0d;

            for(Object j : PredictionValues){
                JSONObject aPrediction = (JSONObject) j;
                Double score = (Double) aPrediction.get("value");
                String label = (String) aPrediction.get("label");
                if(label.equals(MainPrediction)){
                    MainScore = NormalizeScore(score);
                    break;
                }
            }

            DictionaryValue dictionaryValue = new DictionaryValue();
            Map<String, Value> ClassificationMap = new LinkedHashMap();
            ClassificationMap.put("score", new StringValue(Double.toString(MainScore)));
            ClassificationMap.put("label", new StringValue(MainPrediction));
            ClassificationMap.put("rowId", new StringValue(Long.toString(RowID)));

            dictionaryValue.set(ClassificationMap);
            pageClassificationResults.add(dictionaryValue);
            //System.out.println("DEBUG:"+MainPrediction+"|"+MainScore);
        }

        classificationOutput.set(pageClassificationResults);
        return classificationOutput;
    }
}
