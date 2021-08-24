# TourGuide
Openclassrooms project number 8

<!-- ABOUT THE PROJECT -->
## About The Project

Application TourGuide for the startup TripMaster. This is a Gradle multi-project Spring Boot + Docker application for project number 8 of [Openclassrooms](https://openclassrooms.com/) java back-end formation.

Project goals:
* Improve gpsUtil execution time in TourGuideService.trackUserLocation.
* Improve RewardsCentral execution time in RewardsService.getRewardPoints.
* Add an entrypoint for user preferencies modifications, this will allow tripPricer to calculate with correct inputs.
* Split TourGuide in a Gradle multi-project and deploy in docker containers.
* Modify the method TourGuideController.getNearbyAttractions to return closest five tourist attractions to the user.
* Correct unit tests bugs due to "java.util.ConcurrentModificationException"
* Modify performance load tests to use multithreaded solution developped for gpsUtil/RewardsCentral execution time.
* Modify the method TourGuideController.getAllCurrentLocations according to the [TODO](https://github.com/OpenClassrooms-Student-Center/JavaPathENProject8/blob/master/TourGuide/src/main/java/tourGuide/TourGuideController.java#L56), and create unit test.


![UML-class-diagram](https://raw.githubusercontent.com/jerome13250/JavaPathENProject8/master/images/classDiagram.gif)

![database](https://github.com/jerome13250/paymybuddy/blob/master/images/PayMyBuddy_diagram.png)


### Built With

* [Java 8](https://adoptopenjdk.net/)

<!-- GETTING STARTED -->
## Getting Started

This is how to set up the project locally.
To get a local copy up and running follow these simple example steps:

### Prerequisites

Check that you have : 
* Java 11 installed
  ```sh
  java -version
  ```

### Installation

1. Choose a directory
   ```sh
   cd /path/to/directory/project
   ```
2. Clone the repo
   ```sh
   git clone https://github.com/jerome13250/paymybuddy.git
   ```
3. Select the paymybuddy directory
   ```sh
   cd paymybuddy
   ```
4. Package the application (fat jar file) using [maven wrapper](https://github.com/takari/maven-wrapper) provided in the folder, it downloads automatically the correct Maven version if it's not found.
   ```sh
   mvnw package
   ```
5. Execute the jar file
   ```JS
   java -jar ./target/paymybuddy-0.0.1-SNAPSHOT.jar
   ```
6. To access the application, open your browser, go to [http://localhost:8080](http://localhost:8080)

7. Note that the first time, since you start with an empty database, you need to [register](http://localhost:8080/registration) some users to be able to do some operations.

![homepage](https://github.com/jerome13250/paymybuddy/blob/master/images/PayMyBuddy_homepage.png)