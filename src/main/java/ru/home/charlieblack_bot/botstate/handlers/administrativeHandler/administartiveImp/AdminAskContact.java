package ru.home.charlieblack_bot.botstate.handlers.administrativeHandler.administartiveImp;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.home.charlieblack_bot.botstate.BotStateEnum;
import ru.home.charlieblack_bot.botstate.handlers.Booking;
import ru.home.charlieblack_bot.botstate.handlers.AbstractBooking;
import ru.home.charlieblack_bot.keyboardbuilders.InlineButtons;
import ru.home.charlieblack_bot.keyboardbuilders.InlineButtonsBuilder;

import java.util.ArrayList;
import java.util.List;

import static ru.home.charlieblack_bot.botstate.handlers.BookingCore.editMessage;

public class AdminAskContact extends AbstractBooking implements Booking {

    public AdminAskContact(Update update) {
        super(update);
    }

    @Override
    public SendMessage getResponse() {
        setAnotherBotState(BotStateEnum.ADMIN_ADD);

        return editMessage(update, "Пришлите контакт", getKeyboard());

    }

    private InlineKeyboardMarkup getKeyboard(){
        List<InlineButtons> buttons = new ArrayList<>();
        buttons.add(new InlineButtons("Отмена", "Администраторы"));
        return new InlineButtonsBuilder().getButtons(buttons);
    }
}
