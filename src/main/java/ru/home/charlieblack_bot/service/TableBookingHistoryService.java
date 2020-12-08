package ru.home.charlieblack_bot.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.home.charlieblack_bot.cache.TableInfoCache;
import ru.home.charlieblack_bot.model.TableBookingHistory;
import ru.home.charlieblack_bot.model.TableInfo;
import ru.home.charlieblack_bot.repository.TableBookingHistoryPostgreRepository;
import ru.home.charlieblack_bot.repository.TableInfoPostgreRepository;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TableBookingHistoryService implements DataService{

    private TableBookingHistoryPostgreRepository tableBookingHistoryPostgreRepository;
    private TableInfoPostgreRepository tableInfoPostgreRepository;
    private TableInfoCache tableInfoCache;

    public TableBookingHistoryService(TableBookingHistoryPostgreRepository tableBookingHistoryPostgreRepository,
                                      TableInfoPostgreRepository tableInfoPostgreRepository,
                                      TableInfoCache tableInfoCache) {
        this.tableBookingHistoryPostgreRepository = tableBookingHistoryPostgreRepository;
        this.tableInfoPostgreRepository = tableInfoPostgreRepository;
        this.tableInfoCache = tableInfoCache;

    }

    @Async
    public void saveTableBookingHistory(TableBookingHistory tableBookingHistory){

        //бронирование столов попадающих в промежуток (bookingTime - duration; bookingTime + duration)

        List<TableInfo> tableInfoList = new ArrayList<>();

        LocalTime bookingTime = LocalTime.parse(tableBookingHistory.getBookingTime());

        LocalTime leftTime = bookingTime.minusMinutes(tableBookingHistory.getDuration());

        LocalTime rightTime = bookingTime.plusMinutes(tableBookingHistory.getDuration());

        LocalTime tempLocalTime = leftTime;

        while (tempLocalTime.isBefore(rightTime)){
            TableInfo tableInfo = tableInfoCache.getTableInfoByTableNumAndBookingTime(tableBookingHistory.getTableInfo().getTableNumber(), tempLocalTime.toString());
            tableInfo.setBooked(true);

            tableInfoList.add(tableInfo);

            tempLocalTime = tempLocalTime.plusMinutes(15);
        }

        tableInfoList.get(0).setBooked(false);

        tableInfoCache.saveAll(tableInfoList);

        tableBookingHistoryPostgreRepository.save(tableBookingHistory);

    }

    public boolean existsByTableNumAndBookingTime(String bookingTime, long userId){
        return tableBookingHistoryPostgreRepository.existsByBookingTimeAndUserChatId(bookingTime, userId);
    }

    public TableBookingHistory getTableBookingHistory(int tableNum, String bookingTime){
        return tableBookingHistoryPostgreRepository.findByTableInfoAndBookingTime(tableNum, bookingTime);
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
}

