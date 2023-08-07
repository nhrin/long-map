package de.comparus.opensource.longmap;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.Stream;

public class LongMapImpl<V> implements LongMap<V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;

    private int capacity;                 // The current capacity of the hash table.
    private int size;                     // The number of key-value pairs stored in the map.
    private final double loadFactor;      // The load factor determines when to resize the map.
    private LinkedList<Node<V>>[] hashTable; // The array of linked lists used for hash table.

    // Inner class representing a node in the linked list.
    private static class Node<V> {
        long key;   // The key used for hashing.
        V value;    // The value associated with the key.

        Node(long key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    // Default constructor, initializes the map with default capacity and load factor.
    public LongMapImpl() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    // Constructor with custom initial capacity and load factor.
    public LongMapImpl(int initialCapacity, double loadFactor) {
        this.capacity = initialCapacity;
        this.loadFactor = loadFactor;
        this.size = 0;
        this.hashTable = new LinkedList[capacity];
    }

    // A simple hash function to map keys to the array index.
    private int hash(long key) {
        int h = (int) (key ^ (key >>> 32));
        return h & (capacity - 1);
    }

    // Adds or updates a key-value pair in the map and resizes if necessary.
    public V put(long key, V value) {
        int index = hash(key);
        LinkedList<Node<V>> bucket = hashTable[index];

        if (bucket == null) {
            bucket = new LinkedList<>();
            hashTable[index] = bucket;
        }

        Node<V> existingNode = findEntry(bucket, key);

        if (existingNode != null) {
            // Key already exists, update the value and return the old value.
            V oldValue = existingNode.value;
            existingNode.value = value;
            return oldValue;
        }

        // Key doesn't exist, add a new node to the bucket and resize if needed.
        Node<V> newNode = new Node<>(key, value);
        bucket.add(newNode);
        size++;

        if (size >= loadFactor * capacity) {
            resize();
        }

        return null;
    }

    // Retrieves the value associated with the given key.
    public V get(long key) {
        int index = hash(key);
        LinkedList<Node<V>> bucket = hashTable[index];
        Node<V> node = findEntry(bucket, key);
        return node != null ? node.value : null;
    }

    // Removes a key-value pair from the map based on the given key.
    public V remove(long key) {
        int index = hash(key);
        LinkedList<Node<V>> bucket = hashTable[index];

        if (bucket == null) {
            return null;
        }

        Node<V> node = findEntry(bucket, key);

        if (node != null) {
            bucket.remove(node);
            size--;
            return node.value;
        }

        return null;
    }

    // Checks if the map is empty.
    public boolean isEmpty() {
        return size == 0;
    }

    // Checks if the map contains the given key.
    public boolean containsKey(long key) {
        int index = hash(key);
        LinkedList<Node<V>> bucket = hashTable[index];
        return bucket != null && findEntry(bucket, key) != null;
    }

    // Checks if the map contains the given value.
    public boolean containsValue(V value) {
        return Stream.of(hashTable)
                .filter(Objects::nonNull)
                .flatMap(LinkedList::stream)
                .anyMatch(node -> node.value.equals(value));
    }

    // Returns an array containing all the keys in the map.
    public long[] keys() {
        return Stream.of(hashTable)
                .filter(Objects::nonNull)
                .flatMap(LinkedList::stream)
                .mapToLong(node -> node.key)
                .toArray();
    }

    // Returns an array containing all the values in the map.
    public V[] values() {
        return Stream.of(hashTable)
                .filter(Objects::nonNull)
                .flatMap(LinkedList::stream)
                .map(entry -> entry.value)
                .toArray(size -> (V[]) new Object[size]);
    }

    // Returns the number of key-value pairs in the map.
    public long size() {
        return size;
    }

    // Clears the map by setting all buckets to null and size to 0.
    public void clear() {
        Arrays.fill(hashTable, null);
        size = 0;
    }

    // Helper method to find a specific key in the given bucket (linked list).
    private Node<V> findEntry(LinkedList<Node<V>> bucket, long key) {
        if (bucket == null) {
            return null;
        }

        for (Node<V> node : bucket) {
            if (node.key == key) {
                return node;
            }
        }

        return null;
    }

    // Helper method to resize the hash table when the load factor is exceeded.
    private void resize() {
        int newCapacity = capacity * 2;
        LinkedList<Node<V>>[] newHashTable = new LinkedList[newCapacity];

        Stream.of(hashTable)
                .filter(Objects::nonNull)
                .flatMap(LinkedList::stream)
                .forEach(node -> {
                    int newIndex = hash(node.key) & (newCapacity - 1);
                    LinkedList<Node<V>> newBucket = newHashTable[newIndex];

                    if (newBucket == null) {
                        newBucket = new LinkedList<>();
                        newHashTable[newIndex] = newBucket;
                    }

                    newBucket.add(node);
                });

        capacity = newCapacity;
        hashTable = newHashTable;
    }
}
