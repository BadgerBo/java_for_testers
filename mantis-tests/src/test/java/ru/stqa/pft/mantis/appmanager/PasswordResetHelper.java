package ru.stqa.pft.mantis.appmanager;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class PasswordResetHelper extends HelperBase {

  public PasswordResetHelper(ApplicationManager app) {
	super(app);
  }

  public void login(String username, String password) {
    wd.get(app.getProperty("web.baseUrl") + "login_page.php");
    type(By.name("username"), username);
    type(By.name("password"), password);
    click(By.cssSelector("input[value='Login']"));
  }

  public void chooseUser(int id) {
    goToManageUsersPage();
    click(By.cssSelector(String.format("a[href='manage_user_edit_page.php?user_id=%s'", Integer.toString(id))));
    click(By.cssSelector("input[value='Reset Password']"));
  }

  public void goToManageUsersPage() {
    click(By.cssSelector("a[href='/mantisbt-1.2.18/manage_user_page.php'"));
  }
}