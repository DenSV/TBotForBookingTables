package ru.home.charlieblack_bot.botstate.handlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.home.charlieblack_bot.botstate.BotStateEnum;
import ru.home.charlieblack_bot.botstate.handlers.bookingHandler.bookingImp.*;
import ru.home.charlieblack_bot.botstate.handlers.showProfileHandler.showingProfileImp.*;

import java.util.LinkedHashMap;
import java.util.Map;

public class BookingMapping {
    private Map<BotStateEnum, String> map = new LinkedHashMap<>();
    private Update update;

    public BookingMapping(Update update) {

        this.update = update;

        map.put(BotStateEnum.BOOKING_BOOK,              Book.class.getName());
        map.put(BotStateEnum.BOOKING_ASK_TIME,          BookAskTime.class.getName());
        map.put(BotStateEnum.BOOKING_ASK_PERSON_COUNT,  BookAskPersonCount.class.getName());
        map.put(BotStateEnum.BOOKING_ASK_TABLE,         BookAskTable.class.getName());
        map.put(BotStateEnum.BOOKING_ASK_NAME,          BookAskName.class.getName());
        map.put(BotStateEnum.BOOKING_ASK_NUMBER,        BookAskUserNumber.class.getName());

        map.put(BotStateEnum.ASK_USER_NAME,         AskUserName.class.getName());
        map.put(BotStateEnum.CHANGE_USER_NAME,      ChangeUserName.class.getName());
        map.put(BotStateEnum.ASK_USER_NUM,          AskUserNumber.class.getName());
        map.put(BotStateEnum.CHANGE_USER_NUM,       ChangeUserNumber.class.getName());
        map.put(BotStateEnum.SHOW_USER_PROFILEDATA, ShowUserProfileData.class.getName());

    }

    public SendMessage runBooking(BotStateEnum botStateEnum) {
            try {

                Booking o = (Booking) Class.forName(map.get(botStateEnum))
                                            .getConstructor(Update.class)
                                            .newInstance(update);

                return o.getResponse();
            } catch (ReflectiveOperationException e){
                e.printStackTrace();
                return null;
            }

    }

}
