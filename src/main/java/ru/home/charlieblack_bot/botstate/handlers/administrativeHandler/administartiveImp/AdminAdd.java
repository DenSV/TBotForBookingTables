package ru.home.charlieblack_bot.botstate.handlers.administrativeHandler.administartiveImp;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.home.charlieblack_bot.botstate.BotStateEnum;
import ru.home.charlieblack_bot.botstate.handlers.Booking;
import ru.home.charlieblack_bot.botstate.handlers.AbstractBooking;
import ru.home.charlieblack_bot.model.UserProfileData;

import static ru.home.charlieblack_bot.botstate.handlers.BookingCore.getReplyKeyBoardBackToStartPage;

public class AdminAdd extends AbstractBooking implements Booking {

    public AdminAdd(Update update) {
        super(update);
    }

    @Override
    public SendMessage getResponse() {
        
        if(update.getMessage().hasContact()){
            Contact adminContact = update.getMessage().getContact();
            UserProfileData adminProfile = userDataCache.getUserProfileData(adminContact.getUserID());
            adminProfile.setUserRole("admin");
            adminProfile.setPhoneNumber(adminContact.getPhoneNumber());
            adminProfile.setName(adminContact.getFirstName());

            userDataCache.saveUserProfileData(adminContact.getUserID(), adminProfile);
            userDataCache.setUsersCurrentBotState(userId, currentBotStateEnum.getMainBotState());
        } else{
            userDataCache.setUsersCurrentBotState(userId, BotStateEnum.ADMIN_ADD);
            return messagesService.getReplyMessage(userId, "Администратор не может быть добавлен. " +
                    "Введены неправильные данные. " +
                    "Повторите попытку")
                    .setReplyMarkup(getReplyKeyBoardBackToStartPage());
        }

        return messagesService.getReplyMessage(userId, "Администратор добавлен");
    }
}
