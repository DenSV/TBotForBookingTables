package ru.home.charlieblack_bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.home.charlieblack_bot.model.UserProfileData;

public interface UserProfilePostgreRepository extends JpaRepository<UserProfileData, Integer> {

    UserProfileData findByChatId(long chatId);

}
