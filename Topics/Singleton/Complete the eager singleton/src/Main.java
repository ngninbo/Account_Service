
class SimpleCounter {
    public static final SimpleCounter INSTANCE = new SimpleCounter();

    public int counter;

    private SimpleCounter() {
    }

    public static SimpleCounter getInstance() {

        if (INSTANCE == null) {
            new SimpleCounter();
        }

        return INSTANCE;
    }
}