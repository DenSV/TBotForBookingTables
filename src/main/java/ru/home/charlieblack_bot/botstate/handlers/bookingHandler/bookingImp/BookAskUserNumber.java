package ru.home.charlieblack_bot.botstate.handlers.bookingHandler.bookingImp;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.home.charlieblack_bot.botstate.BotStateEnum;
import ru.home.charlieblack_bot.botstate.handlers.Booking;
import ru.home.charlieblack_bot.botstate.handlers.BookingAbstract;
import ru.home.charlieblack_bot.botstate.handlers.BookingCore;

public class BookAskUserNumber extends BookingAbstract implements Booking {

    //private String inputMsg;

    public BookAskUserNumber(Update update) {
        super(update);
        //this.inputMsg = update.getMessage().getText();

    }

    @Override
    public SendMessage getResponse() {

        setPhoneNumberAndCurrentBotState();
        return messagesService.getReplyMessage(userId,  "Столик забронирован на имя " + profileData.getName() +
                                                                    "\n" + "Телефон для связи: " + profileData.getPhoneNumber() +
                                                                    "\nЖдем вас к " + profileData.getBookingTime()).setReplyMarkup(BookingCore.getReplyKeyBoardBackToStartPage());

    }

    public void setPhoneNumberAndCurrentBotState(){

        setNewData();

        saveNewData();

    }

    private void setNewData(){
        userDataCache.getUserProfileData(userId).setChatId(userId);
        userDataCache.setUsersCurrentBotState(userId, BotStateEnum.getNextBotState(currentBotStateEnum));

        profileData.setPhoneNumber(inputMsg);
        /*if (update.getMessage().hasContact()) {
            profileData.setPhoneNumber(update.getMessage().getContact().getPhoneNumber());
        } else {
            profileData.setPhoneNumber(inputMsg);
        }*/
    }

    private void saveNewData(){
        userDataCache.saveUserProfileData(userId, profileData);
        tableBookingHistoryCache.save(profileData, 60);
    }
}
