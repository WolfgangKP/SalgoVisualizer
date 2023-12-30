# Salgo-Visualizer

Summary:
This is a maze generation and pathfinding visualizer written in Java. Mazes are generated randomly and with a depth first traversal. The A*, Breadth First, Depth First, Dijkstra, and Greedy Best First search algorithms are supported for pathfinding.

Grid-based implementation:
This project is grid-based with movement restricted to the horizontal and vertical. All the edges to a particular grid node are uniform in cost, which is defined by that node's weight. By default, all grid nodes have a weight of one. Nodes set to custom, and the node set to finish, can be assigned an integer weight ranging from zero to 100. Nodes set to wall, and the node set to start, always have a weight of zero. I have eliminated the "better path" check from A* and Dijkstra, since the first time a node is discovered, so is the best path to it.

Controls:
A user is able to adjust grid size, visualization speed, and the weight associated with certain node types (custom and finish). There are four different node types: start, finish, custom, and wall. One start node and one finish node are necessary to search. They can be placed anywhere. Multiple custom nodes and multiple wall nodes are allowed. They can be placed anywhere.

Data:
Upon the completion of a search, the number of discovered nodes, visited nodes, and path length from start to finish is displayed. Hovering over a node will provide the user with information too. "W" stands for weight, "TC" stands for total cost from the start to this node, and "H" stands for heuristic.

Heuristic:
The heuristic used in A* and Greedy Best First is Manhattan Distance.


