package ru.home.charlieblack_bot.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.home.charlieblack_bot.model.TableInfo;
import ru.home.charlieblack_bot.repository.TableInfoPostgreRepository;

import java.time.LocalTime;
import java.util.List;

@Service
@Transactional
public class TableInfoService {
    private TableInfoPostgreRepository tableInfoPostgreRepository;

    public TableInfoService(TableInfoPostgreRepository tableInfoPostgreRepository) {
        this.tableInfoPostgreRepository = tableInfoPostgreRepository;
    }

    public boolean existsById(int id){
        return tableInfoPostgreRepository.existsById(id);
    }

    public TableInfo getTableInfoById(int id){ return tableInfoPostgreRepository.findById(id); }

    public List<TableInfo> getAllRows(){
        return tableInfoPostgreRepository.findAll();
    }

    public void saveTableInfo(int tableNum, LocalTime bookingTime, int capacity){
        TableInfo tableInfo = new TableInfo();

        tableInfo.setBookingTime(bookingTime);
        tableInfo.setBooked(false);
        tableInfo.setCapacity(capacity);
        tableInfo.setTableNumber(tableNum);
        tableInfo.setBookingName(null);

        tableInfoPostgreRepository.save(tableInfo);
    }

    public List<TableInfo> getAllFreeTablesOnCurrentTime(String bookingTime, boolean booked){
        return tableInfoPostgreRepository.findAllByBookingTimeAndBooked(bookingTime, booked);
    }

    public void saveAll(List<TableInfo> tableInfoList){
        tableInfoPostgreRepository.saveAll(tableInfoList);
    }

}
