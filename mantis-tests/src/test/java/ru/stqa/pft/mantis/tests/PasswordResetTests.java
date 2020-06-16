package ru.stqa.pft.mantis.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import ru.lanwen.verbalregex.VerbalExpression;
import ru.stqa.pft.mantis.model.MailMessage;
import ru.stqa.pft.mantis.model.UserData;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.mail.MessagingException;

import static org.testng.Assert.assertTrue;

public class PasswordResetTests extends TestBase {
  
  @BeforeMethod
  public void ensurePreconditions() throws SQLException {
    if (app.db().getUsers().size() == 0) {
      long now = System.currentTimeMillis();
      String user = String.format("user%s", now);
      String email = String.format("user%s@localdomain.localhost", now);
      String password = "password";
      app.james().createUser(user, password);
      app.registration().start(user, email);
    }
  }

  @Test
  public void testPasswordReset() throws MessagingException, IOException, SQLException {
    app.reset().login("administrator", "root");
    UserData user = app.db().getUsers().iterator().next();
    app.reset().chooseUser(user.getId());
    List<MailMessage> mailMessages = app.james().waitForMail(user.getUsername(), user.getPassword(), 60000);
    String resetLink = findResetLink(mailMessages, user.getEmail());
  }

  private String findResetLink(List<MailMessage> mailMessages, String email) {
    MailMessage mailMessage = mailMessages.stream().filter((m) -> m.to.equals(email)).findFirst().get();
    VerbalExpression regex = VerbalExpression.regex().find("_____").nonSpace().oneOrMore().build();
    return regex.getText(mailMessage.text);
  }
}