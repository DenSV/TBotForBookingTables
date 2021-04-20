package ru.home.charlieblack_bot.botstate.handlers.bookingHandler.bookingImp;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.home.charlieblack_bot.botstate.handlers.Booking;
import ru.home.charlieblack_bot.botstate.handlers.AbstractBooking;
import ru.home.charlieblack_bot.keyboardbuilders.InlineButtonsBuilder;

import static ru.home.charlieblack_bot.botstate.handlers.BookingCore.*;

public class Book extends AbstractBooking implements Booking {

    public Book(Update update) {
        super(update);
    }

    @Override
    public SendMessage getResponse() {

        return editMessage(update, getReplyMessage(), getKeyboard());

    }

    private InlineKeyboardMarkup getChangeBookingTime(){

        return new InlineButtonsBuilder().getButtons("Отменить бронь",
                                                            "Изменить время",
                                                            "Вернуться на главную");

    }

    @Override
    protected String getReplyMessage() {
        if(tableBookingHistoryCache.hasUserBooked(userId)
                && tableBookingHistoryCache.hasConsidering(userId)){
            return "Ваша бронь на рассмотрении. " +
                    "В ближайшее время администратор с вами свяжется";
        } else if(tableBookingHistoryCache.hasUserBooked(userId)){
            return "За вами уже забронирован столик № "
                    + profileData.getTableNum() + " к "
                    + profileData.getBookingTime() + ". \n"
                    + "Хотите отменить бронирование или выбрать другое время?";
        } else {
            return "Укажите время";
        }
    }

    private InlineKeyboardMarkup getKeyboard(){
        if(tableBookingHistoryCache.hasUserBooked(userId)
                && tableBookingHistoryCache.hasConsidering(userId)){
            return getInlineKeyBoardBackToStartPage();
        } else if(tableBookingHistoryCache.hasUserBooked(userId)){
            return getChangeBookingTime();
        } else {
            return getInlineKeyBoardTimeButtons();
        }
    }
}
