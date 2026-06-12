package arvore;

import java.util.Comparator;
//======== Alterações ==========
/*
*       => AVL<T extends Comparable<T>> -> AVL<T>
*       => construtor AVL exige Comparator (Faça Player.filters(Atributes.PLAYER_ID); como default!)
*       => .comparTo -> comparator.compare(T left, T right)
 */
//==============================
public class AVL<T> {

    private TreeNode<T> root;
    private final Comparator<? super T> comparator;
    private int nodeAmount = 0;

    public AVL(TreeNode<T> data, Comparator<? super T> comparator) {
        this.root = data;
        nodeAmount++;
        this.comparator = comparator;
    }

    public AVL(Comparator<? super T> comparator){
        this.root = null;
        this.comparator = comparator;
    }

    @SafeVarargs
    public AVL(Comparator<? super T> comparator, T... values){
        this(comparator);

        for(T value : values){
            add(value);
        }
    }

    public AVL(T[] values, Comparator<? super T> comparator){
        this(comparator);

        for(T value : values){
            add(value);
        }
    }

    public void add(T value){
        if(this.root == null){
            this.root = new TreeNode<>(value);
            return;
        }
        nodeAmount++;
        addRecursivelly(value, root, null);
    }

    public boolean remove(T value){
        if(this.root == null){
            return false;
        }
        nodeAmount--;
        return removeRecursivelly(value, this.root, null);


    }
    private boolean removeRecursivelly(T value, TreeNode<T> node, TreeNode<T> parent){

        if (node == null) {
            return false;
        }

        boolean removed = false;
        int comparison = comparator.compare(value, node.getElement());

        if(comparison < 0){
            removed = removeRecursivelly(value, node.getLeft(), node);
        }
        else if(comparison > 0){
            removed = removeRecursivelly(value, node.getRight(), node);
        }
        else {
            //No nao tem nenhum filho
            if(node.getLeft() == null && node.getRight() == null){
                if(node == this.root){
                    this.root = null;
                }else if(node == parent.getLeft()){
                    parent.setLeft(null);
                }else{
                    parent.setRight(null);
                }

                removed = true;
            }

            //No tem somente 1 filho a direita
            if(node.getLeft() == null && node.getRight() != null){
                if (node == this.root) {
                    this.root = node.getRight();
                }else if(node == parent.getLeft()){
                    parent.setLeft(node.getRight());
                }else{
                    parent.setRight(node.getRight());
                }
                removed = true;
            }

            //No tem somente 1 filho a esquerda
            if(node.getLeft() != null && node.getRight() == null){
                if (node == this.root) {
                    this.root = node.getLeft();
                }else if(node == parent.getLeft()){
                    parent.setLeft(node.getLeft());
                }else{
                    parent.setRight(node.getLeft());
                }
                removed = true;
            }

            //No tem somente 2 filhos
            if(node.getLeft() != null && node.getRight() != null){
                TreeNode<T> swapNode = searchMin(node.getRight());
                node.setElement(swapNode.getElement());
                removeRecursivelly(swapNode.getElement(), node.getRight(), node);

                removed = true;
            }
        }
        if(removed){
            updateNodeState(node);
            if(node.getFB() > 1){
                if(node.getLeft().getFB() < 0){
                    rotateLeft(node.getLeft(), node);
                }
                rotateRight(node, parent);
            }
            if(node.getFB() < -1){
                if(node.getRight().getFB() > 0){
                    rotateRight(node.getRight(), node);
                }
                rotateLeft(node, parent);
            }
        }

        return removed;
    }

    private TreeNode<T> searchMin(TreeNode<T> node){
        if(node.getLeft() == null){
            return node;
        }
        return searchMin(node.getLeft());
    }

    public int calculateDepth(TreeNode<T> node){
        if(node == null){
            return -1;
        }

        return 1 + Math.max(calculateDepth(node.getLeft()), calculateDepth(node.getRight()));

    }

    private void updateNodeState(TreeNode<T> node) {
        if (node == null) return;

        int leftHeight = node.getLeft() == null ? 0 : node.getLeft().getHeight();
        int rightHeight = node.getRight() == null ? 0 : node.getRight().getHeight();

        node.setHeight(1 + Math.max(leftHeight, rightHeight));

        node.setFB(leftHeight - rightHeight);
    }


    public boolean contains(T value){
        if(this.root == null){
            return false;
        }
        return containsRecursivelly(value, root);
    }

    private boolean containsRecursivelly(T value, TreeNode<T> data){
        if (data == null){
            return false;
        }
        if (comparator.compare(value, data.getElement()) == 0){
            return true;
        }

        if(comparator.compare(value, data.getElement()) < 0){ // Entrada e menor que o node (esta na esquerda)
            return containsRecursivelly(value, data.getLeft());
        }

        else if(comparator.compare(value, data.getElement()) > 0){// Entrada e maior que o node (esta na direita)
            return containsRecursivelly(value, data.getRight());
        }

        return false;
    }

    private void addRecursivelly(T value, TreeNode<T> data, TreeNode<T> parent){
        if(comparator.compare(value, data.getElement()) < 0){ // Entrada e menor que o node (esta na esquerda)
            if(data.getLeft() == null){
                data.setLeft(new TreeNode<>(value));
            }else{
                addRecursivelly(value, data.getLeft(), data);
            }
        }

        else if(comparator.compare(value, data.getElement()) > 0){// Entrada e maior que o node (esta na direita)
            if(data.getRight() == null){
                data.setRight(new TreeNode<>(value));
            }else{
                addRecursivelly(value, data.getRight(), data);
            }
        }

        updateNodeState(data);

        if(data.getFB() > 1){
            if(data.getLeft().getFB() < 0){
                rotateLeft(data.getLeft(), data);
            }
            rotateRight(data, parent);
        }
        if(data.getFB() < -1){
            if(data.getRight().getFB() > 0){
                rotateRight(data.getRight(), data);
            }
            rotateLeft(data, parent);
        }
    }

    private void rotateRight(TreeNode<T> data, TreeNode<T> parent){
        TreeNode<T> temp = data.getLeft();
        data.setLeft(data.getLeft().getRight());
        temp.setRight(data);

        updateNodeState(data);
        updateNodeState(temp);

        if(parent == null){
            root = temp;
            return;
        }
        if(parent.getLeft() == data){
            parent.setLeft(temp);
        }else{
            parent.setRight(temp);
        }

    }

    public int getSize() {
        return nodeAmount;
    }

    private void rotateLeft(TreeNode<T> data, TreeNode<T> parent){
        TreeNode<T> temp = data.getRight();
        data.setRight(data.getRight().getLeft());
        temp.setLeft(data);

        updateNodeState(data);
        updateNodeState(temp);

        if(parent == null){
            root = temp;
            return;
        }
        if(parent.getLeft() == data){
            parent.setLeft(temp);
        }else{
            parent.setRight(temp);
        }
    }

    private String preOrder(TreeNode<T> data){
        String result = "";
        if(data == null){
            return result;
        }
        result += data.getElement();
        String left = preOrder(data.getLeft());
        String right = preOrder(data.getRight());
        if(!left.isEmpty()){
            result += "\n"  + left;
        }
        if(!right.isEmpty()){
            result +="\n"  + right;
        }

        return result;
    }

    private String inOrder(TreeNode<T> data){
        String result = "";
        if(data == null){
            return result;
        }
        String left = inOrder(data.getLeft());
        String right = inOrder(data.getRight());

        if(!left.isEmpty()){
            result += left + "\n";
        }
        result += data.getElement();
        if(!right.isEmpty()){
            result +="\n"  + right;
        }

        return result;
    }

    private String posOrder(TreeNode<T> data){
        String result = "";
        if(data == null){
            return result;
        }

        String left = posOrder(data.getLeft());
        String right = posOrder(data.getRight());

        if(!left.isEmpty()){
            result += left + "\n";
        }
        if(!right.isEmpty()){
            result += right + "\n";
        }
        result += data.getElement();

        return result;
    }

    @Override
    public String toString() {
//        return preOrder(this.root);
        return inOrder(this.root);
//        return posOrder(this.root);
    }
}
