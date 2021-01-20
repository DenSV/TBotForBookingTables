package ru.home.charlieblack_bot.botstate.handlers.administrativeHandler.administartiveImp;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.home.charlieblack_bot.botstate.BotStateEnum;
import ru.home.charlieblack_bot.botstate.handlers.Booking;
import ru.home.charlieblack_bot.botstate.handlers.AbstractBooking;
import ru.home.charlieblack_bot.model.TableBookingHistory;
import ru.home.charlieblack_bot.model.UserProfileData;

import java.util.ArrayList;
import java.util.List;

import static ru.home.charlieblack_bot.botstate.handlers.BookingCore.sendMessage;

public class AdminGetBookHistory extends AbstractBooking implements Booking {


    public AdminGetBookHistory(Update update) {
        super(update);
    }

    @Override
    public SendMessage getResponse() {

        if (inputMsg.equals("Показать историю бронирования")){
            userDataCache.setUsersCurrentBotState(userId, BotStateEnum.ADMIN_START);
            return messagesService.getReplyMessage(userId, getBookingHistory());
        } else if(inputMsg.equals("Администраторы")) {
            userDataCache.setUsersCurrentBotState(userId, BotStateEnum.ADMIN_CHANGE_LIST);

            return getAdminList();
        }

        return null;
    }

    private String getBookingHistory(){
        final String[] result = {""};

        List<TableBookingHistory> tableBookingHistoryList = tableBookingHistoryCache.getAllApprovedBookingHistory();

        if (tableBookingHistoryList.isEmpty()){
            return "История бронирования пуста";
        }

        tableBookingHistoryList.forEach(tableBookingHistory ->
                result[0] = result[0] + tableBookingHistory + "\n");

        return result[0];
    }

    private SendMessage getAdminList() {
        List<UserProfileData> result = userDataCache.getAdminList();

        if(result.size() > 0){
            //вывести сообщениями список админов (сообщение + inline-кнопка)
            sendListOfMessages(result);
            return null;

        } else {
            return messagesService.getReplyMessage(userId, "Список администраторов пуст").setReplyMarkup(getReplyKeyBoard());
        }

    }

    private ReplyKeyboardMarkup getReplyKeyBoard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        List<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(new KeyboardButton("Добавить администратора"));
        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(new KeyboardButton("Вернуться на главную"));
        keyboardRows.add(keyboardRow1);
        keyboardRows.add(keyboardRow2);

        return replyKeyboardMarkup.setKeyboard(keyboardRows);

    }

    private void sendListOfMessages(List<UserProfileData> adminList) {

        //Отправка сообщения-заголовка
        SendMessage sendMessageHeader = new SendMessage()
                .setChatId(userId)
                .setText("Список администраторов")
                .setReplyMarkup(getReplyKeyBoard());
        sendMessage(sendMessageHeader);

        //Отправка списка админов

            adminList.forEach(profileData -> {

                InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

                List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

                List<InlineKeyboardButton> inButton = new ArrayList<>();

                inButton.add(new InlineKeyboardButton()
                            .setText("Удалить из списка")
                            .setCallbackData("delete_" + profileData.getChatId()));

                rowList.add(inButton);
                inlineKeyboardMarkup.setKeyboard(rowList);

                String you = "";
                if (userId == profileData.getChatId()) you = "(Вы)";

                SendMessage sendMessage = new SendMessage()
                        .setChatId(userId)
                        .setText(profileData.getName() + you + "\n" + profileData.getPhoneNumber())
                        .setReplyMarkup(inlineKeyboardMarkup);
                sendMessage(sendMessage);
            });

    }



}
