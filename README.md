# sse-app

This is a Server-Sent-Event application build in Clojure with help of Boot. The idea of this application is to prove how can clojure JVM world can increase the performance of Web Application by manipulating data directly with a high speed output, elaborate simple server sent events with core.async, use authorization token, and way much more. I wanted to target how can a clojure sse server could easily fix how 2DA-Analytics handles API endpoints. Using SSE will avoid the UI to be making excesive amount of queries every time to check if any have changed. Instead, the server should be responsible of handling every changed made to the data and leave the UI to do only what is supposed to do, make data human readble. Thus, sse will create subscriptions for every API endpoint, handle data changes, manipulate the data, load it to the right place, retrieve data, and send events as any of the mentioned happens.

## First things first
Instead of the Client Side making API queries to the server for every change, the Server will handle the changes properly to the corresponding data. A server sent event is a type of subcription or push technology that enables the UI to receive automatic updates from the server through HTTP. This connection is establish as soon the UI is opened. 

### What I am using for the server?
Main server is done with Immutant and core.async. Immuntant is a library that contains necessary function to create a simple http server. I used pedestal which is a framework that contains the necessary immutant calls and http interceptors to facilitated the demontrastion of the purpose of this project.

### Boot cut Jeans.
I use boot as my main clojure build tool. For more information go to www.boot-clj.org to learn how to install it. I use openJDK 8.

## Kick them boots
If you have boot, the only thing you must do is build a jar file and run it.
```
boot build
...
java -jar target/sse-app-1.0.0-SNAPSHOT-standalone.jar
```
There you go, now you have a server with 3 endpoints. You can change the port, add more routes, or break this apart and cry.
