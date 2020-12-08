package ru.home.charlieblack_bot.botstate.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.home.charlieblack_bot.botstate.BotStateEnum;
import ru.home.charlieblack_bot.botstate.InputMessageHandler;
import ru.home.charlieblack_bot.cache.UserDataCache;
import ru.home.charlieblack_bot.model.UserProfileData;
import ru.home.charlieblack_bot.service.ReplyMessagesService;
import ru.home.charlieblack_bot.service.UsersProfileDataService;

@Slf4j
@Component
public class FillingProfileHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessagesService messagesService;
    private UsersProfileDataService profileDataService;

    public FillingProfileHandler(UserDataCache userDataCache,
                                 ReplyMessagesService messagesService,
                                UsersProfileDataService profileDataService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
        this.profileDataService = profileDataService;
    }

    @Override
    public SendMessage handle(Update update) {
//        if (userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotStateEnum.FILLING_PROFILE)) {
//            userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotStateEnum.ASK_FUEL_CONSUMPTION);
//        }
//        return processUsersInput(message);
        return null;
    }

    @Override
    public BotStateEnum getHandlerName() {
        return BotStateEnum.FILLING_PROFILE;
    }

    private SendMessage processUsersInput(Message inputMsg, Update update) {
        String usersAnswer = inputMsg.getText();
        long userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

        UserProfileData profileData = userDataCache.getUserProfileData(userId);
        BotStateEnum botState = userDataCache.getUsersCurrentBotState(userId);

        SendMessage replyToUser = null;

        /*
        if (botState.equals(BotStateEnum.ASK_FUEL_CONSUMPTION)) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.askPriceForGas");
            //profileData.setPriceForGas(usersAnswer);
            userDataCache.setUsersCurrentBotState(userId, BotStateEnum.ASK_PRICE_FOR_GAS);
        }

        if (botState.equals(BotStateEnum.ASK_PRICE_FOR_GAS)) {
            profileData.setPriceForGas(usersAnswer);

            replyToUser = messagesService.getReplyMessage(chatId, "reply.askFuelConsumption");
            userDataCache.setUsersCurrentBotState(userId, BotStateEnum.PROFILE_FILLED);
        }

        if (botState.equals(BotStateEnum.PROFILE_FILLED)) {
            profileData.setFuelConsumption(usersAnswer);
            profileData.setChatId(chatId);
            final UserProfileData profileDataFromMongo = profileDataService.getUserDescription(chatId);
            if (profileDataFromMongo == null){
                profileDataService.saveUserDescription(profileData);
            } else {
                profileDataFromMongo.copy(profileData);
                profileDataService.saveUserDescription(profileDataFromMongo);
            }

            userDataCache.setUsersCurrentBotState(userId, BotStateEnum.GET_DEPATURE_POINT);
            replyToUser = new SendMessage(chatId, String.format("%s %s", "Данные по вашей анкете", profileData));
        }

        userDataCache.saveUserProfileData(userId, profileData);

        return replyToUser;

         */
        return null;
    }


}
