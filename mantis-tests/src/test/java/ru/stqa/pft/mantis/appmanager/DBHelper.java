package ru.stqa.pft.mantis.appmanager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.SortNatural;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import ru.stqa.pft.mantis.model.UserData;
import ru.stqa.pft.mantis.model.Users;

public class DBHelper extends HelperBase {
  
  public DBHelper(ApplicationManager app) {
    super(app);
  }

  public Users getUsers() throws SQLException {
    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bugtracker?user=root&password=");
    Statement st = conn.createStatement();
    ResultSet rs = st.executeQuery("SELECT id, username, email, password FROM mantis_user_table WHERE username != 'administrator'");
    Users users = new Users();
    while (rs.next()) {
      users.add(new UserData().withId(rs.getInt("id")).withUsername(rs.getString("username"))
      .withEmail(rs.getString("email")).withPassword("password"));
    }
    rs.close();
    st.close();
    conn.close();
    return users;
  }

}