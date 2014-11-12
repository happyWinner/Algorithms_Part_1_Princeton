public class KdTree {
    private Node root;
    private int size;

    private static class Node {
        private Point2D p;    // the point
        private RectHV rect;  // the axis-aligned rectangle corresponding to this node
        private Node lb;      // the left/bottom subtree
        private Node rt;      // the right/top subtree

        private Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
            lb = null;
            rt = null;
        }
    }

    // Construct an empty tree of points
    public KdTree() {
        root = null;
        size = 0;
    }

    // Is the tree empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // Number of points in the tree
    public int size() {
        return size;
    }

    // Add the point p to the tree (if it is not already in the tree)
    public void insert(Point2D p) {
        if (!contains(p)) {
            root = insert(root, p, 0, 0., 0., 1., 1.);
            ++size;
        }
    }

    private Node insert(Node node, Point2D p, int level, double xmin, double ymin, double xmax, double ymax) {
        if (node == null) {
            return new Node(p, new RectHV(xmin, ymin, xmax, ymax));
        }
        else {
            if (level % 2 == 0) {
                int cmp = Point2D.X_ORDER.compare(p, node.p);
                if (cmp < 0) {
                    node.lb = insert(node.lb, p, level + 1, xmin, ymin, node.p.x(), ymax);
                }
                else {
                    node.rt = insert(node.rt, p, level + 1, node.p.x(), ymin, xmax, ymax);
                }
            }
            else {
                int cmp = Point2D.Y_ORDER.compare(p, node.p);
                if (cmp < 0) {
                    node.lb = insert(node.lb, p, level + 1, xmin, ymin, xmax, node.p.y());
                }
                else {
                    node.rt = insert(node.rt, p, level + 1, xmin, node.p.y(), xmax, ymax);
                }
            }
            return node;
        }
    }

    // Does the tree contain the point p
    public boolean contains(Point2D p) {
        int level = 0;
        Node node = root;
        while (node != null) {
            if (level % 2 == 0) {
                int cmp = Point2D.X_ORDER.compare(p, node.p);
                if (cmp < 0) {
                    node = node.lb;
                }
                else if (cmp == 0 && Point2D.Y_ORDER.compare(p, node.p) == 0) {
                    return true;
                }
                else {
                    node = node.rt;
                }
            }
            else {
                int cmp = Point2D.Y_ORDER.compare(p, node.p);
                if (cmp < 0) {
                    node = node.lb;
                }
                else if (cmp == 0 && Point2D.X_ORDER.compare(p, node.p) == 0) {
                    return true;
                }
                else {
                    node = node.rt;
                }
            }
            ++level;
        }
        return false;
    }

    // Draw all of the points to standard draw
    public void draw() {
        if (root != null) {
            draw(root, 0);
        }
    }

    private void draw(Node node, int level) {
        if (node != null) {
            // draw the point
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            node.p.draw();

            // draw the splitting line
            StdDraw.setPenRadius();
            if (level % 2 == 0) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
            }

            // draw child nodes recursively
            draw(node.lb, level + 1);
            draw(node.rt, level + 1);
        }
    }

    // All points in the tree that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        Queue<Point2D> queue = new Queue<Point2D>();
        range(root, 0, rect, queue);
        return queue;
    }

    private void range(Node node, int level, RectHV rect, Queue<Point2D> queue) {
        if (node != null) {
            if (rect.intersects(node.rect)) {
                if (level % 2 == 0 && node.p.x() > rect.xmax()
                        || level % 2 != 0 && node.p.y() > rect.ymax()) {
                    range(node.lb, level + 1, rect, queue);
                }
                else if (level % 2 == 0 && node.p.x() < rect.xmin()
                        || level % 2 != 0 && node.p.y() < rect.ymin()) {
                    range(node.rt, level + 1, rect, queue);
                }
                else {
                    if (rect.contains(node.p)) {
                        queue.enqueue(node.p);
                    }
                    range(node.lb, level + 1, rect, queue);
                    range(node.rt, level + 1, rect, queue);
                }
            }
        }
    }

    // A nearest neighbor in the tree to p; null if tree is empty
    public Point2D nearest(Point2D p) {
        if (root == null) {
            return null;
        }
        else {
            return nearest(root, 0, p, root.p);
        }
    }

    private Point2D nearest(Node node, int level, Point2D p, Point2D closerPoint) {
        Point2D nearestPoint = closerPoint;
        if (node != null) {
            double squaredDistance = p.distanceSquaredTo(nearestPoint);
            if (squaredDistance >= node.rect.distanceSquaredTo(p)) {
                if (p.distanceSquaredTo(node.p) < squaredDistance) {
                    nearestPoint = node.p;
                }

                if (level % 2 == 0 && p.x() <= node.p.x()
                        || level % 2 != 0 && p.y() <= node.p.y()) {
                    nearestPoint = nearest(node.lb, level + 1, p, nearestPoint);
                    nearestPoint = nearest(node.rt, level + 1, p, nearestPoint);
                }
                else {
                    nearestPoint = nearest(node.rt, level + 1, p, nearestPoint);
                    nearestPoint = nearest(node.lb, level + 1, p, nearestPoint);
                }
            }
        }
        return nearestPoint;
    }
}