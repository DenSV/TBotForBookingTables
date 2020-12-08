package ru.home.charlieblack_bot.cache;

import org.springframework.stereotype.Component;
import ru.home.charlieblack_bot.botstate.BotStateEnum;
import ru.home.charlieblack_bot.model.UserProfileData;
import ru.home.charlieblack_bot.service.UsersProfileDataService;

import java.util.HashMap;
import java.util.List;

@Component
public class UserDataCache implements DataCache {
    private HashMap<Long, BotStateEnum> botStateCache = new HashMap<>();
    private HashMap<Long, UserProfileData> userProfileCache = new HashMap<>();
    private UsersProfileDataService usersProfileDataService;


    public UserDataCache(UsersProfileDataService usersProfileDataService){

        this.usersProfileDataService = usersProfileDataService;

        List<UserProfileData> userProfileDataListFromDB = usersProfileDataService.getAllUsers();
        for (UserProfileData profileData: userProfileDataListFromDB) {
            botStateCache.put(profileData.getChatId(), BotStateEnum.BOOKING_BOOK);
            userProfileCache.put(profileData.getChatId(), profileData);
        }
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

    public boolean isUserExist(long userId){
        if(userProfileCache.get(userId) != null){
            return true;
        } else {
            return false;
        }
    }

}
