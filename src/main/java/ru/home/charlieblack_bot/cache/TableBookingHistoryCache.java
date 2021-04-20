package ru.home.charlieblack_bot.cache;

import org.springframework.stereotype.Component;
import ru.home.charlieblack_bot.AppContProvider;
import ru.home.charlieblack_bot.ScheduledTasks;
import ru.home.charlieblack_bot.model.TableBookingHistory;
import ru.home.charlieblack_bot.model.TableInfo;
import ru.home.charlieblack_bot.model.UserProfileData;
import ru.home.charlieblack_bot.service.TableBookingHistoryService;

import javax.annotation.PostConstruct;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class TableBookingHistoryCache {

    private Map<String, TableBookingHistory> tableBookingHistoryCache = new HashMap<>();
    private TableBookingHistoryService tableBookingHistoryService;
    private TableInfoCache tableInfoCache;
    private UserDataCache userDataCache;
    private List<String> messagesForAdmins = new ArrayList<>();


    public TableBookingHistoryCache(TableBookingHistoryService tableBookingHistoryService,
                                    TableInfoCache tableInfoCache,
                                    UserDataCache userDataCache){
        this.tableBookingHistoryService = tableBookingHistoryService;
        this.tableInfoCache = tableInfoCache;
        this.userDataCache = userDataCache;

    }

    @PostConstruct
    public void postConstruct(){
        tableBookingHistoryService.getAllRows()
                .forEach(tableBookingHistory -> tableBookingHistoryCache.put(getKey(tableBookingHistory), tableBookingHistory));
    }

    public static TableBookingHistoryCache getBeanFromContext(){
        return AppContProvider.getApplicationContext().getBean(TableBookingHistoryCache.class);
    }

    public TableBookingHistory getTableBookingHistoryByUserId(Long userId){

        return tableBookingHistoryCache.entrySet()
                .stream()
                .filter(entry -> keyIsEqualsUserId(entry.getKey(), userId))
                .findFirst()
                .get()
                .getValue();

    }

    public TableBookingHistory getTableBookingHistoryByPersonalData(String personalData){

        return tableBookingHistoryCache.values()
                .stream()
                .filter(value -> value.getPersonalData().equals(personalData))
                .findFirst()
                .get();
    }

    public void save(UserProfileData profileData, int duration){

        TableBookingHistory tableBookingHistory = initializeTableBookingHistory(profileData, duration);

        tableBookingHistoryCache.put(profileData.getChatId() +
                "_" + tableBookingHistory.getTimeReserved(), tableBookingHistory);

        makesTableBooked(tableBookingHistory);

        tableBookingHistoryService.saveTableBookingHistory(tableBookingHistory);

    }

    public void saveWithoutBookingTables(UserProfileData profileData, int duration){

        TableBookingHistory tableBookingHistory = initializeTableBookingHistory(profileData, duration);

        tableBookingHistoryCache.put(profileData.getChatId() +
                "_" + tableBookingHistory.getTimeReserved(), tableBookingHistory);

        tableBookingHistoryService.saveTableBookingHistory(tableBookingHistory);

    }

    private TableBookingHistory initializeTableBookingHistory(UserProfileData profileData, int duration){

        //Берем стол из БД
        TableBookingHistory tableBookingHistory = new TableBookingHistory();

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


        tableBookingHistory.setPersonalData(profileData.getName() + " " + profileData.getPhoneNumber());
        if(profileData.getChatId() == 0) {
            tableBookingHistory.setBookingStatus("approved");
            TableInfo tableForBooking =
                    tableInfoCache.getTableInfoByTableNumAndBookingTime(
                            profileData.getTableNum(),
                            profileData.getBookingTime());
            tableBookingHistory.setTableInfo(tableForBooking);
            tableBookingHistory.setTableNumId(tableForBooking.getId());
        } else {
            tableBookingHistory.setBookingStatus("considering");
        }

        return tableBookingHistory;

    }

    public void deleteByBookingTimeAndUserId(UserProfileData profileData){

        TableBookingHistory tableBookingHistoryForRemove = tableBookingHistoryCache.entrySet()
                .stream().filter(entry -> keyIsEqualsUserId(entry.getKey(), profileData.getChatId()))
                .findFirst()
                .get()
                .getValue();


        tableBookingHistoryCache.keySet()
                .removeIf(key -> keyIsEqualsUserId(key, profileData.getChatId()));

        makeTablesNonBooked(tableBookingHistoryForRemove);

        List<TableBookingHistory> tableBookingHistoryList = getBookingTimesOfTable(tableBookingHistoryForRemove);
        if(tableBookingHistoryList.size() > 0) {

            tableBookingHistoryList.forEach(this::makesTableBooked);

        }

        tableBookingHistoryService.deleteByBookingTimeAndUserID(profileData);


    }

    public void deleteByBookingTimeAndUserIdWithoutBooking(UserProfileData profileData){

        TableBookingHistory tableBookingHistoryForRemove = tableBookingHistoryCache.entrySet()
                .stream().filter(entry -> keyIsEqualsUserId(entry.getKey(), profileData.getChatId()))
                .findFirst()
                .get()
                .getValue();


        tableBookingHistoryCache.keySet()
                .removeIf(key -> keyIsEqualsUserId(key, profileData.getChatId()));

        tableBookingHistoryService.deleteByBookingTimeAndUserID(profileData);


    }

    public void deleteByUserNameAndPhone(String userNameAndPhone){

        TableBookingHistory tableBookingHistoryForRemove = tableBookingHistoryCache.values()
                .stream()
                .filter(value -> value.getPersonalData().equals(userNameAndPhone))
                .findFirst()
                .get();

        tableBookingHistoryCache.entrySet()
                .removeIf(value -> value.getValue().getPersonalData().equals(userNameAndPhone));

        makeTablesNonBooked(tableBookingHistoryForRemove);

        List<TableBookingHistory> tableBookingHistoryList = getBookingTimesOfTable(tableBookingHistoryForRemove);
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

        return tableBookingHistoryCache.keySet()
                .stream()
                .anyMatch(key -> keyIsEqualsUserId(key, userId));

    }

    public List<TableBookingHistory> getListOfBookingHistory(){
        List<TableBookingHistory> result = new ArrayList<>(tableBookingHistoryCache.values());
        return result;
    }

    public void saveTableBookingHistory(long userId, TableBookingHistory tableBookingHistory){

        ScheduledTasks scheduledTasks = AppContProvider.getApplicationContext().getBean(ScheduledTasks.class);

        tableBookingHistory.setBookingStatus("approved");

        scheduledTasks.addUserForNotification(userId, userDataCache.getUserProfileData(userId).getBookingTime());

        tableBookingHistoryCache.put(userId +
                "_" + tableBookingHistory.getTimeReserved(), tableBookingHistory);

        makesTableBooked(tableBookingHistory);

        tableBookingHistoryService.save(tableBookingHistory);
    }

    public boolean hasConsidering(long userId){

        return tableBookingHistoryCache.entrySet()
                .stream()
                .anyMatch(entry -> keyIsEqualsUserId(entry.getKey(), userId) &&
                entry.getValue().getBookingStatus().equals("considering"));

    }

    public List<TableBookingHistory> getAllApprovedBookingHistory(){

        return tableBookingHistoryCache.values()
                .stream()
                .filter(value -> value.getBookingStatus().equals("approved"))
                .collect(Collectors.toList());

    }

    public List<TableBookingHistory> getBookingTimesOfTable(TableBookingHistory tBHForRemove){

        int tableNum = tBHForRemove.getTableInfo().getTableNumber();
        String timeForBooking = tBHForRemove.getBookingTime();

        return tableBookingHistoryCache.values()
                .stream()
                .filter(value -> value.getTableInfo().getTableNumber() == tableNum
                                && !value.getBookingTime().equals(timeForBooking))
                .sorted(Collections.reverseOrder())
                .collect(Collectors.toList());
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

    private boolean keyIsEqualsUserId(String key, Long userId){
        return key.split("_")[0].equals(userId.toString());
    }

    private String getKey(TableBookingHistory tableBookingHistory){
        return tableBookingHistory.getUserChatId()+ "_"+ tableBookingHistory.getTimeReserved();
    }
}
