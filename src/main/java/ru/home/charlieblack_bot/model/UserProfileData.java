package ru.home.charlieblack_bot.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "users")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
//@Document(collection = "userProfileData")
public class UserProfileData implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    String name;

    @Column(name = "booking_time")
    String bookingTime;

    @Column(name = "person_count")
    int personCount;

    @Column(name = "table_num")
    int tableNum;

    @Column(name = "phone_number")
    String phoneNumber;

    @Column(name = "chat_id")
    long chatId;

    @Override
    public String toString() {
        return "Информация о вас:\n" +
                "\n" +
                "Ваше имя - " + name + "\n" +
                "Тел. номер - " + phoneNumber + "\n" +
                "Число человек - " + personCount + "\n" +
                "Номер столика - " + tableNum + "\n" +
                "Время бронирования - " + bookingTime;
    }

    public static long getUserIdFromUpdate(Update update){
        Message inputMsg = update.getMessage();
        return (!update.hasCallbackQuery() ? inputMsg.getFrom().getId() : update.getCallbackQuery().getMessage().getChatId());
    }
}
