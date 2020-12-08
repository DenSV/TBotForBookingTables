package ru.home.charlieblack_bot.botstate.handlers.showActualInformationHandler;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.home.charlieblack_bot.botstate.BotStateEnum;
import ru.home.charlieblack_bot.botstate.InputMessageHandler;
import ru.home.charlieblack_bot.botstate.handlers.BookingCore;
import ru.home.charlieblack_bot.cache.UserDataCache;
import ru.home.charlieblack_bot.model.UserProfileData;
import ru.home.charlieblack_bot.service.ReplyMessagesService;


@Component
public class ShowActualInformation implements InputMessageHandler {


    private UserDataCache userDataCache;
    private ReplyMessagesService messagesService;
    private SendMessage replyToUser;

    public ShowActualInformation(UserDataCache userDataCache,
                          ReplyMessagesService messagesService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
    }


    @Override
    public SendMessage handle(Update update) {
        return processUsersInput(update);
    }

    @Override
    public BotStateEnum getHandlerName() {return BotStateEnum.SHOW_ACTUAL_INFORMATION;}

    private SendMessage processUsersInput(Update update) {

        long userId = UserProfileData.getUserIdFromUpdate(update);

        BotStateEnum botState = userDataCache.getUsersCurrentBotState(userId);

        if (botState.equals(BotStateEnum.SHOW_ACTUAL_INFORMATION)){

            replyToUser = messagesService.getReplyMessage(userId, getInfoAboutCafe());
            replyToUser.setReplyMarkup(BookingCore.getAdressInlineButton());
        }

        return replyToUser;
    }

    private String getInfoAboutCafe(){
        return "Цена за один кальян с любым табаком: 700р\n" +
                "\n" +
                "Часы работы:\n" +
                "Пн-Чт-Вс с 15:00 до 01:00\n" +
                "Пт-Сб с 15:00 до 04:00\n" +
                "_________________\n" +
                "Тел.: +7(925)617-48-16\n" +
                "Адрес: г.Раменское, северное шоссе, д.4";
    }


}
