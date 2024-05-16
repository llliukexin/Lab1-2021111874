import java.io.*;
import java.util.*;
public class Graph {
    private Map<String, Map<String, Integer>> directedGraph;

    /**
     * 构造函数，初始化有向图。
     */
    public Graph() {
        directedGraph = new HashMap<>();
    }

    /**
     * 添加节点到图中。
     *
     * @param node 节点名称
     */
    public void addNode(String node) {
        directedGraph.putIfAbsent(node, new HashMap<>());
    }

    /**
     * 添加边到图中。
     *
     * @param source      边的起始节点
     * @param destination 边的目标节点
     */
    public void addEdge(String source, String destination) {
        directedGraph.get(source).merge(destination, 1, Integer::sum);
    }

    /**
     * 从文本文件构建有向图。
     *
     * @param filePath 文本文件路径
     * @throws IOException 如果读取文件出错则抛出 IOException
     */
    public void buildDirectedGraph(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append(" ");
            }
        }
        String processedContent = content.toString().replaceAll("[^a-zA-Z\\n\\r]", " ").toLowerCase();
        String[] words = processedContent.split("\\s+");
        for (int i = 0; i < words.length - 1; i++) {
            String currentWord = words[i];
            String nextWord = words[i + 1];
            addNode(currentWord);
            addNode(nextWord);
            addEdge(currentWord, nextWord);
        }
    }

    /**
     * 获取有向图。
     *
     * @return 有向图的映射关系
     */
    public Map<String, Map<String, Integer>> getDirectedGraph() {
        return directedGraph;
    }
}