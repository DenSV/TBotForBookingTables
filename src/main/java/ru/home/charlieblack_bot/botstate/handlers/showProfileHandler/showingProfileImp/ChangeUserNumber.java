package ru.home.charlieblack_bot.botstate.handlers.showProfileHandler.showingProfileImp;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.home.charlieblack_bot.botstate.handlers.Booking;
import ru.home.charlieblack_bot.botstate.handlers.AbstractBooking;

import static ru.home.charlieblack_bot.botstate.handlers.BookingCore.getReplyKeyBoardChangeUserInfo;

public class ChangeUserNumber extends AbstractBooking implements Booking {

    public ChangeUserNumber(Update update) {
        super(update);
    }

    @Override
    public SendMessage getResponse() {
        userDataCache.setUsersCurrentBotState(userId, currentBotStateEnum.getMainBotState());
        userDataCache.saveUserProfileData(userId, profileData);
        return messagesService.getReplyMessage(userId, profileData.toString())
                .setReplyMarkup(getReplyKeyBoardChangeUserInfo());
    }
}
