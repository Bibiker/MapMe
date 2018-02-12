package com.MP.Nikita;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MapMe<K,V> implements Map<K,V> {

    int size;
    Obj<K,V> firstElement, lastElement;

    MapMe() {
        this.size = 0;
        this.firstElement = null;
        this.lastElement = null;
    }

    static class Obj<K,V> implements Map.Entry<K,V> {

        K Key;
        V Value;
        Obj<K, V> nextObj, prevObj;

        public Obj() {
            this.Key = null;
            this.Value = null;
            this.prevObj = null;
            this.nextObj = null;
        }

        public Obj (K key, V value, Obj<K, V> nextobj, Obj<K,V> prevobj) {
            this.Key = key;
            this.Value = value;
            this.nextObj = nextobj;
            this.prevObj = prevobj;
        }

        @Override
        public K getKey() { return Key; }

        @Override
        public final V getValue() { return Value; }

        @Override
        public V setValue(Object value) {
            V oldval = Value;
            Value = (V)value;
            return oldval;
        }
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public boolean containsKey(Object key) {
        return (get(key) != null);
    }

    @Override
    public boolean containsValue(Object value) {
        return (getOgjByValue(value) != null);
    }

    private Obj<K,V> getOgjByValue(Object value) {
        Obj<K,V> element = firstElement,
            reverselement = lastElement;

        if (this.isEmpty()) return null;

        while (element != null)
        {
            if (element.Value == value)
                return element;
            if (reverselement.Value == value)
                return reverselement;

            element = element.nextObj;
            reverselement = reverselement.prevObj;
        }
        return null;
    }

    @Override
    public V get(Object key) {
        return null;
    }

    @Override
    public V put(K key, V value) {

        if (firstElement == null) {
            firstElement = new Obj<K, V>(key, value, null, null);
            lastElement = firstElement;
            size++;
            return null;
        }

            //дописать поиск элемента (get)

            Obj<K, V> element = new Obj<K, V>(key, value, null, lastElement);
            lastElement.nextObj = element;
            lastElement = lastElement.nextObj;
            return null;

    }

    @Override
    public V remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set keySet() {
        return null;
    }

    @Override
    public Collection values() {
        return null;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }
}
