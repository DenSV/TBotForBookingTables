package ru.home.charlieblack_bot.botstate.handlers.bookingHandler.bookingImp;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import ru.home.charlieblack_bot.botstate.handlers.Booking;
import ru.home.charlieblack_bot.botstate.handlers.AbstractBooking;

import static ru.home.charlieblack_bot.botstate.handlers.BookingCore.*;

public class BookAskName extends AbstractBooking implements Booking {

    private boolean hasPhoneNumber;

    public BookAskName(Update update) {
        super(update);
        this.hasPhoneNumber = profileData.hasPhoneNumber();
    }

    @Override
    public SendMessage getResponse() {

        return processResponse();

    }

    private SendMessage processResponse(){

        saveUserData();

        return messagesService.getReplyMessage(userId, getReplyMessage()).setReplyMarkup(getKeyboard());

    }

    @Override
    protected String getReplyMessage() {
        if(!hasPhoneNumber){
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
        if(!hasPhoneNumber){
            return getReplyKeyboardContact();
        } else {
            saveBookingHistory();
            setMainBotState();
            //Отправка уведомления о бронировании админу
            sendMessageToAdmin(profileData);
            return getReplyKeyBoardBackToStartPage();
        }
    }

}
