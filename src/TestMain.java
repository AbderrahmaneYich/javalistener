import config.Env;

public class TestMain {
    public static void main(String[] args) {
//        new DotEnvProps(".env.example");
        System.out.println(Env.Database.db_host);
    }
}
