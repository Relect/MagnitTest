import java.sql.*;


public class TestService implements java.io.Serializable{
    private String urldb;
    private String username;
    private String password;
    private int N;

    public int getN() {
        return N;
    }

    public void setN(int n) {
        N = n;
    }

    public String getUrldb() {
        return urldb;
    }

    public void setUrldb(String urldb) {
        this.urldb = urldb;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public TestService() {}

    public void createTestTable() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(urldb, username, password);
            String query = "Drop table IF EXISTS TEST;";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.execute();
            query = "Create table IF NOT EXISTS TEST (id int(11) auto_increment primary key not null, FIELD int(11));"; //
            Statement st2 = conn.createStatement();
            st2.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveTestTableN() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(urldb, username, password);
            String query = "Insert INTO TEST values (id, ?)";
            PreparedStatement pst = conn.prepareStatement(query);
            for (int i = 0; i < N; i++) {
                pst.setInt(1, i+1);
                pst.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int readTestTableN() throws SQLException{
        try {
            Connection conn = DriverManager.getConnection(urldb, username, password);
            String query = "SELECT FIELD FROM TEST ORDER BY FIELD DESC LIMIT 1";
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            if ((rs.next())) {
                N = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return N;
    }

    @Override
    public String toString() {
        return "TestService{" +
                "urldb='" + urldb + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", N=" + N +
                '}';
    }

}