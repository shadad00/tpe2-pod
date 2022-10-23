package ar.edu.pod.tp2;

import java.io.Serializable;

public class Pair<K,V> implements Serializable {
    private final K key;
    private final V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return key.hashCode() + value.hashCode(); //TODO
    }

    @Override
    public boolean equals(Object obj) {
        if(this==obj)
            return true;
        if(!(obj instanceof Pair))
            return false;
        Pair<K,V> other = (Pair<K, V>) obj;
        return key.equals(other.getKey()) && value.equals(other.getValue());
    }
}
