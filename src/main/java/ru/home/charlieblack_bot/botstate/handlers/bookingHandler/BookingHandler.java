package ru.home.charlieblack_bot.botstate.handlers.bookingHandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.home.charlieblack_bot.botstate.BotStateEnum;
import ru.home.charlieblack_bot.botstate.InputMessageHandler;
import ru.home.charlieblack_bot.botstate.handlers.AbstractHandler;
import ru.home.charlieblack_bot.cache.UserDataCache;

/**
 * Задает вопросы по бронированию
 */

@Slf4j
@Component
public class BookingHandler extends AbstractHandler implements InputMessageHandler {

    public BookingHandler(UserDataCache userDataCache) { super(userDataCache); }

    @Override
    public SendMessage handle(Update update) {
        return super.handle(update);
    }

    @Override
    public BotStateEnum getHandlerName() {
        return BotStateEnum.BOOKING_BOOK;
    }

}
