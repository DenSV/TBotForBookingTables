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
import ru.home.charlieblack_bot.cache.TableInfoCache;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static ru.home.charlieblack_bot.botstate.handlers.BookingCore.editMessage;

public class AdminShowBookingMapping extends AbstractBooking implements Booking {

    public AdminShowBookingMapping(Update update) {
        super(update);
    }

    @Override
    public SendMessage getResponse() {

        userDataCache.setUsersCurrentBotState(userId, BotStateEnum.ADMIN_BOOK_SHOW_BOOKING_MAP);


        if(inputMsg.contains("booking_mapping")){
            return getTimesOfTable();
        } else if(inputMsg.contains("booking_map_back")){
            editMessage( new EditMessageText()
                    .setChatId(update.getCallbackQuery().getMessage().getChatId())
                    .setMessageId(update.getCallbackQuery().getMessage().getMessageId())
                    .setText("Выберите стол")
                    .setReplyMarkup(getListOfTables()));

            return null;
        }


        return getBookingMap();
    }

    private SendMessage getBookingMap(){

        return new SendMessage(userId, "Выберите стол").setReplyMarkup(getListOfTables());

    }

    private SendMessage getTimesOfTable(){

        int tableNum = Integer.parseInt(inputMsg.split("=")[1]);

        editMessage( new EditMessageText()
                .setChatId(update.getCallbackQuery().getMessage().getChatId())
                .setMessageId(update.getCallbackQuery().getMessage().getMessageId())
                .setText("Стол № " + tableNum)
                .setReplyMarkup(getInlineTimesOfTable(tableNum)));

        return null;
    }

    public static InlineKeyboardMarkup getInlineTimesOfTable(int tableNum){

        TableInfoCache tableInfoCache = AppContProvider.getApplicationContext().getBean(TableInfoCache.class);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        LocalTime startTime = LocalTime.of(15, 00);

        //красный круг 🔴

        //зеленый круг 🟢

        for(int j = 0; j < 5; j++){
            List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
            for (int k = 0; k < 4; k++) {

                String status = (tableInfoCache.hasBooked(tableNum, startTime.toString()) ? "\uD83D\uDD34" : "\uD83D\uDFE2");

                if(!startTime.equals(LocalTime.MIN) && !startTime.equals(LocalTime.MIN.plusMinutes(30))) {
                    inlineKeyboardButtons.add(new InlineKeyboardButton()
                            .setText(startTime.toString() + " " + status)
                            .setCallbackData("admin_table_number=" + tableNum + "&time=" + startTime.toString()));
                }
                startTime = startTime.plusMinutes(30);
            }

            rowList.add(inlineKeyboardButtons);

        }

        List<InlineKeyboardButton> inlineKeyboardButtonBack = new ArrayList<>();
        inlineKeyboardButtonBack.add(new InlineKeyboardButton()
                .setText("Назад")
                .setCallbackData("booking_map_back"));

        List<InlineKeyboardButton> inlineKeyboardButtonBackToStartPage = new ArrayList<>();
        inlineKeyboardButtonBack.add(new InlineKeyboardButton()
                .setText("Вернуться на главную")
                .setCallbackData("Вернуться на главную"));

        rowList.add(inlineKeyboardButtonBack);
        rowList.add(inlineKeyboardButtonBackToStartPage);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;

    }

    private InlineKeyboardMarkup getListOfTables(){

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();

        for(int i = 1; i <= 10; i++){
            List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
            inlineKeyboardButtons.add(new InlineKeyboardButton()
                    .setText("Стол № " + i)
                    .setCallbackData("booking_mapping=" + i));
            rowList.add(inlineKeyboardButtons);
        }

        List<InlineKeyboardButton> inlineKeyboardButtonBack = new ArrayList<>();
        inlineKeyboardButtonBack.add(new InlineKeyboardButton()
                .setText("Вернуться на главную")
                .setCallbackData("Вернуться на главную"));

        rowList.add(inlineKeyboardButtonBack);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

}
