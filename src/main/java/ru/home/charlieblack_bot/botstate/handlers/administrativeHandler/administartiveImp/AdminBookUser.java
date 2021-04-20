package ru.home.charlieblack_bot.botstate.handlers.administrativeHandler.administartiveImp;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.home.charlieblack_bot.AppContProvider;
import ru.home.charlieblack_bot.botstate.BotStateEnum;
import ru.home.charlieblack_bot.botstate.handlers.*;
import ru.home.charlieblack_bot.cache.TableInfoCache;
import ru.home.charlieblack_bot.keyboardbuilders.InlineButtons;
import ru.home.charlieblack_bot.keyboardbuilders.InlineButtonsBuilder;
import ru.home.charlieblack_bot.keyboardbuilders.InlineKeyboardRow;
import ru.home.charlieblack_bot.model.TableInfo;
import ru.home.charlieblack_bot.model.UserProfileData;

import java.util.ArrayList;
import java.util.List;

public class AdminBookUser extends AbstractBooking implements Booking {

    private TableInfoCache tableInfoCache;

    public AdminBookUser(Update update) {
        super(update);
        this.tableInfoCache = AppContProvider.getApplicationContext().getBean(TableInfoCache.class);

    }

    @Override
    public SendMessage getResponse() {

        if(inputMsg.contains("admin_table_number=")) {


            int tableNum = Integer.parseInt(inputMsg.split("[&=]+")[1]);
            String bookingTime = inputMsg.split("[&=]+")[3];

            if(tableInfoCache.hasBooked(tableNum, bookingTime)){
                setCurrentBotState();

                TableInfo tableInfo = getTableInfo(tableNum, bookingTime);

                String userName = tableInfo.getUserNameFromBookingName();
                String userPhone = tableInfo.getUserPhoneFromBookingName();

                String bookingTimeForUser = getBookingTimeForUser(tableInfo);

                return BookingCore.editMessage(update,
                        "Этот стол занят к "+ bookingTimeForUser +" на имя - " +
                        userName + ". Телефон - " + userPhone,
                        getInlineButtonForCancelingBooking(tableNum, bookingTime));

            }

            setUserData(tableNum, bookingTime);

        } else if(inputMsg.contains("canceling_booking")){

            int tableNum = Integer.parseInt(inputMsg.split("[=&]")[1]);
            String bookingTime = inputMsg.split("[=&]")[2];
            TableInfo tableInfo = getTableInfo(tableNum, bookingTime);

            setAnotherBotState(BotStateEnum.ADMIN_BOOK_SHOW_BOOKING_MAP);
            deleteBookingHistory(tableInfo);

            return BookingCore.editMessage(update, "Бронь отменена",
                    AdminShowBookingMapping.getInlineTimesOfTableWithRows(tableInfo.getTableNumber()));


        } else if(inputMsg.contains("back_to_timing_table")){
            int tableNum = Integer.parseInt(inputMsg.split("=")[1]);

            setAnotherBotState(BotStateEnum.ADMIN_BOOK_SHOW_BOOKING_MAP);

            return BookingCore.editMessage(update, "Cтол № " + tableNum,
                    AdminShowBookingMapping.getInlineTimesOfTableWithRows(tableNum));

        }

        return BookingCore.editMessage(update, "Введите имя гостя", getInlineButtonForCancel(userDataCache.getProfileData()));

    }

    private InlineKeyboardMarkup getInlineButtonForCancel(UserProfileData profileData){
        List<InlineButtons> inlineButtons = new ArrayList<>();
        inlineButtons.add(new InlineButtons("Отмена", "cancel_book=" + profileData.getTableNum()));
        return new InlineButtonsBuilder().getButtons(inlineButtons);
    }

    private InlineKeyboardMarkup getInlineButtonForCancelingBooking(int tableNum, String bookingTime){

        List<InlineKeyboardRow> inlineRowList = new ArrayList<>();

        inlineRowList.add(new InlineKeyboardRow(new InlineButtons("Отменить бронь", "canceling_booking="+ tableNum + "&" + bookingTime)));
        inlineRowList.add(new InlineKeyboardRow(new InlineButtons("Назад", "back_to_timing_table=" + tableNum),
                                                new InlineButtons("Вернуться на главную")));
        return new InlineButtonsBuilder().getButtonsWithRows(inlineRowList);

    }

    private void setUserData(int tableNum, String bookingTime){
        UserProfileData userProfileData = userDataCache.getProfileData();

        userProfileData.setTableNum(tableNum);
        userProfileData.setPersonCount(BookingCore.getTableCapacity(userProfileData.getTableNum()));
        userProfileData.setBookingTime(bookingTime);
        userProfileData.setUserRole("user");

        userDataCache.setProfileData(userProfileData);
    }

    private TableInfo getTableInfo(int tableNum, String bookingTime){
        return tableInfoCache.getTableInfoByTableNumAndBookingTime(tableNum, bookingTime);
    }

    private String getBookingTimeForUser(TableInfo tableInfo){
        return tableBookingHistoryCache
                .getTableBookingHistoryByPersonalData(tableInfo.getBookingName())
                .getBookingTime();
    }

    private void deleteBookingHistory(TableInfo tableInfo){
        tableBookingHistoryCache.deleteByUserNameAndPhone(tableInfo.getBookingName());
    }
}
