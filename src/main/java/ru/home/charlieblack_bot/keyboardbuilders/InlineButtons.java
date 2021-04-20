package ru.home.charlieblack_bot.keyboardbuilders;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Getter
@Setter
public class InlineButtons {

    private InlineKeyboardButton inlineKeyboardButton;

    public InlineButtons(String text, String callBackData) {
        this.inlineKeyboardButton = new InlineKeyboardButton()
                .setText(text)
                .setCallbackData(callBackData);

    }

    public InlineButtons(){
        this.inlineKeyboardButton = new InlineKeyboardButton();
    }

    public InlineButtons(String text){
        this.inlineKeyboardButton = new InlineKeyboardButton()
                .setText(text)
                .setCallbackData(text);
    }

    public InlineButtons setText(String text){
        inlineKeyboardButton.setText(text);
        return this;
    }

    public InlineButtons setCallBackData(String callBackData){
        inlineKeyboardButton.setCallbackData(callBackData);
        return this;
    }
}
