package ch.zhaw.pm2.racetrack.algorithm.pathfinder;
import ch.zhaw.pm2.racetrack.PositionVector;
import ch.zhaw.pm2.racetrack.PositionVector.Direction;
import ch.zhaw.pm2.racetrack.algorithm.bresenham.BresenhamPathCalculator;
import ch.zhaw.pm2.racetrack.algorithm.bresenham.PositionPath;
/**
 * A list of {@link GraphVectorDirection} which form a path on a graph
 */
public record GraphPath(java.util.List<ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphVectorDirection> vectors) {
    /**
     *
     * @return transforms this {@link GraphPath} to a {@link PositionPath}. The
    waypoints in {@link #vectors} are connected with a direct line (using
    {@link BresenhamPathCalculator})) and put in a {@link PositionPath};
     */
    public ch.zhaw.pm2.racetrack.algorithm.bresenham.PositionPath toPositionPath() {
        java.util.List<ch.zhaw.pm2.racetrack.PositionVector> wayPoints = vectors.stream().map(ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphVectorDirection::pos).toList();
        return new ch.zhaw.pm2.racetrack.algorithm.bresenham.PositionPath(generatePointsBetweenWaypoints(wayPoints));
    }

    /**
     *
     * @return the direction used to get from one way point to the other. The start
    velocity might not be
    {@link ch.zhaw.pm2.racetrack.PositionVector.Direction#NONE}.
     */
    public java.util.List<ch.zhaw.pm2.racetrack.PositionVector.Direction> toDirectionList() {
        return vectors.stream().map(ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphVectorDirection::direction).toList();
    }

    /**
     * Generates a list of {@link PositionVector} between the given waypoints
     *
     * @param waypoints
     * 		the list of waypoints
     * @return the list of {@link PositionVector} between the given waypoints
     */
    private java.util.List<ch.zhaw.pm2.racetrack.PositionVector> generatePointsBetweenWaypoints(java.util.List<ch.zhaw.pm2.racetrack.PositionVector> waypoints) {
        java.util.Set<ch.zhaw.pm2.racetrack.PositionVector> points = new java.util.LinkedHashSet<>();
        java.util.Iterator<ch.zhaw.pm2.racetrack.PositionVector> iterator = waypoints.iterator();
        ch.zhaw.pm2.racetrack.PositionVector lastPos = iterator.next();
        while (iterator.hasNext()) {
            ch.zhaw.pm2.racetrack.PositionVector currentPos = iterator.next();
            ch.zhaw.pm2.racetrack.algorithm.bresenham.PositionPath path = new ch.zhaw.pm2.racetrack.algorithm.bresenham.BresenhamPathCalculator(lastPos, currentPos).calulatePath();
            points.addAll(path.points());
        } 
        return new java.util.ArrayList<>(points);
    }
}