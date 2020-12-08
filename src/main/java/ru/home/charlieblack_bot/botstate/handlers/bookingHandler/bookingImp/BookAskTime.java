package ru.home.charlieblack_bot.botstate.handlers.bookingHandler.bookingImp;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.home.charlieblack_bot.botstate.BotStateEnum;
import ru.home.charlieblack_bot.botstate.handlers.Booking;
import ru.home.charlieblack_bot.botstate.handlers.BookingAbstract;

import java.util.ArrayList;
import java.util.List;

public class BookAskTime extends BookingAbstract implements Booking {

    public BookAskTime(Update update) { super(update); }

    @Override
    public SendMessage getResponse() {

        setTimeAndCurrentBotState();

        return messagesService.getReplyMessage(userId, "Сколько будет человек?")
                .setReplyMarkup(getInlineKeyBoard());

    }


    private void setTimeAndCurrentBotState(){
        profileData.setChatId(userId);
        profileData.setBookingTime(inputMsg);
        userDataCache.setUsersCurrentBotState(userId, BotStateEnum.getNextBotState(currentBotStateEnum));
    }

    private InlineKeyboardMarkup getInlineKeyBoard(){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton from1to3 = new InlineKeyboardButton().setText("1-3").setCallbackData("3");
        InlineKeyboardButton from4to6 = new InlineKeyboardButton().setText("4-6").setCallbackData("6");
        InlineKeyboardButton from7to9 = new InlineKeyboardButton().setText("7-9").setCallbackData("9");
        InlineKeyboardButton moreThan10 = new InlineKeyboardButton().setText("больше 9").setCallbackData("10");

        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        keyboardButtonsRow.add(from1to3);
        keyboardButtonsRow.add(from4to6);
        keyboardButtonsRow.add(from7to9);
        keyboardButtonsRow.add(moreThan10);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;

    }
}
