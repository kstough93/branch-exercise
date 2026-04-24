Platform Engineer
A recently signed customer wants to integrate a subset of GitHub’s data into their application. We
have discussed their needs and they want an endpoint they can provide a username that will then
return the data in JSON format as specified below (that also serves as an example):


~~~
{
    user_name: "octocat",
    display_name: "The Octocat",
    avatar: "https://avatars.githubusercontent.com/u/583231?v=4",
    geo_location: "San Francisco",
    email: null,
    url: "https://api.github.com/users/octocat",
    created_at: "Tue, 25 Jan 2011 18:44:36 GMT",
    repos: [{
        name: "boysenberry-repo-1",
        url: "https://api.github.com/repos/octocat/boysenberry-repo-1
    }, ...
    ]
}
~~~

Getting Started: https://docs.github.com/en/rest/guides/getting-started-with-the-rest-api
Data Sources: https://api.github.com/users/octocat | https://api.github.com/users/octocat/repos

The example response above is the result of calling the API with the username “octocat”. The data is
merged after calling the two APIs noted. Be sure to take note of the difference(s) in parameter
names as well as any potential formatting differences between GitHub’s APIs and the expected
response.
No token or signup is necessary to use these Github APIs; however, you can be rate limited. Perhaps
implementing a caching mechanism might help?

## Approach
Uses SpringBoot 3.5 and Java 25
Controller, service layout with models and a mapper to handle mapping responses from GitHub
back to the customer.

Uses following libraries:
- Spring Boot Cache to cache responses using caffeine cache which could be replaced later with Redis.
- MapStruct to map response into response going back to customer
- CaffeinCache for caching responses in case of 429 errors
- Log4j2 to handle logging

Controller will service which will call GitHub API user and user/repos endpoints and map data to response.
May need to use paging, but haven't tested that yet.

Service List
- GitHubService interface for calling GitHub endpoints through Http Service
- HttpService for calling GitHub API
- GitHubUserRepos called by controller to retrieve and format the data from GitHub

### To Run
Download project and build
~~~
./gradlew build
~~~

Execute the project from the IDE or with
~~~
./gradlew bootRun
~~~

Open browser and run endpoint: 
~~~
http://localhost:8080/githubrepos/v1/{username}
~~~