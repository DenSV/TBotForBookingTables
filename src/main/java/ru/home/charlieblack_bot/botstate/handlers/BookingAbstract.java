package ru.home.charlieblack_bot.botstate.handlers;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.home.charlieblack_bot.AppContProvider;
import ru.home.charlieblack_bot.botstate.BotStateEnum;
import ru.home.charlieblack_bot.cache.TableBookingHistoryCache;
import ru.home.charlieblack_bot.cache.UserDataCache;
import ru.home.charlieblack_bot.model.UserProfileData;
import ru.home.charlieblack_bot.service.ReplyMessagesService;

public abstract class BookingAbstract implements Booking {

    protected UserDataCache userDataCache;
    protected ReplyMessagesService messagesService;
    protected long userId;
    protected BotStateEnum currentBotStateEnum;
    protected UserProfileData profileData;
    protected TableBookingHistoryCache tableBookingHistoryCache;
    protected Update update;
    protected String inputMsg;

    public BookingAbstract(Update update) {
        this.userDataCache = AppContProvider.getApplicationContext().getBean(UserDataCache.class);
        this.messagesService = AppContProvider.getApplicationContext().getBean(ReplyMessagesService.class);
        this.userId = UserProfileData.getUserIdFromUpdate(update);
        this.currentBotStateEnum = userDataCache.getUsersCurrentBotState(userId);
        this.profileData = userDataCache.getUserProfileData(userId);
        this.tableBookingHistoryCache = AppContProvider.getApplicationContext().getBean(TableBookingHistoryCache.class);
        this.update = update;
        this.inputMsg = BookingCore.getInputMsgFromUpdate(update);
    }



}
