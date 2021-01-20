package ru.home.charlieblack_bot.botstate.handlers.administrativeHandler.administartiveImp;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.home.charlieblack_bot.botstate.BotStateEnum;
import ru.home.charlieblack_bot.botstate.handlers.Booking;
import ru.home.charlieblack_bot.botstate.handlers.AbstractBooking;

import static ru.home.charlieblack_bot.botstate.handlers.BookingCore.getReplyKeyBoardBackToStartPage;

public class AdminAskContact extends AbstractBooking implements Booking {

    public AdminAskContact(Update update) {
        super(update);
    }

    @Override
    public SendMessage getResponse() {
        userDataCache.setUsersCurrentBotState(userId, BotStateEnum.ADMIN_ADD);

        return messagesService.getReplyMessage(userId, "Пришлите контакт")
                .setReplyMarkup(getReplyKeyBoardBackToStartPage());
    }
}
