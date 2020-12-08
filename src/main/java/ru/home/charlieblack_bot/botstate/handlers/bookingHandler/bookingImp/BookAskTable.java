package ru.home.charlieblack_bot.botstate.handlers.bookingHandler.bookingImp;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.home.charlieblack_bot.botstate.BotStateEnum;
import ru.home.charlieblack_bot.botstate.handlers.Booking;
import ru.home.charlieblack_bot.botstate.handlers.BookingAbstract;
import ru.home.charlieblack_bot.botstate.handlers.BookingCore;

import java.util.ArrayList;
import java.util.List;

public class BookAskTable extends BookingAbstract implements Booking {

    public BookAskTable(Update update) { super(update); }

    @Override
    public SendMessage getResponse() {

        return processResponse(profileData.getName(), profileData.getPhoneNumber());

    }

    private SendMessage processResponse(String userName, String userNumber){

        setTableNumAndCurrentBotState();

        String replyMessage = "";

        if(userName == null) {
            replyMessage = "На какое имя бронировать?";
            return messagesService.getReplyMessage(userId, replyMessage);
        } else if (userNumber == null){
            userDataCache.setUsersCurrentBotState(userId, BotStateEnum.BOOKING_ASK_NUMBER);
            replyMessage = "Введите номер мобильного телефона для связи";
            return messagesService.getReplyMessage(userId, replyMessage).setReplyMarkup(BookingCore.getReplyKeyboardContact());
        } else {

            tableBookingHistoryCache.save(profileData, 60);

            userDataCache.setUsersCurrentBotState(userId, BotStateEnum.getMainBotState(currentBotStateEnum));
            replyMessage =  "Столик забронирован на имя " + profileData.getName() +
                            "\n" + "Телефон для связи: " + profileData.getPhoneNumber() +
                            "\nЖдем вас к " + profileData.getBookingTime();
            return  messagesService.getReplyMessage(userId, replyMessage).setReplyMarkup(getReplyKeyBoardMarkup());
        }

    }

    private void setTableNumAndCurrentBotState(){

        profileData.setChatId(userId);
        userDataCache.setUsersCurrentBotState(userId, BotStateEnum.getNextBotState(currentBotStateEnum));
        profileData.setTableNum(Integer.parseInt(inputMsg));


    }

    private ReplyKeyboardMarkup getReplyKeyBoardMarkup(){

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Вернуться на главную"));
        keyboard.add(row1);
        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }

}
