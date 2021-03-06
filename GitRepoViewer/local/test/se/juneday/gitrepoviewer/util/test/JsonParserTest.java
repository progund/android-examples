package se.juneday.gitrepoviewer.util.test;

import java.util.List;

import se.juneday.gitrepoviewer.domain.Repository;
import se.juneday.gitrepoviewer.util.JsonParser;
import static se.juneday.gitrepoviewer.domain.Repository.RepoAccess;

public class JsonParserTest {

  public static void main(String[] args) {

    System.out.print("Parsing JSON String: ");
    List<Repository> repos = JsonParser.parse(jsonData);
    System.out.println("OK");

    System.out.print("Check nr of elements: ");
    assert (repos.size()==2) : "Fail, number of elements (" + repos.size() + ") not 2";
    System.out.println("OK");

    System.out.println("Check repositories: ");
    for (Repository r : repos) {
      System.out.print(" * check name: ");
      assert ( r.name() != null && r.name().length() > 0 ) : "Fail, name null or empty";
      System.out.println("OK");
      
      System.out.print(" * check access : ");
      assert ( r.access() != null
               &&
               ( r.access() == RepoAccess.PRIVATE ||
                 r.access() == RepoAccess.PUBLIC)) : "Fail, access null or faulty";
      System.out.println("OK");
      
      System.out.print(" * check description not null: ");
      assert ( r.description() != null ) : "Fail, description null";
      System.out.println("OK");
      
      System.out.print(" * check license not null: ");
      assert ( r.license() != null ) : "Fail, license null";
      System.out.println("OK");
      
    }
    
    System.out.println("JsonParserTest succeeded :)");
  }


  private static String jsonData = "[  {   \"id\": 24987853,   \"node_id\": \"MDEwOlJlcG9zaXRvcnkyNDk4Nzg1Mw==\",   \"name\": \"timelapse\",   \"full_name\": \"progund/timelapse\",   \"private\": false,   \"owner\": {     \"login\": \"progund\",     \"id\": 19474334,     \"node_id\": \"MDEyOk9yZ2FuaXphdGlvbjE5NDc0MzM0\",     \"avatar_url\": \"https://avatars0.githubusercontent.com/u/19474334?v=4\",     \"gravatar_id\": \"\",     \"url\": \"https://api.github.com/users/progund\",     \"html_url\": \"https://github.com/progund\",     \"followers_url\": \"https://api.github.com/users/progund/followers\",     \"following_url\": \"https://api.github.com/users/progund/following{/other_user}\",     \"gists_url\": \"https://api.github.com/users/progund/gists{/gist_id}\",     \"starred_url\": \"https://api.github.com/users/progund/starred{/owner}{/repo}\",     \"subscriptions_url\": \"https://api.github.com/users/progund/subscriptions\",     \"organizations_url\": \"https://api.github.com/users/progund/orgs\",     \"repos_url\": \"https://api.github.com/users/progund/repos\",     \"events_url\": \"https://api.github.com/users/progund/events{/privacy}\",     \"received_events_url\": \"https://api.github.com/users/progund/received_events\",     \"type\": \"Organization\",     \"site_admin\": false   },   \"html_url\": \"https://github.com/progund/timelapse\",   \"description\": \"Timelapse scripts (bash) for Raspberry PI\",   \"fork\": false,   \"url\": \"https://api.github.com/repos/progund/timelapse\",   \"forks_url\": \"https://api.github.com/repos/progund/timelapse/forks\",   \"keys_url\": \"https://api.github.com/repos/progund/timelapse/keys{/key_id}\",   \"collaborators_url\": \"https://api.github.com/repos/progund/timelapse/collaborators{/collaborator}\",   \"teams_url\": \"https://api.github.com/repos/progund/timelapse/teams\",   \"hooks_url\": \"https://api.github.com/repos/progund/timelapse/hooks\",   \"issue_events_url\": \"https://api.github.com/repos/progund/timelapse/issues/events{/number}\",   \"events_url\": \"https://api.github.com/repos/progund/timelapse/events\",   \"assignees_url\": \"https://api.github.com/repos/progund/timelapse/assignees{/user}\",   \"branches_url\": \"https://api.github.com/repos/progund/timelapse/branches{/branch}\",   \"tags_url\": \"https://api.github.com/repos/progund/timelapse/tags\",   \"blobs_url\": \"https://api.github.com/repos/progund/timelapse/git/blobs{/sha}\",   \"git_tags_url\": \"https://api.github.com/repos/progund/timelapse/git/tags{/sha}\",   \"git_refs_url\": \"https://api.github.com/repos/progund/timelapse/git/refs{/sha}\",   \"trees_url\": \"https://api.github.com/repos/progund/timelapse/git/trees{/sha}\",   \"statuses_url\": \"https://api.github.com/repos/progund/timelapse/statuses/{sha}\",   \"languages_url\": \"https://api.github.com/repos/progund/timelapse/languages\",   \"stargazers_url\": \"https://api.github.com/repos/progund/timelapse/stargazers\",   \"contributors_url\": \"https://api.github.com/repos/progund/timelapse/contributors\",   \"subscribers_url\": \"https://api.github.com/repos/progund/timelapse/subscribers\",   \"subscription_url\": \"https://api.github.com/repos/progund/timelapse/subscription\",   \"commits_url\": \"https://api.github.com/repos/progund/timelapse/commits{/sha}\",   \"git_commits_url\": \"https://api.github.com/repos/progund/timelapse/git/commits{/sha}\",   \"comments_url\": \"https://api.github.com/repos/progund/timelapse/comments{/number}\",   \"issue_comment_url\": \"https://api.github.com/repos/progund/timelapse/issues/comments{/number}\",   \"contents_url\": \"https://api.github.com/repos/progund/timelapse/contents/{+path}\",   \"compare_url\": \"https://api.github.com/repos/progund/timelapse/compare/{base}...{head}\",   \"merges_url\": \"https://api.github.com/repos/progund/timelapse/merges\",   \"archive_url\": \"https://api.github.com/repos/progund/timelapse/{archive_format}{/ref}\",   \"downloads_url\": \"https://api.github.com/repos/progund/timelapse/downloads\",   \"issues_url\": \"https://api.github.com/repos/progund/timelapse/issues{/number}\",   \"pulls_url\": \"https://api.github.com/repos/progund/timelapse/pulls{/number}\",   \"milestones_url\": \"https://api.github.com/repos/progund/timelapse/milestones{/number}\",   \"notifications_url\": \"https://api.github.com/repos/progund/timelapse/notifications{?since,all,participating}\",   \"labels_url\": \"https://api.github.com/repos/progund/timelapse/labels{/name}\",   \"releases_url\": \"https://api.github.com/repos/progund/timelapse/releases{/id}\",   \"deployments_url\": \"https://api.github.com/repos/progund/timelapse/deployments\",   \"created_at\": \"2014-10-09T12:19:20Z\",   \"updated_at\": \"2018-10-08T14:16:35Z\",   \"pushed_at\": \"2018-10-08T14:16:33Z\",   \"git_url\": \"git://github.com/progund/timelapse.git\",   \"ssh_url\": \"git@github.com:progund/timelapse.git\",   \"clone_url\": \"https://github.com/progund/timelapse.git\",   \"svn_url\": \"https://github.com/progund/timelapse\",   \"homepage\": null,   \"size\": 19,   \"stargazers_count\": 0,   \"watchers_count\": 0,   \"language\": \"Shell\",   \"has_issues\": true,   \"has_projects\": true,   \"has_downloads\": true,   \"has_wiki\": true,   \"has_pages\": false,   \"forks_count\": 0,   \"mirror_url\": null,   \"archived\": false,   \"open_issues_count\": 0,   \"license\": {     \"key\": \"gpl-3.0\",     \"name\": \"GNU General Public License v3.0\",     \"spdx_id\": \"GPL-3.0\",     \"url\": \"https://api.github.com/licenses/gpl-3.0\",     \"node_id\": \"MDc6TGljZW5zZTk=\"   },   \"forks\": 0,   \"open_issues\": 0,   \"watchers\": 0,   \"default_branch\": \"master\",   \"permissions\": {     \"admin\": false,     \"push\": false,     \"pull\": true   } } , {   \"id\": 61542578,   \"node_id\": \"MDEwOlJlcG9zaXRvcnk2MTU0MjU3OA==\",   \"name\": \"computer-introduction\",   \"full_name\": \"progund/computer-introduction\",   \"private\": false,   \"owner\": {     \"login\": \"progund\",     \"id\": 19474334,     \"node_id\": \"MDEyOk9yZ2FuaXphdGlvbjE5NDc0MzM0\",     \"avatar_url\": \"https://avatars0.githubusercontent.com/u/19474334?v=4\",     \"gravatar_id\": \"\",     \"url\": \"https://api.github.com/users/progund\",     \"html_url\": \"https://github.com/progund\",     \"followers_url\": \"https://api.github.com/users/progund/followers\",     \"following_url\": \"https://api.github.com/users/progund/following{/other_user}\",     \"gists_url\": \"https://api.github.com/users/progund/gists{/gist_id}\",     \"starred_url\": \"https://api.github.com/users/progund/starred{/owner}{/repo}\",     \"subscriptions_url\": \"https://api.github.com/users/progund/subscriptions\",     \"organizations_url\": \"https://api.github.com/users/progund/orgs\",     \"repos_url\": \"https://api.github.com/users/progund/repos\",     \"events_url\": \"https://api.github.com/users/progund/events{/privacy}\",     \"received_events_url\": \"https://api.github.com/users/progund/received_events\",     \"type\": \"Organization\",     \"site_admin\": false   },   \"html_url\": \"https://github.com/progund/computer-introduction\",   \"description\": null,   \"fork\": false,   \"url\": \"https://api.github.com/repos/progund/computer-introduction\",   \"forks_url\": \"https://api.github.com/repos/progund/computer-introduction/forks\",   \"keys_url\": \"https://api.github.com/repos/progund/computer-introduction/keys{/key_id}\",   \"collaborators_url\": \"https://api.github.com/repos/progund/computer-introduction/collaborators{/collaborator}\",   \"teams_url\": \"https://api.github.com/repos/progund/computer-introduction/teams\",   \"hooks_url\": \"https://api.github.com/repos/progund/computer-introduction/hooks\",   \"issue_events_url\": \"https://api.github.com/repos/progund/computer-introduction/issues/events{/number}\",   \"events_url\": \"https://api.github.com/repos/progund/computer-introduction/events\",   \"assignees_url\": \"https://api.github.com/repos/progund/computer-introduction/assignees{/user}\",   \"branches_url\": \"https://api.github.com/repos/progund/computer-introduction/branches{/branch}\",   \"tags_url\": \"https://api.github.com/repos/progund/computer-introduction/tags\",   \"blobs_url\": \"https://api.github.com/repos/progund/computer-introduction/git/blobs{/sha}\",   \"git_tags_url\": \"https://api.github.com/repos/progund/computer-introduction/git/tags{/sha}\",   \"git_refs_url\": \"https://api.github.com/repos/progund/computer-introduction/git/refs{/sha}\",   \"trees_url\": \"https://api.github.com/repos/progund/computer-introduction/git/trees{/sha}\",   \"statuses_url\": \"https://api.github.com/repos/progund/computer-introduction/statuses/{sha}\",   \"languages_url\": \"https://api.github.com/repos/progund/computer-introduction/languages\",   \"stargazers_url\": \"https://api.github.com/repos/progund/computer-introduction/stargazers\",   \"contributors_url\": \"https://api.github.com/repos/progund/computer-introduction/contributors\",   \"subscribers_url\": \"https://api.github.com/repos/progund/computer-introduction/subscribers\",   \"subscription_url\": \"https://api.github.com/repos/progund/computer-introduction/subscription\",   \"commits_url\": \"https://api.github.com/repos/progund/computer-introduction/commits{/sha}\",   \"git_commits_url\": \"https://api.github.com/repos/progund/computer-introduction/git/commits{/sha}\",   \"comments_url\": \"https://api.github.com/repos/progund/computer-introduction/comments{/number}\",   \"issue_comment_url\": \"https://api.github.com/repos/progund/computer-introduction/issues/comments{/number}\",   \"contents_url\": \"https://api.github.com/repos/progund/computer-introduction/contents/{+path}\",   \"compare_url\": \"https://api.github.com/repos/progund/computer-introduction/compare/{base}...{head}\",   \"merges_url\": \"https://api.github.com/repos/progund/computer-introduction/merges\",   \"archive_url\": \"https://api.github.com/repos/progund/computer-introduction/{archive_format}{/ref}\",   \"downloads_url\": \"https://api.github.com/repos/progund/computer-introduction/downloads\",   \"issues_url\": \"https://api.github.com/repos/progund/computer-introduction/issues{/number}\",   \"pulls_url\": \"https://api.github.com/repos/progund/computer-introduction/pulls{/number}\",   \"milestones_url\": \"https://api.github.com/repos/progund/computer-introduction/milestones{/number}\",   \"notifications_url\": \"https://api.github.com/repos/progund/computer-introduction/notifications{?since,all,participating}\",   \"labels_url\": \"https://api.github.com/repos/progund/computer-introduction/labels{/name}\",   \"releases_url\": \"https://api.github.com/repos/progund/computer-introduction/releases{/id}\",   \"deployments_url\": \"https://api.github.com/repos/progund/computer-introduction/deployments\",   \"created_at\": \"2016-06-20T11:27:02Z\",   \"updated_at\": \"2016-06-27T15:23:30Z\",   \"pushed_at\": \"2016-06-20T12:09:11Z\",   \"git_url\": \"git://github.com/progund/computer-introduction.git\",   \"ssh_url\": \"git@github.com:progund/computer-introduction.git\",   \"clone_url\": \"https://github.com/progund/computer-introduction.git\",   \"svn_url\": \"https://github.com/progund/computer-introduction\",   \"homepage\": null,   \"size\": 30,   \"stargazers_count\": 0,   \"watchers_count\": 0,   \"language\": \"HTML\",   \"has_issues\": true,   \"has_projects\": true,   \"has_downloads\": true,   \"has_wiki\": true,   \"has_pages\": false,   \"forks_count\": 0,   \"mirror_url\": null,   \"archived\": false,   \"open_issues_count\": 0,   \"license\": null,   \"forks\": 0,   \"open_issues\": 0,   \"watchers\": 0,   \"default_branch\": \"master\",   \"permissions\": {     \"admin\": false,     \"push\": false,     \"pull\": true   } }   ]";
  
}
