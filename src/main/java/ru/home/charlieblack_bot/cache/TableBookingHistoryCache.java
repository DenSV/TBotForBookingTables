package ru.home.charlieblack_bot.cache;

import org.springframework.stereotype.Component;
import ru.home.charlieblack_bot.model.TableBookingHistory;
import ru.home.charlieblack_bot.model.TableInfo;
import ru.home.charlieblack_bot.model.UserProfileData;
import ru.home.charlieblack_bot.service.TableBookingHistoryService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TableBookingHistoryCache {

    private Map<Long, TableBookingHistory> tableBookingHistoryCache = new HashMap<>();
    private TableBookingHistoryService tableBookingHistoryService;
    private TableInfoCache tableInfoCache;

    public TableBookingHistoryCache(TableBookingHistoryService tableBookingHistoryService,
                                    TableInfoCache tableInfoCache){
        this.tableBookingHistoryService = tableBookingHistoryService;
        this.tableInfoCache = tableInfoCache;

        List<TableBookingHistory> tableBookingHistoryList = tableBookingHistoryService.getAllRows();
        tableBookingHistoryList.forEach(tableBookingHistory -> tableBookingHistoryCache.put(tableBookingHistory.getUserChatId(), tableBookingHistory));
    }

    public TableBookingHistory getTableBookingHistoryByUserId(long userId){
        return tableBookingHistoryCache.get(userId);
    }

    public void save(UserProfileData profileData, int duration){

        TableBookingHistory tableBookingHistory = initializeTableBookingHistory(profileData, duration);

        tableBookingHistoryCache.put(profileData.getChatId(), tableBookingHistory);
        tableBookingHistoryService.saveTableBookingHistory(tableBookingHistory);
    }

    private TableBookingHistory initializeTableBookingHistory(UserProfileData profileData, int duration){

        //Берем стол из БД
        TableBookingHistory tableBookingHistory = new TableBookingHistory();

        TableInfo tableForBooking = tableInfoCache.getTableInfoByTableNumAndBookingTime(profileData.getTableNum(), profileData.getBookingTime());

        tableBookingHistory.setBookingTime(profileData.getBookingTime());
        tableBookingHistory.setUserChatId(profileData.getChatId());
        //добавление пользователя
        tableBookingHistory.setBookingUser(profileData);
        //продолжительность бронирования
        tableBookingHistory.setDuration(duration);
        tableBookingHistory.setPersonCount(profileData.getPersonCount());
        //время когда добавлена запись в БД
        tableBookingHistory.setTimeReserved(new Date().toString());
        //добавить бронируем стол
        tableBookingHistory.setTableInfo(tableForBooking);
        tableBookingHistory.setTableNumId(tableForBooking.getId());

        return tableBookingHistory;

    }

    public boolean hasUserBooked(long userId){
        if(tableBookingHistoryCache.get(userId) != null){
            return true;
        } else {
            return false;
        }
    }


}
