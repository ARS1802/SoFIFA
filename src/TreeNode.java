public class TreeNode<T> {
    private TreeNode<T> left;
    private T element;
    private TreeNode<T> right;
    private int FB = 0;
    private int height = 1;

    public TreeNode(T elemento, TreeNode<T> proximo, TreeNode<T> anterior) {
        this.element = elemento;
        this.right = proximo;
        this.left = anterior;
    }

    public TreeNode(T elemento) {
        this.element = elemento;
        this.right = null;
        this.left = null;
    }

    public int getFB() {
        return FB;
    }

    public void setFB(int FB) {
        this.FB = FB;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public TreeNode<T> getLeft() {
        return left;
    }

    public void setLeft(TreeNode<T> previous) {
        this.left = previous;
    }

    public T getElement() {
        return element;
    }

    public void setElement(T element) {
        this.element = element;
    }

    public TreeNode<T> getRight() {
        return right;
    }

    public void setRight(TreeNode<T> next) {
        this.right = next;
    }

    @Override
    public String toString() {
        return element.toString();
    }


}