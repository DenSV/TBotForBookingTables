package ru.home.charlieblack_bot.botstate.handlers;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.home.charlieblack_bot.AppContProvider;
import ru.home.charlieblack_bot.botstate.BotStateEnum;
import ru.home.charlieblack_bot.cache.TableBookingHistoryCache;
import ru.home.charlieblack_bot.cache.UserDataCache;
import ru.home.charlieblack_bot.model.UserProfileData;
import ru.home.charlieblack_bot.service.ReplyMessagesService;

import static ru.home.charlieblack_bot.botstate.handlers.BookingCore.getInputMsgFromUpdate;

public abstract class AbstractBooking implements Booking {

    protected UserDataCache userDataCache;
    protected ReplyMessagesService messagesService;
    protected long userId;
    protected BotStateEnum currentBotStateEnum;
    protected UserProfileData profileData;
    protected TableBookingHistoryCache tableBookingHistoryCache;
    protected Update update;
    protected String inputMsg;

    public AbstractBooking(Update update) {
        this.userDataCache = AppContProvider.getApplicationContext().getBean(UserDataCache.class);
        this.messagesService = AppContProvider.getApplicationContext().getBean(ReplyMessagesService.class);
        this.userId = UserProfileData.getUserIdFromUpdate(update);
        this.currentBotStateEnum = userDataCache.getUsersCurrentBotState(userId);
        this.profileData = userDataCache.getUserProfileData(userId);
        this.tableBookingHistoryCache = AppContProvider.getApplicationContext().getBean(TableBookingHistoryCache.class);
        this.update = update;
        this.inputMsg = getInputMsgFromUpdate(update);

        this.profileData.setChatId(userId);
        this.profileData.setInputMsg(inputMsg);
        this.userDataCache.setUsersCurrentBotState(userId, BotStateEnum.getNextBotState(currentBotStateEnum));


        if (profileData.getUserRole() == null || !profileData.getUserRole().equals("admin"))
        this.profileData.setUserRole("user");

    }

    protected void setCurrentBotState(){
        userDataCache.setUsersCurrentBotState(userId, currentBotStateEnum);
    }

    protected void setAnotherBotState(BotStateEnum botState){
        userDataCache.setUsersCurrentBotState(userId, botState);
    }

    protected void setMainBotState(){
        userDataCache.setUsersCurrentBotState(userId, currentBotStateEnum.getMainBotState());
    }
    protected void saveBookingHistory(){
        tableBookingHistoryCache.save(profileData, 150);
    }

    protected void saveUserData(){
        userDataCache.saveUserProfileData(userId, profileData);
    }

    protected String getReplyMessage(){

        return "Заяка отправлена на рассмотрение. В ближайшее время " +
                "администратор свяжется с вами и подберет для вас столик";
    }
}
