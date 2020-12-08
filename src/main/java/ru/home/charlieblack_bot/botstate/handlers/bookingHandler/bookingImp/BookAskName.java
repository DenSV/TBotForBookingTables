package ru.home.charlieblack_bot.botstate.handlers.bookingHandler.bookingImp;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.home.charlieblack_bot.botstate.BotStateEnum;
import ru.home.charlieblack_bot.botstate.handlers.Booking;
import ru.home.charlieblack_bot.botstate.handlers.BookingAbstract;
import ru.home.charlieblack_bot.botstate.handlers.BookingCore;

public class BookAskName extends BookingAbstract implements Booking {

    public BookAskName(Update update) { super(update); }

    @Override
    public SendMessage getResponse() {

        return processResponse(profileData.getPhoneNumber());

    }

    private SendMessage processResponse(String phoneNumber){

        String replyMessage = "";

        setNameAndCurrentBotState();

        if (phoneNumber == null){
            return messagesService.getReplyMessage(userId,"Введите номер мобильного телефона для связи").setReplyMarkup(BookingCore.getReplyKeyboardContact());


        } else {

            tableBookingHistoryCache.save(profileData, 60);
            userDataCache.setUsersCurrentBotState(userId, BotStateEnum.getMainBotState(currentBotStateEnum));
            return messagesService.getReplyMessage(userId, getReplyMessageIfPhoneNull()).setReplyMarkup(BookingCore.getReplyKeyBoardBackToStartPage());
        }


    }

    private void setNameAndCurrentBotState(){
        profileData.setChatId(userId);
        profileData.setName(inputMsg);
        userDataCache.setUsersCurrentBotState(userId, BotStateEnum.getNextBotState(currentBotStateEnum));

        userDataCache.saveUserProfileData(userId, profileData);

    }

    private String getReplyMessageIfPhoneNull(){

        return "Столик забронирован на имя " + profileData.getName() +
                "\n" + "Телефон для связи: " + profileData.getPhoneNumber() +
                "\nЖдем вас к " + profileData.getBookingTime();

    }

}
