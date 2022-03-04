package FlinkSql;

import java.util.ArrayList;

// 链表
public class LinkRealize {
    public static void main(String[] args) {
        ListNode node1 = new ListNode(12, null);
        ListNode node2 = new ListNode(97, null);
        ListNode node3 = new ListNode(34, null);
        node1.next = node2;
        node2.next = node3;

        ListNode head = node1;
        while (head != null) {
            System.out.println(head.val);
            head = head.next;
        }

        //       5
        //      / \
        //     3   6
        //    / \
        //   1   4
        TreeNode root = new TreeNode(5);
        root.left = new TreeNode(3);
        root.right = new TreeNode(6);
        root.left.left = new TreeNode(1);
        root.left.right = new TreeNode(4);

        preOrderTraversal(root);
        inOrderTraversal(root);

        System.out.println(treeSearch(root, 1));



        GraphNode nodeA = new GraphNode(1);
        GraphNode nodeB = new GraphNode(2);
        GraphNode nodeC = new GraphNode(3);

        nodeA.nabour.add(nodeB);
        nodeB.nabour.add(nodeC);
        nodeC.nabour.add(nodeA);
    }

    public static Boolean treeSearch(TreeNode root, int val) {
        if (root.val == val) {
            return true;
        } else if (root.val < val) {
            return treeSearch(root.right, val);
        } else if (root.val > val){
            return treeSearch(root.left, val);
        }
        return false;
    }

    // 先序遍历
    // 1. 遍历根节点；2. 对左子树进行先序遍历；3. 对右子树进行先序遍历
    public static void preOrderTraversal(TreeNode root) {
        if (root != null) {
            System.out.println(root.val);
            preOrderTraversal(root.left);
            preOrderTraversal(root.right);
        }
    }

    public static void inOrderTraversal(TreeNode root) {
        if (root != null) {
            inOrderTraversal(root.left);
            System.out.println(root.val);
            inOrderTraversal(root.right);
        }
    }

    public static void postOrderTraversal(TreeNode root) {
        if (root != null) {
            postOrderTraversal(root.left);
            postOrderTraversal(root.right);
            System.out.println(root.val);
        }
    }

    public static class ListNode {
        public int val;
        public ListNode next;

        public ListNode() {

        }

        public ListNode(int val) {
            this.val = val;
        }

        public ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
    }

    public static class TreeNode {
        public int val;
        public TreeNode left;
        public TreeNode right;

        public TreeNode() {

        }

        public TreeNode(int val) {
            this.val = val;
        }

        public TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    public static class GraphNode {
        public int val;
        public ArrayList<GraphNode> nabour = new ArrayList<>();

        public GraphNode() {

        }

        public GraphNode(int val) {
            this.val = val;
        }

        public GraphNode(int val, ArrayList<GraphNode> arrayList) {
            this.val = val;
            this.nabour = arrayList;
        }
    }
}
