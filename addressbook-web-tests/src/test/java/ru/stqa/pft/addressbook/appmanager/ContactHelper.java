package ru.stqa.pft.addressbook.appmanager;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import ru.stqa.pft.addressbook.model.ContactData;
import ru.stqa.pft.addressbook.model.Contacts;

public class ContactHelper extends HelperBase {
  public ContactHelper(WebDriver wd) {
    super(wd);
  }

  public void returnToHomePage() {
    click(By.cssSelector("img[title='Addressbook']"));
  }

  public void initContactCreation() {
    click(By.linkText("add new"));
  }

  public void fillContactForm(ContactData contactData, boolean creation) {
    type(By.name("firstname"), contactData.getFirstname());
    type(By.name("lastname"), contactData.getLastname());
    attach(By.name("photo"), contactData.getPhoto());

    if (creation) {
      if (contactData.getGroups().size() > 0) {
        Assert.assertTrue(contactData.getGroups().size() == 1);
        new Select(wd.findElement(By.name("new_group"))).selectByVisibleText(contactData.getGroups().iterator().next().getName());
      } //else {
        //Assert.assertFalse(isElementPresent(By.name("new_group")));
      //}
    }    
  }

  public void submitContactCreation() {
    click(By.name("submit"));
  }

  public void deleteSelectedContacts() {
    click(By.cssSelector("input[value='Delete']"));
    isAlertPresent();
  }

  public void selectContactById(int id) {
    wd.findElement(By.cssSelector("input[id='" + id + "']")).click();
  }

  public void initContactModification() {
    click(By.cssSelector("img[alt='Edit']"));
  }

  public void submitContactModification() {
    click(By.name("update"));
  }

  public void create(ContactData contact, boolean creation) {
    initContactCreation();
    fillContactForm(contact, creation);
    submitContactCreation();
    returnToHomePage();
  }

  public void modify(ContactData contact) {
    selectContactById(contact.getId());
    initContactModification();
    fillContactForm(contact, false);
    submitContactModification();
    contactCache = null;
    returnToHomePage();
  }

  public void delete(ContactData contact) {
    selectContactById(contact.getId());
    deleteSelectedContacts();
    contactCache = null;
    returnToHomePage();
  }

  public boolean isThereAContact() {
    return isElementPresent(By.name("entry"));
  }

  public int count() {
    return wd.findElements(By.name("entry")).size();
  }

  public void viewContactsWithoutGroups() {
    Select select = new Select(wd.findElement(By.name("group")));
    select.selectByIndex(1);
  }

  public void selectDropDown(String locator, int index) {
    List<Integer> indices = new ArrayList<>();
    Select select = new Select(wd.findElement(By.name(locator)));
    List<WebElement> options = select.getOptions();
    for (int i = index; i < options.size(); i++) {
      indices.add(i);
    }
    new Select(wd.findElement(By.name(locator))).selectByIndex(indices.iterator().next());
  }

  public void viewContactsInGroup() {
    selectDropDown("group", 2);
  }

  public void addGroupTo(ContactData contact) {
    wd.findElement(By.cssSelector("input[id='" + contact.getId() + "']")).click();
    selectDropDown("to_group", 0);
    wd.findElement(By.name("add")).click();
  }

  public void removeGroupFrom(ContactData contact) {
    wd.findElement(By.cssSelector("input[id='" + contact.getId() + "']")).click();
    wd.findElement(By.name("remove")).click();
  }

  private Contacts contactCache = null;

  public Contacts all() {
    /*
    if (contactCache != null) {
      return new Contacts(contactCache);
    }
    */
    contactCache = new Contacts();
    List<WebElement> rows = wd.findElements(By.name("entry"));
    for (WebElement row : rows) {
      List<WebElement> cells = row.findElements(By.tagName("td"));
      int id = Integer.parseInt(cells.get(0).findElement(By.tagName("input")).getAttribute("value"));
      String lastname = cells.get(1).getText();
      String firstname = cells.get(2).getText();
      String allPhones = cells.get(5).getText();
      String allEmails = cells.get(4).getText();
      String address = cells.get(3).getText();
      contactCache.add(new ContactData().withId(id).withFirstname(firstname).withLastname(lastname)
      .withAllPhones(allPhones).withAllEmails(allEmails).withAddress(address));
    }
    return new Contacts(contactCache);
  }

  public ContactData infoFromEditForm(ContactData contact) {
    initContactModificationById(contact.getId());
    String firstname = wd.findElement(By.name("firstname")).getAttribute("value");
    String lastname = wd.findElement(By.name("lastname")).getAttribute("value");
    String homePhone = wd.findElement(By.name("home")).getAttribute("value");
    String mobilePhone = wd.findElement(By.name("mobile")).getAttribute("value");
    String workPhone = wd.findElement(By.name("work")).getAttribute("value");
    String email1 = wd.findElement(By.name("email")).getAttribute("value");
    String email2 = wd.findElement(By.name("email2")).getAttribute("value");
    String email3 = wd.findElement(By.name("email3")).getAttribute("value");
    String address = wd.findElement(By.name("address")).getText();
    wd.navigate().back();
    return new ContactData().withId(contact.getId()).withFirstname(firstname).withLastname(lastname)
            .withHomePhone(homePhone).withMobilePhone(mobilePhone).withWorkPhone(workPhone)
            .withEmail1(email1).withEmail2(email2).withEmail3(email3).withAddress(address);
  }

  private void initContactModificationById(int id) {
    WebElement checkbox = wd.findElement(By.cssSelector(String.format("input[value='%s']", id)));
    WebElement row = checkbox.findElement(By.xpath("./../.."));
    List<WebElement> cells = row.findElements(By.tagName("td"));
    cells.get(7).findElement(By.tagName("a")).click();
        //wd.findElement(By.xpath(String.format("//input[@value='%s']/../../td[8]/a", id))).click();
    //wd.findElement(By.xpath(String.format("//tr[.//input[@value='%s']]/td[8]/a", id))).click();
    //wd.findElement(By.cssSelector(String.format("a[href='edit.php?id=%s']", id))).click();
  }

  public String getInfoFromDetailsPage(ContactData contact) {
    initContactDetailsById(contact.getId());
    String info = wd.findElement(By.cssSelector(String.format("div[id='content']"))).getText()
    .replaceAll("H: |M: |W: ", "").replaceAll("\\(www.+", "").replaceAll("[-()]", "").replaceAll(" |\n|\n\n", "").replaceAll("Mem.+", "");
    System.out.println(info);
    return info;
    /*
    return new ContactData().withId(contact.getId()).withFirstname(firstname).withLastname(lastname)
            .withHomePhone(homePhone).withMobilePhone(mobilePhone).withWorkPhone(workPhone)
            .withEmail1(email1).withEmail2(email2).withEmail3(email3).withAddress(address);
    */
  }

  private void initContactDetailsById(int id) {
    wd.findElement(By.cssSelector(String.format("a[href='view.php?id=%s']", id))).click();
  }
}
