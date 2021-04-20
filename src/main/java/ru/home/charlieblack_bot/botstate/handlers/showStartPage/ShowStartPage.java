package ru.home.charlieblack_bot.botstate.handlers.showStartPage;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.home.charlieblack_bot.botstate.BotStateEnum;
import ru.home.charlieblack_bot.botstate.InputMessageHandler;
import ru.home.charlieblack_bot.botstate.handlers.BookingCore;
import ru.home.charlieblack_bot.keyboardbuilders.InlineButtonsBuilder;
import ru.home.charlieblack_bot.keyboardbuilders.InlineButtons;
import ru.home.charlieblack_bot.cache.UserDataCache;
import ru.home.charlieblack_bot.model.UserProfileData;
import ru.home.charlieblack_bot.service.MainMenuService;
import ru.home.charlieblack_bot.service.ReplyMessagesService;

import java.util.ArrayList;
import java.util.List;

@Component
public class ShowStartPage implements InputMessageHandler {

    private ReplyMessagesService messagesService;
    private MainMenuService mainMenuService;
    private UserDataCache userDataCache;

    public ShowStartPage(ReplyMessagesService messagesService,
                         MainMenuService mainMenuService,
                         UserDataCache userDataCache) {

        this.messagesService = messagesService;
        this.mainMenuService = mainMenuService;
        this.userDataCache = userDataCache;
    }

    @Override
    public SendMessage handle(Update update) {
        return processUsersInput(update);
    }

    @Override
    public BotStateEnum getHandlerName() {return BotStateEnum.SHOW_STARTPAGE;}

    private SendMessage processUsersInput(Update update) {

        long userId = UserProfileData.getUserIdFromUpdate(update);

        String replyMessage = "Вас приветствует бот кальянной CharlieBlack\n" +
                '\n' +
                "Здесь вы можете:\n" +
                "-забронировать столик\n" +
                "-получить по кальянной актуальную информацию";

        if (update.hasCallbackQuery()){

            BookingCore.editMessage(update, replyMessage, getKeyboard(userId));
            return null;

        }

        return messagesService.getReplyMessage(userId, replyMessage)
                    .setReplyMarkup(getKeyboard(userId));

    }

    private InlineKeyboardMarkup getKeyboard(long userId){

        List<InlineButtons> buttons = new ArrayList<>();
        buttons.add(new InlineButtons("Забронировать столик"));
        buttons.add(new InlineButtons("О кальянной"));


        if (userDataCache.isUserExist(userId) && userDataCache.getUserProfileData(userId).getUserRole().equals("admin")){
            buttons.add(new InlineButtons("Администрирование"));
        }

        return new InlineButtonsBuilder().getButtons(buttons);
    }
}
