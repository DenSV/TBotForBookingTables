package ru.home.charlieblack_bot.botstate.handlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.home.charlieblack_bot.cache.UserDataCache;
import ru.home.charlieblack_bot.model.UserProfileData;

public abstract class AbstractHandler {

    protected UserDataCache userDataCache;

    public AbstractHandler(UserDataCache userDataCache) {
        this.userDataCache = userDataCache;
    }

    protected SendMessage handle(Update update){

        long userId = UserProfileData.getUserIdFromUpdate(update);

        try {

            SendMessage replyToUser;

            replyToUser = new BookingMapping(update).runBooking(userDataCache.getUsersCurrentBotState(userId));

            userDataCache.saveUserProfileData(userId, userDataCache.getUserProfileData(userId));

            return replyToUser;

        } catch (Exception e){
            e.printStackTrace();
            return new SendMessage(userId, e.toString());
        }

    }
}
