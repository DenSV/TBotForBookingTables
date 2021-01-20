package ru.home.charlieblack_bot.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.home.charlieblack_bot.model.TableBookingHistory;
import ru.home.charlieblack_bot.model.UserProfileData;
import ru.home.charlieblack_bot.repository.TableBookingHistoryPostgreRepository;

import java.util.List;

@Service
@Transactional
public class TableBookingHistoryService implements DataService{

    private TableBookingHistoryPostgreRepository tableBookingHistoryPostgreRepository;

    public TableBookingHistoryService(TableBookingHistoryPostgreRepository tableBookingHistoryPostgreRepository) {
        this.tableBookingHistoryPostgreRepository = tableBookingHistoryPostgreRepository;
    }

    @Async
    public void saveTableBookingHistory(TableBookingHistory tableBookingHistory){
        tableBookingHistoryPostgreRepository.save(tableBookingHistory);
    }

    public List<TableBookingHistory> getAllRows(){
        return tableBookingHistoryPostgreRepository.findAll();
    }

    public List<TableBookingHistory> getTablesByNum(int tableNum){
        return tableBookingHistoryPostgreRepository.findByTableNumId(tableNum);
    }

    public void save(TableBookingHistory tableBookingHistory){
        tableBookingHistoryPostgreRepository.save(tableBookingHistory);
    }

    @Async
    public void deleteByBookingTimeAndUserID(UserProfileData profileData, String timeForBooking){
        tableBookingHistoryPostgreRepository.deleteByBookingTimeAndUserChatId(timeForBooking, profileData.getChatId());
    }

    @Async
    public void deleteByPersonalData(String userNameAndPhone){
        tableBookingHistoryPostgreRepository.deleteByPersonalData(userNameAndPhone);
    }

    @Async
    public void deleteAllRows(){
        tableBookingHistoryPostgreRepository.deleteAll();
    }

}

