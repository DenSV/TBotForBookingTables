package ru.home.charlieblack_bot.cache;

import ru.home.charlieblack_bot.botstate.BotStateEnum;
import ru.home.charlieblack_bot.model.UserProfileData;

public interface DataCache {
    void setUsersCurrentBotState(long userId, BotStateEnum botState);

    BotStateEnum getUsersCurrentBotState(long userId);

    UserProfileData getUserProfileData(long userId);

    void saveUserProfileData(long userId, UserProfileData userProfileData);
}
