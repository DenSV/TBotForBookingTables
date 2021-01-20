package ru.home.charlieblack_bot.botstate.handlers.bookingHandler.bookingImp;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.home.charlieblack_bot.botstate.handlers.Booking;
import ru.home.charlieblack_bot.botstate.handlers.AbstractBooking;

import java.util.ArrayList;
import java.util.List;

import static ru.home.charlieblack_bot.botstate.handlers.BookingCore.getInlineKeyBoardTimeButtons;
import static ru.home.charlieblack_bot.botstate.handlers.BookingCore.getReplyKeyBoardBackToStartPage;

public class Book extends AbstractBooking implements Booking {

    public Book(Update update) {
        super(update);
    }

    @Override
    public SendMessage getResponse() {

        //Если столик забронирован, то предложить отменить бронь или выбрать другое время
        if(tableBookingHistoryCache.hasUserBooked(userId)){

            if(tableBookingHistoryCache.hasConsidering(userId)){
                return messagesService.getReplyMessage(userId, "Ваша бронь на рассмотрении. " +
                        "В ближайшее время администратор с вами свяжется")
                        .setReplyMarkup(getReplyKeyBoardBackToStartPage());
            }

            return messagesService.getReplyMessage(userId, "За вами уже забронирован столик № "
                    + profileData.getTableNum() + " к "
                    + profileData.getBookingTime() + ". \n"
                    + "Хотите отменить бронирование или выбрать другое время?")
                    .setReplyMarkup(getChangeBookingTime());

        } else {

            return messagesService.getReplyMessage(userId, "Укажите время")
                    .setReplyMarkup(getInlineKeyBoardTimeButtons());
        }
    }

    private InlineKeyboardMarkup getChangeBookingTime(){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        List<InlineKeyboardButton> inlineKeyboardButtonRow1 = new ArrayList<>();
        inlineKeyboardButtonRow1.add(new InlineKeyboardButton()
                .setText("Отменить бронь")
                .setCallbackData("Отменить бронь"));

        List<InlineKeyboardButton> inlineKeyboardButtonRow2 = new ArrayList<>();
        inlineKeyboardButtonRow2.add(new InlineKeyboardButton()
                .setText("Изменить время")
                .setCallbackData("Изменить время"));

        rowList.add(inlineKeyboardButtonRow1);
        rowList.add(inlineKeyboardButtonRow2);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;

    }

}
