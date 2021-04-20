package ru.home.charlieblack_bot.cache;

import org.springframework.stereotype.Component;
import ru.home.charlieblack_bot.AppContProvider;
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
        tableInfoService.getAllRows().forEach(tableInfo -> tableInfoCache.put(getKey(tableInfo), tableInfo));

    }

    public static TableInfoCache getBeanFromContext(){
        return AppContProvider.getApplicationContext().getBean(TableInfoCache.class);
    }

    public TableInfo getTableInfoByTableNumAndBookingTime(int tableNum, String bookingTime){
        return tableInfoCache.get(tableNum + bookingTime);
    }

    public void saveAll(List<TableInfo> tableInfoList){

        tableInfoList.forEach(tableInfo -> tableInfoCache.get(getKey(tableInfo)).setBooked(tableInfo.isBooked()));

        tableInfoService.saveAll(tableInfoList);

    }

    public void makeAllTablesFree(){

        tableInfoCache.forEach((s, tableInfo) -> {
            if(tableInfo.isBooked()) {
                tableInfo.setBooked(false);
                tableInfo.setBookingName(null);
            }
        });

        tableInfoService.makeAllTableFree();

    }

    public boolean hasBooked(int tableNum, String bookingTime){
        if (tableInfoCache.get(tableNum+bookingTime) == null){
            return false;
        } else {
            return tableInfoCache.get(tableNum+bookingTime).isBooked();
        }

    }

    private static String getKey(TableInfo tableInfo){
        return tableInfo.getTableNumber() + tableInfo.getBookingTime();
    }
}
