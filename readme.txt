Group members (Group-92):
2022264 - Kuvam
2022274 - Manan Kadecha


How to run Game
1) open the extracted zip folder in cmd and go to the Stick Hero folder.
2) type in command "mvn package" to compile and package a Java project, creating a distributable JAR or WAR file.
3) type in command "java -jar target/StickHero-1.0.jar" to exceuted the program

How to play Game
To start the game click on the play button , the game starts, you have to use your mouse to extend a stick to desired length so that the player can go toh the next bridge , hold the mouse button to start generating the stick and release the button toh stop generating, after the player starts moving you can collect the cherries by flipping the player using mouse button , on dying, you can revive yourself using 3 cherries 
The restart button is used for restarting the game 
The home button is used to return back to home



Design pattern
singleton
The Singleton pattern is used to ensure that there is only one instance of the StickHero and GameEngine classes in the application. This is often useful when a single point of control or coordination is required, as in the case of managing the game's main character (StickHero) and the game logic (GameEngine). The pattern provides a global point of access to these instances through static methods or variables, allowing easy and centralized control over their behavior. This can help in avoiding unnecessary instantiations and ensuring that the state and behavior of these classes remain consistent throughout the application.



