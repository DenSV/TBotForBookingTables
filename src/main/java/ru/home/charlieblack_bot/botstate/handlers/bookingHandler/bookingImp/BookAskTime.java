package ru.home.charlieblack_bot.botstate.handlers.bookingHandler.bookingImp;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.home.charlieblack_bot.AppContProvider;
import ru.home.charlieblack_bot.ScheduledTasks;
import ru.home.charlieblack_bot.botstate.BotStateEnum;
import ru.home.charlieblack_bot.botstate.handlers.Booking;
import ru.home.charlieblack_bot.botstate.handlers.AbstractBooking;
import ru.home.charlieblack_bot.model.TableInfo;

import java.util.List;

import static ru.home.charlieblack_bot.botstate.handlers.BookingCore.*;

public class BookAskTime extends AbstractBooking implements Booking {

    private List<TableInfo> freeTables;

    public BookAskTime(Update update) {
        super(update);
        this.freeTables = getFreeTables(profileData.getBookingTime());
    }

    @Override
    public SendMessage getResponse() {

        //проверка входящих данных
        if(update.hasMessage() && update.getMessage().hasText()){

            userDataCache.setUsersCurrentBotState(userId, currentBotStateEnum);

            return messagesService.getReplyMessage(userId, "Данные введены некорректно. Выберите время из списка")
                    .setReplyMarkup(getInlineKeyBoardTimeButtons());
        }


        if(inputMsg.equals("Отменить бронь")){

            ScheduledTasks scheduledTasks = AppContProvider.getApplicationContext().getBean(ScheduledTasks.class);
            scheduledTasks.removeUserForNotification(userId);
            tableBookingHistoryCache.deleteByBookingTimeAndUserId(profileData, 150);
            userDataCache.setUsersCurrentBotState(userId, BotStateEnum.SHOW_STARTPAGE);

            sendMessageToAdminIfBookCancelled(profileData);

            return messagesService.getReplyMessage(userId, "Ваша бронь отменена")
                    .setReplyMarkup(getReplyKeyBoardBackToStartPage());

        } else if(inputMsg.equals("Изменить время")){

            tableBookingHistoryCache.deleteByBookingTimeAndUserId(profileData, 150);
            userDataCache.setUsersCurrentBotState(userId, currentBotStateEnum);

            editMessage( new EditMessageText()
                    .setChatId(update.getCallbackQuery().getMessage().getChatId())
                    .setMessageId(update.getCallbackQuery().getMessage().getMessageId())
                    .setText("Выберите другое время")
                    .setReplyMarkup(getInlineKeyBoardTimeButtons()));
            return null;

        } else {

            editMessage( new EditMessageText()
                    .setChatId(update.getCallbackQuery().getMessage().getChatId())
                    .setMessageId(update.getCallbackQuery().getMessage().getMessageId())
                    .setText("Выберите столик")
                    .setReplyMarkup(getInlineMessageButtons(freeTables)));
            return null;

        }

    }

}
