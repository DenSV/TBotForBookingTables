package ru.home.charlieblack_bot.botstate;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


public interface InputMessageHandler {
    SendMessage handle(Update update);

    BotStateEnum getHandlerName();
}
