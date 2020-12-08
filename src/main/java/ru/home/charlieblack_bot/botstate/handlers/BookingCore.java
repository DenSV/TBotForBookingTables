package ru.home.charlieblack_bot.botstate.handlers;

/*
    Задача класса:
        - вывести списком доступное время для юзера(с учетом текущего времени);
        - вывести списком доступные столы на это время(учитывая время, коли-во человек);
        -
*/


import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.home.charlieblack_bot.AppContProvider;
import ru.home.charlieblack_bot.model.TableBookingHistory;
import ru.home.charlieblack_bot.model.TableInfo;
import ru.home.charlieblack_bot.service.TableBookingHistoryService;
import ru.home.charlieblack_bot.service.TableInfoService;

import java.time.LocalTime;
import java.util.*;


public class BookingCore {


    /*public BookingCore(TableInfoService tableInfoService){
        this.tableInfoService = tableInfoService;
    }*/

    public static List<TableInfo> getFreeTables(String bookingTime) {

        //объявление необходимых сервисов;
        TableInfoService tableInfoService = AppContProvider.getApplicationContext().getBean(TableInfoService.class);

        //TableBookingHistoryService tableBookingHistoryService = ApplicationContextProvider.getApplicationContext().getBean(TableBookingHistoryService.class);

        //список свободных столов из БД
        List<TableInfo> freeTableList = new ArrayList<>(tableInfoService.getAllFreeTablesOnCurrentTime(bookingTime, false));

        Collections.sort(freeTableList);

        return freeTableList;

/*Map<Integer, Set> allTablesWithAllTime = new LinkedHashMap<>();

        for(int i = 0; i < tableInfoList1.size(); i++){
            allTablesWithAllTime.put(i, allTimesSet);
        }*//*

        //нижний предел
        //верхний предел


        LocalTime startTime = LocalTime.of(15, 00);

        for (TableInfo tableInfo: tableInfoList1) {
            for(int i = 0; i < 4; i++){
                    tableInfo.setBookingTime(startTime);
                    resultTableInfo1.add(tableInfo);
                    startTime = startTime.plusMinutes(15);
                //startTime = startTime.plusHours(1);
            }
        }

        for (int i = 0; i < resultTableInfo.size(); i++) {

        }
*/




    }

    public static boolean isTableBooked(int tableNum, String booking_time){
        boolean result = false;

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


                if (bookingTime.isAfter(leftTime) && bookingTime.isBefore(rightTime)) {
                    return true;
                } else {
                    return false;
                }

            }
        }

        return result;

    }

    private Map<Integer, LinkedHashSet> removeTimesFromTable(Map<Integer, LinkedHashSet<String>> tables, String bookingTime){

        for (Map.Entry<Integer, LinkedHashSet<String>> entry: tables.entrySet()) {


            //LinkedHashSet<String> newSet = new LinkedHashSet<>(entry.getValue());

            entry.getValue().removeIf(tableTime -> isTableBooked(entry.getKey(), tableTime));

            //entry.getValue() = newSet;
        }

        return null;

    }

    public static void addTables(){
        TableInfoService tableInfoService = AppContProvider.getApplicationContext().getBean(TableInfoService.class);


        int tableNum = 9;

        while (tableNum != 11) {

            LocalTime startTime = LocalTime.of(15, 00);

            int capacity = 6;


                //for (int j = 0; j < 37; j++) {
                while (startTime != LocalTime.parse("00:00")){
                    tableInfoService.saveTableInfo(tableNum, startTime, capacity);
                    startTime = startTime.plusMinutes(15);
                    //startTime = startTime.plusHours(1);
                }

            tableNum++;
        }

        String string = "";

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

}
