package config;

import app.tools.Console;
import app.tools.Tools;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.File;
import java.util.Objects;
import java.util.Properties;

public class Env {

    public static Dotenv dotenv = Dotenv.load();
    public static boolean app_debug = Boolean.parseBoolean(dotenv.get("APP_DEBUG", "true"));

    public static final String app_version = dotenv.get("APP_VERSION");
    public static String loggerPath = String.format("%s/%s/%s/", dotenv.get("LOGS_PATH"), Tools.DateTime.dateNow("MM/dd"), Tools.DateTime.dateNow("HH-mm-ss"));


    // This block configure the logger with handler and formatter
    public static String getLoggerPath() {
        File directory = new File(loggerPath);
        if (!directory.exists()) {
            directory.mkdirs();
            Console.println("log path: " + directory.getAbsolutePath());
        }
        return directory.getAbsolutePath();
    }

    public static void printConfig() {
        Console.println("###########################config data############################", Console.Colors.getYELLOW());
        Console.println("database=" + Database.db_host, Console.Colors.getYELLOW());
        Console.println("AlertServices=" + Services.alertServices.length + "   " + String.join(",", Services.alertServices), Console.Colors.getYELLOW());
        Console.println("calculatorService=" + Services.calculatorService, Console.Colors.getYELLOW());
        Console.println("##################################################################", Console.Colors.getYELLOW());
    }


    public static class MSG {
        public static final int RECORDS = 1;
        public static final int EXTENDED_RECORDS = 68;
//        public static final int DTCS = 9;
    }

    public static class SMTP {
        public static final String[] mails = dotenv.get("MAIL_TO", "").split(",");
        public static final String username = dotenv.get("MAIL_USERNAME");
        public static final String password = dotenv.get("MAIL_PASSWORD");
        public static final String mail_from = dotenv.get("MAIL_FROM");

        public static Properties props() {
            Properties props = new Properties();
            props.put("mail.smtp.auth", dotenv.get("MAIL_ENABLE_AUTH", "true"));
            props.put("mail.smtp.starttls.enable", dotenv.get("MAIL_ENABLE_TLS", "true"));
            props.put("mail.smtp.host", dotenv.get("MAIL_SMTP_HOST", "smtp.gmail.com"));
            props.put("mail.smtp.port", Integer.parseInt(dotenv.get("MAIL_SMTP_PORT", "587")));
            return props;
        }
    }

    public static class Database {
        public static final String login = dotenv.get("DB_USER");
        public static final String password = dotenv.get("DB_PASSWORD");
        public static final int REQUIRED_CONNECTIONS_ELEMENTS = Integer.parseInt(dotenv.get("DB_MAX_CNX", "20"));
        static final String host = dotenv.get("DB_HOST", "localhost");
        static final int port = Integer.parseInt(dotenv.get("DB_PORT", "3306"));
        static final String dbName = dotenv.get("DB_NAME");


        public static final String db_host = String.format("jdbc:mysql://%s:%d/%s?useSSL=false", host, port, dbName);
    }

    // Services
    // to disable a service replace its value with 'disabled'
    public static class Services {
        //        public static ArrayList<String> alertServices = new ArrayList<String>() {{
////            add("alertService_url1");
////            add("alertService_url2");
////            add("alertService_url3");
//        }};//or =null
        public static final String[] alertServices = dotenv.get("ALERT_SERVICES") != null && !Objects.equals(dotenv.get("ALERT_SERVICES"), "") ? dotenv.get("ALERT_SERVICES", "").split(",") : new String[0];

        public static final String calculatorService = "disabled"; //set calculatorService_url
    }
}