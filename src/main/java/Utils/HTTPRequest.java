package Utils;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commands.DataRobotIntegration.GetBestPrediction;
import com.automationanywhere.commandsdk.i18n.Messages;
import com.automationanywhere.commandsdk.i18n.MessagesFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HTTPRequest {

    private static final Messages MESSAGES = MessagesFactory.getMessages("com.automationanywhere.botcommand.demo.messages");
    private static final Logger LOGGER = LogManager.getLogger(GetBestPrediction.class);

    public static String GET(String APIKey, String url) throws IOException {
        URL urlForGET = new URL(url);
        String auth = "Bearer " + APIKey;
        String readLine;
        String output="";
        HttpURLConnection connection = (HttpURLConnection) urlForGET.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", auth);
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            StringBuffer response = new StringBuffer();
            while ((readLine = in.readLine()) != null) {
                response.append(readLine);
            }
            in.close();
            output = response.toString();
            //return null;
        }
        return output;
    }

    public static String POST(String APIKey, String url, String body, String DRKey) throws IOException {
        String auth = "Bearer " + APIKey;
        URL urlForGetRequest = new URL(url);
        String readLine;
        String output="";
        HttpURLConnection connection = (HttpURLConnection) urlForGetRequest.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", auth);
        connection.setRequestProperty("datarobot-key", DRKey);
        //Include JSON Body in POST request
        connection.setDoOutput(true);
        OutputStream outStream = connection.getOutputStream();
        OutputStreamWriter outStreamWriter = new OutputStreamWriter(outStream, StandardCharsets.UTF_8);
        outStreamWriter.write(body);
        outStreamWriter.flush();
        outStreamWriter.close();
        outStream.close();
        connection.connect();
        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            StringBuffer response = new StringBuffer();
            while ((readLine = in.readLine()) != null) {
                response.append(readLine);
            }
            in.close();
            output = response.toString();
        }else{
            throw new BotCommandException(MESSAGES.getString("APIError", "Code: "+responseCode+" - Message: "+connection.getResponseMessage()));
        }
        return output;
    }
}
