package ru.stqa.pft.addressbook.tests;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.stqa.pft.addressbook.model.ContactData;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;

public class ContactDetailsTests extends TestBase {

  @BeforeMethod
  public void ensurePreconditions() {
    app.goTo().homePage();
    if (app.db().contacts().size() == 0) {
      File photo = new File("addressbook-web-tests/src/test/resources/stru.png");
      app.contact().create(new ContactData().withFirstname("test_name").withLastname("test_surname").withPhoto(photo), true);
    }
  }
    
  @Test
  public void testContactDetais() {
    app.goTo().homePage();
    ContactData contactInfoFromHomePage = app.contact().all().iterator().next();
    String contactInfoFromDetailsPage = app.contact().getInfoFromDetailsPage(contactInfoFromHomePage);
    assertThat(convertToString(contactInfoFromHomePage), equalTo(contactInfoFromDetailsPage));
  }
  
  public String convertToString(ContactData contact) {
      String string = "";
      if (contact.getFirstname() != null) { string += contact.getFirstname(); }
      if (contact.getLastname() != null) { string += contact.getLastname(); }
      if (contact.getAddress() != null) { string += contact.getAddress();}
      if (contact.getAllPhones() !=null) { string += contact.getAllPhones();}
      if (contact.getAllEmails() !=null) { string += contact.getAllEmails();}
      return string.replaceAll(" |\n|\n\n|-", "");
  }
}