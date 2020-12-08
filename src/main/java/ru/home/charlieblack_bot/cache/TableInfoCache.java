package ru.home.charlieblack_bot.cache;

import org.springframework.stereotype.Component;
import ru.home.charlieblack_bot.model.TableInfo;
import ru.home.charlieblack_bot.service.TableInfoService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TableInfoCache {
    private Map<String, TableInfo> tableInfoCache = new HashMap<>();
    private TableInfoService tableInfoService;

    public TableInfoCache(TableInfoService tableInfoService) {
        this.tableInfoService = tableInfoService;

        List<TableInfo> tableInfoList = tableInfoService.getAllRows();

        tableInfoList.forEach(tableInfo -> tableInfoCache.put(tableInfo.getTableNumber() + tableInfo.getBookingTime(), tableInfo));

    }

    public TableInfo getTableInfoByTableNumAndBookingTime(int tableNum, String bookingTime){
        return tableInfoCache.get(tableNum + bookingTime);
    }

    public void saveAll(List<TableInfo> tableInfoList){
        for (TableInfo tableInfo: tableInfoList) {
            tableInfoCache.get(tableInfo.getTableNumber() + tableInfo.getBookingTime()).setBooked(tableInfo.isBooked());
            tableInfoService.saveAll(tableInfoList);
        }

    }
}