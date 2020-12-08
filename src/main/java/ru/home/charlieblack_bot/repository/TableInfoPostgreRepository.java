package ru.home.charlieblack_bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.home.charlieblack_bot.model.TableInfo;

import java.util.List;

public interface TableInfoPostgreRepository extends JpaRepository<TableInfo, Integer> {

    boolean existsById(int id);

    TableInfo findByTableNumberAndBookingTime(int tableNumber, String bookingTime);

    TableInfo findById(int id);

    List<TableInfo> findAll();

    List<TableInfo> findAllByTableNumberAndBookingTime(int tableNumber, String bookingTime);

    List<TableInfo> findAllByBookingTimeAndBooked(String bookingTime, boolean booked);

}
