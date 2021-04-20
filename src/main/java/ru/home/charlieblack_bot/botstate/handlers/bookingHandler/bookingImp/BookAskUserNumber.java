package ru.home.charlieblack_bot.botstate.handlers.bookingHandler.bookingImp;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.home.charlieblack_bot.botstate.handlers.Booking;
import ru.home.charlieblack_bot.botstate.handlers.AbstractBooking;

import static ru.home.charlieblack_bot.botstate.handlers.BookingCore.*;

public class BookAskUserNumber extends AbstractBooking implements Booking {

    public BookAskUserNumber(Update update) { super(update); }

    @Override
    public SendMessage getResponse() {

        saveUserData();
        saveBookingHistory();

        //Отправка уведомления о бронировании админу
        sendMessageToAdmin(profileData);

        return messagesService.getReplyMessage(userId,  getReplyMessage())
                .setReplyMarkup(getReplyKeyBoardBackToStartPage());

    }

    @Override
    protected void saveBookingHistory() {
        tableBookingHistoryCache.saveWithoutBookingTables(profileData, 150);
    }

}
