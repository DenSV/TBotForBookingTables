package ru.home.charlieblack_bot.keyboardbuilders;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InlineButtonsBuilder {

    private InlineKeyboardMarkup inlineKeyboardMarkup;
    private List<List<InlineKeyboardButton>> rowList;

    public InlineButtonsBuilder() {
        this.inlineKeyboardMarkup = new InlineKeyboardMarkup();
        this.rowList = new ArrayList<>();
    }

    public InlineKeyboardMarkup getButtons(String... args){
        List<String> argsList = Arrays.asList(args);

        argsList.forEach(arg -> {
            List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
            inlineKeyboardButtons.add(new InlineKeyboardButton()
                    .setText(arg)
                    .setCallbackData(arg));
            rowList.add(inlineKeyboardButtons);
        });

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;

    }


    public InlineKeyboardMarkup getButtons(List<InlineButtons> buttons){

        buttons.forEach(inlineButton -> {
            List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
            inlineKeyboardButtons.add(inlineButton.getInlineKeyboardButton());
            rowList.add(inlineKeyboardButtons);

        });


        return inlineKeyboardMarkup.setKeyboard(rowList);

    }

    public InlineKeyboardMarkup getButtonsWithRows(List<InlineKeyboardRow> inlineKeyboardRows){
        inlineKeyboardRows.forEach(inlineKeyboardRow -> rowList.add(inlineKeyboardRow.getRow()));
        return inlineKeyboardMarkup.setKeyboard(rowList);
    }



}
