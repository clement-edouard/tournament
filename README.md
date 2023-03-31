# Tournament

| [![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?&style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/) | [![MongoDB](https://img.shields.io/badge/MongoDB-4EA94B?style=for-the-badge&logo=mongodb&logoColor=white)](https://www.mongodb.com/)                                                                                                          |[![Gradle](https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)](https://gradle.org/)                                                                                                          |
| ------------------------------------------------------------------------------------------------------------------------ | -----------------------------------------------------|------------------------------------------------------- |

REST API in Kotlin with Ktor for managing players in a tournament. The objective of this API is to manage players by recording their scores. Players can be sorted by their scores and ranked in the leaderboard.

## Prerequisites
To have a MongoDb database locally on port 27017.  
If not, launch a MongoDb container with Docker using the following command:  
`docker run --name mongodb-cedouard -d -p 27017:27017 mongo`
## Build

Clone the project

```bash
  git clone https://link-to-project/tournament
```

Go to the project directory

```bash
  cd tournament
```
Build the project with Gradle

```bash
  ./gradlew build
```

## Run Locally

Start the server

```bash
  java -jar build/libs/tournament-all.jar
```


## Important URL
- Swagger : [http://localhost:8080/swagger](http://localhost:8080/swagger)
- Players ordered by score : [http://localhost:8080/player](http://localhost:8080/player)
- Players ordered by score with rank : [http://localhost:8080/player/rank](http://localhost:8080/player/rank)

## Running Tests

To run tests, run the following command

```bash
  ./gradlew test
```

The results of the unit tests execution can be found here:  
`tournament/build/reports/tests/test/index.html`

## Author

- [@clement-edouard](https://github.com/clement-edouard)