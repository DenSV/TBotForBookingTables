package ru.home.charlieblack_bot.botstate.handlers.showProfileHandler.showingProfileImp;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.home.charlieblack_bot.botstate.BotStateEnum;
import ru.home.charlieblack_bot.botstate.handlers.BookingCore;
import ru.home.charlieblack_bot.botstate.handlers.Booking;
import ru.home.charlieblack_bot.botstate.handlers.BookingAbstract;

public class AskUserNumber extends BookingAbstract implements Booking {

    public AskUserNumber(Update update) {
        super(update);
    }

    @Override
    public SendMessage getResponse() {
        userDataCache.setUsersCurrentBotState(userId, BotStateEnum.CHANGE_USER_NUM);
        return messagesService.getReplyMessage(userId, "Введите ваш телефонный номер")
                .setReplyMarkup(BookingCore.getReplyKeyboardContact());

    }
}
