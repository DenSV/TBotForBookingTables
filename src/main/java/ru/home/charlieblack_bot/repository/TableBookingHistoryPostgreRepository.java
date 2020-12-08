package ru.home.charlieblack_bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.home.charlieblack_bot.model.TableBookingHistory;

import java.util.List;

public interface TableBookingHistoryPostgreRepository extends JpaRepository<TableBookingHistory, Integer> {

    boolean existsByBookingTimeAndUserChatId(String bookingTime, long userId);

    TableBookingHistory findByTableInfoAndBookingTime(int tableNum, String bookingTime);

    List<TableBookingHistory> findAll();

    List<TableBookingHistory> findByTableNumId(int tableNum);

}
