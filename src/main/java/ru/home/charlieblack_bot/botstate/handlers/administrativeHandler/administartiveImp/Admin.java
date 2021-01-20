package ru.home.charlieblack_bot.botstate.handlers.administrativeHandler.administartiveImp;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.home.charlieblack_bot.botstate.handlers.Booking;
import ru.home.charlieblack_bot.botstate.handlers.AbstractBooking;

import java.util.ArrayList;
import java.util.List;

public class Admin extends AbstractBooking implements Booking {

    public Admin(Update update) {
        super(update);
    }

    @Override
    public SendMessage getResponse() {

        return messagesService.getReplyMessage(userId, "Возможности администрирования")
                .setReplyMarkup(getReplyKeyboardMarkup());
    }

    private ReplyKeyboardMarkup getReplyKeyboardMarkup(){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);


        List<KeyboardRow> rowList = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Карта бронирования"));
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("Показать историю бронирования"));
        KeyboardRow row3 = new KeyboardRow();
        row3.add(new KeyboardButton("Администраторы"));
        KeyboardRow row4 = new KeyboardRow();
        row4.add(new KeyboardButton("Вернуться на главную"));
        rowList.add(row1);
        rowList.add(row2);
        rowList.add(row3);
        rowList.add(row4);

        return replyKeyboardMarkup.setKeyboard(rowList);

    }

}
