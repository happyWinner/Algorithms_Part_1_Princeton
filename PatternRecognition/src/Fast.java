import java.util.Arrays;

public class Fast {
    public static void main(String[] args) {
        In in = new In(args[0]);
        int numberOfPoints = in.readInt();
        Point[] points = new Point[numberOfPoints];
        Point[] pointsCopy = new Point[numberOfPoints];

        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);

        for (int i = 0; i < numberOfPoints; ++i) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
            points[i].draw();
            pointsCopy[i] = new Point(x, y);
        }

        for (int i = 0; i < numberOfPoints; ++i) {
            Arrays.sort(points, 0, numberOfPoints, pointsCopy[i].SLOPE_ORDER);
            int start = 0;
            while (start <= numberOfPoints - 3) {
                double slope = pointsCopy[i].slopeTo(points[start]);
                if (slope == pointsCopy[i].slopeTo(points[start + 1])
                        && slope == pointsCopy[i].slopeTo(points[start + 2])) {
                    int end = start + 3;
                    while (end < numberOfPoints && slope == pointsCopy[i].slopeTo(points[end])) {
                        ++end;
                    }

                    int numberOfCollinearPoints = end - start + 1;
                    Point[] collinearPoints = new Point[numberOfCollinearPoints];
                    for (int j = 0; j < numberOfCollinearPoints - 1; ++j) {
                        collinearPoints[j] = points[start + j];
                    }
                    collinearPoints[numberOfCollinearPoints - 1] = pointsCopy[i];
                    Arrays.sort(collinearPoints);

                    // prevent duplicates
                    // only output when the invoking point is the lowest one 
                    if (collinearPoints[0].slopeTo(pointsCopy[i]) == Double.NEGATIVE_INFINITY) {
                        for (int j = 0; j < numberOfCollinearPoints - 1; ++j) {
                            StdOut.print(collinearPoints[j].toString() + " -> ");
                        }
                        StdOut.println(collinearPoints[numberOfCollinearPoints - 1].toString());
                        collinearPoints[0].drawTo(collinearPoints[numberOfCollinearPoints - 1]);
                    }

                    start = end;
                }
                else {
                    ++start;
                }
            }
        }
    }
}