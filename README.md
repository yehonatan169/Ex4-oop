# Ex4:
Fourth assignment of OOP course. Contributers: Yehonatan Amosi, Amit Goffer

# Description
project contains 2 parts:
  - first part:
    deals with the construction of directional weighted graphstarting from the creation of the nodes and edges in the graph,
    continues with the creation of the graph itself (such as connecting the nodes in the graph and more) and basic algorithms related to the graph (paths in the parent link         graph and more). A graph is made up of four interfaces arranged according to the hierarchy from the creation of a node to the execution of an algorithm on the graph.
  - Second part:
    Engaged in creating the Pokemon game from building the game itself graphics, actions performed in the game, placing the agents and building a victory strategy.
    
# First Part
  # Graph and Algorithems:

 the first part is creating the graph, and the functions which are used on it, all of which was made in Ex2, and explained in detail at Ex2 Task.<br>
 for full details you can go right here: https://github.com/yehonatan169/Ex2_oop <br>
 
           
# The second part: Pokemon game 
- The second part of the project is the one in which we gonna explain in full details:<br>
it takes the graph from Ex2, and runs the game on it.

- How it works: the client interface gets data from the server in real time, which includes the prokemons, score, time and moves.
- the client goal is to control the agents on the game to capture the pokemons as best as they can, based on the time it will take to reach the pokemons<br>
and the pokemons values, with restriction of 10 moves per second.
in order to anchieve highest score possibles, it requires to develop a strategy to obtain the pokemons, and implement it with algoirithms:

# Project structure:
- the project is made with key classes:

## Functionality classes: 
# Agent class:
Class representing an agent in a graph for each agent Multiple fields:.
id, value, src, dest, speed, pos, stations, nextTarget.

- id, value src, dest, speed and pos are fields which are being set by the server, either on initilization or when given updates about the agents from<br>
the client.

- stations and nextTargets are additional classes which are not reconinized by the server, there has to be manually updated by the client in order to
- run the agents.

stations field is a list of integers, which represents the id's of all the next stations - the graph nodes, the agents needs to visit.
for every 2 consecutive numbers - in the graph, there is a direct edge from any current node with the id of the number in the list, to the next <br>
number which represents an node's id.

nextTargets field is a list of all pokimons in which the agent is currently set to capture.<br>

with the stations field we give each agents a route to take to obtain the pokemons, and with nextTargets field he knows to report back when he captured the pokemons on route.

it is nextTargets and stations field which are used the most for the calculations of which pokimons the agents should be assigned at any given time.

the class also contain an update agent function, which update the agent with the most relevent data from the server

# Pokemon class:
This class defines pokemon:
- its default fields are 
type: which helps us to know on which direction of an edge you can find the pokemon.
value: how much the pokemon worths.
pos - the pokemon's location on the graph.

pokemon has an addional field which is "assignedTo" - which tells us if the pokemon is currently being targeted by an agent.
if it does - the value will be the agent's id, else - it will be -1.

the pokemon class has compareTo function, which purpose is to sort any list of pokemons based on their value - from the most valued to the lowest.

# fileWriter:
fileWriter is an aid class whose purpose is to save graphs for later loadings to the game.

# DataLoad:
DataLoad class purpose is to obtain the JSON Data given to it from the server, and return the relevant objects with the data:
It loads pokemons, agents, and gameInfo


# Client: 
a class which purpose is to communicate with the server, from which we can obtain the data, and update the server for any prograss we had made.

# StudentCode:
the studentCode class is the main game runner, it serves as a communication class from the server, to the calculator "Game" class, and the GUI classes.
First - it accepts the data from the server, and while the game is being run, it goes in a loop - constantly updates the data.
in the loop you check constantly for any captures of pokemons, and assigning tasks for the agents.

# Game:
The Game Class is the body of the project - in which the calculation functions of the game is being stores.
first - it initilizes the data from the server with initilize method (with the help of the aid classes) to the objects, and set the graph on which the calculations are made.
it also constantly updates them with the "update" method.

- Game also has serveral more functions:
findPokemon: from the server we recieve only x,y geolocation, without any further context to the graph.
the purpose of findPokemon method is to cross the coordinates on the graph, see if there any match to any of its edges.
the pokemon type value helps us to detemine on which direction of the edge the pokemon is location.
the function recieves a pokemon, and returnes the Edge he is located in.

reportApproximatityToPokemon: 
the function compares the agent's current geolocation, and compares it with any of the pokemon, if it detects a match - it reports back to the server.
this function is being activated from the main loop.

goToNextStation:
this function is also being activated from main loop.
with this function - the main runner askes for the agent current status - if he is currently moving to some destination, do nothing.
else - assign orders for the agents with agentOrders function.

agentOrders:
this function is being activated when the agent is not currently moving.
if he is not moving - checks if the agent has any next stations.
if he has - assign the agent the first element in the list, it will be his next station.
else - it means the agents as no current course to go to, so one will has to be assigned to it.

setCourse: the main function of the class.
it recieved an agents, and check the current positions of all the pokemons on the graph.
then, with graphAlgo fuunction getBothDistanceAndPath it calculates with dijextra the shortest path from the agent to every node in the graph.
getBothDistanceAndPath returns DistanceReturnedData object, which contains in additional the shortestDist and path to a certain node,
2 arrays of data - allDists and indexesArray.
allDists holds the value of the shortest distance it takes the agent to reach every node.
indexesArray holds for every node, the value of the previous node in the shortest path.
that way, it is possible to contruct a path from the agent to any node.<br>
after the distance to every pokemon is recieved, the function selects the pokemon which is the closest to the agent, and assign it to the agent.
if there are any addional pokemon on the same edge - they are assigned as well.
then the function gives the agent his next station, and next targets.



## GUI:
- the GUI classes are to recieve the data in orderly manner, and presents it to the user at real time.
- the gui interface is being  made from some classes:
FrameGUI - the main holder of the GUI window, which recived the data and passes it to the panels under it.
the frameGUI is being made from 2 panels - PanelGUI which displayes the game itself.<br>
and the panelDataGUI - which displays in real time the game data, such as time, score and number of moves.
the 2 panels work independantly from each other, for best performence.

with all the classes working together - the user can enjoy seeing his agents catch them all.
