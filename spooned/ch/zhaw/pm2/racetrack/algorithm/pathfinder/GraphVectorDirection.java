package ch.zhaw.pm2.racetrack.algorithm.pathfinder;
import ch.zhaw.pm2.racetrack.PositionVector;
import ch.zhaw.pm2.racetrack.PositionVector.Direction;
/**
 * A graph vector which contains a {@link PositionVector} and the
 * {@link ch.zhaw.pm2.racetrack.PositionVector.Direction}
 */
public record GraphVectorDirection(ch.zhaw.pm2.racetrack.PositionVector pos, ch.zhaw.pm2.racetrack.PositionVector.Direction direction) {}