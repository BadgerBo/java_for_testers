package ru.stqa.pft.rest.tests;

import org.testng.annotations.Test;
import ru.stqa.pft.rest.model.Issue;

import java.io.IOException;
import java.util.Set;

import static org.testng.Assert.assertEquals;

// ПРОБЛЕМЫ С АВТОРИЗАЦИЕЙ
// ПОД ВОПРОСОМ РАБОТОСПОСОБНОСТЬ ИНТЕРФЕЙСА

public class RestTests extends TestBase {

  @Test
  public void testCreateIssue() throws IOException {
    //int issueID = 1;
    //skipIfNotFixed(issueID);
    Set<Issue> oldIssues = getIssues();
    Issue newIssue = new Issue().withSubject("Subject").withDescription("Description");
    int issueId = createIssue(newIssue);
    Set<Issue> newIssues = getIssues();
    oldIssues.add(newIssue.withId(issueId));
    assertEquals(newIssues, oldIssues);
  }
}
