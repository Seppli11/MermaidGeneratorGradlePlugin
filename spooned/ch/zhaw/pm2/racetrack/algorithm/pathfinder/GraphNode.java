package ch.zhaw.pm2.racetrack.algorithm.pathfinder;
import ch.zhaw.pm2.racetrack.PositionVector;
import ch.zhaw.pm2.racetrack.PositionVector.Direction;
/**
 * A node in a {@link Graph}
 */
public class GraphNode {
    /**
     * the connection to the previous graph node. This is set by the
     * {@link #setPrevious(GraphConnection)}
     */
    private ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphConnection previous;

    /**
     * the weight of this node. By default the {@link Integer#MAX_VALUE}
     */
    private int time = java.lang.Integer.MAX_VALUE;

    /**
     * the position of this velocity
     */
    private ch.zhaw.pm2.racetrack.PositionVector position;

    /**
     * the velocity of this node
     */
    private ch.zhaw.pm2.racetrack.PositionVector velocity;

    /**
     * All out going connections
     */
    private java.util.List<ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphConnection> connections = new java.util.LinkedList<>();

    /**
     * Constructor
     *
     * @param position
     * 		the position
     * @param velocity
     * 		the velocity
     */
    public GraphNode(ch.zhaw.pm2.racetrack.PositionVector position, ch.zhaw.pm2.racetrack.PositionVector velocity) {
        this.position = position;
        this.velocity = velocity;
    }

    /**
     *
     * @return the list of out going connections
     */
    public java.util.List<ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphConnection> getConnections() {
        return connections;
    }

    /**
     * Connects the given node to this node. The connection goes from this node to
     * the given node and is not bidirectional. The connection stores the given time
     * and direction. The other Node isn't told about this connection.
     *
     * @param node
     * 		the node to which the connection is build
     * @param time
     * 		the time
     * @param direction
     * 		the direction
     */
    public void connectNode(ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphNode node, int time, ch.zhaw.pm2.racetrack.PositionVector.Direction direction) {
        ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphConnection connection = new ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphConnection(this, time, node, direction);
        if (connections.contains(connection))
            return;

        this.connections.add(connection);
    }

    /**
     *
     * @return the previous connection
     */
    public ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphConnection getPrevious() {
        return previous;
    }

    /**
     *
     * @param previous
     * 		the previous to set
     */
    public void setPrevious(ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphConnection previous) {
        this.previous = previous;
    }

    /**
     *
     * @return the previous node
     */
    public ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphNode getPreviousNode() {
        if (previous == null)
            return null;

        return previous.previous();
    }

    /**
     *
     * @return the weight
     */
    public int getTime() {
        return time;
    }

    /**
     *
     * @param time
     * 		the weight to set
     */
    public void setTime(int time) {
        this.time = time;
    }

    /**
     *
     * @return the position
     */
    public ch.zhaw.pm2.racetrack.PositionVector getPosition() {
        return position;
    }

    /**
     *
     * @return the velocity a car has when it is at this node
     */
    public ch.zhaw.pm2.racetrack.PositionVector getVelocity() {
        return velocity;
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(position, time, velocity);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphNode)) {
            return false;
        }
        ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphNode other = ((ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphNode) (obj));
        return (java.util.Objects.equals(position, other.position) && (time == other.time)) && java.util.Objects.equals(velocity, other.velocity);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "GraphNode [%s/%s - %s]".formatted(position, velocity, getPreviousNode());
    }
}