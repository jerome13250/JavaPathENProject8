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

### Global class diagram
![UML-class-diagram](https://raw.githubusercontent.com/jerome13250/JavaPathENProject8/master/images/classDiagram.gif)



### Built With

* [Java 8](https://adoptopenjdk.net/)
* [Docker](https://docs.docker.com/)


<!-- GETTING STARTED -->
## Getting Started

This is how to set up the project locally.
To get a local copy up and running follow these simple example steps:

### Prerequisites

Check that you have : 
* Java 8 installed
  ```sh
  java -version
  ```
* For Windows Home Edition, use WSL2 (Windows subsystem Linux)  
  [How to install and run Docker natively on Windows 10 Home](https://www.padok.fr/en/blog/docker-windows-10)

* Docker Desktop  
  [Install Docker Desktop on Windows](https://docs.docker.com/desktop/windows/install/)
  
  
### Installation

1. Choose a directory
   ```sh
   cd /path/to/directory/project
   ```
2. Clone the repo
   ```sh
   git clone https://github.com/jerome13250/JavaPathENProject8.git
   ```
3. Select the JavaPathENProject8 directory
   ```sh
   cd JavaPathENProject8
   ```
4. Assemble executable jar archives using gradle wrapper provided in the folder, it downloads automatically the correct Gradle version if necessary.
   ```sh
   gradlew clean bootJar
   ```
5. On Windows system, use WSL2 (Windows subsystem Linux)
   ```sh
   wsl
   ```
6. Launch the docker-compose to launch the 4 components:
   ```sh
   docker-compose up
   ```
7. To access the application endpoints, open your browser, go to :  
	[webapp Swagger](http://localhost:9000/swagger-ui/#/)  
	[gps-api Swagger](http://localhost:9001/swagger-ui/#/)  
	[reward-api Swagger](http://localhost:9002/swagger-ui/#/)  
	[trippricer-api Swagger](http://localhost:9003/swagger-ui/#/)  
	

   


