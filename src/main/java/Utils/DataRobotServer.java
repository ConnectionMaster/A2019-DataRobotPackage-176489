package Utils;

public class DataRobotServer {
    public String getBaseURL() {
        return BaseURL;
    }

    public String getAccountURL() {
        return AccountURL;
    }

    public String getAPIKey() {
        return APIKey;
    }

    String BaseURL;
    String AccountURL;
    String APIKey;

    public DataRobotServer(String BaseURL, String AccountBaseURL, String APIKey){
        this.APIKey = APIKey;
        this.BaseURL = BaseURL;
        this.AccountURL = AccountBaseURL;
    }
}
