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


## elkscroller

Elasticsearch scroll backup. Creates a json file of each document in index.

* Usage:

** Edit and override app.properties in resources dir

```
java -jar elkscroller.jar
```

## csv2xslx

Converts CSV file to XSLX. 

* Required for NLB-NER excel report format 
* Default delimiter for cell data ;
* app.properties can be loaded to classpath to change some configuration (delimiter, output extension, header text)

Execution:
```
java -jar csv2xslx.jar input.csv
```
Result: input.xslx file in the path were jar was executed. A log file will be created to track errors during conversion.


## BatchProcess

Batch process of Csv2xlsx. Producer-Consumer model. 10 workers using an ArrayBlockingQueue for concurrent execution. 

app properties

* file.filter.extension=.html.txt.csv
* queue.size=100
* batch.workers=10
* csv.delimeter=;
* dir.output=output


Execution:
```
java -jar batchprocess.jar input
```
input: file or directory of csv files

Result: output/input.xslx file in the path were jar was

## csv2rdf

Converts CSV file to RDF using Apache Jena libraries. Batch process using Producer-Consumer model. 10 workers using an ArrayBlockingQueue for concurrent execution. 

app properties:

* file.filter.extension=.html.txt.csv
* queue.size=100
* batch.workers=10
* csv.delimeter=;
* dir.output=output
* dir.rdf=transform

Execution:
```
java -jar csv2rdf.jar input
```
input: file or directory of csv files

Result: output/*.rdf file in the path were jar was
