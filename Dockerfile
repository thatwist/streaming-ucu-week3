FROM java:8
WORKDIR /
ADD target/scala-2.12/streaming-ucu-week3-assembly-0.1.jar hello.jar
CMD java -jar hello.jar
