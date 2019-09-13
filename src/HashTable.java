import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class HashTable<K, V> {
    private int startingSize = 100000;
    private K[] keys;
    private V[] values;
    private int size;

    @SuppressWarnings("unchecked")
    public HashTable() {
        keys = (K[]) new Object[startingSize];
        values = (V[]) new Object[startingSize];
    }

    public void put(K key, V value) {
        if (size < startingSize) {
            int index = hash(key);
            System.out.println("Index for \"" + key.toString() + "\": " + index + "\n");

            if (values[index] != null) {
                if (keys[index] == key) {
                    System.out.println("Duplicate key found: " + key.toString() + "\n");
                } else {
                    System.out.println("Collision detected at index: " + index);

                    System.out.println("Key 1: " + keys[index] + " - Key 2: " + key.toString() + "");
                    // TODO handle collisions
                    int newIndex = nextFreeIndex(index);

                    if (newIndex != -1){
                        System.out.println("New index for \"" + key.toString() + "\": " + newIndex + "\n");

                        keys[newIndex] = key;
                        values[newIndex] = value;
                        size++;
                    } else {
                        System.out.println("Table is full..\n");
                    }
                }
            } else {
                keys[index] = key;
                values[index] = value;
                size++;
            }
        } else {
            // TODO resize arrays or notify user that table is full.
            System.out.println("Table is full..");
        }
    }

    public V get(K key) {
        int index = hash(key);

        if (keys[index] != null) {
            if (keys[index] == key) {
                return values[index];
            } else {
                int newIndex = getIndexOfKey(key, index);
                if (newIndex != -1) {
                    return values[newIndex];
                } else {
                    System.out.println("No value associated with key \"" + key.toString() + "\"\n");
                    return null;
                }
            }
        } else {
            System.out.println("No value associated with key \"" + key.toString() + "\"\n");
            return null;
        }

    }

    public void remove(K key) {
        int index = hash(key);
        if (keys[index] != null) {
            if (keys[index] == key) {
                System.out.println("Key \"" + key.toString() + "\" found");
                keys[index] = null;
                values[index] = null;
                size--;
            } else {
                int newIndex = getIndexOfKey(key, index);
                if (newIndex != -1) {
                    System.out.println("Key \"" + key.toString() + "\" found");
                    keys[index] = null;
                    values[index] = null;
                    size--;
                } else {
                    System.out.println("No value associated with key \"" + key.toString() + "\"\n");
                }
            }
        } else {
            System.out.println("Key \"" + key.toString() + "\" not found");

        }
    }

    public int getSize() {
        return size;
    }

    private int nextFreeIndex(int index) {
        for (int i=index; i<startingSize; i++) {
            if (values[i] == null) {
                return i;
            }
        }
        // Table is full from index onwards, check start
        for (int i=0; i<index; i++) {
            if (values[i] == null) {
                return i;
            }
        }
        return -1;
    }

    private int getIndexOfKey(K key, int index) {
        for (int i=index; i<startingSize; i++) {
            if (keys[i] == key) {
                return i;
            }
        }
        //Key is not from index onwards, checking start
        for (int i=0; i<index; i++) {
            if (keys[i] == key) {
                return i;
            }
        }
        return -1;
    }

    private int hash(K key) {
        String result = Integer.toString(key.hashCode());
        int index = 1;

        //Mapping function
        for (int i=0; i<result.length(); i++) {
            index = index*33 + result.charAt(i);
        }

        //Compressing function
        index = index % startingSize;

        return Math.abs(index);
    }

    public Iterable<K> getKeys() {
        ArrayList<K> iterable = new ArrayList<>();
        for (int i=0; i<startingSize; i++) {
            if (keys[i] != null) {
                iterable.add(keys[i]);
            }
        }

        return iterable;
    }

    public Iterable<V> getValues() {
        ArrayList<V> iterable = new ArrayList<>();
        for (int i=0; i<startingSize; i++) {
            if (values[i] != null) {
                iterable.add(values[i]);
            }
        }
        return iterable;
    }

    public static void main(String[] args) {
        HashTable<String, Integer> table = new HashTable<>();

        for (int i=0; i<50000; i++) {
            String key = "Key " + ThreadLocalRandom.current().nextInt();

            table.put(key, ThreadLocalRandom.current().nextInt());
        }

        String key1 = "Key 1097204242";
        String key2 = "Key 60383659";
        String key3 = "hola";

        table.put(key1, 23);
        table.put(key2, 55);
        table.put(key3, 149);

        long start = System.currentTimeMillis();
        table.get(key1);
        long end = System.currentTimeMillis();
        System.out.println("Total time 1: " + (end - start) + " ms");

        long start2 = System.currentTimeMillis();
        table.get(key2);
        long end2 = System.currentTimeMillis();
        System.out.println("Total time 2: " + (end2 - start2) + " ms");

        long start3 = System.currentTimeMillis();
        table.get(key3);
        long end3 = System.currentTimeMillis();
        System.out.println("Total time 3: " + (end3 - start3) + " ms");
    }
}