package ru.home.charlieblack_bot.botstate.handlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.home.charlieblack_bot.botstate.BotStateEnum;

import java.util.*;

public class BookingMapping {
    private Map<BotStateEnum, String> map = new LinkedHashMap<>();
    private Update update;

    public BookingMapping(Update update) {

        this.update = update;

        List<BotStateEnum> botStateEnums = new ArrayList<>(Arrays.asList(BotStateEnum.values()));
        botStateEnums.forEach(botStateEnum -> map.put(botStateEnum, botStateEnum.getFullImpName()));

    }

    public SendMessage runBooking(BotStateEnum botStateEnum) {
            try {

                Booking o = (Booking) Class.forName(map.get(botStateEnum))
                                            .getConstructor(Update.class)
                                            .newInstance(update);

                return o.getResponse();
            } catch (ReflectiveOperationException e){
                e.printStackTrace();
                return null;
            }

    }

}
