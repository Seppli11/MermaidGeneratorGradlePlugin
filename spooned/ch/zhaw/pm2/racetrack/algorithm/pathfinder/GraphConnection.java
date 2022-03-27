package ch.zhaw.pm2.racetrack.algorithm.pathfinder;
import ch.zhaw.pm2.racetrack.PositionVector.Direction;
/**
 * A connection between two nodes. The connection is uni-directional from the
 * {@link #previous} to the {@link #next} node
 *
 * @param previous
 * 		the previous node
 * @param time
 * 		the time it takes to travel from the {@link #previous} node
 * 		to the {@link #next} node
 * @param next
 * 		the next node
 * @param direction
 * 		the direction the car needs to accelerate into to get to the
 * 		next direction
 */
public record GraphConnection(ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphNode previous, int time, ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphNode next, ch.zhaw.pm2.racetrack.PositionVector.Direction direction) {}