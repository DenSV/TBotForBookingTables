package ru.home.charlieblack_bot.botstate;

import ru.home.charlieblack_bot.botstate.handlers.administrativeHandler.administartiveImp.*;
import ru.home.charlieblack_bot.botstate.handlers.bookingHandler.bookingImp.*;
import ru.home.charlieblack_bot.botstate.handlers.showProfileHandler.showingProfileImp.*;

import java.util.ArrayList;
import java.util.List;

public enum BotStateEnum {
    BOOKING_BOOK            ("BOOKING_BOOK",         "user", Book.class),
    BOOKING_ASK_TIME        ("BOOKING_BOOK",         "user", BookAskTime.class),
    BOOKING_ASK_TABLE       ("BOOKING_BOOK",         "user", BookAskTable.class),
    BOOKING_ASK_NAME        ("BOOKING_BOOK",         "user", BookAskName.class),
    BOOKING_ASK_NUMBER      ("BOOKING_BOOK",         "user", BookAskUserNumber.class),
    FILLING_PROFILE         ("FILLING_PROFILE",      "user",  null),
    PROFILE_FILLED          ("SHOW_USER_PROFILEDATA","user",  null),
    SHOW_USER_PROFILEDATA   ("SHOW_USER_PROFILEDATA","user",  ShowUserProfileData.class),
    SHOW_ACTUAL_INFORMATION ("SHOW_ACTUAL_INFORMATION","user",  null),
    SHOW_STARTPAGE          ("SHOW_STARTPAGE",       "user", null),
    ASK_USER_NAME           ("SHOW_USER_PROFILEDATA","user", AskUserName.class),
    CHANGE_USER_NAME        ("SHOW_USER_PROFILEDATA","user", ChangeUserName.class),
    ASK_USER_NUM            ("SHOW_USER_PROFILEDATA","user", AskUserNumber.class),
    CHANGE_USER_NUM         ("SHOW_USER_PROFILEDATA","user", ChangeUserNumber.class),
    ERROR                   ("ERROR",                "user",  null),
    ADMIN_START             ("ADMIN_START",          "admin",  Admin.class),
    ADMIN_GET_BOOK_HISTORY  ("ADMIN_START",          "admin", AdminGetBookHistory.class),
    ADMIN_CHANGE_LIST       ("ADMIN_START",          "admin", AdminChangeList.class),
    ADMIN_ASK_CONTACT       ("ADMIN_START",          "admin", AdminAskContact.class),
    ADMIN_ADD               ("ADMIN_START",          "admin", AdminAdd.class),
    ADMIN_BOOK_USER         ("ADMIN_START",           "admin", AdminBookUser.class),
    ADMIN_BOOK_ASK_NUMBER   ("ADMIN_START",           "admin", AdminBookAskNumber.class),
    ADMIN_BOOK_FINISH       ("ADMIN_START", "admin", AdminBookFinish.class),
    ADMIN_BOOK_SHOW_BOOKING_MAP("ADMIN_START", "admin", AdminShowBookingMapping.class);


    private String handler;
    private String fullImpName;
    private String role;

    <T> BotStateEnum(String handler, String role, Class<T> tClass) {
        this.handler = handler;
        this.role = role;
        this.fullImpName = (tClass != null? tClass.getName() : null);

    }

    public static BotStateEnum getNextBotState(BotStateEnum currentBotState){



        List<BotStateEnum> list = new ArrayList<>();
        for (BotStateEnum botStateEnum: values()) {
            if(botStateEnum.handler.equals(currentBotState.handler)){
                list.add(botStateEnum);
            }
        }

        for (int i = 0; i < list.size(); i++) {
            if(currentBotState.equals(list.get(i))){
                currentBotState = (i != list.size() - 1 ? list.get(i + 1) : list.get(0));
                break;
            }
        }

        return currentBotState;
    }

    public static BotStateEnum getMainBotState(BotStateEnum currentBotStateEnum){
        return valueOf(currentBotStateEnum.handler);
    }

    public String getFullImpName(){
        return this.fullImpName;
    }

    public BotStateEnum getMainBotState(){
        return valueOf(this.handler);
    }

    public String getRole() {
        return role;
    }
}
