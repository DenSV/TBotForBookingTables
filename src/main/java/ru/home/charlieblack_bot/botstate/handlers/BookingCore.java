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
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.home.charlieblack_bot.AppContProvider;
import ru.home.charlieblack_bot.CharlieBlackTelegramBot;
import ru.home.charlieblack_bot.cache.TableBookingHistoryCache;
import ru.home.charlieblack_bot.cache.TableInfoCache;
import ru.home.charlieblack_bot.cache.UserDataCache;
import ru.home.charlieblack_bot.keyboardbuilders.InlineButtons;
import ru.home.charlieblack_bot.keyboardbuilders.InlineButtonsBuilder;
import ru.home.charlieblack_bot.keyboardbuilders.InlineKeyboardRow;
import ru.home.charlieblack_bot.keyboardbuilders.KeyboardButtonsBuilder;
import ru.home.charlieblack_bot.model.TableBookingHistory;
import ru.home.charlieblack_bot.model.TableInfo;
import ru.home.charlieblack_bot.model.UserProfileData;
import ru.home.charlieblack_bot.service.TableBookingHistoryService;
import ru.home.charlieblack_bot.service.TableInfoService;

import java.time.LocalTime;
import java.util.*;


public class BookingCore {


    public static List<TableInfo> getFreeTables(String bookingTime) {

        //объявление необходимых сервисов;
        TableInfoService tableInfoService = AppContProvider.getApplicationContext().getBean(TableInfoService.class);

        //список свободных столов из БД
        List<TableInfo> freeTableList = new ArrayList<>(tableInfoService.getAllFreeTablesOnCurrentTime(bookingTime, false));

        Collections.sort(freeTableList);

        return freeTableList;

    }

    public static boolean isTableBooked(int tableNum, String booking_time){

        TableBookingHistoryService tableBookingHistoryService = AppContProvider.getApplicationContext().getBean(TableBookingHistoryService.class);

        List<TableBookingHistory> tableBookingHistories = tableBookingHistoryService.getTablesByNum(tableNum);

        if(tableBookingHistories.size() == 0){
            return false;
        } else {

            for (TableBookingHistory tableBookingHistory : tableBookingHistories) {

                //нижний предел
                LocalTime bookedTime = LocalTime.parse(tableBookingHistory.getBookingTime());

                LocalTime bookingTime = LocalTime.parse(booking_time);

                LocalTime leftTime = bookedTime.minusMinutes(tableBookingHistory.getDuration());
                LocalTime rightTime = bookedTime.plusMinutes(tableBookingHistory.getDuration());


                return bookingTime.isAfter(leftTime) && bookingTime.isBefore(rightTime);

            }
        }

        return false;

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

        return new KeyboardButtonsBuilder()
                .getButtons("Вернуться на главную");

    }

    public static InlineKeyboardMarkup getInlineKeyBoardBackToStartPage(){
        return new InlineButtonsBuilder().getButtons("Вернуться на главную");
    }

    public static ReplyKeyboardMarkup getReplyKeyboardContact(){

        return new KeyboardButtonsBuilder()
                .setOneTimeKeyBoard(true)
                .getButtons("Отправить свой контакт ☎",
                        "Вернуться на главную");

    }

    public static InlineKeyboardMarkup getAdressInlineButton(){

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton adressButton = new InlineKeyboardButton();
        adressButton.setText("Открыть адрес в Яндекс.Картах");
        adressButton.setUrl("https://yandex.ru/maps/10750/ramenskoe/?ll=38.216398%2C55.578913&mode=poi&poi%5Bpoint%5D=38.216195%2C55.578989&poi%5Buri%5D=ymapsbm1%3A%2F%2Forg%3Foid%3D1382133920&z=19.52");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(adressButton);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(new InlineButtons("Вернуться на главную").getInlineKeyboardButton());

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup getReplyKeyBoardChangeUserInfo(){

        return new KeyboardButtonsBuilder().getButtons(
                "Изменить имя",
                "Изменить номер",
                "Вернуться на главную");

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

        List<InlineKeyboardButton> inlineKeyboardButtonsBackToStart = new ArrayList<>();
        inlineKeyboardButtonsBackToStart.add(new InlineKeyboardButton()
                .setText("Вернуться на главную")
                .setCallbackData("Вернуться на главную"));

        rowList.add(inlineKeyboardButtonsBackToStart);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;

    }

    public static InlineKeyboardMarkup getInlineMessageButtons(List<TableInfo> freeTables){
        Set<String> uniqueCapacity = new LinkedHashSet<>();

        List<InlineKeyboardRow> inlineRowList = new ArrayList<>();

        freeTables.forEach(tableInfo -> {
            String capacity = tableInfo.getCapacity();

            if(!uniqueCapacity.contains(capacity)) {
                inlineRowList.add(new InlineKeyboardRow(
                        new InlineButtons(capacity + " чел.",
                                "person_count=" + capacity)));
            }

            uniqueCapacity.add(capacity);

        });

        inlineRowList.sort(Comparator.comparing(o -> o.getRow().get(0).getText()));

        inlineRowList.add(new InlineKeyboardRow(new InlineButtons("Вернуться на главную")));
        return new InlineButtonsBuilder().getButtonsWithRows(inlineRowList);


    }

    public static InlineKeyboardMarkup getInlineKeyBoardPersonCount(){

        List<InlineKeyboardRow> inlineRowList = new ArrayList<>();
        inlineRowList.add(new InlineKeyboardRow(new InlineButtons("1-3", "person_count-3")));
        inlineRowList.add(new InlineKeyboardRow(new InlineButtons("4-6", "person_count-6")));
        inlineRowList.add(new InlineKeyboardRow(new InlineButtons("7-9", "person_count-9")));
        inlineRowList.add(new InlineKeyboardRow(new InlineButtons("больше 9", "person_count-10")));

        return new InlineButtonsBuilder().getButtonsWithRows(inlineRowList);

    }

    private static ReplyKeyboard getInlineKeyBoardApproveBooking(UserProfileData profileData) {

        long userId = profileData.getChatId();
        String capacity = profileData.getPersonCount();
        String bookingTime = profileData.getBookingTime();
        List<TableInfo> freeTables = getFreeTables(bookingTime);
        List<InlineButtons> buttons = new ArrayList<>();



        freeTables.stream()
                .filter(tableInfo -> tableInfo.getCapacity().equals(capacity))
                .forEach(tableInfo -> {
                    buttons.add(new InlineButtons("Стол № " + tableInfo.getTableNumber(),
                            "booking_request_approve=" + userId +
                                    "&table_num=" + tableInfo.getTableNumber() +
                                    "&capacity=" + capacity));
                });

        buttons.add(new InlineButtons("Отменить", "booking_request_decline=" + userId));

        return new InlineButtonsBuilder().getButtons(buttons);

    }

    public static InlineKeyboardMarkup getInlinekeyboardForBookingContinue() {

        List<InlineButtons> buttons = new ArrayList<>();
        buttons.add(new InlineButtons("Подтвердить", "booking_continue_true"));
        buttons.add(new InlineButtons("Отменить", "booking_continue_false"));

        return new InlineButtonsBuilder().getButtons(buttons);
    }

    /*
        Отправка сообщений администратору
     */

    public static void sendMessageToAdmin(UserProfileData profileData){

        //Метод должен получить список свободных столов и выбрать те, которые отобраны по количеству человек

        UserDataCache userDataCache = AppContProvider.getApplicationContext().getBean(UserDataCache.class);

        TableBookingHistoryCache tBHCache = AppContProvider.getApplicationContext().getBean(TableBookingHistoryCache.class);

        CharlieBlackTelegramBot telegramBot = AppContProvider.getApplicationContext().getBean(CharlieBlackTelegramBot.class);
        List<UserProfileData> adminList = userDataCache.getAdminList();

        adminList.forEach(admin -> {
            SendMessage sendMessageHeader = new SendMessage()
                    .setChatId(admin.getChatId())
                    .setText("Гость хочет забронировать стол к " + profileData.getBookingTime() +
                            //"Стол № " + profileData.getTableNum() +
                            //" забронирован к " + profileData.getBookingTime() +
                            " на " + profileData.getPersonCount() +
                            " чел. " + " Имя - " + profileData.getName() +
                            ", тел. номер: " + profileData.getPhoneNumber())
                    .setReplyMarkup(getInlineKeyBoardApproveBooking(profileData));


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
        long userId = Long.parseLong(inputMsg.split("[&=]+")[1]);


        TableBookingHistoryCache tableBookingHistoryCache = AppContProvider.getApplicationContext().getBean(TableBookingHistoryCache.class);
        UserDataCache userDataCache = AppContProvider.getApplicationContext().getBean(UserDataCache.class);
        TableInfoCache tableInfoCache = AppContProvider.getApplicationContext().getBean(TableInfoCache.class);

        TableBookingHistory tableBookingHistory = tableBookingHistoryCache.getTableBookingHistoryByUserId(userId);

        if(inputMsg.contains("booking_request_approve")){

            UserProfileData userProfileData = userDataCache.getUserProfileData(userId);

            int tableNum = Integer.parseInt(inputMsg.split("[&=]+")[3]);

            userProfileData.setTableNum(tableNum);

            userDataCache.saveUserProfileData(userId, userProfileData);

            TableInfo tableInfo = tableInfoCache.getTableInfoByTableNumAndBookingTime(tableNum, userDataCache.getUserProfileData(userId).getBookingTime());

            tableBookingHistory.setTableInfo(tableInfo);
            tableBookingHistory.setTableNumId(tableInfo.getId());

            tableBookingHistoryCache.saveTableBookingHistory(userId, tableBookingHistory);
            SendMessage sendMessage = new SendMessage().setChatId(userId).setText("Ваша бронь подтверждена. Ждем вас");

            //Если админ подтвердил бронь, то происходит удаление сообщений у других администраторов
            List<String> messageForAdmins = tableBookingHistoryCache.getMessagesForAdmins(userId);
            messageForAdmins.forEach(str -> {

                deleteMessage(str);

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
            tableBookingHistoryCache.deleteByBookingTimeAndUserIdWithoutBooking(userDataCache.getUserProfileData(userId));

            SendMessage sendMessage = new SendMessage().setChatId(userId).setText("Ваша бронь отменена");

            List<String> messageForAdmins = tableBookingHistoryCache.getMessagesForAdmins(userId);
            messageForAdmins.forEach(str -> {
                deleteMessage(str);

                sendMessage(new SendMessage()
                        .setChatId(str.split("_")[0])
                        .setText("Бронь отменена " + //"Бронь на стол №" + tableBookingHistory.getTableInfo().getTableNumber() +
                                " к " + tableBookingHistory.getBookingTime() + " отменил администратор " +
                                userDataCache.getUserProfileData(userId).getName()));
            });

            tableBookingHistoryCache.clearMessagesForAdmins();

            sendMessage(sendMessage);

            return new SendMessage(adminId, "Бронь отменена");
        }

        return null;
    }


    public static void notifyForAdminsAboutBookingFromPhoneCall(long userId, String message){

        TableBookingHistoryCache tableBookingHistoryCache = AppContProvider.getApplicationContext().getBean(TableBookingHistoryCache.class);
        UserDataCache userDataCache = AppContProvider.getApplicationContext().getBean(UserDataCache.class);

        String messageForOtherAdmins = message.replace("забронирован", "забронировал администратор " +
                userDataCache.getUserProfileData(userId).getName() );


        tableBookingHistoryCache.getMessagesForAdmins(userId).forEach(str -> {

            sendMessage(new SendMessage()
                    .setChatId(str.split("_")[0])
                    .setText(messageForOtherAdmins));

        });

    }


    public static void sendMessage(SendMessage sendMessage){
        CharlieBlackTelegramBot telegramBot = AppContProvider.getApplicationContext().getBean(CharlieBlackTelegramBot.class);

        try {

            telegramBot.execute(sendMessage);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(Update update, String text, ReplyKeyboard replyKeyboard){
        CharlieBlackTelegramBot telegramBot = AppContProvider.getApplicationContext().getBean(CharlieBlackTelegramBot.class);

        try {
            telegramBot.execute(new SendMessage()
                    .setChatId(UserProfileData.getUserIdFromUpdate(update))
                    .setText(text)
                    .setReplyMarkup(replyKeyboard));

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(Update update, String text){

        try {
            CharlieBlackTelegramBot.getBeanFromContext().execute(
                    new SendMessage()
                    .setChatId(UserProfileData.getUserIdFromUpdate(update))
                    .setText(text));

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

    public static SendMessage editMessage(Update update, String text){
        CharlieBlackTelegramBot telegramBot = AppContProvider.getApplicationContext().getBean(CharlieBlackTelegramBot.class);

        try {
            telegramBot.execute(new EditMessageText()
                    .setChatId(update.getCallbackQuery().getMessage().getChatId())
                    .setMessageId(update.getCallbackQuery().getMessage().getMessageId())
                    .setText(text));

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static SendMessage editMessage(Update update, String text, InlineKeyboardMarkup inlineKeyboardMarkup){
        CharlieBlackTelegramBot telegramBot = AppContProvider.getApplicationContext().getBean(CharlieBlackTelegramBot.class);

        try {
            telegramBot.execute(new EditMessageText()
                    .setChatId(update.getCallbackQuery().getMessage().getChatId())
                    .setMessageId(update.getCallbackQuery().getMessage().getMessageId())
                    .setText(text)
                    .setReplyMarkup(inlineKeyboardMarkup)
                    .setParseMode("Markdown"));

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void deleteMessage(String adminId){
        CharlieBlackTelegramBot telegramBot = AppContProvider.getApplicationContext().getBean(CharlieBlackTelegramBot.class);

        try {
            telegramBot.execute(new DeleteMessage()
                    .setChatId(adminId.split("_")[0])
                    .setMessageId(Integer.parseInt(adminId.split("_")[1])));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static String getTableCapacity(int tableNum){

        TableInfoCache tableInfoCache = AppContProvider.getApplicationContext().getBean(TableInfoCache.class);
        return tableInfoCache.getTableInfoByTableNumAndBookingTime(tableNum, "15:00").getCapacity();

    }
}
