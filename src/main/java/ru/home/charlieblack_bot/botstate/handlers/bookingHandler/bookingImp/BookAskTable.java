package ru.home.charlieblack_bot.botstate.handlers.bookingHandler.bookingImp;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.home.charlieblack_bot.botstate.BotStateEnum;
import ru.home.charlieblack_bot.botstate.handlers.Booking;
import ru.home.charlieblack_bot.botstate.handlers.AbstractBooking;
import ru.home.charlieblack_bot.model.TableInfo;

import java.util.List;

import static ru.home.charlieblack_bot.botstate.handlers.BookingCore.*;

public class BookAskTable extends AbstractBooking implements Booking {

    private boolean updateHasMessage;
    private boolean hasName;
    private boolean hasPhoneNumber;
    private List<TableInfo> freeTables;

    public BookAskTable(Update update) {
        super(update);
        this.freeTables = getFreeTables(profileData.getBookingTime());
        this.updateHasMessage = update.hasMessage() && update.getMessage().hasText();
        this.hasName = profileData.hasName();
        this.hasPhoneNumber = profileData.hasPhoneNumber();
    }

    @Override
    public SendMessage getResponse() {

        return processResponse();

    }

    private SendMessage processResponse(){

        return messagesService.getReplyMessage(userId, getReplyMessage())
                .setReplyMarkup(getKeyboard());

    }

    @Override
    protected String getReplyMessage() {
        if(updateHasMessage){
            return "Данные введены неправильно. " +
                    "Выберите количество человек";
        } else if(freeTables.isEmpty()){
            return "К сожалению, все столы на "
                    + profileData.getBookingTime()
                    + " заняты. Выберите другое время";
        } else if(!hasName){
            return "На какое имя бронировать?";
        } else if(!hasPhoneNumber){
            return "Введите номер мобильного телефона для связи";
        } else {
            return super.getReplyMessage();
        }
    }

    @Override
    protected void saveBookingHistory() {
        tableBookingHistoryCache.saveWithoutBookingTables(profileData, 150);
    }

    private ReplyKeyboard getKeyboard(){
        if(updateHasMessage){
            setCurrentBotState();
            return getInlineMessageButtons(freeTables);
        } else if(freeTables.isEmpty()){
            setAnotherBotState(BotStateEnum.BOOKING_ASK_TIME);
            return getInlineKeyBoardTimeButtons();
        } else if(!hasName){
            return null;
        } else if(!hasPhoneNumber){
            setAnotherBotState(BotStateEnum.BOOKING_ASK_NUMBER);
            return getReplyKeyboardContact();
        } else {
            setMainBotState();
            saveBookingHistory();
            //Отправка уведомления о бронировании админу
            sendMessageToAdmin(profileData);
            return getReplyKeyBoardBackToStartPage();
        }
    }
}
