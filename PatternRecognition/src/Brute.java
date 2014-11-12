import java.util.Arrays;

public class Brute {
    public static void main(String[] args) {
        In in = new In(args[0]);
        int numberOfPoints = in.readInt();
        Point[] points = new Point[numberOfPoints];

        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);

        for (int i = 0; i < numberOfPoints; ++i) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
            points[i].draw();
        }

        for (int i = 0; i < numberOfPoints; ++i) {
            for (int j = i + 1; j < numberOfPoints; ++j) {
                for (int k = j + 1; k < numberOfPoints; ++k) {
                    double slope1 = points[i].slopeTo(points[j]);
                    double slope2 = points[j].slopeTo(points[k]);
                    if (slope1 == slope2) {
                        for (int l = k + 1; l < numberOfPoints; ++l) {
                            double slope3 = points[k].slopeTo(points[l]);
                            if (slope2 == slope3) {
                                Point[] collinearPoints = new Point[4];
                                collinearPoints[0] = points[i];
                                collinearPoints[1] = points[j];
                                collinearPoints[2] = points[k];
                                collinearPoints[3] = points[l];
                                Arrays.sort(collinearPoints);
                                StdOut.print(collinearPoints[0].toString() + " -> ");
                                StdOut.print(collinearPoints[1].toString() + " -> ");
                                StdOut.print(collinearPoints[2].toString() + " -> ");
                                StdOut.println(collinearPoints[3].toString());
                                collinearPoints[0].drawTo(collinearPoints[3]);
                            }
                        }
                    }
                }
            }
        }
    }
}