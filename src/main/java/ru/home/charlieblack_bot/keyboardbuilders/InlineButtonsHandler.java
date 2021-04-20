package ru.home.charlieblack_bot.keyboardbuilders;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.home.charlieblack_bot.AppContProvider;
import ru.home.charlieblack_bot.botstate.handlers.BookingCore;
import ru.home.charlieblack_bot.cache.TableBookingHistoryCache;
import ru.home.charlieblack_bot.cache.UserDataCache;
import ru.home.charlieblack_bot.model.UserProfileData;

import java.time.LocalTime;

public class InlineButtonsHandler {

    public static SendMessage getSendMessageFromInlineButtons(Update update){

        if(!update.hasCallbackQuery()) return null;

        long userId = UserProfileData.getUserIdFromUpdate(update);
        String callBackQueryData = update.getCallbackQuery().getData();

        UserDataCache userDataCache = AppContProvider.getApplicationContext().getBean(UserDataCache.class);
        UserProfileData profileData = userDataCache.getUserProfileData(userId);


        if(callBackQueryData.contains("booking_request"))
            return BookingCore.approveBooking(update);

        if (callBackQueryData.contains("booking_continue_true")){
            TableBookingHistoryCache tableBookingHistoryCache = AppContProvider.getApplicationContext().getBean(TableBookingHistoryCache.class);

            profileData.setBookingTime(LocalTime.parse(profileData.getBookingTime()).plusHours(2).plusMinutes(30).toString());
            userDataCache.saveUserProfileData(userId, profileData);
            tableBookingHistoryCache.save(profileData, 150);

            BookingCore.sendMessageToAdminForContinueBooking(profileData);

            return new SendMessage(userId, "Ваша бронь продлена");
        } else if (callBackQueryData.contains("booking_continue_false")){
            return new SendMessage(userId, "Ваша бронь закончилась");
        }

        return null;
    }

}
