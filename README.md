# JMultiGameServer
A game server that supports multiple games written in Java. Multiple games can be played at once by clients, and the server can be expanded to support many different games.

# Java Version
The source code is tested and run using OpenJDK 11.

# Running the server
First, compile the Server file to bytecode:

> javac Server.java

Then run the resulting bytecode:

> java Server

# Using the client
First, compile the client:
> javac Client.java

Then run the resulting bytecode and provide the server address and port number in arguments:

> java Client [Server Address] [Port Number]

