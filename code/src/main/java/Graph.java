import java.io.*;
import java.util.*;
public class Graph {
    private Map<String, Map<String, Integer>> directedGraph;

    /**
     * ���캯������ʼ������ͼ��
     */
    public Graph() {
        directedGraph = new HashMap<>();
    }

    /**
     * ��ӽڵ㵽ͼ�С�
     *
     * @param node �ڵ�����
     */
    public void addNode(String node) {
        directedGraph.putIfAbsent(node, new HashMap<>());
    }

    /**
     * ��ӱߵ�ͼ�С�
     *
     * @param source      �ߵ���ʼ�ڵ�
     * @param destination �ߵ�Ŀ��ڵ�
     */
    public void addEdge(String source, String destination) {
        directedGraph.get(source).merge(destination, 1, Integer::sum);
    }

    /**
     * ���ı��ļ���������ͼ��
     *
     * @param filePath �ı��ļ�·��
     * @throws IOException �����ȡ�ļ��������׳� IOException
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
     * ��ȡ����ͼ��
     *
     * @return ����ͼ��ӳ���ϵ
     */
    public Map<String, Map<String, Integer>> getDirectedGraph() {
        return directedGraph;
    }
}