# DataRobot Integration Package
_ Package to integrate with DataRobot Prediction Deployments_

## Features
 * Get API Key - retrieves a user's API Key that can be used for subsequent actions
 * Get Deployments - retrieves info on model deployments available to a specific user
 * Get Predictions - generates prediction values and prediction result for an uploaded data set in CSV format
    




## FAQ
### How do I use this package?
1. Input user login credentials in action called "Get API Key"
2. use API Key as input to get available model deployments - returned as a Dictionary Variable with URL, datarobot-key, and deployment IDs
3. Input Dictionary Variable values at key "URL", "datarobot-key" and the model name you wish to use, along with the CSV dataset to be used for the predictions.

For additional details on this package, please see the package's Bot Store listing page at: https://botstore.automationanywhere.com/bot/a2019-datarobot-integration/
