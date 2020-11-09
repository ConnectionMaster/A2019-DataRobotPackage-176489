package com.automationanywhere.commands.DataRobotIntegration;

import com.automationanywhere.bot.service.GlobalSessionContext;
import com.automationanywhere.botcommand.BotCommand;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.i18n.Messages;
import com.automationanywhere.commandsdk.i18n.MessagesFactory;
import com.automationanywhere.core.security.SecureString;
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

public final class StartSessionCommand implements BotCommand {
  private static final Logger logger = LogManager.getLogger(StartSessionCommand.class);

  private static final Messages MESSAGES_GENERIC = MessagesFactory.getMessages("com.automationanywhere.commandsdk.generic.messages");

  @Deprecated
  public Optional<Value> execute(Map<String, Value> parameters, Map<String, Object> sessionMap) {
    return execute(null, parameters, sessionMap);
  }

  public Optional<Value> execute(GlobalSessionContext globalSessionContext,
      Map<String, Value> parameters, Map<String, Object> sessionMap) {
    logger.traceEntry(() -> parameters != null ? parameters.toString() : null, ()-> sessionMap != null ?sessionMap.toString() : null);
    StartSession command = new StartSession();
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

    if(parameters.containsKey("InferenceURL") && parameters.get("InferenceURL") != null && parameters.get("InferenceURL").get() != null) {
      convertedParameters.put("InferenceURL", parameters.get("InferenceURL").get());
      if(!(convertedParameters.get("InferenceURL") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","InferenceURL", "String", parameters.get("InferenceURL").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("InferenceURL") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","InferenceURL"));
    }

    if(parameters.containsKey("AccountBaseURL") && parameters.get("AccountBaseURL") != null && parameters.get("AccountBaseURL").get() != null) {
      convertedParameters.put("AccountBaseURL", parameters.get("AccountBaseURL").get());
      if(!(convertedParameters.get("AccountBaseURL") instanceof String)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","AccountBaseURL", "String", parameters.get("AccountBaseURL").get().getClass().getSimpleName()));
      }
    }
    if(convertedParameters.get("AccountBaseURL") == null) {
      throw new BotCommandException(MESSAGES_GENERIC.getString("generic.validation.notEmpty","AccountBaseURL"));
    }

    if(parameters.containsKey("APIKeyInput") && parameters.get("APIKeyInput") != null && parameters.get("APIKeyInput").get() != null) {
      convertedParameters.put("APIKeyInput", parameters.get("APIKeyInput").get());
      if(!(convertedParameters.get("APIKeyInput") instanceof SecureString)) {
        throw new BotCommandException(MESSAGES_GENERIC.getString("generic.UnexpectedTypeReceived","APIKeyInput", "SecureString", parameters.get("APIKeyInput").get().getClass().getSimpleName()));
      }
    }

    command.setSessions(sessionMap);
    command.setGlobalSessionContext(globalSessionContext);
    try {
      command.action((String)convertedParameters.get("sessionName"),(String)convertedParameters.get("InferenceURL"),(String)convertedParameters.get("AccountBaseURL"),(SecureString)convertedParameters.get("APIKeyInput"));Optional<Value> result = Optional.empty();
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
