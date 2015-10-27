# myjavaapps
Alro Java Apps

##eltiempo

Spring-boot REST API (that consume a OpenWeatherMap info).

* Maven Usage:
mvn spring-boot:run

## playlistcreator

Creates a playlists file from a directory mp3s (based on MP3 IDv2tags) and a directory depth level of 2.

* Usage:
** arg: directory path of the playlist files or subdirectories

```
java -jar playlistcreator-1.0-jar-with-dependencies.jar arg
```

## mywebapp

Webapp template using Java, Spring MVC.

## mqpublisher

RabbitMQ publisher client

* Usage:

```
java -jar mqpublisher-jar-with-dependencies.jar [application.properties] [queue-name] [message] 
```

## mqsubscriber

RabbitMQ subscriber client

* Usage:

```
java -jar mqsubscriber-jar-with-dependencies.jar [application.properties] [queue-name]
```


