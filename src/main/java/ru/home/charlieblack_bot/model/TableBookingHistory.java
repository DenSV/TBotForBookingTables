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
        String personName = personalData.split(" ")[0];
        String personPhone = personalData.split(" ")[1];

        return "Стол №" + tableInfo.getTableNumber() +
                " забронирован к " + bookingTime +
                " на " + personCount +
                " чел. Имя - " + personName +
                ", тел. номер: " + personPhone;

    }

    @Override
    public int compareTo(TableBookingHistory o) {
        return this.tableNumId.compareTo(o.tableNumId);

    }
}
