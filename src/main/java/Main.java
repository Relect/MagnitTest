import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        long m = System.currentTimeMillis();

        service util = new service();
        util.setUrldb("jdbc:mysql://localhost:3306/java?serverTimezone=UTC");
        util.setUsername("root");
        util.setPassword("root");
        util.setN(2000);

        util.createTestTable();
        util.saveTestTableN();
        System.out.println(util.readTestTableN());

        System.out.println((System.currentTimeMillis() - m)/1000);

    }
}
