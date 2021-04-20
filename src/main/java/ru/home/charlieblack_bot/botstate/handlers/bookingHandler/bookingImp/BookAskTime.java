package ru.home.charlieblack_bot.botstate.handlers.bookingHandler.bookingImp;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.home.charlieblack_bot.AppContProvider;
import ru.home.charlieblack_bot.ScheduledTasks;
import ru.home.charlieblack_bot.botstate.BotStateEnum;
import ru.home.charlieblack_bot.botstate.handlers.Booking;
import ru.home.charlieblack_bot.botstate.handlers.AbstractBooking;
import ru.home.charlieblack_bot.model.TableInfo;

import java.util.List;

import static ru.home.charlieblack_bot.botstate.handlers.BookingCore.*;

public class BookAskTime extends AbstractBooking implements Booking {

    private boolean updateHasMessage;

    private List<TableInfo> freeTables;

    public BookAskTime(Update update) {
        super(update);
        this.freeTables = getFreeTables(profileData.getBookingTime());
        this.updateHasMessage = update.hasMessage() && update.getMessage().hasText();
    }

    @Override
    public SendMessage getResponse() {

        return editMessage(update, getReplyMessage(), getKeyBoard());

    }

    private void removeUserForNotification(){
        ScheduledTasks scheduledTasks = AppContProvider.getApplicationContext().getBean(ScheduledTasks.class);
        scheduledTasks.removeUserForNotification(userId);
    }

    private void deleteBookingHistory(){
        tableBookingHistoryCache.deleteByBookingTimeAndUserId(profileData);
    }

    @Override
    protected String getReplyMessage() {
        if(updateHasMessage){
            return "Данные введены некорректно. Выберите время из списка";
        } else if(inputMsg.equals("Отменить бронь")){
            return "Ваша бронь отменена";
        } else if(inputMsg.equals("Изменить время")){
            return "Выберите другое время";
        } else {
            return "Ваше время: *" + profileData.getBookingTime() + "*\n" + "Выберите количество человек";
        }
    }

    private InlineKeyboardMarkup getKeyBoard(){
        if(updateHasMessage){
            setCurrentBotState();

            return getInlineKeyBoardTimeButtons();
        } else if(inputMsg.equals("Отменить бронь")){
            removeUserForNotification();
            deleteBookingHistory();
            setAnotherBotState(BotStateEnum.SHOW_STARTPAGE);
            sendMessageToAdminIfBookCancelled(profileData);

            return getInlineKeyBoardBackToStartPage();
        } else if(inputMsg.equals("Изменить время")){
            deleteBookingHistory();
            setCurrentBotState();

            return getInlineKeyBoardTimeButtons();
        } else {
            return getInlineMessageButtons(freeTables);
        }
    }
}
