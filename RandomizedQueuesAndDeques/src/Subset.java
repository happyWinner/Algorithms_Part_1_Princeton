public class Subset {
    public static void main(String[] args) {
        int numberOfOutputs = Integer.parseInt(args[0]);
        RandomizedQueue<String> strings = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            strings.enqueue(StdIn.readString());
        }

        for (int i = 0; i < numberOfOutputs; ++i) {
            StdOut.println(strings.dequeue());
        }
    }
}