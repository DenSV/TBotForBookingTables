package ru.home.charlieblack_bot.keyboardbuilders;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InlineKeyboardRow {
    private List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();

    public InlineKeyboardRow() {
    }

    public InlineKeyboardRow(InlineButtons... args) {
        Arrays.stream(args).forEach(inlineButtons -> inlineKeyboardButtons.add(inlineButtons.getInlineKeyboardButton()));
    }

    public void setButtonsInRow(InlineButtons... args){
        Arrays.stream(args).forEach(inlineButtons -> inlineKeyboardButtons.add(inlineButtons.getInlineKeyboardButton()));
    }

    public List<InlineKeyboardButton> getRow(){
        return this.inlineKeyboardButtons;
    }
}
