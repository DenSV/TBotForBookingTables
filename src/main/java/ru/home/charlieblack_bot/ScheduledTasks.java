package ru.home.charlieblack_bot;

import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.home.charlieblack_bot.botstate.handlers.BookingCore;
import ru.home.charlieblack_bot.cache.TableBookingHistoryCache;
import ru.home.charlieblack_bot.cache.TableInfoCache;
import ru.home.charlieblack_bot.cache.UserDataCache;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;


@Component
public class ScheduledTasks {

    private TableBookingHistoryCache tableBookingHistoryCache;
    private TableInfoCache tableInfoCache;
    private UserDataCache userDataCache;
    private Map<Long, String> bookingTimeMap = new HashMap<>();

    public ScheduledTasks() {

        ApplicationContext context = AppContProvider.getApplicationContext();

        this.tableBookingHistoryCache = context.getBean(TableBookingHistoryCache.class);
        this.tableInfoCache = context.getBean(TableInfoCache.class);
        this.userDataCache = context.getBean(UserDataCache.class);

    }

    @Async
    @Scheduled(cron = "0 0 6 * * *", zone = "Europe/Moscow")
    public void startBookingControlling(){
        //очищение истории бронирования
        tableBookingHistoryCache.deleteAllHistory();

        //освобождение столов
        tableInfoCache.makeAllTablesFree();

        //очищение информации о столах у пользователей
        userDataCache.clearDataAboutTables();

        bookingTimeMap.clear();
    }

    @Async
    @Scheduled(cron = "0 0/15 15-23 * * *", zone = "Europe/Moscow")
    public void notifyAboutBooking(){

        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        dateFormat.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Europe/Moscow")));
        Date date = new Date();
        String dateStr = dateFormat.format(date);

        bookingTimeMap.forEach((userId, bookingTime) -> {
            if(bookingTime.equals(dateStr)) {
                BookingCore.sendMessage(new SendMessage()
                        .setChatId(userId)
                        .setText("Ваша бронь заканчивается. Выберите для продолжения:")
                        .setReplyMarkup(BookingCore.getInlinekeyboardForBookingContinue()));
            }
        });

    }

    public void addUserForNotification(long userId, String bookingTime){

        LocalTime timeForNotification = LocalTime
                .parse(bookingTime)
                .plusHours(2)
                .plusMinutes(15);

        bookingTimeMap.put(userId, timeForNotification.toString());
    }

    public void removeUserForNotification(long userId){
        bookingTimeMap.remove(userId);
    }
}
