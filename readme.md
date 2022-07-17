# Task for fetching Articles along with test cases

An android application to get the articles from remote server, show the list in the recyclerview, and upon clicking the item, open the details of that article.
In this application, following Android Architecture components and libraries are used:

**MVVM** Design Pattern
**Room** To store data in the local database
**HILT (DI)** For Dependency Injection
**Retrofit** For remote API calls
**Coroutines** For Asynchronous calls to the remote server

When you first run the application, data is fetched from the remote server using retrofit, and this data is saved in the local database using Room library. Next time when user opens the application, it will check for the Internet connectivity, if the user is connected, it will bring new data from remote, if user is not connected, app will fetch data from the local database.
The app will show progress bar and take time only on the first launch of application to load data from remote server. Once it's downloaded, it will be stored locally for future use, and the recyclerview will be populated instantly without any delay on second launch of app.

There are test cases, which can be seen in both **(test)** and **(androidTest)** packages.

**Test** is for unit testing in which no android frameworks needed.
**AndroidTest** is for database testing and it requires android framework like context.

<br>
<p align="center">
    <img src="Screenshot 1.png" width="180"/>
    <img src="Screenshot 2.png" width="180"/>
</p>
<br>
