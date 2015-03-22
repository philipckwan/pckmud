# Introduction #

A server application that reads in client inputs and pick the next state. The result is an experience to client like an interactive fiction. The ultimate goal is to make a "multi-user dungeon" type of game.


# Details #

The project can be built by ant and Eclipse IDE.

## build by ant ##

To cleanup:

>ant clean

To build:

>ant compile

or

>ant build (This will do a "ant clean" first)

To run:

>ant run

To jar:

>ant jar

## Running the program ##
The build.xml already contains the default argument list to run the program.

If you want to run it manually (assume you are at the project root, same place you running the ant commands, also assume you have created the jar file):

```
>java -cp build\jars\mud.jar com.pck.mud.MudMain -states_filename polygon_categorizer.txt -start_state A
```

## Connecting to the running program ##
You need to use telnet from command line:

>telnet localhost 8091

## Default polygon\_categorizer.txt ##
My first example of running this MUD engine is the polygon categorizer. Just fire it up, connect to it and answer the questions to see the effects.

## Writing your own logic ##
Please see the polygon\_categorizer.txt as example. I might post more detail explanation later.