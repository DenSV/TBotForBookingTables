package ru.home.charlieblack_bot.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.home.charlieblack_bot.model.UserProfileData;
import ru.home.charlieblack_bot.repository.UserProfilePostgreRepository;

import java.util.List;

@Service
@Transactional
public class UsersProfileDataService implements DataService{

    private UserProfilePostgreRepository profilePostgreRepository;
    public UsersProfileDataService(UserProfilePostgreRepository userProfilePostgreRepository){
        this.profilePostgreRepository = userProfilePostgreRepository;
    }

    @Async
    public void saveUserData(UserProfileData userProfileData){

        UserProfileData userProfileDataFromDB = profilePostgreRepository.findByChatId(userProfileData.getChatId());
        if(userProfileDataFromDB != null){
            userProfileData.setId(userProfileDataFromDB.getId());
        }

        profilePostgreRepository.save(userProfileData);
    }
    
    public UserProfileData getUserProfileData(long chatId) {return profilePostgreRepository.findByChatId(chatId);}

    public List<UserProfileData> getAllUsers(){ return profilePostgreRepository.findAll(); }

    @Async
    public void clearDataAboutTables(){
        List<UserProfileData> usersList = profilePostgreRepository.findAll();
        usersList.forEach(profileData -> {
            profileData.setBookingTime(null);
            profileData.setPersonCount(null);
            profileData.setTableNum(0);
        });

        profilePostgreRepository.saveAll(usersList);

    }

}
