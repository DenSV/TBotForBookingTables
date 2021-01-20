package ru.home.charlieblack_bot.appconfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import ru.home.charlieblack_bot.CharlieBlackTelegramBot;
import ru.home.charlieblack_bot.botstate.TelegramFacade;


@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "telegrambot")
@EnableAsync
@EnableScheduling
@EnableTransactionManagement
public class BotConfig {
    private String webHookPath;
    private String botUserName;
    private String botToken;

    @Bean
    public CharlieBlackTelegramBot MySuperTelegramBot(TelegramFacade telegramFacade) {
        DefaultBotOptions options = ApiContext
                .getInstance(DefaultBotOptions.class);

       CharlieBlackTelegramBot mySuperTelegramBot = new CharlieBlackTelegramBot(options, telegramFacade);
        mySuperTelegramBot.setBotUserName(botUserName);
        mySuperTelegramBot.setBotToken(botToken);
        mySuperTelegramBot.setWebHookPath(webHookPath);

        return mySuperTelegramBot;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

}
