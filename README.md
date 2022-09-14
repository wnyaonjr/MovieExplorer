# 09122022-winifredo-ya-on-jr-android

## Name
Movie Explorer

## Description
An Android Application that allows user to search movies and add them as favorites, supports local saving of favorite movies

## Persistence
The application uses Android Room for data persistence, it saves all the response from server based on the search results.
Before saving search results, it will delete all data that is not set as user favorite.
Setting of movie as favorite is also done through Android room.
Android Room becomes the source of truth, GetDisplayMoviesUseCase sorts and filters current movies based on the search result or display all favorite movies when there is no search input.

## Architecture
The application uses Dependency Injection pattern along with "screaming architecture" for use cases definition. I often use this since it makes components reusable in the application, it also makes the functionalities descriptive through the use cases.
A disadvantage for this architecture is that it oftens create more codes and need to find a way to group related use cases.

## Other information
Jetpack compose was used for this application to showcase enthusiasm in learning new technologies related to Android, I also find it useful specially in reducing boilerplate codes when creating lists.
Please do note that I can still do layout using XML.