package ru.home.charlieblack_bot.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalTime;


@Entity
@Table(name = "table_info")
@Data
@Getter
@Setter
public class TableInfo implements Serializable, Comparable<TableInfo> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "table_num")
    private int tableNumber;

    @Column(name = "user_chat_id")
    private String bookingName;

    @Column(name = "booking_time")
    private String bookingTime;

    @Column
    private String capacity;

    @Column
    private boolean booked;

    public TableInfo(){

    }

    public TableInfo(int tableNumber, String capacity, String bookingName, boolean booked){
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.bookingName = bookingName;
        this.booked = booked;
    }

    public void setBookingTime(LocalTime localTime){
        this.bookingTime = localTime.toString();
    }

    public void setBookingTime(String bookingTime){
        this.bookingTime = bookingTime;
    }

    public String getUserNameFromBookingName(){
        return bookingName.split(" ")[0];
    }

    public String getUserPhoneFromBookingName(){
        return bookingName.split(" ")[1];
    }

    @Override
    public int compareTo(TableInfo o) {
        return tableNumber - o.getTableNumber();
    }
}
