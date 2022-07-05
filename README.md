Animal Park
=========
### Main description

Animal island simulator in java.

The program populates the island with animals and simulates their life with elements of randomness.
Program settings are requested at startup, or default settings are used.

The default settings are contained in the folder ```resources```:
- ```app.properties``` - global app settings
- ```creature-settings.yaml``` - creatures configurations

Frameworks and libraries used:
- jline for reading from the console
- JColor for displaying text in color to the console

Build project:

```
$ cd creature-park
$ mvn package
```

Run project:
```
$ java -jar ./target/creature-park.jar
```

### Restrictions

- The maximum size of the map or any other configurable parameters is limited to ``Integer.MAX_VALUE``
- The move takes place in rounds. Until everyone in the current round comes off, the next round won't start
- The functionality of clearing the console and reading the input to open the menu is supported only in the native console

### Brief description of packages

In the root package of the project `com.mckeydonelly.animalpark` there is a class `App` containing the entry point to the application.

Packages:  
- ``utils`` - contains classes for reading from the console and displaying statistics
- ``park`` - contains the main class for starting data preparation and starting logic, as well as statistics output
- ``menu`` - contains classes for working with menus
- ``settings`` - contains classes for working with program settings
- ``creature`` - contains animal classes and their parameters
- ``map`` - contains classes for working with the map
- ``activity`` - contains classes for running animal life in multithreaded mode 

Example:
![demo-creature-park](https://user-images.githubusercontent.com/10290445/175464988-e40f30af-f3ca-4593-8fbe-cd859a29ed79.gif)
