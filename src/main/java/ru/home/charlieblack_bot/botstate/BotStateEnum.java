package ru.home.charlieblack_bot.botstate;

import java.util.ArrayList;
import java.util.List;

public enum BotStateEnum {
    BOOKING_BOOK            ("BOOKING_BOOK"),
    BOOKING_ASK_TIME        ("BOOKING_BOOK"),
    BOOKING_ASK_PERSON_COUNT("BOOKING_BOOK"),
    BOOKING_ASK_TABLE       ("BOOKING_BOOK"),
    BOOKING_ASK_NAME        ("BOOKING_BOOK"),
    BOOKING_ASK_NUMBER      ("BOOKING_BOOK"),
    FILLING_PROFILE         ("FILLING_PROFILE"),
    PROFILE_FILLED          ("SHOW_USER_PROFILEDATA"),
    SHOW_USER_PROFILEDATA   ("SHOW_USER_PROFILEDATA"),
    SHOW_ACTUAL_INFORMATION ("SHOW_ACTUAL_INFORMATION"),
    SHOW_STARTPAGE          ("SHOW_STARTPAGE"),
    ASK_USER_NAME           ("SHOW_USER_PROFILEDATA"),
    CHANGE_USER_NAME        ("SHOW_USER_PROFILEDATA"),
    ASK_USER_NUM            ("SHOW_USER_PROFILEDATA"),
    CHANGE_USER_NUM         ("SHOW_USER_PROFILEDATA"),
    ERROR                   ("ERROR");

    private String handler;

    BotStateEnum(String handler) {
        this.handler = handler;
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


}
