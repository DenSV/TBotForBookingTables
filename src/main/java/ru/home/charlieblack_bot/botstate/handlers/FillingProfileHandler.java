package ru.home.charlieblack_bot.botstate.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.home.charlieblack_bot.botstate.BotStateEnum;
import ru.home.charlieblack_bot.botstate.InputMessageHandler;

@Slf4j
@Component
public class FillingProfileHandler implements InputMessageHandler {

    @Override
    public SendMessage handle(Update update) {return null; }

    @Override
    public BotStateEnum getHandlerName() {
        return BotStateEnum.FILLING_PROFILE;
    }

}
