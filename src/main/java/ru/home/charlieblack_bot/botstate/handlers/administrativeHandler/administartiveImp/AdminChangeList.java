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

public class AdminChangeList extends AbstractBooking implements Booking {

    private UserProfileData adminProfile;

    public AdminChangeList(Update update) {
        super(update);
    }

    @Override
    public SendMessage getResponse() {

        if(inputMsg.contains("delete_")){
            saveUserData();
            setAnotherBotState(BotStateEnum.ADMIN_START);

            return editMessage(update, adminProfile.getName() + " удален из списка администраторов", getKeyboard());
        }

        return null;
    }

    @Override
    protected void saveUserData(){
        long adminId = Long.parseLong(inputMsg.split("_")[1]);

        adminProfile = userDataCache.getUserProfileData(adminId);
        adminProfile.setUserRole("user");
        userDataCache.saveUserProfileData(adminId, adminProfile);
    }

    private InlineKeyboardMarkup getKeyboard(){
        List<InlineButtons> buttons = new ArrayList<>();
        buttons.add(new InlineButtons("Администрирование"));
        buttons.add(new InlineButtons("Вернуться на главную"));
        return new InlineButtonsBuilder().getButtons(buttons);
    }

}
