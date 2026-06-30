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
Standard REST API is implemented, but without HATEOS since this is a custom response.

### Uses following libraries:
- Spring Boot Cache to cache responses using Caffeine cache which could be replaced later with Redis or another caching framework depending on traffic volume and growth.
- MapStruct to map response into response going back to customer.  This library makes it easy to map JSON objects to DTOs.  The library does use reflection so if volume increase and performance is impacted, we may want to switch to hand-coded translations for speed.
- CaffeineCache for caching responses in case of 429 errors
- Log4j2 to handle logging

Controller will service which will call GitHub API user and user/repos endpoints and map data to response.

### Service List
- GitHubService interface for calling GitHub endpoints through Http Service
- HttpService for calling GitHub API
- GitHubUserRepos called by controller to retrieve and format the data from GitHub

### Future enhancements
- Caffeine cache could be replaced easily with Redis cache if traffic and volume grows beyond what the in-memory cache can handle.
- JSON to DTO translations could also be hand-coded if volume increases to the point where the reflection in Mapstruct is causing performance issues.  Need to keep an eye on performance metrics.
- For users with a large number of repositories, we can implement paging on the call to retrieve user repos.  The current max for one page on this call is 30.  If there are more than 30 results, GitHub will return a link header with links to relative pages.
- Sequential GitHub API calls - GitHubUserRepoService.getGitHubUserRepos() calls the user endpoint and then the repos endpoint sequentially. These are completely independent calls — the response of the first is not needed to make the second. Running them in parallel would halve the latency for cache misses. With Java 25,      
structured concurrency (StructuredTaskScope) or CompletableFuture.allOf() both work cleanly here.


### To Run
Download project and build
Make sure you are using Java 25, this project was built with Temurin 25.0.3 

To build and execute unit and integration tests
~~~
./gradlew build
~~~

Execute unit tests only
~~~
./gradlew test
~~~

Execute integration tests only
~~~
./gradlew integrationTest
~~~

Execute the project from the IDE or with
~~~
./gradlew bootRun
~~~

Open browser and go to URL: 
~~~
http://localhost:8080/githubrepos/v1/{username}
~~~

