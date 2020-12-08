package backendapplication.datalayer;

public class DBSettings {
    private static String url = "jdbc:mysql://database-backend.cqhaf52g7rcf.us-east-2.rds.amazonaws.com:3306/accomodations?useLegacyDatetimeCode=false&serverTimezone=UTC";
    private static String user = "backenduser";
    private static String driver = "com.mysql.cj.jdbc.Driver";
    private static String DB_password = "JFNbn4243nirfb2iu3b4rfj324";

    public String getUrl() {
        return url;
    }

    public String getUser() {
        return user;
    }

    public String getDriver() {
        return driver;
    }

    public String getDB_password() {
        return DB_password;
    }
}
