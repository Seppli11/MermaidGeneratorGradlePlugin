package ch.zhaw.pm2.racetrack.algorithm.pathfinder.graphgenerator;
import ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphNode;
import ch.zhaw.pm2.racetrack.PositionVector;
import ch.zhaw.pm2.racetrack.PositionVector.Direction;
import ch.zhaw.pm2.racetrack.Track;
import ch.zhaw.pm2.racetrack.algorithm.bresenham.BresenhamPathCalculator;
import ch.zhaw.pm2.racetrack.given.ConfigSpecification.SpaceType;
/**
 * Generates a graph by bruteforcing all possibilities
 */
public class BruteforceGenerator {
    /**
     * the max velocity allowed.
     * The higher this value is, the more nodes are needed in a graph
     */
    private float MAX_VELOCITY = 10;

    /**
     * The nodes being generated
     */
    private java.util.HashMap<ch.zhaw.pm2.racetrack.algorithm.pathfinder.graphgenerator.BruteforceGenerator.GraphVector, ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphNode> nodes = new java.util.HashMap<>();

    /**
     * The coordinates of nodes already visited by the generator. This is to avoid
     * generating the same nodes multiple times
     */
    private java.util.Set<ch.zhaw.pm2.racetrack.algorithm.pathfinder.graphgenerator.BruteforceGenerator.GraphVector> nodeVisited = new java.util.HashSet<>();

    /**
     * A queue of coordinates to be processed
     */
    private java.util.Queue<ch.zhaw.pm2.racetrack.algorithm.pathfinder.graphgenerator.BruteforceGenerator.GraphVector> nodeQueue = new java.util.LinkedList<>();

    /**
     * The start node where the generating begins
     */
    private ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphNode startNode = null;

    /**
     * The track
     */
    private ch.zhaw.pm2.racetrack.Track track;

    /**
     * Constructor
     */
    public BruteforceGenerator() {
    }

    /**
     * Generates the graph
     *
     * @param track
     * 		the track
     * @param startPosition
     * 		where to start
     * @return the generated graph
     */
    public ch.zhaw.pm2.racetrack.algorithm.pathfinder.Graph generateGraph(ch.zhaw.pm2.racetrack.Track track, ch.zhaw.pm2.racetrack.PositionVector startPosition) {
        return generateGraph(track, startPosition, Direction.NONE.vector);
    }

    /**
     * Generates the graph but with a start velocity
     *
     * @param track
     * 		the track
     * @param startPosition
     * 		the start position
     * @param startVelocity
     * 		the start velocity
     * @return the generated graph
     */
    public ch.zhaw.pm2.racetrack.algorithm.pathfinder.Graph generateGraph(ch.zhaw.pm2.racetrack.Track track, ch.zhaw.pm2.racetrack.PositionVector startPosition, ch.zhaw.pm2.racetrack.PositionVector startVelocity) {
        this.track = track;
        startNode = new ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphNode(startPosition, startVelocity);
        ch.zhaw.pm2.racetrack.algorithm.pathfinder.graphgenerator.BruteforceGenerator.GraphVector startVector = new ch.zhaw.pm2.racetrack.algorithm.pathfinder.graphgenerator.BruteforceGenerator.GraphVector(startPosition, startVelocity);
        nodes.put(startVector, startNode);
        nodeQueue.add(startVector);
        while (!nodeQueue.isEmpty()) {
            nextIteration();
        } 
        java.util.List<ch.zhaw.pm2.racetrack.PositionVector> finishLinePotions = track.getFinishLines();
        java.util.List<ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphNode> endNodes = nodes.values().stream().filter(n -> finishLinePotions.contains(n.getPosition())).toList();
        return new ch.zhaw.pm2.racetrack.algorithm.pathfinder.Graph(new java.util.ArrayList<>(nodes.values()), endNodes);
    }

    /**
     * Generates all possible connections from the next node in {@link #nodeQueue}
     */
    private void nextIteration() {
        ch.zhaw.pm2.racetrack.algorithm.pathfinder.graphgenerator.BruteforceGenerator.GraphVector currentVector = nodeQueue.poll();
        ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphNode currentNode = nodes.get(currentVector);
        nodeVisited.add(currentVector);
        for (ch.zhaw.pm2.racetrack.PositionVector.Direction direction : ch.zhaw.pm2.racetrack.PositionVector.Direction.values()) {
            ch.zhaw.pm2.racetrack.PositionVector nextVelocity = ch.zhaw.pm2.racetrack.PositionVector.add(currentVector.velocity(), direction.vector);
            ch.zhaw.pm2.racetrack.PositionVector nextPos = ch.zhaw.pm2.racetrack.PositionVector.add(currentVector.pos(), nextVelocity);
            ch.zhaw.pm2.racetrack.given.ConfigSpecification.SpaceType spaceType = track.getSpaceType(nextPos);
            // if the currentPos and nextPos is the same
            if (nextPos.equals(currentVector.pos()))
                continue;

            // do not allow the velocity to exceed MAX_VELOCITY
            if (getAbsoluteVelocity(nextVelocity) > MAX_VELOCITY)
                continue;

            // do not crash in to wall
            if (isCrossingWall(currentVector.pos(), nextPos))
                continue;

            // only allow a connection when driving over the finishing line the right way
            if (isCrossingFinishLineInWrongDirection(spaceType, nextVelocity))
                continue;

            ch.zhaw.pm2.racetrack.algorithm.pathfinder.graphgenerator.BruteforceGenerator.GraphVector nextVector = new ch.zhaw.pm2.racetrack.algorithm.pathfinder.graphgenerator.BruteforceGenerator.GraphVector(nextPos, nextVelocity);
            ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphNode nextNode = getOrCreate(nextVector);
            currentNode.connectNode(nextNode, 1, direction);
            if ((!nodeVisited.contains(nextVector)) && (!nodeQueue.contains(nextVector))) {
                nodeQueue.add(nextVector);
            }
        }
    }

    /**
     * Checks if there is a wall between the given points
     *
     * @param last
     * 		the last position
     * @param next
     * 		the next position
     * @return if there id a wall between
     */
    private boolean isCrossingWall(ch.zhaw.pm2.racetrack.PositionVector last, ch.zhaw.pm2.racetrack.PositionVector next) {
        return new ch.zhaw.pm2.racetrack.algorithm.bresenham.BresenhamPathCalculator(last, next).calulatePath().hasSpaceType(SpaceType.WALL, track);
    }

    /**
     * Checks whether the given space type is driven over in the wrong direction
     *
     * @param spaceType
     * 		the current space type
     * @param velocity
     * 		the current velocity the car would have driving over the
     * 		given space type
     * @return if the space is driven over in the wrong direction
     */
    private boolean isCrossingFinishLineInWrongDirection(ch.zhaw.pm2.racetrack.given.ConfigSpecification.SpaceType spaceType, ch.zhaw.pm2.racetrack.PositionVector velocity) {
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
     * @param vector
     * 		the vector
     * @return the absolute value of the vector
     */
    private float getAbsoluteVelocity(ch.zhaw.pm2.racetrack.PositionVector vector) {
        return ((float) (java.lang.Math.sqrt((vector.getX() * vector.getX()) + (vector.getY() * vector.getY()))));
    }

    /**
     * Gets a {@link GraphNode} for the given vector. If the node doesn't exist, a
     * new node is created.
     *
     * @param vector
     * 		the position and velocity
     * @return the node
     */
    public ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphNode getOrCreate(ch.zhaw.pm2.racetrack.algorithm.pathfinder.graphgenerator.BruteforceGenerator.GraphVector vector) {
        if (!nodes.containsKey(vector)) {
            ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphNode node = new ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphNode(vector.pos(), vector.velocity());
            nodes.put(vector, node);
        }
        return nodes.get(vector);
    }

    /**
     * A "position" on the graph. A position consists of a position on the map and
     * the velocity the car has at this position
     */
    private record GraphVector(ch.zhaw.pm2.racetrack.PositionVector pos, ch.zhaw.pm2.racetrack.PositionVector velocity) {}
}