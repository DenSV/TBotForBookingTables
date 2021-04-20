package ru.home.charlieblack_bot.model;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "table_booking_history")
@Data
@Getter
@Setter
public class TableBookingHistory implements Serializable, Comparable<TableBookingHistory> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "tableinfo_num_id", referencedColumnName = "id")
    private TableInfo tableInfo;

    @Column(name = "person_count")
    private String personCount;

    @Column(name = "booking_time")
    private String bookingTime;

    @Column(name = "user_chat_id")
    private long userChatId;

    @Column(name = "time_reserved")
    private String timeReserved;

    @Column
    private int duration;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserProfileData bookingUser;

    @Column(name = "table_num_id")
    private Integer tableNumId;

    @Column(name = "booking_status")
    private String bookingStatus;

    @Column(name = "user_name_phone")
    private String personalData;

    @Override
    public String toString() {

        String[] personalDataArray = personalData.split(" ");

        String personName, personPhone;
        if (personalDataArray.length > 2){
            personName = personalDataArray[0] + " " + personalDataArray[1];
            personPhone = personalDataArray[2];
        } else {
            personName = personalDataArray[0];
            personPhone = personalDataArray[1];
        }

        return "Стол №" + tableInfo.getTableNumber() +
                " забронирован к " + bookingTime +
                " на " + personCount +
                " чел. Имя - " + personName +
                ", тел. номер: " + personPhone;

        //Стол <№ стола> забронирован к <время> на <кол-во человек>. Имя <Имя пользователя> тел. номер: <тел. номер юзера>
    }

    @Override
    public int compareTo(TableBookingHistory o) {
        return this.tableNumId.compareTo(o.tableNumId);

    }
}
