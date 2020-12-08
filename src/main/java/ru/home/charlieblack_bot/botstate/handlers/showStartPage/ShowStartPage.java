package ru.home.charlieblack_bot.botstate.handlers.showStartPage;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.home.charlieblack_bot.botstate.BotStateEnum;
import ru.home.charlieblack_bot.botstate.InputMessageHandler;
import ru.home.charlieblack_bot.cache.UserDataCache;
import ru.home.charlieblack_bot.model.UserProfileData;
import ru.home.charlieblack_bot.service.MainMenuService;
import ru.home.charlieblack_bot.service.ReplyMessagesService;
import ru.home.charlieblack_bot.service.UsersProfileDataService;

@Component
public class ShowStartPage implements InputMessageHandler {

    private UserDataCache userDataCache;
    private ReplyMessagesService messagesService;
    private UsersProfileDataService profileDataService;
    private MainMenuService mainMenuService = new MainMenuService();

    public ShowStartPage(UserDataCache userDataCache,
                          ReplyMessagesService messagesService,
                          UsersProfileDataService profileDataService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
        this.profileDataService = profileDataService;
    }

    @Override
    public SendMessage handle(Update update) {
        return processUsersInput(update);
    }

    @Override
    public BotStateEnum getHandlerName() {return BotStateEnum.SHOW_STARTPAGE;}

    private SendMessage processUsersInput(Update update) {

        SendMessage replyToUser = null;


        long userId = UserProfileData.getUserIdFromUpdate(update);

        BotStateEnum botState = userDataCache.getUsersCurrentBotState(userId);

        String replyMessage = "Вас приветствует бот кальянной Charlie&Black\n" +
                '\n' +
                "Здесь вы можете:\n" +
                "-забронировать столик\n" +
                "-получить по кальянной актуальную информацию\n" +
                "-заполнить информацию о себе";


        if(botState.equals(BotStateEnum.SHOW_STARTPAGE)){
            replyToUser = messagesService.getReplyMessage(userId, replyMessage).setReplyMarkup(mainMenuService.getMainMenuKeyboard());
        }

        return replyToUser;
    }
}
