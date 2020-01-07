# Cineast_android


<img alt="screenshots/google-play-badge.png" src="screenshots/google-play-badge.png" />

Cineast is an android app querying [TMDb API](https://developers.themoviedb.org/3/getting-started/introduction) to provide information on popular, trending, upcoming, top rated movies and popular actors. Cineast comes in with sharing, searching features, when signed in a user can add movies to their Favorites and  watch lists or rate them.  

This Android App is a clone of the iOS [Cineast](https://apps.apple.com/us/app/cineast/id376167296) App which uses [TMDb API](https://developers.themoviedb.org/3/getting-started/introduction) as well.

## Prerequisites

  #### 1. Obtain Key
  
   Create an account with TMDB to obtain an API key.  
   
  #### 2. Configure gradle.properties  
  
  On obtention of your TMDB key, you will need to add it to a global gradle.properties file so as not to add it to version     control and expose your key and configure the projects app module build.gradle to reflect the name you gave your API Key. To add the newly obtained key to a global gradle.properties: 
   - In the roots folder of the project,  add a txt file named `apikeys.properties` 
   - then inside the file add a property `api_key = TMDb API KEY`. 
   
 On completion of the above steps, the app will be able to pick up your TMDb API KEY.
 
## Project Structure
  This app is built to be modularized to some degree. The modules are:
  
  - app: brings everything together. 
  - core: contains the data models.
  - database: contains the database and repository.
  
  This [blog post](https://proandroiddev.com/build-a-modular-android-app-architecture-25342d99de82) explains the basics of modularization and its benefits. 


## Project Architecture

This app uses the Model-View-Presenter architecture, with the help of the [Mythos library](https://github.com/jhavatar/mythos). 

[Room ORM](https://developer.android.com/topic/libraries/architecture/room) which is part of Android Architecture Components is used. 

A mixture of Callbacks and Coroutines are used for async/background. Future milestones are going to be the sole usage of Coroutines for async/background operations. This [blog post](https://medium.com/androiddevelopers/coroutines-on-android-part-i-getting-the-background-3e0e54d20bb) explains the basics of Coroutines in kotlin.   
 
## Screenshots

<img src="screenshots/Screenshot_20200101-014642.jpg" width="200"> <img src="screenshots/Screenshot_20200101-014651.jpg" width="200"> <img src="screenshots/Screenshot_20200101-014702.jpg" width="200"> <img src="screenshots/Screenshot_20200101-014722.jpg" width="200"> <img src="screenshots/Screenshot_20200101-014737.jpg" width="200"> <img src="screenshots/Screenshot_20200101-014823.jpg" width="200">  <img src="screenshots/Screenshot_20200101-014833.jpg" width="200"> <img src="screenshots/Screenshot_20200101-014848.jpg" width="200">  <img src="screenshots/Screenshot_20200101-014901.jpg" width="200"> <img src="screenshots/Screenshot_20200101-014915.jpg" width="200">  <img src="screenshots/Screenshot_20200101-014924.jpg" width="200"> <img src="screenshots/Screenshot_20200101-014958.jpg" width="200"> <img src="screenshots/Screenshot_20200101-015015.jpg" width="200">  <img src="screenshots/Screenshot_20200101-015050.jpg" width="200"> 






## Libraries Used
[Mythos](https://github.com/jhavatar/mythos) - A Model-View-Presenter library for Android apps.

[CoroutineHelper](https://github.com/flatcircle/CoroutineHelper) - Various helper functions, delegates and extension functions for use in Coroutines

[Leak Canary](https://github.com/square/leakcanary) - Capture Memory Leaks


[Retrofit](https://square.github.io/retrofit/) - Http Client for Api Calls


[Gson](https://github.com/google/gson) - Serialization/Deserialization Library


[Okhttp](https://github.com/square/okhttp) - An HTTP+HTTP/2 client for Android


[Picasso](https://square.github.io/picasso/) - Image Loading Library


[Kodein](https://github.com/Kodein-Framework/Kodein-DI) - Dependency Injection Library


[Architecture Components](https://developer.android.com/topic/libraries/architecture) - Room ORM

[RxJava 2](https://github.com/ReactiveX/RxAndroid)
