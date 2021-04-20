package ru.home.charlieblack_bot.botstate.handlers.administrativeHandler.administartiveImp;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.home.charlieblack_bot.botstate.handlers.Booking;
import ru.home.charlieblack_bot.botstate.handlers.AbstractBooking;
import ru.home.charlieblack_bot.botstate.handlers.BookingCore;
import ru.home.charlieblack_bot.keyboardbuilders.InlineButtonsBuilder;

public class Admin extends AbstractBooking implements Booking {

    public Admin(Update update) {
        super(update);
    }

    @Override
    public SendMessage getResponse() {

        return BookingCore.editMessage(update, "Возможности администрирования", getButtons());

    }

    public static InlineKeyboardMarkup getButtons(){
        return new InlineButtonsBuilder().getButtons(
                "Карта бронирования",
                "Показать историю бронирования",
                "Администраторы",
                "Вернуться на главную");
    }

}
