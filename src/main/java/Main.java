import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        long m = System.currentTimeMillis();

        TestService service = new TestService();
        service.setUrldb("jdbc:mysql://localhost:3306/java?serverTimezone=UTC");
        service.setUsername("root");
        service.setPassword("root");
        service.setN(2000);

        service.createTestTable();
        service.saveTestTableN();
        System.out.println(service.readTestTableN());

        System.out.println((System.currentTimeMillis() - m)/1000);

    }
}
