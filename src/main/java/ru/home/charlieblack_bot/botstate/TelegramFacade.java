package ru.home.charlieblack_bot.botstate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.home.charlieblack_bot.cache.UserDataCache;
import ru.home.charlieblack_bot.model.UserProfileData;

@Component
@Slf4j
public class TelegramFacade {
    private BotStateContext botStateContext;
    private UserDataCache userDataCache;
    private BotResponse botResponse;

    public TelegramFacade(BotStateContext botStateContext, UserDataCache userDataCache) {
        this.botStateContext = botStateContext;
        this.userDataCache = userDataCache;
        this.botResponse = new BotResponse();
    }

    public SendMessage handleUpdate(Update update) {

        return handleInputMessage(update);
    }

    private SendMessage handleInputMessage(Update update) {

        BotStateEnum botState = botResponse.getBotStateEnum(update);
        userDataCache.setUsersCurrentBotState(UserProfileData.getUserIdFromUpdate(update), botState);

        return botStateContext.processInputMessage(botState, update);


    }
}
