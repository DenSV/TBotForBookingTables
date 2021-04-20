package ru.home.charlieblack_bot.cache;

import org.springframework.stereotype.Component;
import ru.home.charlieblack_bot.AppContProvider;
import ru.home.charlieblack_bot.botstate.BotStateEnum;
import ru.home.charlieblack_bot.model.UserProfileData;
import ru.home.charlieblack_bot.service.UsersProfileDataService;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserDataCache implements DataCache {
    private HashMap<Long, BotStateEnum> botStateCache = new HashMap<>();
    private HashMap<Long, UserProfileData> userProfileCache = new HashMap<>();
    private UsersProfileDataService usersProfileDataService;
    private UserProfileData userProfileData;


    public UserDataCache(UsersProfileDataService usersProfileDataService){

        this.usersProfileDataService = usersProfileDataService;

        usersProfileDataService.getAllUsers().forEach(profileData -> {
            botStateCache.put(profileData.getChatId(), BotStateEnum.BOOKING_BOOK);
            userProfileCache.put(profileData.getChatId(), profileData);
        });

        this.userProfileData = new UserProfileData();
    }

    @Override
    public void setUsersCurrentBotState(long userId, BotStateEnum botState) {
        botStateCache.put(userId, botState);
    }

    @Override
    public BotStateEnum getUsersCurrentBotState(long userId) {
        BotStateEnum botStateEnum = botStateCache.get(userId);

        if (botStateEnum == null )
        {
            botStateEnum = BotStateEnum.BOOKING_BOOK;
        }
        return botStateEnum;
    }

    @Override
    public UserProfileData getUserProfileData(long userId) {
        UserProfileData userProfileData = userProfileCache.get(userId);
        if(userProfileData == null){
            userProfileData = new UserProfileData();

        }
        return userProfileData;
    }

    @Override
    public void saveUserProfileData(long userId, UserProfileData userProfileData) {
        userProfileCache.put(userId, userProfileData);
        usersProfileDataService.saveUserData(userProfileCache.get(userId));
    }

    public static UserDataCache getBeanFromContext(){
        return AppContProvider.getApplicationContext().getBean(UserDataCache.class);
    }

    public boolean isUserExist(long userId){
        return userProfileCache.get(userId) != null;
    }

    public void clearDataAboutTables(){
        userProfileCache.forEach((aLong, profileData) -> {
            profileData.setTableNum(0);
            profileData.setPersonCount(null);
            profileData.setBookingTime(null);
        });

        usersProfileDataService.clearDataAboutTables();
    }

    public void clearDataAboutTablesByUserId(long userId){
        UserProfileData profileData = userProfileCache.get(userId);
        profileData.setTableNum(0);
        profileData.setPersonCount(null);
        profileData.setBookingTime(null);
        userProfileCache.put(userId, profileData);

        usersProfileDataService.saveUserData(profileData);
    }

    public List<UserProfileData> getAdminList(){

        return userProfileCache
                .values()
                .stream()
                .filter(userProfileData1 -> userProfileData1.getUserRole().equals("admin"))
                .collect(Collectors.toList());

    }

    public UserProfileData getProfileData() {
        return userProfileData;
    }

    public void setProfileData(UserProfileData userProfileData) {
        this.userProfileData = userProfileData;
    }
}
