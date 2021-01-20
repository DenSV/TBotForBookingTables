package ru.home.charlieblack_bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.home.charlieblack_bot.model.TableBookingHistory;

import java.util.List;

public interface TableBookingHistoryPostgreRepository extends JpaRepository<TableBookingHistory, Integer> {

    List<TableBookingHistory> findAll();

    List<TableBookingHistory> findByTableNumId(int tableNum);

    void deleteByBookingTimeAndUserChatId(String bookingTime, long userId);

    void deleteByPersonalData(String userNameAndPhone);
}
