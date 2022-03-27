package ch.zhaw.pm2.racetrack.algorithm.pathfinder;
import ch.zhaw.pm2.racetrack.Car;
import ch.zhaw.pm2.racetrack.PositionVector;
import ch.zhaw.pm2.racetrack.Track;
import ch.zhaw.pm2.racetrack.algorithm.bresenham.BresenhamPathCalculator;
import ch.zhaw.pm2.racetrack.algorithm.bresenham.PositionPath;
import ch.zhaw.pm2.racetrack.given.ConfigSpecification.SpaceType;
/**
 * Uses the dijkstra algorithm to find the shortest path in the graph
 */
public class DijkstraPathFinder {
    /**
     * the nodes of the graph
     */
    private java.util.List<ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphNode> nodes = new java.util.LinkedList<>();

    /**
     * the start node
     */
    private ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphNode startNode;

    /**
     * the end nodes
     */
    private java.util.List<ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphNode> endNode = new java.util.ArrayList<>();

    /**
     * The priority queue for the next node
     */
    private java.util.PriorityQueue<ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphNode> queue = new java.util.PriorityQueue<>(ch.zhaw.pm2.racetrack.algorithm.pathfinder.DijkstraPathFinder::compareGraphNodes);

    /**
     * the nodes already visited by dijsktra
     */
    private java.util.Set<ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphNode> visitedNodes = new java.util.HashSet<>();

    /**
     * the track
     */
    private ch.zhaw.pm2.racetrack.Track track;

    /**
     * the var
     */
    private ch.zhaw.pm2.racetrack.Car car;

    /**
     * Constructor
     *
     * @param graph
     * 		the graph
     * @param track
     * 		the track
     * @param car
     * 		the car
     */
    public DijkstraPathFinder(ch.zhaw.pm2.racetrack.algorithm.pathfinder.Graph graph, ch.zhaw.pm2.racetrack.Track track, ch.zhaw.pm2.racetrack.Car car) {
        this.track = track;
        this.car = car;
        this.nodes.addAll(graph.nodes());
        this.endNode.addAll(graph.endNodes());
        nodes.forEach(n -> n.setTime(java.lang.Integer.MAX_VALUE));
        queue.addAll(nodes);
    }

    /**
     * Tries to find the shortest path.
     *
     * @param start
     * 		the start position
     * @param velocity
     * 		the velocity of the start position
     * @return the graph path or an empty optional if no path could be found
     */
    public java.util.Optional<ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphPath> findPath(ch.zhaw.pm2.racetrack.PositionVector start, ch.zhaw.pm2.racetrack.PositionVector velocity) {
        // init start node
        java.util.Optional<ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphNode> startNodeOptional = nodes.stream().filter(n -> start.equals(n.getPosition()) && velocity.equals(n.getVelocity())).findAny();
        if (startNodeOptional.isEmpty()) {
            return java.util.Optional.empty();
        }
        startNode = startNodeOptional.get();
        startNode.setTime(0);
        // re-add startNode to update order
        queue.remove(startNode);
        queue.add(startNode);
        // start algorithm
        ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphNode lastNode = null;
        while (!queue.isEmpty()) {
            lastNode = queue.poll();
            visitNode(lastNode);
        } 
        return endNode.stream().sorted(java.util.Comparator.comparing(ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphNode::getTime)).findFirst().flatMap(this::constructPath);
    }

    /**
     * Visits a node. If a shorter path to a connected node is found, the given node
     * is set as the previous thread
     *
     * @param currentNode
     * 		the node to visit
     */
    private void visitNode(ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphNode currentNode) {
        for (ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphConnection connection : currentNode.getConnections()) {
            ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphNode nextNode = connection.next();
            if ((!visitedNodes.contains(nextNode)) && canNodesBeConnected(currentNode, connection, nextNode)) {
                connectNodes(currentNode, connection, nextNode);
            }
        }
        visitedNodes.add(currentNode);
    }

    /**
     * If two nodes can be connected
     *
     * @param currentNode
     * 		the current node
     * @param connection
     * 		the connection between the nodes
     * @param nextNode
     * 		the next node
     * @return if the connection is allowed
     */
    private boolean canNodesBeConnected(ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphNode currentNode, ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphConnection connection, ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphNode nextNode) {
        ch.zhaw.pm2.racetrack.algorithm.bresenham.PositionPath path = new ch.zhaw.pm2.racetrack.algorithm.bresenham.BresenhamPathCalculator(currentNode.getPosition(), nextNode.getPosition()).calulatePath();
        return (!willCarLapCorrectly(path, nextNode.getVelocity())) && (!path.hasCar(car, track));
    }

    /**
     * Connects two nodes if the current node is the shortest path to the nextNode
     *
     * @param currentNode
     * 		the current node
     * @param connection
     * 		the connection between the nodes
     * @param nextNode
     * 		the next node
     */
    private void connectNodes(ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphNode currentNode, ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphConnection connection, ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphNode nextNode) {
        int timeToNext = currentNode.getTime() + connection.time();
        if (nextNode.getTime() > timeToNext) {
            nextNode.setTime(timeToNext);
            nextNode.setPrevious(connection);
            // re add nextNode to sort the queue
            queue.remove(nextNode);
            queue.add(nextNode);
        }
    }

    /**
     *
     * @param path
     * 		the path
     * @param velocity
     * 		the velocity
     * @return If the car drives over the finish line in the correct direction
     */
    private boolean willCarLapCorrectly(ch.zhaw.pm2.racetrack.algorithm.bresenham.PositionPath path, ch.zhaw.pm2.racetrack.PositionVector velocity) {
        java.util.stream.Stream<ch.zhaw.pm2.racetrack.given.ConfigSpecification.SpaceType> finishLineSpaceTypes = path.stream().map(track::getSpaceType).filter(this::isFinishLine);
        return finishLineSpaceTypes.anyMatch(tile -> willCarLapIncorrectly(tile, velocity));
    }

    /**
     *
     * @param spaceType
     * 		the finish line space type
     * @param velocity
     * 		the velocity
     * @return if with the given velocity the car drives over the finish line in
    the incorrect direction
     */
    private boolean willCarLapIncorrectly(ch.zhaw.pm2.racetrack.given.ConfigSpecification.SpaceType spaceType, ch.zhaw.pm2.racetrack.PositionVector velocity) {
        return switch (spaceType) {
            case FINISH_RIGHT ->
                velocity.getX() < 0;
            case FINISH_LEFT ->
                velocity.getX() > 0;
            case FINISH_DOWN ->
                velocity.getY() < 0;
            case FINISH_UP ->
                velocity.getY() > 0;
            default ->
                false;
        };
    }

    /**
     *
     * @param spaceType
     * 		the space type to check
     * @return if the finish line is a space type
     */
    private boolean isFinishLine(ch.zhaw.pm2.racetrack.given.ConfigSpecification.SpaceType spaceType) {
        switch (spaceType) {
            case FINISH_DOWN :
            case FINISH_UP :
            case FINISH_LEFT :
            case FINISH_RIGHT :
                return true;
            default :
                return false;
        }
    }

    /**
     * Constructs the path from the given end node to the start point
     *
     * @param endNode
     * 		the end node
     * @return the path or {@link Optional#empty()} if no path was found
     */
    private java.util.Optional<ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphPath> constructPath(ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphNode endNode) {
        java.util.List<ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphVectorDirection> path = new java.util.ArrayList<>();
        ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphNode currentNode = endNode;
        while (!startNode.equals(currentNode)) {
            ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphConnection previousConnection = currentNode.getPrevious();
            if (previousConnection == null) {
                return java.util.Optional.empty();
            }
            ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphVectorDirection vectorDirection = new ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphVectorDirection(currentNode.getPosition(), previousConnection.direction());
            path.add(0, vectorDirection);
            currentNode = previousConnection.previous();
        } 
        return java.util.Optional.of(new ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphPath(path));
    }

    /**
     * Compares two graph nodes by their time value
     *
     * @param o1
     * 		node 1
     * @param o2
     * 		node 2
     * @return -1 if o1's time value is smaller, 0 if it is equal and 1 if it is
    bigger
     */
    private static int compareGraphNodes(ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphNode o1, ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphNode o2) {
        return java.lang.Integer.compare(o1.getTime(), o2.getTime());
    }
}