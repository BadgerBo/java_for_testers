package ru.stqa.pft.addressbook.tests;

import java.io.File;
import java.sql.SQLException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;
import ru.stqa.pft.addressbook.model.GroupData;
import ru.stqa.pft.addressbook.model.Groups;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ContactAddToGroupTests extends TestBase {
  
  @BeforeMethod
  public void ensurePreconditions() {
    if (app.db().groups().size() == 0) {
      app.group().create(new GroupData().withName("test"));
    }
    if (app.db().contacts().size() == 0) {
      File photo = new File("addressbook-web-tests/src/test/resources/stru.png");
      app.contact().create(new ContactData().withFirstname("test_name").withLastname("test_surname").withPhoto(photo), false);
    }
  }

  @Test
  public void testContactAddToGroup() throws SQLException {
    app.goTo().homePage();
    app.contact().viewContactsWithoutGroups();
    Contacts contacts = app.contact().all();
    if (contacts.size() == 0) {
      File photo = new File("addressbook-web-tests/src/test/resources/stru.png");
      app.contact().create(new ContactData().withFirstname("test_name").withLastname("test_surname").withPhoto(photo), false);
      app.goTo().homePage();
      app.contact().viewContactsWithoutGroups();
      contacts = app.contact().all();
    }
    ContactData selectedContact = contacts.iterator().next();
    Groups before = app.db().getGroupsFrom(selectedContact);
    app.contact().addGroupTo(selectedContact);
    Groups after = app.db().getGroupsFrom(selectedContact);
    assertThat(after.size(), equalTo(before.size() + 1));
  }
}