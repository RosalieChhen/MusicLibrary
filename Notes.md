# Music library

## Start

- Project creation with *spring initializr* https://start.spring.io/
- Use of *Gradle* as Build tool
- Use of Java 17 and JDK 17 => *Lombok 1.8* is not compatible with *Java 21* 
- Achtung : change build config of Gradle to use JDK 17

## Modelisation

### Entity

- Implements **Serializable** : allows to convert an Object to stream to send over the network or to store into a file
- Use of **Java Persistence API** now called **Jakarta** : define a standard for management of persistence and object/relational mapping in Java environments