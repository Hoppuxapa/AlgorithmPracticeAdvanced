class Node {
    int data;
    Node pointToParent;
    Node pointToLeftChild;
    Node pointToRightChild;
    int color; // 1-Red, 0-Black
    RedBlackTree redBlackTree;



}

public class RedBlackTree {

    private int i = 100;
    private Node root;
    private Node leaf;

    public RedBlackTree() {
        leaf = new Node();
        leaf.color = 0;
        leaf.pointToLeftChild = null;
        leaf.pointToRightChild = null;
        root = leaf;
    }

    private void preOrderHelper(Node node) {
        if (node != leaf) {
            System.out.print(node.data + " ");
            preOrderHelper(node.pointToLeftChild);
            preOrderHelper(node.pointToRightChild);
        }
    }

    private void inOrderHelper(Node node) {
        if (node != leaf) {
            inOrderHelper(node.pointToLeftChild);
            System.out.print(node.data + " ");
            inOrderHelper(node.pointToRightChild);
        }
    }

    private void postOrderHelper(Node node) {
        if (node != leaf) {
            postOrderHelper(node.pointToLeftChild);
            postOrderHelper(node.pointToRightChild);
            System.out.print(node.data + " ");
        }
    }

    private Node searchTreeHelper(Node node, int key) {
        if (node == leaf || key == node.data) {
            return node;
        }

        if (key < node.data) {
            return searchTreeHelper(node.pointToLeftChild, key);
        }
        return searchTreeHelper(node.pointToRightChild, key);
    }

    // fix the rb tree modified by the delete operation

    private void fixDelete(Node x) {
        Node s;
        while (x != root && x.color == 0) {
            if (x == x.pointToParent.pointToLeftChild) {
                s = x.pointToParent.pointToRightChild;
                if (s.color == 1) {
                    // case 3.1
                    s.color = 0;
                    x.pointToParent.color = 1;
                    leftRotate(x.pointToParent);
                    s = x.pointToParent.pointToRightChild;
                }

                if (s.pointToLeftChild.color == 0 && s.pointToRightChild.color == 0) {
                    // case 3.2
                    s.color = 1;
                    x = x.pointToParent;
                } else {
                    if (s.pointToRightChild.color == 0) {
                        // case 3.3
                        s.pointToLeftChild.color = 0;
                        s.color = 1;
                        rightRotate(s);
                        s = x.pointToParent.pointToRightChild;
                    }

                    // case 3.4
                    s.color = x.pointToParent.color;
                    x.pointToParent.color = 0;
                    s.pointToRightChild.color = 0;
                    leftRotate(x.pointToParent);
                    x = root;
                }
            } else {
                s = x.pointToParent.pointToLeftChild;
                if (s.color == 1) {
                    // case 3.1
                    s.color = 0;
                    x.pointToParent.color = 1;
                    rightRotate(x.pointToParent);
                    s = x.pointToParent.pointToLeftChild;
                }

                if (s.pointToRightChild.color == 0 && s.pointToRightChild.color == 0) {
                    // case 3.2
                    s.color = 1;
                    x = x.pointToParent;
                } else {
                    if (s.pointToLeftChild.color == 0) {
                        // case 3.3
                        s.pointToRightChild.color = 0;
                        s.color = 1;
                        leftRotate(s);
                        s = x.pointToParent.pointToLeftChild;
                    }

                    // case 3.4
                    s.color = x.pointToParent.color;
                    x.pointToParent.color = 0;
                    s.pointToLeftChild.color = 0;
                    rightRotate(x.pointToParent);
                    x = root;
                }
            }
        }
        x.color = 0;
    }


    private void rbTransplant(Node u, Node v) {
        if (u.pointToParent == null) {
            root = v;
        } else if (u == u.pointToParent.pointToLeftChild) {
            u.pointToParent.pointToLeftChild = v;
        } else {
            u.pointToParent.pointToRightChild = v;
        }
        v.pointToParent = u.pointToParent;
    }

    private void deleteNodeHelper(Node node, int key) {
        // find the node containing key
        Node z = leaf;
        Node x, y;
        while (node != leaf) {
            if (node.data == key) {
                z = node;
            }

            if (node.data <= key) {
                node = node.pointToRightChild;
            } else {
                node = node.pointToLeftChild;
            }
        }

        if (z == leaf) {
            System.out.println("Couldn't find key in the tree");
            return;
        }

        y = z;
        int yOriginalColor = y.color;
        if (z.pointToLeftChild == leaf) {
            x = z.pointToRightChild;
            rbTransplant(z, z.pointToRightChild);
        } else if (z.pointToRightChild == leaf) {
            x = z.pointToLeftChild;
            rbTransplant(z, z.pointToLeftChild);
        } else {
            y = minimum(z.pointToRightChild);
            yOriginalColor = y.color;
            x = y.pointToRightChild;
            if (y.pointToParent == z) {
                x.pointToParent = y;
            } else {
                rbTransplant(y, y.pointToRightChild);
                y.pointToRightChild = z.pointToRightChild;
                y.pointToRightChild.pointToParent = y;
            }

            rbTransplant(z, y);
            y.pointToLeftChild = z.pointToLeftChild;
            y.pointToLeftChild.pointToParent = y;
            y.color = z.color;
        }
        if (yOriginalColor == 0) {
            fixDelete(x);
        }
    }

    // fix the red-black tree

    private void fixInsert(Node k) {
        Node u;
        while (k.pointToParent.color == 1) {
            if (k.pointToParent == k.pointToParent.pointToParent.pointToRightChild) {
                u = k.pointToParent.pointToParent.pointToLeftChild; // uncle
                if (u.color == 1) {
                    // case 3.1
                    u.color = 0;
                    k.pointToParent.color = 0;
                    k.pointToParent.pointToParent.color = 1;
                    k = k.pointToParent.pointToParent;
                } else {
                    if (k == k.pointToParent.pointToLeftChild) {
                        // case 3.2.2
                        k = k.pointToParent;
                        rightRotate(k);
                    }
                    // case 3.2.1
                    k.pointToParent.color = 0;
                    k.pointToParent.pointToParent.color = 1;
                    leftRotate(k.pointToParent.pointToParent);
                }
            } else {
                u = k.pointToParent.pointToParent.pointToRightChild; // uncle

                if (u.color == 1) {
                    // mirror case 3.1
                    u.color = 0;
                    k.pointToParent.color = 0;
                    k.pointToParent.pointToParent.color = 1;
                    k = k.pointToParent.pointToParent;
                } else {
                    if (k == k.pointToParent.pointToRightChild) {
                        // mirror case 3.2.2
                        k = k.pointToParent;
                        leftRotate(k);
                    }
                    // mirror case 3.2.1
                    k.pointToParent.color = 0;
                    k.pointToParent.pointToParent.color = 1;
                    rightRotate(k.pointToParent.pointToParent);
                }
            }
            if (k == root) {
                break;
            }
        }
        root.color = 0;
    }

    private void printHelper(Node root, String indent, boolean last) {
        // print the tree structure on the screen
        if (root != leaf) {
            System.out.print(indent);
            if (last) {
                System.out.print("R----");
                indent += "     ";
            } else {
                System.out.print("L----");
                indent += "|    ";
            }

            String sColor = root.color == 1 ? "RED" : "BLACK";
            System.out.println(root.data + "(" + sColor + ")");
            printHelper(root.pointToLeftChild, indent, false);
            printHelper(root.pointToRightChild, indent, true);
        }
    }
    // Pre-Order traversal
    // Node.Left Subtree.Right Subtree

    public void preorder() {
        preOrderHelper(this.root);
    }
    // In-Order traversal
    // Left Subtree . Node . Right Subtree

    public void inorder() {
        inOrderHelper(this.root);
    }
    // Post-Order traversal
    // Left Subtree . Right Subtree . Node

    public void postorder() {
        postOrderHelper(this.root);
    }
    // search the tree for the key k
    // and return the corresponding node

    public Node searchTree(int k) {
        return searchTreeHelper(this.root, k);
    }
    // find the node with the minimum key

    public Node minimum(Node node) {
        while (node.pointToLeftChild != leaf) {
            node = node.pointToLeftChild;
        }
        return node;
    }
    // find the node with the maximum key

    public Node maximum(Node node) {
        while (node.pointToRightChild != leaf) {
            node = node.pointToRightChild;
        }
        return node;
    }
    // find the successor of a given node

    public Node successor(Node x) {
        // if the right subtree is not null,
        // the successor is the leftmost node in the
        // right subtree
        if (x.pointToRightChild != leaf) {
            return minimum(x.pointToRightChild);
        }

        // else it is the lowest ancestor of x whose
        // left child is also an ancestor of x.
        Node y = x.pointToParent;
        while (y != leaf && x == y.pointToRightChild) {
            x = y;
            y = y.pointToParent;
        }
        return y;
    }
    // find the predecessor of a given node

    public Node predecessor(Node x) {
        // if the left subtree is not null,
        // the predecessor is the rightmost node in the
        // left subtree
        if (x.pointToLeftChild != leaf) {
            return maximum(x.pointToLeftChild);
        }

        Node y = x.pointToParent;
        while (y != leaf && x == y.pointToLeftChild) {
            x = y;
            y = y.pointToParent;
        }

        return y;
    }
    // rotate left at node x

    public void leftRotate(Node x) {
        Node y = x.pointToRightChild;
        x.pointToRightChild = y.pointToLeftChild;
        if (y.pointToLeftChild != leaf) {
            y.pointToLeftChild.pointToParent = x;
        }
        y.pointToParent = x.pointToParent;
        if (x.pointToParent == null) {
            this.root = y;
        } else if (x == x.pointToParent.pointToLeftChild) {
            x.pointToParent.pointToLeftChild = y;
        } else {
            x.pointToParent.pointToRightChild = y;
        }
        y.pointToLeftChild = x;
        x.pointToParent = y;
    }
    // rotate right at node x

    public void rightRotate(Node x) {
        Node y = x.pointToLeftChild;
        x.pointToLeftChild = y.pointToRightChild;
        if (y.pointToRightChild != leaf) {
            y.pointToRightChild.pointToParent = x;
        }
        y.pointToParent = x.pointToParent;
        if (x.pointToParent == null) {
            this.root = y;
        } else if (x == x.pointToParent.pointToRightChild) {
            x.pointToParent.pointToRightChild = y;
        } else {
            x.pointToParent.pointToLeftChild = y;
        }
        y.pointToRightChild = x;
        x.pointToParent = y;
    }
    // insert the key to the tree in its appropriate position
    // and fix the tree

    public void insert(int key) {
        // Ordinary Binary Search Insertion
        Node node = new Node();
        node.pointToParent = null;
        node.data = key;
        node.pointToLeftChild = leaf;
        node.pointToRightChild = leaf;
        node.color = 1; // new node must be red

        Node y = null;
        Node x = this.root;

        while (x != leaf) {
            y = x;
            if (node.data < x.data) {
                x = x.pointToLeftChild;
            } else {
                x = x.pointToRightChild;
            }
        }

        // y is parent of x
        node.pointToParent = y;
        if (y == null) {
            root = node;
        } else if (node.data < y.data) {
            y.pointToLeftChild = node;
        } else {
            y.pointToRightChild = node;
        }

        // if new node is a root node, simply return
        if (node.pointToParent == null) {
            node.color = 0;
            return;
        }

        // if the grandparent is null, simply return
        if (node.pointToParent.pointToParent == null) {
            return;
        }

        // Fix the tree
        fixInsert(node);
    }

    public Node getRoot() {
        return this.root;
    }
    // delete the node from the tree

    public void deleteNode(int data) {
        deleteNodeHelper(this.root, data);
    }
    // print the tree structure on the screen

    public void prettyPrint() {
        printHelper(this.root, "", true);
    }

}
