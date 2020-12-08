package ru.home.charlieblack_bot.botstate;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BotStateContext {
    private Map<BotStateEnum, InputMessageHandler> messageHandlers = new HashMap<>();

    public BotStateContext(List<InputMessageHandler> messageHandlers) {
        messageHandlers.forEach(handler -> this.messageHandlers.put(handler.getHandlerName(), handler));
    }

    public SendMessage processInputMessage(BotStateEnum currentState, Update update) {
        InputMessageHandler currentMessageHandler = findMessageHandler(currentState);
        return currentMessageHandler.handle(update);
    }

    private InputMessageHandler findMessageHandler(BotStateEnum currentState) {
        return messageHandlers.get(isFillingProfileState(currentState));
    }

    private BotStateEnum isFillingProfileState(BotStateEnum currentState) {
        return BotStateEnum.getMainBotState(currentState);
    }


}
