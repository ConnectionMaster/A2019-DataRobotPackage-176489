package com.automationanywhere.commands.DataRobotIntegration;

import com.automationanywhere.bot.service.GlobalSessionContext;
import com.automationanywhere.botcommand.BotCommand;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.i18n.Messages;
import com.automationanywhere.commandsdk.i18n.MessagesFactory;
import java.lang.ClassCastException;
import java.lang.Deprecated;
import java.lang.Object;
import java.lang.String;
import java.lang.Throwable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class GetBestPredictionCommand implements BotCommand {
  private static final Logger logger = LogManager.getLogger(GetBestPredictionCommand.class);

  private static final Messages MESSAGES_GENERIC = MessagesFactory.getMessages("com.automationanywhere.commandsdk.generic.messages");

  @Deprecated
  public Optional<Value> execute(Map<String, Value> parameters, Map<String, Object> sessionMap) {
    return execute(null, parameters, sessionMap);
  }

  public Optional<Value> execute(GlobalSessionContext globalSessionContext,
      Map<String, Value> parameters, Map<String, Object> sessionMap) {
    logger.traceEntry(() -> parameters != null ? parameters.toString() : null, ()-> sessionMap != null ?sessionMap.toString() : null);
    GetBestPrediction command = new GetBestPrediction();
    HashMap<String, Object> convertedParameters = new HashMap<String, Object>();
    if(parameters.containsKey("sessionName") && parameters.get("sessionName") != null && parameters.get("sessionName").get() != null) {
      convertedParameters.put("sessionName", parameters.get("sessionName").get());
      if(!(convertedParameters.get("sessionName") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","sessionName", "String", parameters.get("sessionName").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("sessionName") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","sessionName"));
    }

    if(parameters.containsKey("DRKey") && parameters.get("DRKey") != null && parameters.get("DRKey").get() != null) {
      convertedParameters.put("DRKey", parameters.get("DRKey").get());
      if(!(convertedParameters.get("DRKey") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","DRKey", "String", parameters.get("DRKey").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("DRKey") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","DRKey"));
    }

    if(parameters.containsKey("ModelID") && parameters.get("ModelID") != null && parameters.get("ModelID").get() != null) {
      convertedParameters.put("ModelID", parameters.get("ModelID").get());
      if(!(convertedParameters.get("ModelID") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","ModelID", "String", parameters.get("ModelID").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("ModelID") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","ModelID"));
    }

    if(parameters.containsKey("PredictionType") && parameters.get("PredictionType") != null && parameters.get("PredictionType").get() != null) {
      convertedParameters.put("PredictionType", parameters.get("PredictionType").get());
      if(!(convertedParameters.get("PredictionType") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","PredictionType", "String", parameters.get("PredictionType").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("PredictionType") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","PredictionType"));
    }
    if(convertedParameters.get("PredictionType") != null) {
      switch((String)convertedParameters.get("PredictionType")) {
        case "Multiclass" : {

        } break;
        case "Binary" : {

        } break;
        case "BinaryExpl" : {

        } break;
        default : throw new BotCommandException(MESSAGES_GENERIC.getString("generic.InvalidOption","PredictionType"));
      }
    }

    if(parameters.containsKey("InputFile") && parameters.get("InputFile") != null && parameters.get("InputFile").get() != null) {
      convertedParameters.put("InputFile", parameters.get("InputFile").get());
      if(!(convertedParameters.get("InputFile") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","InputFile", "String", parameters.get("InputFile").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("InputFile") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","InputFile"));
    }

    command.setSessions(sessionMap);
    command.setGlobalSessionContext(globalSessionContext);
    try {
      Optional<Value> result =  Optional.ofNullable(command.action((String)convertedParameters.get("sessionName"),(String)convertedParameters.get("DRKey"),(String)convertedParameters.get("ModelID"),(String)convertedParameters.get("PredictionType"),(String)convertedParameters.get("InputFile")));
      return logger.traceExit(result);
    }
    catch (ClassCastException e) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.IllegalParameters","action"));
    }
    catch (BotCommandException e) {
      logger.fatal(e.getMessage(),e);
      throw e;
    }
    catch (Throwable e) {
      logger.fatal(e.getMessage(),e);
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.NotBotCommandException",e.getMessage()),e);
    }
  }
}
