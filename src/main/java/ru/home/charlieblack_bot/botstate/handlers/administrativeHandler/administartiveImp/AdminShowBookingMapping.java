package ru.home.charlieblack_bot.botstate.handlers.administrativeHandler.administartiveImp;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.home.charlieblack_bot.AppContProvider;
import ru.home.charlieblack_bot.botstate.BotStateEnum;
import ru.home.charlieblack_bot.botstate.handlers.Booking;
import ru.home.charlieblack_bot.botstate.handlers.AbstractBooking;
import ru.home.charlieblack_bot.keyboardbuilders.InlineButtons;
import ru.home.charlieblack_bot.keyboardbuilders.InlineButtonsBuilder;
import ru.home.charlieblack_bot.keyboardbuilders.InlineKeyboardRow;
import ru.home.charlieblack_bot.cache.TableInfoCache;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static ru.home.charlieblack_bot.botstate.handlers.BookingCore.editMessage;

public class AdminShowBookingMapping extends AbstractBooking implements Booking {

    private final static LocalTime START_TIME = LocalTime.of(15, 00);

    public AdminShowBookingMapping(Update update) {
        super(update);
    }

    @Override
    public SendMessage getResponse() {

        setAnotherBotState(BotStateEnum.ADMIN_BOOK_SHOW_BOOKING_MAP);

        return editMessage(update, getReplyMessage(), getKeyboard());

    }

    @Override
    protected String getReplyMessage() {
        if(inputMsg.equals("back")){
            return "Возможности администрирования";
        } else if(inputMsg.contains("booking_mapping")){
            int tableNum = Integer.parseInt(inputMsg.split("=")[1]);
            return "Стол № " + tableNum;
        } else if(inputMsg.contains("booking_map_back")){
            return "Выберите стол";
        } else {
            return "Выберите стол";
        }
    }

    private InlineKeyboardMarkup getKeyboard(){
        if(inputMsg.equals("back")){
            setAnotherBotState(BotStateEnum.ADMIN_START);
            return Admin.getButtons();
        } else if(inputMsg.contains("booking_mapping")){
            int tableNum = Integer.parseInt(inputMsg.split("=")[1]);
            return getInlineTimesOfTableWithRows(tableNum);
        } else if(inputMsg.contains("booking_map_back")){
            return getListOfTables();
        } else {
            return getListOfTables();
        }
    }

    public static InlineKeyboardMarkup getInlineTimesOfTableWithRows(int tableNum){

        TableInfoCache tableInfoCache = AppContProvider.getApplicationContext().getBean(TableInfoCache.class);

        LocalTime startTime = START_TIME;

        //красный круг 🔴

        //зеленый круг 🟢

        List<InlineKeyboardRow> rowList = new ArrayList<>();

        for (int j = 0; j < 5; j++) {

            InlineKeyboardRow inlineKeyboardRow = new InlineKeyboardRow();

            for (int k = 0; k < 4; k++) {

                String status = (tableInfoCache.hasBooked(tableNum, startTime.toString()) ? "\uD83D\uDD34" : "\uD83D\uDFE2");

                if(!startTime.equals(LocalTime.MIN) && !startTime.equals(LocalTime.MIN.plusMinutes(30))) {
                    inlineKeyboardRow.setButtonsInRow(new InlineButtons(
                            startTime.toString() + " " + status,
                            "admin_table_number=" + tableNum + "&time=" + startTime.toString()));

                }
                startTime = startTime.plusMinutes(30);

            }

            rowList.add(inlineKeyboardRow);

        }

        InlineKeyboardRow inlineKeyboardRow = new InlineKeyboardRow(new InlineButtons("Назад", "booking_map_back"),
                                                                    new InlineButtons("Вернуться на главную"));
        rowList.add(inlineKeyboardRow);

        return new InlineButtonsBuilder().getButtonsWithRows(rowList);

    }

    private InlineKeyboardMarkup getListOfTables(){

        List<InlineButtons> buttons = new ArrayList<>();

        for(int i = 1; i <= 10; i++){
            buttons.add(new InlineButtons("Стол № " + i, "booking_mapping=" + i));
        }

        buttons.add(new InlineButtons("Назад", "back"));
        buttons.add(new InlineButtons("Вернуться на главную"));

        return new InlineButtonsBuilder().getButtons(buttons);

    }
}
