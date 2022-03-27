package ch.zhaw.pm2.racetrack.algorithm.pathfinder;
/**
 * A graph created by a
 * {@link ch.zhaw.pm2.racetrack.algorithm.pathfinder.graphgenerator.GraphGenerator}
 *
 * @param nodes
 * 		the nodes of the graph
 * @param endNodes
 * 		the end nodes
 */
public record Graph(java.util.List<ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphNode> nodes, java.util.List<ch.zhaw.pm2.racetrack.algorithm.pathfinder.GraphNode> endNodes) {}