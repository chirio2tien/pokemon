package com.example.pokemomproj;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

class Node implements Comparable<Node> {
    public int x, y;
    public int gCost, hCost, fCost;
    public Node parent;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.gCost = Integer.MAX_VALUE; // Initialize gCost to a high value
    }

    public void calculateCosts(Node endNode) {
        this.hCost = Math.abs(x - endNode.x) + Math.abs(y - endNode.y);
        this.fCost = gCost + hCost;
    }

    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.fCost, other.fCost);
    }
}

public class AStarPathfinding {
    private static final int[][] DIRECTIONS = {
            {0, 1}, {1, 0}, {0, -1}, {-1, 0},
            {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
    };

    public List<Node> findPath(int[][] grid, Node startNode, Node endNode, Node playerNode) {
        PriorityQueue<Node> openList = new PriorityQueue<>();
        Set<Node> closedList = new HashSet<>();

        startNode.gCost = 0;
        startNode.calculateCosts(endNode);
        openList.add(startNode);

        while (!openList.isEmpty()) {
            Node currentNode = openList.poll();
            if (currentNode.equals(endNode)) {
                return reconstructPath(currentNode);
            }

            closedList.add(currentNode);

            for (int[] direction : DIRECTIONS) {
                int newX = currentNode.x + direction[0];
                int newY = currentNode.y + direction[1];

                if (isValid(grid, newX, newY) && !isInList(closedList, newX, newY) && !isNearPlayer(newX, newY, playerNode)) {
                    Node neighbor = new Node(newX, newY);
                    int tentativeGCost = currentNode.gCost + 1;

                    if (tentativeGCost < neighbor.gCost || !isInList(openList, newX, newY)) {
                        neighbor.gCost = tentativeGCost;
                        neighbor.calculateCosts(endNode);
                        neighbor.parent = currentNode;

                        if (!isInList(openList, newX, newY)) {
                            openList.add(neighbor);
                        }
                    }
                }
            }
        }

        return Collections.emptyList();
    }

    private boolean isNearPlayer(int x, int y, Node playerNode) {
        int safeDistance = 2; // Example safe distance
        return Math.abs(x - playerNode.x) <= safeDistance && Math.abs(y - playerNode.y) <= safeDistance;
    }

    private boolean isValid(int[][] grid, int x, int y) {
        return x >= 0 && y >= 0 && x < grid.length && y < grid[0].length && grid[x][y] == 0;
    }

    private boolean isInList(Collection<Node> list, int x, int y) {
        return list.stream().anyMatch(node -> node.x == x && node.y == y);
    }

    private List<Node> reconstructPath(Node endNode) {
        List<Node> path = new ArrayList<>();
        Node currentNode = endNode;
        while (currentNode != null) {
            currentNode = currentNode.parent;
        }
        Collections.reverse(path);
        return path;
    }
}
