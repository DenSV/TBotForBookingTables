package ru.home.charlieblack_bot.botstate.handlers.administrativeHandler.administartiveImp;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import ru.home.charlieblack_bot.botstate.BotStateEnum;
import ru.home.charlieblack_bot.botstate.handlers.AbstractBooking;
import ru.home.charlieblack_bot.botstate.handlers.Booking;
import ru.home.charlieblack_bot.keyboardbuilders.InlineButtons;
import ru.home.charlieblack_bot.keyboardbuilders.InlineButtonsBuilder;
import ru.home.charlieblack_bot.keyboardbuilders.KeyboardButtonsBuilder;
import ru.home.charlieblack_bot.model.TableBookingHistory;
import ru.home.charlieblack_bot.model.UserProfileData;

import java.util.ArrayList;
import java.util.List;

import static ru.home.charlieblack_bot.botstate.handlers.BookingCore.editMessage;

public class AdminGetBookHistory extends AbstractBooking implements Booking {


    public AdminGetBookHistory(Update update) {
        super(update);
    }

    @Override
    public SendMessage getResponse() {

        if (inputMsg.equals("Показать историю бронирования")){
            setAnotherBotState(BotStateEnum.ADMIN_START);

            return editMessage(update, getBookingHistory(), getInlineKeyboard());
        } else if(inputMsg.equals("Администраторы")) {
            setAnotherBotState(BotStateEnum.ADMIN_CHANGE_LIST);

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
                result[0] = result[0] + tableBookingHistory + "\n--------------------------------------\n");

        return result[0];
    }

    private SendMessage getAdminList() {
        List<UserProfileData> result = userDataCache.getAdminList();

        if(result.size() > 0){
            //вывести сообщениями список админов (сообщение + inline-кнопка)
            return getListOfAdminsInline(result);

        } else {
            return messagesService.getReplyMessage(userId, "Список администраторов пуст")
                    .setReplyMarkup(getReplyKeyBoard());
        }

    }

    private ReplyKeyboardMarkup getReplyKeyBoard() {
        List<String> buttons = new ArrayList<>();
        buttons.add("Добавить администратора");
        buttons.add("Вернуться на главную");

        return new KeyboardButtonsBuilder()
                .setOneTimeKeyBoard(true)
                .getButtons(buttons);

    }

    public InlineKeyboardMarkup getInlineKeyboard(){
        List<InlineButtons> buttons = new ArrayList<>();
        buttons.add(new InlineButtons("Назад", "Администрирование"));
        return new InlineButtonsBuilder().getButtons(buttons);
    }

    private SendMessage getListOfAdminsInline(List<UserProfileData> adminList){
        final String[] result = {"Администраторы:\n"};
        final int[] index = {0};

        List<InlineButtons> buttons = new ArrayList<>();


        adminList.forEach(admin -> {
            index[0]++;

            String you = (userId == admin.getChatId()? "(Вы)": "");
            buttons.add(new InlineButtons("Удалить администратора " + admin.getName() + you,
                    "delete_" + admin.getChatId()));
            result[0] = result[0] + index[0] + ". " + admin.getName() + "\n";

        });

        buttons.add(new InlineButtons("Добавить администратора"));
        buttons.add(new InlineButtons("Назад", "Администрирование"));

        return editMessage(update, result[0], new InlineButtonsBuilder().getButtons(buttons));

    }




}
