package ru.home.charlieblack_bot.botstate.handlers.bookingHandler.bookingImp;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.home.charlieblack_bot.botstate.BotStateEnum;
import ru.home.charlieblack_bot.botstate.handlers.Booking;
import ru.home.charlieblack_bot.botstate.handlers.AbstractBooking;
import ru.home.charlieblack_bot.model.TableInfo;

import java.util.ArrayList;
import java.util.List;

import static ru.home.charlieblack_bot.botstate.handlers.BookingCore.*;

public class BookAskTable extends AbstractBooking implements Booking {

    private String replyMessage;
    private List<TableInfo> freeTables;

    public BookAskTable(Update update) {
        super(update);
        this.freeTables = getFreeTables(profileData.getBookingTime());
    }

    @Override
    public SendMessage getResponse() {

        return processResponse(profileData.getName(), profileData.getPhoneNumber());

    }

    private SendMessage processResponse(String userName, String userNumber){

        //проверка входящих данных
        if(update.hasMessage() && update.getMessage().hasText()){
            userDataCache.setUsersCurrentBotState(userId, currentBotStateEnum);
            return messagesService.getReplyMessage(userId, "Данные введены неправильно. " +
                    "Выберите номер стола из списка")
                    .setReplyMarkup(getInlineMessageButtons(freeTables));
        }

        if (freeTables.isEmpty()) {
            userDataCache.setUsersCurrentBotState(userId, BotStateEnum.BOOKING_ASK_TIME);
            return messagesService.getReplyMessage(userId, "К сожалению, все столы на "
                    + profileData.getBookingTime()
                    + " заняты. Выберите другое время")
                    .setReplyMarkup(getInlineKeyBoardTimeButtons());
        }


        if(userName == null) {
            replyMessage = "На какое имя бронировать?";
            return messagesService.getReplyMessage(userId, replyMessage);
        } else if (userNumber == null){
            userDataCache.setUsersCurrentBotState(userId, BotStateEnum.BOOKING_ASK_NUMBER);
            replyMessage = "Введите номер мобильного телефона для связи";
            return messagesService.getReplyMessage(userId, replyMessage).setReplyMarkup(getReplyKeyboardContact());
        } else {

            tableBookingHistoryCache.save(profileData, 150);

            //Отправка уведомления о бронировании админу
            sendMessageToAdmin(profileData);



            userDataCache.setUsersCurrentBotState(userId, currentBotStateEnum.getMainBotState());
            replyMessage =  "Столик № "+ profileData.getTableNum() + " " + profileData.getBookingTime() +
                            " забронирован на имя " + profileData.getName() +
                            "\n" + "Телефон для связи: " + profileData.getPhoneNumber() +
                            "\n Заяка отправлена на рассмотрение. Ждите ответа от администратора";
            return  messagesService.getReplyMessage(userId, replyMessage).setReplyMarkup(getReplyKeyBoardMarkup());
        }

    }

    private ReplyKeyboardMarkup getReplyKeyBoardMarkup(){

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Вернуться на главную"));
        keyboard.add(row1);
        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }

}
