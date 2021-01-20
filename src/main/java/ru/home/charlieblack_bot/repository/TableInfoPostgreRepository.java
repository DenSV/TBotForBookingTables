package ru.home.charlieblack_bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.home.charlieblack_bot.model.TableInfo;

import java.util.List;

public interface TableInfoPostgreRepository extends JpaRepository<TableInfo, Integer> {

    List<TableInfo> findAll();

    List<TableInfo> findAllByBookingTimeAndBooked(String bookingTime, boolean booked);

    List<TableInfo> findAllByBooked(boolean isBooked);
}
