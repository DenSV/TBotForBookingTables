package ru.home.charlieblack_bot.botstate.handlers.showStartPage;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.home.charlieblack_bot.botstate.BotStateEnum;
import ru.home.charlieblack_bot.botstate.InputMessageHandler;
import ru.home.charlieblack_bot.botstate.handlers.BookingCore;
import ru.home.charlieblack_bot.model.UserProfileData;
import ru.home.charlieblack_bot.service.MainMenuService;
import ru.home.charlieblack_bot.service.ReplyMessagesService;

@Component
public class ShowStartPage implements InputMessageHandler {

    private ReplyMessagesService messagesService;
    private MainMenuService mainMenuService;

    public ShowStartPage(ReplyMessagesService messagesService,
                         MainMenuService mainMenuService) {
        this.messagesService = messagesService;
        this.mainMenuService = mainMenuService;
    }

    @Override
    public SendMessage handle(Update update) {
        return processUsersInput(update);
    }

    @Override
    public BotStateEnum getHandlerName() {return BotStateEnum.SHOW_STARTPAGE;}

    private SendMessage processUsersInput(Update update) {

        long userId = UserProfileData.getUserIdFromUpdate(update);

        String replyMessage = "Вас приветствует бот кальянной Charlie&Black\n" +
                '\n' +
                "Здесь вы можете:\n" +
                "-забронировать столик\n" +
                "-получить по кальянной актуальную информацию\n" +
                "-заполнить информацию о себе";

        if (update.hasCallbackQuery()){

            BookingCore.deleteMessage(new DeleteMessage()
                    .setChatId(userId)
                    .setMessageId(update.getCallbackQuery().getMessage().getMessageId()));

        }

        return messagesService.getReplyMessage(userId, replyMessage)
                    .setReplyMarkup(mainMenuService.getMainMenuKeyboard(userId));

    }
}
