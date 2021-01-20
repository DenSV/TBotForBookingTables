package ru.home.charlieblack_bot.cache;

import org.springframework.stereotype.Component;
import ru.home.charlieblack_bot.AppContProvider;
import ru.home.charlieblack_bot.model.TableBookingHistory;
import ru.home.charlieblack_bot.model.TableInfo;
import ru.home.charlieblack_bot.model.UserProfileData;
import ru.home.charlieblack_bot.service.TableBookingHistoryService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;

@Component
public class TableBookingHistoryCache {

    private Map<String, TableBookingHistory> tableBookingHistoryCache = new HashMap<>();
    private TableBookingHistoryService tableBookingHistoryService;
    private TableInfoCache tableInfoCache;
    private List<String> messagesForAdmins = new ArrayList<>();


    public TableBookingHistoryCache(TableBookingHistoryService tableBookingHistoryService,
                                    TableInfoCache tableInfoCache){
        this.tableBookingHistoryService = tableBookingHistoryService;
        this.tableInfoCache = tableInfoCache;

        List<TableBookingHistory> tableBookingHistoryList = tableBookingHistoryService.getAllRows();
        tableBookingHistoryList.forEach(tableBookingHistory -> tableBookingHistoryCache.put(tableBookingHistory.getUserChatId()+
                "_"+ tableBookingHistory.getTimeReserved(), tableBookingHistory));
    }

    public TableBookingHistory getTableBookingHistoryByUserId(long userId){


        for (String key: tableBookingHistoryCache.keySet()) {
            if(key.split("_")[0].equals(String.valueOf(userId))){
                return tableBookingHistoryCache.get(key);
            }
        }
        return null;

    }

    public TableBookingHistory getTableBookingHistoryByPersonalData(String personalData){

        for (Map.Entry<String, TableBookingHistory> entry: tableBookingHistoryCache.entrySet()) {
            if(entry.getValue().getPersonalData().equals(personalData)){
                return entry.getValue();
            }
        }

        return null;
    }

    public void save(UserProfileData profileData, int duration){

        TableBookingHistory tableBookingHistory = initializeTableBookingHistory(profileData, duration);

        tableBookingHistoryCache.put(profileData.getChatId() +
                "_" + tableBookingHistory.getTimeReserved(), tableBookingHistory);

        makesTableBooked(tableBookingHistory);

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
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        tableBookingHistory.setTimeReserved(dateFormat.format(date));
        //добавить бронируем стол
        tableBookingHistory.setTableInfo(tableForBooking);
        tableBookingHistory.setTableNumId(tableForBooking.getId());
        tableBookingHistory.setPersonalData(profileData.getName() + " " + profileData.getPhoneNumber());
        if(profileData.getChatId() == 0) {
            tableBookingHistory.setBookingStatus("approved");
        } else {
            tableBookingHistory.setBookingStatus("considering");
        }

        return tableBookingHistory;

    }

    public void deleteByBookingTimeAndUserId(UserProfileData profileData, int duration){

        TableBookingHistory tableBookingHistoryForRemove = new TableBookingHistory();
        String keyForRemoving = "";

        for (String key: tableBookingHistoryCache.keySet()) {
            if(key.split("_")[0].equals(String.valueOf(profileData.getChatId()))){
                keyForRemoving = key;
                tableBookingHistoryForRemove = tableBookingHistoryCache.get(key);
                break;
            }
        }

        if(!keyForRemoving.equals("")) tableBookingHistoryCache.remove(keyForRemoving);

        String bookingTime = profileData.getBookingTime();
        int tableNum = profileData.getTableNum();

        makeTablesNonBooked(tableBookingHistoryForRemove);

        TableBookingHistoryCache tableBookingHistoryCache = AppContProvider.getApplicationContext().getBean(TableBookingHistoryCache.class);


        List<TableBookingHistory> tableBookingHistoryList = tableBookingHistoryCache.getBookingTimesOfTable(tableNum, bookingTime);
        if(tableBookingHistoryList.size() > 0) {

            tableBookingHistoryList.forEach(this::makesTableBooked);

        }

        tableBookingHistoryService.deleteByBookingTimeAndUserID(profileData, bookingTime);


    }

    public void deleteByUserNameAndPhone(String userNameAndPhone){

        TableBookingHistory tableBookingHistoryForRemove = new TableBookingHistory();
        String keyForRemove = "";

        for (Map.Entry<String, TableBookingHistory> entry: tableBookingHistoryCache.entrySet()) {
            if(entry.getValue().getPersonalData().equals(userNameAndPhone)){
                tableBookingHistoryForRemove = entry.getValue();
                keyForRemove = entry.getKey();
            }
        }

        if(!keyForRemove.equals("")) tableBookingHistoryCache.remove(keyForRemove);

        makeTablesNonBooked(tableBookingHistoryForRemove);

        TableBookingHistoryCache tableBookingHistoryCache = AppContProvider
                .getApplicationContext()
                .getBean(TableBookingHistoryCache.class);


        List<TableBookingHistory> tableBookingHistoryList = tableBookingHistoryCache
                .getBookingTimesOfTable(tableBookingHistoryForRemove.getTableInfo().getTableNumber(),
                                        tableBookingHistoryForRemove.getBookingTime());
        if(tableBookingHistoryList.size() > 0) {

            tableBookingHistoryList.forEach(this::makesTableBooked);

        }

        tableBookingHistoryService.deleteByPersonalData(userNameAndPhone);

    }

    public void deleteAllHistory(){
        tableBookingHistoryCache.clear();
        tableBookingHistoryService.deleteAllRows();
    }

    public boolean hasUserBooked(long userId){

        for (String key: tableBookingHistoryCache.keySet()) {
            if(key.split("_")[0].equals(String.valueOf(userId))){
                return true;
            }
        }

        return false;

    }

    public void saveTableBookingHistory(long userId, TableBookingHistory tableBookingHistory){
        tableBookingHistoryCache.put(userId +
                "_" + tableBookingHistory.getTimeReserved(), tableBookingHistory);

        makesTableBooked(tableBookingHistory);

        tableBookingHistoryService.save(tableBookingHistory);
    }

    public boolean hasConsidering(long userId){
        for (String key: tableBookingHistoryCache.keySet()) {
            if(key.split("_")[0].equals(String.valueOf(userId))){
                if(tableBookingHistoryCache.get(key).getBookingStatus().equals("considering"))
                    return true;
            }
        }
        return false;
    }

    public List<TableBookingHistory> getAllApprovedBookingHistory(){
        List<TableBookingHistory> result = new ArrayList<>();
        tableBookingHistoryCache.forEach((aLong, tableBookingHistory) -> {
            if(tableBookingHistory.getBookingStatus().equals("approved"))
                result.add(tableBookingHistory);
        });

        return result;
    }

    public List<TableBookingHistory> getBookingTimesOfTable(int tableNum, String timeForBooking){
        List<TableBookingHistory> result = new ArrayList<>();

        tableBookingHistoryCache.forEach((key, tableBookingHistory) -> {
            if(tableBookingHistory.getTableInfo().getTableNumber() == tableNum
                    && !tableBookingHistory.getBookingTime().equals(timeForBooking)){
                result.add(tableBookingHistory);
            }
        });

        Collections.reverse(result);

        return result;
    }

    private void makesTableBooked(TableBookingHistory tableBookingHistory){
        List<TableInfo> tableInfoList = new ArrayList<>();

        LocalTime bookingTime = LocalTime.parse(tableBookingHistory.getBookingTime());

        LocalTime leftTime = bookingTime.minusMinutes(tableBookingHistory.getDuration() - 30);

        LocalTime rightTime = bookingTime.plusMinutes(tableBookingHistory.getDuration());

        LocalTime tempLocalTime = leftTime;


        TableInfo tableInfoFromBookingHistory = tableBookingHistory.getTableInfo();

        while (!tempLocalTime.equals(rightTime)){
            TableInfo tableInfo = tableInfoCache.getTableInfoByTableNumAndBookingTime(tableBookingHistory.getTableInfo().getTableNumber(), tempLocalTime.toString());
            if(tableInfo != null) {
                if(!tableInfo.isBooked() && tableInfo.getId() < tableInfoFromBookingHistory.getId()) {
                    tableInfo.setBooked(true);
                    tableInfo.setBookingName(tableBookingHistory.getPersonalData());
                    tableInfoList.add(tableInfo);
                }

                if(tableInfo.getId() >= tableInfoFromBookingHistory.getId()){
                    tableInfo.setBooked(true);
                    tableInfo.setBookingName(tableBookingHistory.getPersonalData());
                    tableInfoList.add(tableInfo);
                }
            }

            tempLocalTime = tempLocalTime.plusMinutes(15);
        }

        tableInfoCache.saveAll(tableInfoList);
    }

    private void makeTablesNonBooked(TableBookingHistory tableBookingHistory){

        int duration = tableBookingHistory.getDuration();
        String timeForBooking = tableBookingHistory.getBookingTime();
        int tableNum = tableBookingHistory.getTableInfo().getTableNumber();

        List<TableInfo> tableInfoList = new ArrayList<>();

        LocalTime bookingTime = LocalTime.parse(timeForBooking);

        LocalTime leftTime = bookingTime.minusMinutes(duration);

        LocalTime rightTime = bookingTime.plusMinutes(duration);

        LocalTime tempLocalTime = leftTime;

        while (!tempLocalTime.equals(rightTime)){
            TableInfo tableInfo = tableInfoCache.getTableInfoByTableNumAndBookingTime(tableNum, tempLocalTime.toString());
            if(tableInfo != null) {
                tableInfo.setBooked(false);
                tableInfo.setBookingName(null);
                tableInfoList.add(tableInfo);
            }

            tempLocalTime = tempLocalTime.plusMinutes(15);
        }

        tableInfoCache.saveAll(tableInfoList);
    }

    public void addMessageForAdmins(long adminId, int messageId){
        messagesForAdmins.add(adminId + "_" + messageId);
    }

    public List<String> getMessagesForAdmins(long adminId){

        List<String> result = new ArrayList<>(messagesForAdmins);

        for(int i = 0; i < messagesForAdmins.size(); i++){
            if(Long.parseLong(messagesForAdmins.get(i).split("_")[0]) == adminId){
                result.remove(i);
                break;
            }
        }

        return result;
    }

    public void clearMessagesForAdmins(){
        messagesForAdmins.clear();
    }
}
