package ru.home.charlieblack_bot.botstate.handlers;

/*
    Задача класса:
        - вывести списком доступное время для юзера(с учетом текущего времени);
        - вывести списком доступные столы на это время(учитывая время, коли-во человек);
        -
*/


import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.home.charlieblack_bot.AppContProvider;
import ru.home.charlieblack_bot.CharlieBlackTelegramBot;
import ru.home.charlieblack_bot.ScheduledTasks;
import ru.home.charlieblack_bot.cache.TableBookingHistoryCache;
import ru.home.charlieblack_bot.cache.TableInfoCache;
import ru.home.charlieblack_bot.cache.UserDataCache;
import ru.home.charlieblack_bot.model.TableBookingHistory;
import ru.home.charlieblack_bot.model.TableInfo;
import ru.home.charlieblack_bot.model.UserProfileData;
import ru.home.charlieblack_bot.service.TableInfoService;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class BookingCore {

    public static List<TableInfo> getFreeTables(String bookingTime) {

        //объявление необходимых сервисов;
        TableInfoService tableInfoService = AppContProvider.getApplicationContext().getBean(TableInfoService.class);

        //TableBookingHistoryService tableBookingHistoryService = ApplicationContextProvider.getApplicationContext().getBean(TableBookingHistoryService.class);

        //список свободных столов из БД
        List<TableInfo> freeTableList = new ArrayList<>(tableInfoService.getAllFreeTablesOnCurrentTime(bookingTime, false));

        Collections.sort(freeTableList);

        return freeTableList;

    }

    public static String getInputMsgFromUpdate(Update update){

        String inputMsg = "";
        if(update.hasCallbackQuery()){
            inputMsg = update.getCallbackQuery().getData();
        }else if(update.getMessage().hasContact()){
            inputMsg = update.getMessage().getContact().getPhoneNumber();
        } else {
            inputMsg = update.getMessage().getText();
        }

        return inputMsg;

    }

    /*
        KEYBOARD'S BUTTONS
    */


    public static ReplyKeyboardMarkup getReplyKeyBoardBackToStartPage(){

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

    public static ReplyKeyboardMarkup getReplyKeyboardContact(){

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Отправить свой контакт ☎").setRequestContact(true));
        keyboard.add(row1);
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("Вернуться на главную"));
        keyboard.add(row2);
        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getAdressInlineButton(){

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton adressButton = new InlineKeyboardButton();
        adressButton.setText("Открыть адрес в Яндекс.Картах");
        adressButton.setUrl("https://yandex.ru/maps/10750/ramenskoe/?ll=38.216398%2C55.578913&mode=poi&poi%5Bpoint%5D=38.216195%2C55.578989&poi%5Buri%5D=ymapsbm1%3A%2F%2Forg%3Foid%3D1382133920&z=19.52");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(adressButton);

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup getReplyKeyBoardChangeUserInfo(){
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();

        row1.add(new KeyboardButton("Изменить имя"));
        row2.add(new KeyboardButton("Изменить номер"));
        row3.add(new KeyboardButton("Вернуться на главную"));
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);

        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }

    public static InlineKeyboardMarkup getInlineKeyBoardTimeButtons(){

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>(9);

        LocalTime startTime = LocalTime.of(15, 00);

        for(int i = 0; i < 5; i++){
            List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>(4);
            for (int j = 0; j < 4; j++) {
                if(!startTime.equals(LocalTime.MIN) && !startTime.equals(LocalTime.MIN.plusMinutes(30))){
                    inlineKeyboardButtons.add(new InlineKeyboardButton()
                            .setText(startTime.toString())
                            .setCallbackData("time-" + startTime.toString()));
                }
                startTime = startTime.plusMinutes(30);
            }

            rowList.add(inlineKeyboardButtons);

        }

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;

    }

    public static InlineKeyboardMarkup getInlineMessageButtons(List<TableInfo> freeTables) {

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for (TableInfo tableInfo: freeTables) {
            List<InlineKeyboardButton> keyboardButtons = new ArrayList<>();

            String tableNum = String.valueOf(tableInfo.getTableNumber());

            keyboardButtons.add(new InlineKeyboardButton()
                    .setText("Стол №" + tableNum + " (" + tableInfo.getCapacity() + " чел.)")
                    .setCallbackData("table_num=" + tableNum + "&" + "person_count=" + tableInfo.getCapacity()));

            rowList.add(keyboardButtons);
        }

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    private static ReplyKeyboard getInlineKeyBoardApproveBooking(long userId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        List<InlineKeyboardButton> inlineKeyboardButtonRow = new ArrayList<>();

        inlineKeyboardButtonRow.add(new InlineKeyboardButton().setText("Подтвердить").setCallbackData("booking_request_approve=" + userId));
        inlineKeyboardButtonRow.add(new InlineKeyboardButton().setText("Отменить").setCallbackData("booking_request_decline=" + userId));

        rowList.add(inlineKeyboardButtonRow);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;

    }

    public static InlineKeyboardMarkup getInlinekeyboardForBookingContinue() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        List<InlineKeyboardButton> inlineKeyboardButtonRow = new ArrayList<>();

        inlineKeyboardButtonRow.add(new InlineKeyboardButton().setText("Продлить").setCallbackData("booking_continue_true"));
        inlineKeyboardButtonRow.add(new InlineKeyboardButton().setText("Отменить").setCallbackData("booking_continue_false"));

        rowList.add(inlineKeyboardButtonRow);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;

    }

    /*
        Отправка сообщений администратору
     */

    public static void sendMessageToAdmin(UserProfileData profileData){

        UserDataCache userDataCache = AppContProvider.getApplicationContext().getBean(UserDataCache.class);

        TableBookingHistoryCache tBHCache = AppContProvider.getApplicationContext().getBean(TableBookingHistoryCache.class);

        CharlieBlackTelegramBot telegramBot = AppContProvider.getApplicationContext().getBean(CharlieBlackTelegramBot.class);
        List<UserProfileData> adminList = userDataCache.getAdminList();

        adminList.forEach(admin -> {
            SendMessage sendMessageHeader = new SendMessage()
                    .setChatId(admin.getChatId())
                    .setText("Стол № " + profileData.getTableNum() +
                            " забронирован к " + profileData.getBookingTime() +
                            " на " + profileData.getPersonCount() +
                            " чел. " + " Имя - " + profileData.getName() +
                            ", тел. номер: " + profileData.getPhoneNumber())
                    .setReplyMarkup(getInlineKeyBoardApproveBooking(profileData.getChatId()));


            try {
                tBHCache.addMessageForAdmins(admin.getChatId(), telegramBot.execute(sendMessageHeader).getMessageId());

            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        });

        //Отправка сообщения-заголовка

    }

    public static void sendMessageToAdminForContinueBooking(UserProfileData profileData){

        UserDataCache userDataCache = AppContProvider.getApplicationContext().getBean(UserDataCache.class);

        List<UserProfileData> adminList = userDataCache.getAdminList();

        adminList.forEach(admin -> {
            SendMessage sendMessageHeader = new SendMessage()
                    .setChatId(admin.getChatId())
                    .setText("Стол № " + profileData.getTableNum() +
                            " забронирован к " + profileData.getBookingTime() +
                            " на " + profileData.getPersonCount() +
                            " чел. " + " Продлен на следующий сеанс\n"+
                            " Имя - " + profileData.getName() +
                            ", тел. номер: " + profileData.getPhoneNumber());

            sendMessage(sendMessageHeader);

        });

        //Отправка сообщения-заголовка

    }


    public static void sendMessageToAdminIfBookCancelled(UserProfileData profileData){

        UserDataCache userDataCache = AppContProvider.getApplicationContext().getBean(UserDataCache.class);

        List<UserProfileData> adminList = userDataCache.getAdminList();

        adminList.forEach(admin -> {
            SendMessage sendMessageHeader = new SendMessage()
                    .setChatId(admin.getChatId())
                    .setText("Отменен стол № " + profileData.getTableNum() +
                            " " + profileData.getBookingTime() +
                            " на " + profileData.getPersonCount() +
                            " чел. " + " Имя - " + profileData.getName() +
                            ", тел. номер: " + profileData.getPhoneNumber());
            sendMessage(sendMessageHeader);
        });

        userDataCache.clearDataAboutTablesByUserId(profileData.getChatId());
    }

    /*
        Отправка сообщений от администратора
     */

    public static SendMessage approveBooking(Update update){

        String inputMsg = (update.hasCallbackQuery() ? update.getCallbackQuery().getData() : "");

        long adminId = UserProfileData.getUserIdFromUpdate(update);
        long userId = Long.parseLong(inputMsg.split("=")[1]);


        TableBookingHistoryCache tableBookingHistoryCache = AppContProvider.getApplicationContext().getBean(TableBookingHistoryCache.class);
        UserDataCache userDataCache = AppContProvider.getApplicationContext().getBean(UserDataCache.class);
        ScheduledTasks scheduledTasks = AppContProvider.getApplicationContext().getBean(ScheduledTasks.class);

        TableBookingHistory tableBookingHistory = tableBookingHistoryCache.getTableBookingHistoryByUserId(userId);

        if(inputMsg.contains("booking_request_approve")){
            tableBookingHistory.setBookingStatus("approved");
            tableBookingHistoryCache.saveTableBookingHistory(userId, tableBookingHistory);
            scheduledTasks.addUserForNotification(userId, userDataCache.getUserProfileData(userId).getBookingTime());
            SendMessage sendMessage = new SendMessage().setChatId(userId).setText("Ваша бронь подтверждена. Ждем вас");

            //Если админ подтвердил бронь, то происходит удаление сообщений у других администраторов
            List<String> messageForAdmins = tableBookingHistoryCache.getMessagesForAdmins(userId);
            messageForAdmins.forEach(str -> {
                BookingCore.deleteMessage( new DeleteMessage()
                        .setChatId(str.split("_")[0])
                        .setMessageId(Integer.parseInt(str.split("_")[1])));

                sendMessage(new SendMessage()
                        .setChatId(str.split("_")[0])
                        .setText("Бронь на стол №" + tableBookingHistory.getTableInfo().getTableNumber() +
                                " к " + tableBookingHistory.getBookingTime() + " подтвердил администратор " +
                                userDataCache.getUserProfileData(userId).getName()));
            });

            tableBookingHistoryCache.clearMessagesForAdmins();

            sendMessage(sendMessage);

            return new SendMessage(adminId, "Бронь подтверждена");

        } else if(inputMsg.contains("booking_request_decline")){
            tableBookingHistoryCache.deleteByBookingTimeAndUserId(userDataCache.getUserProfileData(userId), 150);

            SendMessage sendMessage = new SendMessage().setChatId(userId).setText("Ваша бронь отменена");

            List<String> messageForAdmins = tableBookingHistoryCache.getMessagesForAdmins(userId);
            messageForAdmins.forEach(str -> {
                BookingCore.deleteMessage( new DeleteMessage()
                        .setChatId(str.split("_")[0])
                        .setMessageId(Integer.parseInt(str.split("_")[1])));

                sendMessage(new SendMessage()
                        .setChatId(str.split("_")[0])
                        .setText("Бронь на стол №" + tableBookingHistory.getTableInfo().getTableNumber() +
                                " к " + tableBookingHistory.getBookingTime() + " отменил администратор " +
                                userDataCache.getUserProfileData(userId).getName()));
            });

            tableBookingHistoryCache.clearMessagesForAdmins();

            sendMessage(sendMessage);

            return new SendMessage(adminId, "Бронь отменена");
        }

        return null;
    }

    public static void sendMessage(SendMessage sendMessage){
        CharlieBlackTelegramBot telegramBot = AppContProvider.getApplicationContext().getBean(CharlieBlackTelegramBot.class);

        try {

            telegramBot.execute(sendMessage);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static void editMessage(EditMessageText editMessageText){
        CharlieBlackTelegramBot telegramBot = AppContProvider.getApplicationContext().getBean(CharlieBlackTelegramBot.class);

        try {
            telegramBot.execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static SendMessage editMessage(long userId, Update update, String text){
        CharlieBlackTelegramBot telegramBot = AppContProvider.getApplicationContext().getBean(CharlieBlackTelegramBot.class);

        try {
            telegramBot.execute(new EditMessageText()
                    .setChatId(userId)
                    .setMessageId(update.getCallbackQuery().getMessage().getMessageId())
                    .setText(text));

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static SendMessage editMessage(long userId, Update update, String text, InlineKeyboardMarkup inlineKeyboardMarkup){
        CharlieBlackTelegramBot telegramBot = AppContProvider.getApplicationContext().getBean(CharlieBlackTelegramBot.class);

        try {
            telegramBot.execute(new EditMessageText()
                    .setChatId(userId)
                    .setMessageId(update.getCallbackQuery().getMessage().getMessageId())
                    .setText(text)
                    .setReplyMarkup(inlineKeyboardMarkup));

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void deleteMessage(DeleteMessage deleteMessage){
        CharlieBlackTelegramBot telegramBot = AppContProvider.getApplicationContext().getBean(CharlieBlackTelegramBot.class);

        try {
            telegramBot.execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    public static String getTableCapacity(int tableNum){

        TableInfoCache tableInfoCache = AppContProvider.getApplicationContext().getBean(TableInfoCache.class);
        return tableInfoCache.getTableInfoByTableNumAndBookingTime(tableNum, "15:00").getCapacity();

    }
}
