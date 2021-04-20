package ru.home.charlieblack_bot.botstate.handlers.administrativeHandler.administartiveImp;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.home.charlieblack_bot.botstate.BotStateEnum;
import ru.home.charlieblack_bot.botstate.handlers.Booking;
import ru.home.charlieblack_bot.botstate.handlers.AbstractBooking;
import ru.home.charlieblack_bot.keyboardbuilders.InlineButtons;
import ru.home.charlieblack_bot.keyboardbuilders.InlineButtonsBuilder;
import ru.home.charlieblack_bot.model.UserProfileData;

import java.util.ArrayList;
import java.util.List;

import static ru.home.charlieblack_bot.botstate.handlers.BookingCore.editMessage;
import static ru.home.charlieblack_bot.botstate.handlers.administrativeHandler.administartiveImp.AdminShowBookingMapping.getInlineTimesOfTableWithRows;

public class AdminBookAskNumber extends AbstractBooking implements Booking {

    public AdminBookAskNumber(Update update) {
        super(update);
    }

    @Override
    public SendMessage getResponse() {


        if(inputMsg.contains("cancel_book=")){
            setAnotherBotState(BotStateEnum.ADMIN_BOOK_SHOW_BOOKING_MAP);

            int tableNum = Integer.parseInt(inputMsg.split("=")[1]);
            return editMessage(update, "Стол № " + tableNum, getInlineTimesOfTableWithRows(tableNum));
        }

        UserProfileData userProfileData = userDataCache.getProfileData();
        userProfileData.setName(inputMsg);
        userDataCache.setProfileData(userProfileData);

        return messagesService.getReplyMessage(userId, "Введите телефонный номер")
                .setReplyMarkup(getInlineButtonForCancel(userProfileData));
    }

    private InlineKeyboardMarkup getInlineButtonForCancel(UserProfileData profileData){
        List<InlineButtons> inlineButtons = new ArrayList<>();
        inlineButtons.add(new InlineButtons("Отмена", "cancel_book=" + profileData.getTableNum()));
        return new InlineButtonsBuilder().getButtons(inlineButtons);
    }

}
