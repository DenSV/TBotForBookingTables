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

    public UserProfileData getUserDescription(long chatId) {
        return profilePostgreRepository.findByChatId(chatId);
    }

    public boolean existsByChatId(long chatId){
        return profilePostgreRepository.existsByChatId(chatId);
    }

    public UserProfileData getUserProfileData(long chatId) {return profilePostgreRepository.findByChatId(chatId);}

    public List<UserProfileData> getAllUsers(){ return profilePostgreRepository.findAll(); }

    /*public List<UserProfileData> getAllProfiles(){return profileMongoRepository.findAll();}

    public void saveUserDescription(UserProfileData userProfileData){profileMongoRepository.save(userProfileData);}

    public boolean existsById(long chatId){return profileMongoRepository.existsByChatId(chatId);}

    public void deleteUserProfileData(String profileDataId){profileMongoRepository.deleteById(profileDataId);}

    public UserProfileData getUserDescription(long chatId){return profileMongoRepository.findByChatId(chatId);}
*/
}
