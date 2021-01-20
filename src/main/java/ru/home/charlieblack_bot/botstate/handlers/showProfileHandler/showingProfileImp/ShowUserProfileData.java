package ru.home.charlieblack_bot.botstate.handlers.showProfileHandler.showingProfileImp;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.home.charlieblack_bot.botstate.handlers.Booking;
import ru.home.charlieblack_bot.botstate.handlers.AbstractBooking;

import static ru.home.charlieblack_bot.botstate.handlers.BookingCore.getReplyKeyBoardChangeUserInfo;

public class ShowUserProfileData extends AbstractBooking implements Booking {

    private String replyMessage;

    public ShowUserProfileData(Update update) {
        super(update);
    }

    @Override
    public SendMessage getResponse() {

        return getReplyMessage(userDataCache.isUserExist(userId));

    }

    private SendMessage getReplyMessage(boolean isUserExist){

        if(isUserExist){
            replyMessage = profileData.toString();
        } else {
            replyMessage = "К сожалению данного профиля нет в БД";
        }

        return messagesService.getReplyMessage(userId, replyMessage)
                .setReplyMarkup(getReplyKeyBoardChangeUserInfo());

    }
}
