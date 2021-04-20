package ru.home.charlieblack_bot.botstate.handlers.administrativeHandler.administartiveImp;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.home.charlieblack_bot.botstate.BotStateEnum;
import ru.home.charlieblack_bot.botstate.handlers.Booking;
import ru.home.charlieblack_bot.botstate.handlers.AbstractBooking;
import ru.home.charlieblack_bot.model.UserProfileData;

import static ru.home.charlieblack_bot.botstate.handlers.BookingCore.editMessage;
import static ru.home.charlieblack_bot.botstate.handlers.BookingCore.notifyForAdminsAboutBookingFromPhoneCall;
import static ru.home.charlieblack_bot.botstate.handlers.administrativeHandler.administartiveImp.AdminShowBookingMapping.getInlineTimesOfTableWithRows;

public class AdminBookFinish extends AbstractBooking implements Booking {

    private UserProfileData userProfileData;

    public AdminBookFinish(Update update) {
        super(update);

        this.userProfileData = userDataCache.getProfileData();
    }

    @Override
    public SendMessage getResponse() {

        if(inputMsg.contains("cancel_book=")){
            setAnotherBotState(BotStateEnum.ADMIN_BOOK_SHOW_BOOKING_MAP);

            int tableNum = Integer.parseInt(inputMsg.split("=")[1]);
            editMessage(update, "Стол № " + tableNum, getInlineTimesOfTableWithRows(tableNum));
            return null;
        }

        setAnotherBotState(BotStateEnum.ADMIN_BOOK_SHOW_BOOKING_MAP);
        updateUserData();
        saveUserData();
        saveBookingHistory();

        String replyMessage = "Стол № "+ userProfileData.getTableNum() +
                " " + userProfileData.getBookingTime() +
                " забронирован на имя " + userProfileData.getName() +
                "\n" + "Телефон для связи: " + userProfileData.getPhoneNumber();

        notifyForAdminsAboutBookingFromPhoneCall(userId, replyMessage);

        return messagesService.getReplyMessage(userId,replyMessage)
                .setReplyMarkup(AdminShowBookingMapping.getInlineTimesOfTableWithRows(userProfileData.getTableNum()));
    }

    @Override
    protected void saveUserData() {
        userDataCache.saveUserProfileData(Long.parseLong(inputMsg), userProfileData);
    }

    @Override
    protected void saveBookingHistory() {
        tableBookingHistoryCache.save(userProfileData, 150);
    }

    private void updateUserData(){
        userProfileData.setPhoneNumber(inputMsg);
        userProfileData.setId(userDataCache.getUserProfileData(0).getId());
        userDataCache.setProfileData(userProfileData);
    }
}
