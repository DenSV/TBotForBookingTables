package ru.home.charlieblack_bot.botstate.handlers.administrativeHandler.administartiveImp;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
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

public class AdminAdd extends AbstractBooking implements Booking {

    private static final String ADMIN_USER_ROLE = "admin";
    private boolean hasContact;

    public AdminAdd(Update update) {
        super(update);
        this.hasContact = update.getMessage().hasContact();
    }

    @Override
    public SendMessage getResponse() {

        return messagesService.getReplyMessage(userId, getReplyMessage())
                .setReplyMarkup(getKeyboard());


    }

    @Override
    protected void saveUserData(){
        Contact contact = update.getMessage().getContact();

        UserProfileData adminProfile = userDataCache.getUserProfileData(contact.getUserID());
        adminProfile.setUserRole(ADMIN_USER_ROLE);
        adminProfile.setPhoneNumber(contact.getPhoneNumber());
        adminProfile.setName(contact.getFirstName());

        userDataCache.saveUserProfileData(contact.getUserID(), adminProfile);
    }

    @Override
    protected String getReplyMessage() {
        if(hasContact){
            saveUserData();
            setMainBotState();
            return "Администратор добавлен";
        } else {
            setAnotherBotState(BotStateEnum.ADMIN_ADD);
            return "Администратор не может быть добавлен. " +
                    "Введены неправильные данные. " +
                    "Повторите попытку";
        }
    }

    private InlineKeyboardMarkup getKeyboard(){
        List<InlineButtons> buttons = new ArrayList<>();
        buttons.add(new InlineButtons("Администрирование"));
        buttons.add(new InlineButtons("Вернуться на главную"));

        return new InlineButtonsBuilder().getButtons(buttons);
    }
}
