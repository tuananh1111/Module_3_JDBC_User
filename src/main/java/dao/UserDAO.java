package dao;

import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDAO implements IUserDAO {
    private  String idbcURL= "jdbc:mysql://localhost:3306/demo?useSSL=false";
    private  String jdbcUsername= "root";
    private String jdbcPassword= "0964068256";
    private  static  final  String INSERT_USERS_SQL= "insert into users (name, email,country) values (?,?,?);";
    private static final String SELECT_USER_BY_ID=  "select id, name, email, country from users where id= ?;";
    private  static final String DELETE_USERS_SQL= "delete from users where id=?;";
    private  static final  String UPDATE_USERS_SQL= "update users set name=?,email=?,country=? where id=?";
    private  static final String SELECT_ALL_USERS = "select * from users";

    public UserDAO(){
    }
    protected  Connection getConnection(){
        Connection connection=null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection= DriverManager.getConnection(idbcURL,jdbcUsername,jdbcPassword);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    @Override
    public void insertUser(User user) throws SQLException {
        System.out.println(INSERT_USERS_SQL);
        Connection connection= getConnection();
        PreparedStatement preparedStatement= connection.prepareStatement(INSERT_USERS_SQL);
        preparedStatement.setString(1,user.getName());
        preparedStatement.setString(2,user.getEmail());
        preparedStatement.setString(3,user.getCountry());
        System.out.println(preparedStatement);
        preparedStatement.executeUpdate();
    }

    public User selectUser(int id){
        User user= null;
        Connection connection= getConnection();
        try {
            PreparedStatement preparedStatement =connection.prepareStatement(SELECT_USER_BY_ID);
            preparedStatement.setInt(1,id);
            System.out.println(preparedStatement);
            ResultSet resultSet= preparedStatement.executeQuery();
            while (resultSet.next()){
                String name= resultSet.getString("name");
                String email= resultSet.getString("email");
                String country= resultSet.getString("country");
                user= new User(id, name, email, country);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }

    @Override
    public List<User> selectAllUsers() {
        List<User> users= new ArrayList<>();
        Connection connection= getConnection();
        try {
            PreparedStatement preparedStatement =connection.prepareStatement(SELECT_ALL_USERS);
            System.out.println(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String name= resultSet.getString("name");
                String email = resultSet.getString("email");
                String country = resultSet.getString("country");
                users.add(new User(id, name, email,country));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return users;
    }

    @Override
    public boolean deleteUser(int id) throws SQLException {
        boolean rowDeleted;
        Connection connection= getConnection();
        PreparedStatement preparedStatement= connection.prepareStatement(DELETE_USERS_SQL);
        preparedStatement.setInt(1,id);
        rowDeleted= preparedStatement.executeUpdate()>0;
        return rowDeleted;
    }

    @Override
    public boolean updateUser(User user) throws SQLException {
        boolean rowUpdate;
        Connection connection= getConnection();
        PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL);
        statement.setString(1,user.getName());
        statement.setString(2,user.getEmail());
        statement.setString(3,user.getCountry());
        statement.setInt(4,user.getId());
        rowUpdate= statement.executeUpdate()>0;
        return rowUpdate;
    }
}
