package ru.home.charlieblack_bot.botstate.handlers.showProfileHandler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.home.charlieblack_bot.botstate.handlers.AbstractHandler;
import ru.home.charlieblack_bot.botstate.BotStateEnum;
import ru.home.charlieblack_bot.botstate.InputMessageHandler;
import ru.home.charlieblack_bot.cache.UserDataCache;

/**
 * Показывает информацию о пользователе
 */

@Component
public class ShowProfileHandler extends AbstractHandler implements InputMessageHandler {

    public ShowProfileHandler(UserDataCache userDataCache) { super(userDataCache); }

    @Override
    public SendMessage handle(Update update) { return super.handle(update); }

    @Override
    public BotStateEnum getHandlerName() { return BotStateEnum.SHOW_USER_PROFILEDATA; }

}
