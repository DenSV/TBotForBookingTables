package ru.home.charlieblack_bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.home.charlieblack_bot.model.AllTableBookingHistory;

import java.util.List;

public interface AllTableBookingHistoryPostgreRepository extends JpaRepository<AllTableBookingHistory, Integer> {
    boolean existsByBookingTimeAndUserChatId(String bookingTime, long userId);

    AllTableBookingHistory findByTableInfoAndBookingTime(int tableNum, String bookingTime);

    List<AllTableBookingHistory> findAll();

    List<AllTableBookingHistory> findByTableNumId(int tableNum);

    void deleteByBookingTimeAndUserChatId(String bookingTime, long userId);

    void deleteByPersonalData(String userNameAndPhone);
}
