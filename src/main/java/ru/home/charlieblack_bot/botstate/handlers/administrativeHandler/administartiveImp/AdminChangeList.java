package ru.home.charlieblack_bot.botstate.handlers.administrativeHandler.administartiveImp;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.home.charlieblack_bot.botstate.BotStateEnum;
import ru.home.charlieblack_bot.botstate.handlers.Booking;
import ru.home.charlieblack_bot.botstate.handlers.AbstractBooking;
import ru.home.charlieblack_bot.model.UserProfileData;

import static ru.home.charlieblack_bot.botstate.handlers.BookingCore.getReplyKeyBoardBackToStartPage;

public class AdminChangeList extends AbstractBooking implements Booking {

    public AdminChangeList(Update update) {
        super(update);
    }

    @Override
    public SendMessage getResponse() {

        if(inputMsg.contains("delete_")){
            long adminId = Long.parseLong(inputMsg.split("_")[1]);

            UserProfileData adminProfile = userDataCache.getUserProfileData(adminId);
            adminProfile.setUserRole("user");
            userDataCache.saveUserProfileData(adminId, adminProfile);

            userDataCache.setUsersCurrentBotState(userId, BotStateEnum.ADMIN_START);

            return messagesService.getReplyMessage(userId, adminProfile.getName() + " удален из списка администраторов")
                    .setReplyMarkup(getReplyKeyBoardBackToStartPage());
        }

        return null;
    }
}
