package ru.home.charlieblack_bot.botstate.handlers.administrativeHandler.administartiveImp;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.home.charlieblack_bot.AppContProvider;
import ru.home.charlieblack_bot.botstate.BotStateEnum;
import ru.home.charlieblack_bot.botstate.handlers.Booking;
import ru.home.charlieblack_bot.botstate.handlers.AbstractBooking;
import ru.home.charlieblack_bot.botstate.handlers.BookingCore;
import ru.home.charlieblack_bot.cache.TableInfoCache;
import ru.home.charlieblack_bot.model.TableInfo;
import ru.home.charlieblack_bot.model.UserProfileData;

import java.util.ArrayList;
import java.util.List;

public class AdminBookUser extends AbstractBooking implements Booking {

    public AdminBookUser(Update update) {
        super(update);
    }

    @Override
    public SendMessage getResponse() {

        TableInfoCache tableInfoCache = AppContProvider.getApplicationContext().getBean(TableInfoCache.class);

        if(inputMsg.contains("admin_table_number=")) {


            int tableNum = Integer.parseInt(inputMsg.split("[&=]+")[1]);
            String bookingTime = inputMsg.split("[&=]+")[3];

            if(tableInfoCache.hasBooked(tableNum, bookingTime)){
                userDataCache.setUsersCurrentBotState(userId, currentBotStateEnum);
                TableInfo tableInfo = tableInfoCache.getTableInfoByTableNumAndBookingTime(tableNum, bookingTime);

                String userName = tableInfo.getBookingName().split(" ")[0];
                String userPhone = tableInfo.getBookingName().split(" ")[1];

                String bookingTimeForUser = tableBookingHistoryCache
                        .getTableBookingHistoryByPersonalData(tableInfo.getBookingName())
                        .getBookingTime();

                BookingCore.editMessage(new EditMessageText()
                        .setChatId(userId)
                        .setMessageId(update.getCallbackQuery().getMessage().getMessageId())
                        .setText("Этот стол занят к "+ bookingTimeForUser +" на имя - " +
                                userName + ". Телефон - " + userPhone)
                        .setReplyMarkup(getInlineButtonForCancelingBooking(tableNum, bookingTime)));

                return null;

            }

            UserProfileData userProfileData = userDataCache.getProfileData();

            userProfileData.setTableNum(tableNum);
            userProfileData.setPersonCount(BookingCore.getTableCapacity(userProfileData.getTableNum()));
            userProfileData.setBookingTime(bookingTime);
            userProfileData.setUserRole("user");

            userDataCache.setProfileData(userProfileData);

        } else if(inputMsg.contains("canceling_booking")){
            TableInfo tableInfo = tableInfoCache.getTableInfoByTableNumAndBookingTime(Integer.parseInt(inputMsg.split("[=&]")[1]), inputMsg.split("[=&]")[2]);
            userDataCache.setUsersCurrentBotState(userId, BotStateEnum.ADMIN_BOOK_SHOW_BOOKING_MAP);

            tableBookingHistoryCache.deleteByUserNameAndPhone(tableInfo.getBookingName());

            BookingCore.editMessage(new EditMessageText()
                    .setChatId(userId)
                    .setMessageId(update.getCallbackQuery().getMessage().getMessageId())
                    .setText("Бронь отменена")
                    .setReplyMarkup(AdminShowBookingMapping.getInlineTimesOfTable(tableInfo.getTableNumber())));

            return null;

        } else if(inputMsg.contains("back_to_timing_table")){
            userDataCache.setUsersCurrentBotState(userId, BotStateEnum.ADMIN_BOOK_SHOW_BOOKING_MAP);

            int tableNum = Integer.parseInt(inputMsg.split("=")[1]);

            BookingCore.editMessage(new EditMessageText()
                    .setChatId(userId)
                    .setMessageId(update.getCallbackQuery().getMessage().getMessageId())
                    .setText("Стол № " + tableNum)
                    .setReplyMarkup(AdminShowBookingMapping.getInlineTimesOfTable(tableNum)));
            return null;
        }

        BookingCore.editMessage(new EditMessageText()
                .setChatId(userId)
                .setMessageId(update.getCallbackQuery().getMessage().getMessageId())
                .setText("Введите имя гостя"));

        return null;
    }

    private InlineKeyboardMarkup getInlineButtonForCancelingBooking(int tableNum, String bookingTime){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        List<InlineKeyboardButton> inlineKeyboardButtonsRow1 = new ArrayList<>();
        inlineKeyboardButtonsRow1.add(new InlineKeyboardButton()
                .setText("Отменить бронь")
                .setCallbackData("canceling_booking="+ tableNum + "&" + bookingTime));

        List<InlineKeyboardButton> inlineKeyboardButtonsRow2 = new ArrayList<>();
        inlineKeyboardButtonsRow2.add(new InlineKeyboardButton()
                .setText("Назад")
                .setCallbackData("back_to_timing_table=" + tableNum));
        inlineKeyboardButtonsRow2.add(new InlineKeyboardButton()
                .setText("Вернуться на главную")
                .setCallbackData("Вернуться на главную"));

        rowList.add(inlineKeyboardButtonsRow1);
        rowList.add(inlineKeyboardButtonsRow2);

        return inlineKeyboardMarkup.setKeyboard(rowList);
    }
}
