package ru.home.charlieblack_bot.botstate.handlers.bookingHandler.bookingImp;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.home.charlieblack_bot.botstate.handlers.Booking;
import ru.home.charlieblack_bot.botstate.handlers.AbstractBooking;

import static ru.home.charlieblack_bot.botstate.handlers.BookingCore.*;

public class BookAskName extends AbstractBooking implements Booking {

    public BookAskName(Update update) { super(update); }

    @Override
    public SendMessage getResponse() {

        return processResponse(profileData.getPhoneNumber());

    }

    private SendMessage processResponse(String phoneNumber){

        userDataCache.saveUserProfileData(userId, profileData);

        if (phoneNumber == null){
            return messagesService.getReplyMessage(userId,"Введите номер мобильного телефона для связи")
                    .setReplyMarkup(getReplyKeyboardContact());


        } else {

            tableBookingHistoryCache.save(profileData, 150);
            userDataCache.setUsersCurrentBotState(userId, currentBotStateEnum.getMainBotState());

            //Отправка уведомления о бронировании админу
            sendMessageToAdmin(profileData);

            return messagesService.getReplyMessage(userId, getReplyMessageIfPhoneNull())
                    .setReplyMarkup(getReplyKeyBoardBackToStartPage());
        }


    }

    private String getReplyMessageIfPhoneNull(){

        return "Столик № "+ profileData.getTableNum() + " " + profileData.getBookingTime() +
                " забронирован на имя " + profileData.getName() +
                "\n" + "Телефон для связи: " + profileData.getPhoneNumber() +
                "\n Заяка отправлена на рассмотрение. Ждите ответа от администратора";

    }

}
