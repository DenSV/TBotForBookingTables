package ru.home.charlieblack_bot.keyboardbuilders;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KeyboardButtonsBuilder {

    private ReplyKeyboardMarkup replyKeyboardMarkup;
    private List<KeyboardRow> keyboard;
    private boolean oneTimeKeyBoard;


    public KeyboardButtonsBuilder() {
        this.replyKeyboardMarkup = new ReplyKeyboardMarkup();
        this.keyboard = new ArrayList<>();
        this.replyKeyboardMarkup.setSelective(true);
        this.replyKeyboardMarkup.setResizeKeyboard(true);


    }

    public KeyboardButtonsBuilder setOneTimeKeyBoard(boolean oneTimeKeyBoard) {
        this.oneTimeKeyBoard = oneTimeKeyBoard;

        return this;
    }

    public ReplyKeyboardMarkup getButtons(String... args) {
        List<String> argsList = Arrays.asList(args);

        replyKeyboardMarkup.setOneTimeKeyboard(oneTimeKeyBoard);

        argsList.forEach(s -> {
            KeyboardRow keyboardRow = new KeyboardRow();
            if (s.contains("Отправить свой контакт")) {
                keyboardRow.add(new KeyboardButton(s).setRequestContact(true));
            } else {
                keyboardRow.add(new KeyboardButton(s));
            }
            keyboard.add(keyboardRow);

        });

        return replyKeyboardMarkup.setKeyboard(keyboard);

    }

    public ReplyKeyboardMarkup getButtons(List<String> buttons){

        replyKeyboardMarkup.setOneTimeKeyboard(oneTimeKeyBoard);

        buttons.forEach(s -> {
            KeyboardRow keyboardRow = new KeyboardRow();
            if (s.contains("Отправить свой контакт")) {
                keyboardRow.add(new KeyboardButton(s).setRequestContact(true));
            } else {
                keyboardRow.add(new KeyboardButton(s));
            }
            keyboard.add(keyboardRow);

        });

        return replyKeyboardMarkup.setKeyboard(keyboard);
    }
}
