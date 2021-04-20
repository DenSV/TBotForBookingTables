package ru.home.charlieblack_bot.botstate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.home.charlieblack_bot.keyboardbuilders.InlineButtonsHandler;
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

        long userId = UserProfileData.getUserIdFromUpdate(update);

        UserProfileData profileData = userDataCache.getUserProfileData(userId);


        if(botState.getRole().equals("admin")){
            if(profileData.getUserRole() == null || profileData.getUserRole().equals("user")){
                return new SendMessage(userId, "Данная команда недействительна");
            }
        }

        SendMessage sendMessage = InlineButtonsHandler.getSendMessageFromInlineButtons(update);

        if(sendMessage != null){
            return sendMessage;
        }

        userDataCache.setUsersCurrentBotState(userId, botState);

        return botStateContext.processInputMessage(botState, update);

    }


}
