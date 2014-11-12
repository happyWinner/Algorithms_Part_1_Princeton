public class PointSET {
    private SET<Point2D> points;

    // Construct an empty set of points
    public PointSET() {
        points = new SET<Point2D>();
    }

    // Is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // Number of points in the set
    public int size() {
        return points.size();
    }

    // Add the point p to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (!points.contains(p)) {
            points.add(p);
        }
    }

    // Does the set contain the point p
    public boolean contains(Point2D p) {
        return points.contains(p);
    }

    // Draw all of the points to standard draw
    public void draw() {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        for (Point2D point : points) {
            point.draw();
        }
    }

    // All points in the set that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        Queue<Point2D> queue = new Queue<Point2D>();
        for (Point2D point : points) {
            if (rect.contains(point)) {
                queue.enqueue(point);
            }
        }
        return queue;
    }

    // A nearest neighbor in the set to p; null if set is empty
    public Point2D nearest(Point2D p) {
        if (isEmpty()) {
            return null;
        }
        else {
            Point2D nearestPoint = points.min();
            double squaredDistance = p.distanceSquaredTo(nearestPoint);
            for (Point2D point : points) {
                if (p.distanceSquaredTo(point) < squaredDistance) {
                    squaredDistance = p.distanceSquaredTo(point);
                    nearestPoint = point;
                }
            }
            return nearestPoint;
        }
    }
}