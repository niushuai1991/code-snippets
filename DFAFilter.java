import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 敏感词过滤器，基于DFA算法实现
 * </p>
 * 
 * @author niushuai
 * @since 0.0.1
 */
public class DFAFilter implements Serializable {
    private static final long serialVersionUID = -8436658134223833551L;

    private Node root = new Node();
    private int matchType = 1; // 1:最小长度匹配 2：最大长度匹配

    /**
     * <p>
     * 添加敏感词
     * </p>
     *
     * @action niushuai 2017年7月8日 下午2:07:31 描述
     *
     * @param keywords
     *            void
     */
    public void addKeywords(List<String> keywords) {
        for (String key : keywords) {
            Node node = root;
            for (int j = 0; j < key.length(); j++) {
                char word = key.charAt(j);
                Node child = node.getMap().get(word);
                if (child != null) {
                    node = child;
                } else {
                    child = new Node();
                    child.setEnd(false);
                    node.getMap().put(word, child);
                    node = child;
                }
                if (j == key.length() - 1) {
                    node.setEnd(true);
                }
            }
        }
    }

    /**
     * 重置关键词
     */
    public void clearKeywords() {
        root = new Node();
    }

    /**
     * 检查一个字符串从begin位置起开始是否有keyword符合， 如果有符合的keyword值，返回值为匹配keyword的长度，否则返回零
     * flag 1:最小长度匹配 2：最大长度匹配
     */
    private int checkKeyWords(String txt, int begin, int flag) {
        Node node = root;
        int maxMatchRes = 0;
        int res = 0;
        int l = txt.length();
        char word = 0;
        for (int i = begin; i < l; i++) {
            word = txt.charAt(i);
            Node child = node.getMap().get(word);
            if (child != null) {
                res++;
                node = child;
                if (node.isEnd()) {
                    if (flag == 1) {
                        return res;
                    } else {
                        maxMatchRes = res;
                    }
                }
            } else {
                return maxMatchRes;
            }
        }
        return maxMatchRes;
    }

    /**
     * 返回txt中关键字的列表
     */
    public Set<String> getTxtKeyWords(String txt) {
        Set<String> set = new HashSet<>();
        int l = txt.length();
        for (int i = 0; i < l;) {
            int len = checkKeyWords(txt, i, matchType);
            if (len > 0) {
                set.add(txt.substring(i, i + len));
                i += len;
            } else {
                i++;
            }
        }
        return set;
    }

    /**
     * 仅判断txt中是否有关键字
     */
    public boolean isContentKeyWords(String txt) {
        for (int i = 0; i < txt.length(); i++) {
            int len = checkKeyWords(txt, i, 1);
            if (len > 0) {
                return true;
            }
        }
        return false;
    }
}

/**
 * <p>
 * 敏感词节点
 * </p>
 * 
 * @author niushuai
 * @since 0.0.1
 */
class Node implements Serializable {
    private static final long serialVersionUID = 8410521076094028044L;
    private boolean isEnd = true;
    private Map<Character, Node> map = new HashMap<>();

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }

    public Map<Character, Node> getMap() {
        return map;
    }

    public void setMap(Map<Character, Node> map) {
        this.map = map;
    }

}