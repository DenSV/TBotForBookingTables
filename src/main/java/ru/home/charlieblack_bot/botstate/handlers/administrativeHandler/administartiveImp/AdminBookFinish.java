package ru.home.charlieblack_bot.botstate.handlers.administrativeHandler.administartiveImp;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.home.charlieblack_bot.botstate.BotStateEnum;
import ru.home.charlieblack_bot.botstate.handlers.Booking;
import ru.home.charlieblack_bot.botstate.handlers.AbstractBooking;
import ru.home.charlieblack_bot.model.UserProfileData;

public class AdminBookFinish extends AbstractBooking implements Booking {

    public AdminBookFinish(Update update) {
        super(update);
    }

    @Override
    public SendMessage getResponse() {

        userDataCache.setUsersCurrentBotState(userId, BotStateEnum.ADMIN_BOOK_SHOW_BOOKING_MAP);

        UserProfileData userProfileData = userDataCache.getProfileData();
        userProfileData.setPhoneNumber(inputMsg);

        userProfileData.setId(userDataCache.getUserProfileData(0).getId());
        userDataCache.setProfileData(userProfileData);
        userDataCache.saveUserProfileData(Long.parseLong(inputMsg), userProfileData);

        tableBookingHistoryCache.save(userProfileData, 150);

        return messagesService.getReplyMessage(userId,
                "Стол № "+ userProfileData.getTableNum() +
                " " + userProfileData.getBookingTime() +
                " забронирован на имя " + userProfileData.getName() +
                "\n" + "Телефон для связи: " + userProfileData.getPhoneNumber())
                .setReplyMarkup(AdminShowBookingMapping.getInlineTimesOfTable(userProfileData.getTableNum()));
    }
}
