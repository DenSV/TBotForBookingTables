package ru.home.charlieblack_bot.botstate.handlers.bookingHandler.bookingImp;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.home.charlieblack_bot.botstate.BotStateEnum;
import ru.home.charlieblack_bot.botstate.handlers.Booking;
import ru.home.charlieblack_bot.botstate.handlers.BookingAbstract;
import ru.home.charlieblack_bot.botstate.handlers.BookingCore;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Book extends BookingAbstract implements Booking {

    public Book(Update update) {
        super(update);
    }

    @Override
    public SendMessage getResponse() {

        profileData.setChatId(userId);

        userDataCache.saveUserProfileData(userId, profileData);

        if(tableBookingHistoryCache.hasUserBooked(userId)){

            userDataCache.setUsersCurrentBotState(userId, BotStateEnum.BOOKING_BOOK);

            return messagesService.getReplyMessage(userId, "За вами уже забронирован столик № "
                    + profileData.getTableNum() + " к "
                    + profileData.getBookingTime())
                    .setReplyMarkup(BookingCore.getReplyKeyBoardBackToStartPage());

        } else {

            setNextBotState();
            return messagesService.getReplyMessage(userId, "Укажите время").setReplyMarkup(getInlineKeyBoardTimeButtons());
        }
    }

    private void setNextBotState(){

        userDataCache.setUsersCurrentBotState(userId, BotStateEnum.getNextBotState(currentBotStateEnum));

    }

    public static InlineKeyboardMarkup getInlineKeyBoardTimeButtons(){

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>(9);

        LocalTime startTime = LocalTime.of(15, 00);

        for(int i = 0; i < 9; i++){
            List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>(4);
            for (int j = 0; j < 4; j++) {
                inlineKeyboardButtons.add(new InlineKeyboardButton().setText(startTime.toString()).setCallbackData(startTime.toString()));
                startTime = startTime.plusMinutes(15);
            }

            rowList.add(inlineKeyboardButtons);

        }

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;

    }

}
