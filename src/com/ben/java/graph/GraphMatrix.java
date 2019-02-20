package com.ben.java.graph;

import java.util.LinkedList;

/**
 * @author zhangchuan622@gmail.com
 * @version 1.0
 * @create 2019/2/18
 * @desc 图的邻接矩阵存储方式
 */
public class GraphMatrix {
    //顶点数量
    private int vertexSize;
    //顶点集合
    private int vertexs[];
    //矩阵
    public int vertexMatrix[][];

    private boolean[] visited;
    /**
     * 最大权值边界
     */
    public static final int MAX_WEIGHT = 99999;

    public int getVertexSize() {
        return vertexSize;
    }

    public int[][] getVertexMatrix() {
        return vertexMatrix;
    }

    public GraphMatrix(int vertexSize) {
        this.vertexSize = vertexSize;
        //init matrix
        visited = new boolean[vertexSize];
        vertexs = new int[vertexSize];
        vertexMatrix = new int[vertexSize][vertexSize];
        for (int i = 0; i < vertexSize; i++) {
            vertexs[i] = i;
        }
    }

    /**
     * 获取顶点的出度
     *
     * @param vertex
     * @return
     */
    public int getOutDegree(int vertex) {
        if (vertex >= vertexSize) {
            throw new IndexOutOfBoundsException();
        }
        int degree = 0;
        for (int i = 0; i < vertexMatrix[vertex].length; i++) {
            if (vertexMatrix[vertex][i] > 0 && vertexMatrix[vertex][i] < MAX_WEIGHT) {
                degree++;
            }
        }
        return degree;
    }

    /**
     * 获取顶点的入度
     *
     * @param vertex
     * @return
     */
    public int getEnterDegree(int vertex) {
        if (vertex >= vertexSize) {
            throw new IndexOutOfBoundsException();
        }
        int degree = 0;
        for (int i = 0; i < vertexSize; i++) {
            if (vertexMatrix[i][vertex] > 0 && vertexMatrix[i][vertex] < MAX_WEIGHT) {
                degree++;
            }
        }
        return degree;
    }

    /**
     * 获取两个顶点之间的权值
     *
     * @param v1
     * @param v2
     * @return
     */
    public int getVertexWeight(int v1, int v2) {
        if (v1 >= vertexSize || v2 >= vertexSize) {
            throw new IndexOutOfBoundsException();
        }
        return vertexMatrix[v1][v2] <= 0 ? 0 : (vertexMatrix[v1][v2] >= MAX_WEIGHT ? -1 : vertexMatrix[v1][v2]);
    }

    /**
     * 获取指定顶点的相邻顶点
     *
     * @param vertex
     * @return
     */
    private int getVertexNeighbor(int vertex) {
        for (int i = 0; i < vertexSize; i++) {
            if (vertexMatrix[vertex][i] > 0 && vertexMatrix[vertex][i] < MAX_WEIGHT) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 获取指定顶点v1相对于v2之后的第一个顶点
     *
     * @param v1
     * @param v2
     * @return
     */
    private int getNextVertex(int v1, int v2) {
        //从V2后开始遍历，不包含v2
        for (int i = v2 + 1; i < vertexSize; i++) {
            if (vertexMatrix[v1][i] > 0 && vertexMatrix[v1][i] < MAX_WEIGHT) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 深度优先遍历<br>
     * 依次优先遍历顶点A的第一个邻接点，并且已遍历的点不可被重复遍历，当邻接点遍历完成遍历顶点A的其他节点。<br>
     * 已被遍历过的顶点不参与后序遍历
     *
     * @param startVertex
     */
    private void depthFirstSearch(int startVertex) {
        //标记当前顶点已被遍历
        visited[startVertex] = true;
        int vertex = getVertexNeighbor(startVertex);
        while (vertex != -1) {
            if (!visited[vertex]) {
                //该顶点未被遍历过
                System.out.println("遍历顶点：" + vertex);
                depthFirstSearch(vertex);
            }
            //遍历当前顶点的同级顶点
            vertex = getNextVertex(startVertex, vertex);
        }

    }

    /**
     * 对外提供遍历接口，防止部分节点存在遍历不到的情况
     */
    public void depthFirstSearch() {
        visited = new boolean[vertexSize];
        for (int i = 0; i < vertexSize; i++) {
            if (!visited[i]) {
                System.out.println("遍历顶点：" + i);
                depthFirstSearch(i);
            }
        }
    }

    /**
     * 广度优先遍历
     */
    private void broadFirstSearch(int startVertex) {
        visited[startVertex] = true;
        LinkedList<Integer> queue = new LinkedList<>();
        //将顶点加入队列中
        queue.addFirst(startVertex);
        //遍历队列
        while (!queue.isEmpty()) {
            //出队
            int a = queue.removeFirst();
            //首先遍历顶点A
            int vertex = getVertexNeighbor(a);
            while (vertex != -1) {
                //判断是否遍历
                if (!visited[vertex]) {
                    System.out.println("遍历顶点：" + vertex);
                    visited[vertex] = true;
                    //放入队列，队列遍历时进入下一层次
                    queue.addFirst(vertex);
                }
                vertex = getNextVertex(a, vertex);
            }

        }
    }

    public void broadFirstSearch() {
        visited = new boolean[vertexSize];
        for (int i = 0; i < vertexSize; i++) {
            if (!visited[i]) {
                System.out.println("遍历顶点：" + i);
                broadFirstSearch(i);
            }
        }
    }

    /**
     * 使用普利姆算法实现图的最小生成树<br>
     * [0,10,#,#,#,11,#,#,#]<br>
     * 查找最小位索引位置<br>
     * .设 i = 1(10)<br>
     * .则 数列s为<br>
     * [10,0,18,#,#,#,16,#,12]<br>
     * 替换后<br>
     */
    public void prim() {
        //最小权值集合
        int[] lowcost = new int[vertexSize];
        int[] weights = new int[vertexSize];
        int min, minId, weightSum = 0;

        //设置最小权值集合
        for (int i = 0; i < vertexSize; i++) {
            lowcost[i] = vertexMatrix[0][i];
        }

        //查找最小权值集合中最小索引位
        for (int j = 1; j < vertexSize; j++) {
            min = MAX_WEIGHT;
            minId = 0;
            for (int i = 1; i < vertexSize; i++) {
                if (lowcost[i] < min && lowcost[i] > 0) {
                    min = lowcost[i];
                    minId = i;
                }
            }
            //根据索引位置与最小权值中的数据比较
            //重置索引位置边
            lowcost[minId] = 0;
            weightSum += min;
            for (int i = 1; i < vertexSize; i++) {
                if (vertexMatrix[minId][i] < lowcost[i] && lowcost[i] > 0) {
                    lowcost[i] = vertexMatrix[minId][i];
                    weights[i] = minId;
                }
            }
        }

        System.out.println("最小生成树权值和：" + weightSum);
    }


}
