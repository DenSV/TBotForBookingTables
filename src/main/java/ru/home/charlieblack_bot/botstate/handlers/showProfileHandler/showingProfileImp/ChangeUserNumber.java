package ru.home.charlieblack_bot.botstate.handlers.showProfileHandler.showingProfileImp;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.home.charlieblack_bot.botstate.BotStateEnum;
import ru.home.charlieblack_bot.botstate.handlers.BookingCore;
import ru.home.charlieblack_bot.botstate.handlers.Booking;
import ru.home.charlieblack_bot.botstate.handlers.BookingAbstract;

public class ChangeUserNumber extends BookingAbstract implements Booking {

    public ChangeUserNumber(Update update) {
        super(update);
    }

    @Override
    public SendMessage getResponse() {
        profileData.setPhoneNumber(inputMsg);
        userDataCache.setUsersCurrentBotState(userId, BotStateEnum.SHOW_USER_PROFILEDATA);
        userDataCache.saveUserProfileData(userId, profileData);
        return messagesService.getReplyMessage(userId, profileData.toString())
                .setReplyMarkup(BookingCore.getReplyKeyBoardChangeUserInfo());
    }
}
