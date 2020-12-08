package ru.home.charlieblack_bot.botstate;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.home.charlieblack_bot.AppContProvider;
import ru.home.charlieblack_bot.cache.UserDataCache;
import ru.home.charlieblack_bot.model.UserProfileData;

import java.util.HashMap;
import java.util.Map;

public class BotResponse {
    private Map<String, BotStateEnum> response = new HashMap<>();

    public BotResponse() {
        response.put("/start", BotStateEnum.SHOW_STARTPAGE);
        response.put("Забронировать столик", BotStateEnum.BOOKING_BOOK);
        response.put("Контактные данные", BotStateEnum.SHOW_USER_PROFILEDATA);
        response.put("О кальянной", BotStateEnum.SHOW_ACTUAL_INFORMATION);
        response.put("Вернуться на главную", BotStateEnum.SHOW_STARTPAGE);
        response.put("Изменить имя", BotStateEnum.ASK_USER_NAME);
        response.put("Изменить номер", BotStateEnum.ASK_USER_NUM);

    }

    public BotStateEnum getBotStateEnum(Update update){

        String inputMsg = (update.hasCallbackQuery() ? "": update.getMessage().getText());

        BotStateEnum botStateEnum = response.get(inputMsg);

        //если находит пару в мапе, то возвращает значение, иначе возвращает текущее состояние бота
        if(botStateEnum != null){
            return botStateEnum;
        } else {
            UserDataCache userDataCache = AppContProvider.getApplicationContext().getBean(UserDataCache.class);

            return userDataCache.getUsersCurrentBotState( UserProfileData.getUserIdFromUpdate(update));
        }

    }


}
