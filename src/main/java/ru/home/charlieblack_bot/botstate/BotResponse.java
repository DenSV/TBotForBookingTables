package ru.home.charlieblack_bot.botstate;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.home.charlieblack_bot.AppContProvider;
import ru.home.charlieblack_bot.cache.UserDataCache;
import ru.home.charlieblack_bot.model.UserProfileData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BotResponse {
    private Map<String, BotStateEnum> response = new HashMap<>();
    private Set<BotStateEnum> statesWithInputData = new HashSet<>();

    public BotResponse() {
        response.put("/start",                  BotStateEnum.SHOW_STARTPAGE);
        response.put("Забронировать столик",    BotStateEnum.BOOKING_BOOK);
        response.put("Контактные данные",       BotStateEnum.SHOW_USER_PROFILEDATA);
        response.put("О кальянной",             BotStateEnum.SHOW_ACTUAL_INFORMATION);
        response.put("Вернуться на главную",    BotStateEnum.SHOW_STARTPAGE);
        response.put("Изменить имя",            BotStateEnum.ASK_USER_NAME);
        response.put("Изменить номер",          BotStateEnum.ASK_USER_NUM);
        response.put("Администрирование",       BotStateEnum.ADMIN_START);
        response.put("Показать историю бронирования", BotStateEnum.ADMIN_GET_BOOK_HISTORY);
        response.put("Администраторы", BotStateEnum.ADMIN_GET_BOOK_HISTORY);
        response.put("Добавить администратора", BotStateEnum.ADMIN_ASK_CONTACT);
        response.put("Карта бронирования", BotStateEnum.ADMIN_BOOK_SHOW_BOOKING_MAP);

        statesWithInputData.add(BotStateEnum.BOOKING_ASK_NAME);
        statesWithInputData.add(BotStateEnum.BOOKING_ASK_NUMBER);
        statesWithInputData.add(BotStateEnum.ASK_USER_NAME);
        statesWithInputData.add(BotStateEnum.ASK_USER_NUM);
    }

    public BotStateEnum getBotStateEnum(Update update){

        String inputMsg = (update.hasCallbackQuery() ? update.getCallbackQuery().getData() : update.getMessage().getText());

        BotStateEnum botStateEnum = response.get(inputMsg);

        //если находит пару в мапе, то возвращает значение, иначе возвращает текущее состояние бота

        if(update.hasCallbackQuery() && update.getCallbackQuery().getData().contains("admin_table_number=")){
            return BotStateEnum.ADMIN_BOOK_USER;
        }

        if(botStateEnum != null){
            return botStateEnum;
        } else {
            UserDataCache userDataCache = AppContProvider.getApplicationContext().getBean(UserDataCache.class);

            return userDataCache.getUsersCurrentBotState( UserProfileData.getUserIdFromUpdate(update));
        }

    }

    public boolean hasValue(String inputMsg){
        return (response.get(inputMsg) != null);
    }


}
