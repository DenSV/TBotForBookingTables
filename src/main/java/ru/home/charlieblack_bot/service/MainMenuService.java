package ru.home.charlieblack_bot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.home.charlieblack_bot.cache.UserDataCache;

import java.util.ArrayList;
import java.util.List;

@Service
public class MainMenuService {

    private UserDataCache userDataCache;

    public MainMenuService(UserDataCache userDataCache) {
        this.userDataCache = userDataCache;
    }

    public SendMessage getMainMenuMessage(final long chatId, final String textMessage) {
        final ReplyKeyboardMarkup replyKeyboardMarkup = getMainMenuKeyboard(chatId);

        return createMessageWithKeyboard(chatId, textMessage, replyKeyboardMarkup);
    }

    public ReplyKeyboardMarkup getMainMenuKeyboard(long userId) {

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();


        row1.add(new KeyboardButton("Забронировать столик"));
        row2.add(new KeyboardButton("Контактные данные"));
        row3.add(new KeyboardButton("О кальянной"));

        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);

        if (userDataCache.isUserExist(userId) && userDataCache.getUserProfileData(userId).getUserRole().equals("admin")){
            KeyboardRow row4 = new KeyboardRow();
            row4.add(new KeyboardButton("Администрирование"));
            keyboard.add(row4);
        }

        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }




    private SendMessage createMessageWithKeyboard(final long chatId,
                                                  String textMessage,
                                                  final ReplyKeyboardMarkup replyKeyboardMarkup) {
        final SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(textMessage);
        if (replyKeyboardMarkup != null) {
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }
        return sendMessage;
    }
}
