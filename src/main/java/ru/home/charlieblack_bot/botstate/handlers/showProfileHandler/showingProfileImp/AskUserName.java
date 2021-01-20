package ru.home.charlieblack_bot.botstate.handlers.showProfileHandler.showingProfileImp;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.home.charlieblack_bot.botstate.BotStateEnum;
import ru.home.charlieblack_bot.botstate.handlers.Booking;
import ru.home.charlieblack_bot.botstate.handlers.AbstractBooking;

public class AskUserName extends AbstractBooking implements Booking {


    public AskUserName(Update update) {
        super(update);
    }

    @Override
    public SendMessage getResponse() {
        userDataCache.setUsersCurrentBotState(userId, BotStateEnum.CHANGE_USER_NAME);
        return messagesService.getReplyMessage(userId, "Введите имя");
    }
}
