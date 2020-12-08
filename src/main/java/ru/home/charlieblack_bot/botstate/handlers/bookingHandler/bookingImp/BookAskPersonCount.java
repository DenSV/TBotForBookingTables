package ru.home.charlieblack_bot.botstate.handlers.bookingHandler.bookingImp;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.home.charlieblack_bot.botstate.BotStateEnum;
import ru.home.charlieblack_bot.botstate.handlers.Booking;
import ru.home.charlieblack_bot.botstate.handlers.BookingAbstract;
import ru.home.charlieblack_bot.botstate.handlers.BookingCore;
import ru.home.charlieblack_bot.model.TableInfo;

import java.util.ArrayList;
import java.util.List;

public class BookAskPersonCount extends BookingAbstract implements Booking {

    public BookAskPersonCount(Update update) { super(update); }

    @Override
    public SendMessage getResponse() {

        setPersonCountAndCurrentBotState();

        List<TableInfo> freeTables = BookingCore.getFreeTables(profileData.getBookingTime());

        if(freeTables.isEmpty()){
            userDataCache.setUsersCurrentBotState(userId, BotStateEnum.BOOKING_ASK_TIME);
            return messagesService.getReplyMessage(userId, "К сожалению, все столы на "
                                                                        + profileData.getBookingTime()
                                                                        + " заняты. Выберите другое время").setReplyMarkup(Book.getInlineKeyBoardTimeButtons());
        } else {

            return messagesService.getReplyMessage(userId, "Выберите столик").setReplyMarkup(getInlineMessageButtons(freeTables));

        }
    }

    private void setPersonCountAndCurrentBotState(){
        userDataCache.getUserProfileData(userId).setChatId(userId);
        profileData.setPersonCount(Integer.parseInt(inputMsg));
        userDataCache.setUsersCurrentBotState(userId, BotStateEnum.getNextBotState(currentBotStateEnum));


    }

    private InlineKeyboardMarkup getInlineMessageButtons(List<TableInfo> freeTables) {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();


        for (TableInfo tableInfo: freeTables) {
            List<InlineKeyboardButton> keyboardButtons = new ArrayList<>();

            String tableNum = String.valueOf(tableInfo.getTableNumber());

            keyboardButtons.add(new InlineKeyboardButton().setText(tableNum).setCallbackData(tableNum));

            rowList.add(keyboardButtons);
        }

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}
