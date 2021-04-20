package ru.home.charlieblack_bot.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.home.charlieblack_bot.AppContProvider;
import ru.home.charlieblack_bot.botstate.BotResponse;
import ru.home.charlieblack_bot.botstate.BotStateEnum;
import ru.home.charlieblack_bot.cache.UserDataCache;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "users")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfileData implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    String name;

    @Column(name = "booking_time")
    String bookingTime;

    @Column(name = "person_count")
    String personCount;

    @Column(name = "table_num")
    int tableNum;

    @Column(name = "phone_number")
    String phoneNumber;

    @Column(name = "chat_id")
    long chatId;

    @Column(name = "user_role")
    String userRole;

    @Override
    public String toString() {
        return "Информация о вас:\n" +
                "\n" +
                "Ваше имя - " + getField(name) + "\n" +
                "Тел. номер - " + getField(phoneNumber) + "\n" +
                "Число человек - " + getField(personCount) + "\n" +
                "Номер столика - " + getField(tableNum) + "\n" +
                "Время бронирования - " + getField(bookingTime);
    }

    public static long getUserIdFromUpdate(Update update){
        Message inputMsg = update.getMessage();
        return (!update.hasCallbackQuery() ? inputMsg.getFrom().getId() : update.getCallbackQuery().getMessage().getChatId());
    }

    public void setInputMsg(String inputMsg){

        UserDataCache userDataCache = AppContProvider.getApplicationContext().getBean(UserDataCache.class);

        BotStateEnum botStateEnum = userDataCache.getUsersCurrentBotState(this.chatId);

        if(inputMsg.contains("admin_table_number=")) return;

        // проверка на время, количество людей, номер стола, Имя, телефонный номер
        if(inputMsg != null && !new BotResponse().hasValue(inputMsg)) {
            if (inputMsg.contains("time")) {
                this.setBookingTime(inputMsg.split("-")[1]);
            } else if (inputMsg.contains("table_num")) {
                this.setTableNum(Integer.parseInt(inputMsg.split("[=&]")[1]));
                this.setPersonCount(inputMsg.split("[=&]")[3]);
            } else if (inputMsg.contains("person_count=")){
                this.setPersonCount(inputMsg.split("=")[1]);
            } else if (botStateEnum.equals(BotStateEnum.BOOKING_ASK_NAME) ||
                            botStateEnum.equals(BotStateEnum.CHANGE_USER_NAME) &&
                            inputMsg.matches("[а-яёА-ЯЁ]+")) {
                this.setName(inputMsg);
            } else if (botStateEnum.equals(BotStateEnum.BOOKING_ASK_NUMBER) ||
                            botStateEnum.equals(BotStateEnum.CHANGE_USER_NUM) &&
                            inputMsg.matches("[0-9]+")){
                this.setPhoneNumber(inputMsg);
            }
        }

    }

    private String getField(String field){
        return (field != null ? field : "неизвестно");
    }

    private String getField(int field){
        return (field != 0 ? String.valueOf(field) : "неизвестно");
    }

    public boolean hasName(){
        return this.getName() != null;
    }

    public boolean hasPhoneNumber(){
        return this.getPhoneNumber() != null;
    }
}
