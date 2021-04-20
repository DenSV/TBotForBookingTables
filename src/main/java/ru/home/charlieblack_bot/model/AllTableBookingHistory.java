package ru.home.charlieblack_bot.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "all_table_booking_history")
@Data
@Getter
@Setter
public class AllTableBookingHistory implements Serializable, Comparable<AllTableBookingHistory> {

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

    public AllTableBookingHistory() {
    }

    public AllTableBookingHistory(TableBookingHistory tableBookingHistory) {
        this.tableInfo = tableBookingHistory.getTableInfo();
        this.bookingStatus = tableBookingHistory.getBookingStatus();
        this.bookingTime = tableBookingHistory.getBookingTime();
        this.bookingUser = tableBookingHistory.getBookingUser();
        this.duration = tableBookingHistory.getDuration();
        this.personalData = tableBookingHistory.getPersonalData();
        this.personCount = tableBookingHistory.getPersonCount();
        this.tableNumId = tableBookingHistory.getTableNumId();
        this.timeReserved = tableBookingHistory.getTimeReserved();
        this.userChatId = tableBookingHistory.getUserChatId();
    }

    @Override
    public String toString() {
        String personName = personalData.split(" ")[0];
        String personPhone = personalData.split(" ")[1];

        return "Стол №" + tableInfo.getTableNumber() +
                " забронирован к " + bookingTime +
                " на " + personCount +
                " чел. Имя - " + personName +
                ", тел. номер: " + personPhone;

        //Стол <№ стола> забронирован к <время> на <кол-во человек>. Имя <Имя пользователя> тел. номер: <тел. номер юзера>
    }

    @Override
    public int compareTo(AllTableBookingHistory o) {
        return this.tableNumId.compareTo(o.tableNumId);

    }
}
