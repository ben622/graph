package com.ben.java.graph;

import org.omg.PortableInterceptor.INACTIVE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author zhangchuan622@gmail.com
 * @version 1.0
 * @create 2019/2/20
 * @desdc 迪杰斯特拉算法实现图的最短路径
 */
public class GraphDijkstraShortPath {
    private boolean[] visited;

    /**
     * 使用迪杰斯特拉算法查找图中的最短路径
     *
     * @param graphMatrix
     */
    public void shortestPath(GraphMatrix graphMatrix) {
        if (graphMatrix == null) {
            throw new NullPointerException();
        }
        visited = new boolean[graphMatrix.getVertexSize()];
        //最短路径集合
        int[] shortestPath = new int[graphMatrix.getVertexSize()];
        //标注当前顶点位置
        int min = GraphMatrix.MAX_WEIGHT;
        int minId = 0;
        //初始化最短路径集合
        for (int i = 0; i < graphMatrix.getVertexSize(); i++) {
            shortestPath[i] = graphMatrix.getVertexMatrix()[0][i];
        }
        //设起点为第0个顶点
        shortestPath[0] = 0;
        //遍历矩阵中各个顶点
        for (int i = 0; i < graphMatrix.getVertexSize(); i++) {
            min = GraphMatrix.MAX_WEIGHT;
            //在最短路径集合中查找最小顶点边,且已遍历过的点不可被重复遍历
            for (int j = 1; j < graphMatrix.getVertexSize(); j++) {
                if (shortestPath[j] < min && !visited[j]) {
                    min = shortestPath[j];
                    minId = j;
                }
            }
            //标记顶点为minId的点已被遍历
            visited[minId] = true;
            //在顶点为minId的点中进行比较，如果最短路径集合中的Vx+Vy>Vx 则替换最短路径中的Vx为Vy
            for (int j = 0; j < graphMatrix.getVertexSize(); j++) {
                if (!visited[j] && (min + graphMatrix.getVertexMatrix()[minId][j]) < shortestPath[j]) {
                    //replace
                    shortestPath[j] = min + graphMatrix.getVertexMatrix()[minId][j];
                }
            }


        }

        for (int i = 0; i < shortestPath.length; i++) {
            System.out.println("顶点V0 --> V" + i + "最短路径为：" + shortestPath[i]);
        }

    }

    /**
     * 使用迪杰斯特拉算法查找图中的最短路径并记录路径点
     *
     * @param graphMatrix
     */
    public void shortestPath2(GraphMatrix graphMatrix) {
        if (graphMatrix == null) {
            throw new NullPointerException();
        }
        visited = new boolean[graphMatrix.getVertexSize()];
        //最短路径集合
        Path[] shortestPath = new Path[graphMatrix.getVertexSize()];
        //路径顶点集合
        List<Integer> vertexs = new ArrayList<>();
        //标注当前顶点位置
        int min = GraphMatrix.MAX_WEIGHT;
        int minId = 0;
        //初始化最短路径集合
        for (int i = 0; i < graphMatrix.getVertexSize(); i++) {
            Path path = new Path();
            path.setWeight(graphMatrix.getVertexMatrix()[0][i]);
            path.setStart(0);
            path.setEnd(i);
            shortestPath[i] = path;
        }

        //遍历矩阵中各个顶点
        for (int i = 0; i < graphMatrix.getVertexSize(); i++) {
            min = GraphMatrix.MAX_WEIGHT;
            //在最短路径集合中查找最小顶点边,且已遍历过的点不可被重复遍历
            for (int j = 1; j < graphMatrix.getVertexSize(); j++) {
                if (shortestPath[j].getWeight() < min && !visited[j]) {
                    min = shortestPath[j].getWeight();
                    minId = j;
                }
            }
            //标记顶点为minId的点已被遍历
            visited[minId] = true;
            vertexs.add(minId);
            //在顶点为minId的点中进行比较，如果最短路径集合中的Vx+Vy>Vx 则替换最短路径中的Vx为Vy
            for (int j = 0; j < graphMatrix.getVertexSize(); j++) {
                if (!visited[j] && (min + graphMatrix.getVertexMatrix()[minId][j]) < shortestPath[j].getWeight()) {
                    //replace
                    shortestPath[j].setWeight(min + graphMatrix.getVertexMatrix()[minId][j]);
                    shortestPath[j].getPaths().clear();
                    shortestPath[j].getPaths().addAll(vertexs);
                    //添加起点和终点
                    shortestPath[j].getPaths().addFirst(shortestPath[j].getStart());
                    shortestPath[j].getPaths().addLast(shortestPath[j].getEnd());
                }
            }


        }

        for (int i = 0; i < shortestPath.length; i++) {
            System.out.println("起始顶点" + shortestPath[i].getStart() + " --> V" + i + "最短路径权值为：" + shortestPath[i].getWeight() + "  路径为：" + Arrays.toString(shortestPath[i].getPaths().toArray()));
        }
    }

    class Path {
        int start;
        int end;
        int weight;
        LinkedList<Integer> paths = new LinkedList<>();

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public int getStart() {
            return start;
        }

        public void setStart(int start) {
            this.start = start;
        }

        public int getEnd() {
            return end;
        }

        public void setEnd(int end) {
            this.end = end;
        }

        public LinkedList<Integer> getPaths() {
            return paths;
        }

        public void setPaths(LinkedList<Integer> paths) {
            this.paths = paths;
        }
    }


}
