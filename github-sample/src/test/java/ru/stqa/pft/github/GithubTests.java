package ru.stqa.pft.github;

import com.google.common.collect.ImmutableMap;
import com.jcabi.github.*;
import org.testng.annotations.Test;

import java.io.IOException;

public class GithubTests {

  @Test
  public void testCommits() throws IOException {
    Github github = new RtGithub("d9b870d2aa9a4cc9c6ca0fbed7db9622948a9008");
    RepoCommits commits = github.repos().get(new Coordinates.Simple("BadgerBo", "libot")).commits();
    for (RepoCommit commit : commits.iterate(new ImmutableMap.Builder<String, String>().build())) {
      System.out.println(new RepoCommit.Smart(commit).sha());
      System.out.println(new RepoCommit.Smart(commit).message());
      System.out.println("\n");
    }
  }
}
