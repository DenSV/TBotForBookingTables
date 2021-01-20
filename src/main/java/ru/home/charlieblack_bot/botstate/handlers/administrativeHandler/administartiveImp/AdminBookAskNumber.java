package ru.home.charlieblack_bot.botstate.handlers.administrativeHandler.administartiveImp;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.home.charlieblack_bot.botstate.handlers.Booking;
import ru.home.charlieblack_bot.botstate.handlers.AbstractBooking;
import ru.home.charlieblack_bot.model.UserProfileData;

public class AdminBookAskNumber extends AbstractBooking implements Booking {

    public AdminBookAskNumber(Update update) {
        super(update);
    }

    @Override
    public SendMessage getResponse() {

        UserProfileData userProfileData = userDataCache.getProfileData();
        userProfileData.setName(inputMsg);
        userDataCache.setProfileData(userProfileData);

        return messagesService.getReplyMessage(userId, "Введите телефонный номер");
    }
}
