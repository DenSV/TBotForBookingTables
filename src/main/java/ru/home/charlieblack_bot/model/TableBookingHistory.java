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
public class TableBookingHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "tableinfo_num_id", referencedColumnName = "id")
    private TableInfo tableInfo;

    @Column(name = "person_count")
    private int personCount;

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
    private int tableNumId;

}
