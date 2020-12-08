package ru.home.charlieblack_bot.botstate.handlers.showProfileHandler.showingProfileImp;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.home.charlieblack_bot.botstate.handlers.BookingCore;
import ru.home.charlieblack_bot.botstate.handlers.Booking;
import ru.home.charlieblack_bot.botstate.handlers.BookingAbstract;

public class ShowUserProfileData extends BookingAbstract implements Booking {

    public ShowUserProfileData(Update update) {
        super(update);
    }

    @Override
    public SendMessage getResponse() {

        return getReplyMessage(userDataCache.isUserExist(userId));

    }

    private SendMessage getReplyMessage(boolean isUserExist){
        String replyMessage = "";

        if(isUserExist){
            replyMessage = profileData.toString();
        } else {
            replyMessage = "К сожалению данного профиля нет в БД";
        }

        return messagesService.getReplyMessage(userId, replyMessage).setReplyMarkup(BookingCore.getReplyKeyBoardChangeUserInfo());

    }
}
