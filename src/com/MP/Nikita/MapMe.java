package main.java.com.MP.Nikita;

import java.util.*;

public class MapMe<K,V> implements Map<K,V> {

    int size;
    Entry<K,V> firstElement, lastElement;

    MapMe() {
        this.size = 0;
        this.firstElement = null;
        this.lastElement = null;
    }

    static class Entry<K,V> implements Map.Entry<K,V> {

        K Key;
        V Value;
        Entry<K, V> nextEntry, prevEntry;

        public Entry() {
            this.Key = null;
            this.Value = null;
            this.prevEntry = null;
            this.nextEntry = null;
        }

        public Entry (K key, V value, Entry<K, V> nextEntry, Entry<K,V> prevEntry) {
            this.Key = key;
            this.Value = value;
            this.nextEntry = nextEntry;
            this.prevEntry = prevEntry;
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
        return (getEntryByValue(value) != null);
    }

    private Entry<K,V> getEntryByValue(Object value) {
        Entry<K,V> element = firstElement,
            reverselement = lastElement;

        if (this.isEmpty()) return null;

        while (element != null) {
            //Идём с двух сторон по списку
            if (element.Value == value)
                return element;
            if (reverselement.Value == value)
                return reverselement;

            //Если указатели сошлись, то элемент не найден, выврлим null
            if (element == reverselement
                    || element.nextEntry == reverselement)
                return null;

            element = element.nextEntry;
            reverselement = reverselement.prevEntry;
        }
        return null;
    }

    @Override
    public V get(Object key) {
        return getEntryByKey(key).Value;
    }

    @Override
    public V put(K key, V value) {

        if (firstElement == null) {
            firstElement = new Entry<K, V>(key, value, null, null);
            lastElement = firstElement;
            size++;
            return null;
        }

        Entry<K,V> val = getEntryByKey(key);

        if (val != null) {
            V oldValue = val.Value;
            val.Value = value;
            return oldValue;
        }

        Entry<K, V> element = new Entry<K, V>(key, value, null, lastElement);
        lastElement.nextEntry = element;
        lastElement = element;
        size++;
        return null;
    }

    private Entry<K,V> getEntryByKey(Object key) {
        if (firstElement == null)
            return null;

        Entry<K,V> element = firstElement,
                reverselement = lastElement;

        while (firstElement != null){
            if (element.Key == key) return element;
            if (reverselement.Key == key) return reverselement;

            if (element == reverselement
                    || element.nextEntry == reverselement)
                return null;

            element = element.nextEntry;
            reverselement = reverselement.prevEntry;
        }
        return null;
    }

    @Override
    public V remove(Object key) {

        if (firstElement == null) {
            return null;
        }

        Entry<K,V> element = getEntryByKey(key);

        if (element !=null) {
            V oldValue = element.Value;
            element.nextEntry.prevEntry = element.prevEntry;
            element.prevEntry.nextEntry = element.nextEntry;
            size--;
            return oldValue;
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        if (m == null) {
            return;
        }

        //пока что ничего

    }

    @Override
    public void clear() {
        size = 0;
        firstElement = lastElement = null;
    }

    @Override
    public Set keySet() {
        Set<K> setofKey = new HashSet<>();
        Entry<K,V> element = firstElement;

        while (element != null) {
            setofKey.add(element.Key);
            element = element.nextEntry;
        }
        return setofKey;
    }

    @Override
    public Collection values() {

        List<V> listofValues = new ArrayList<>();
        Entry<K,V> element = firstElement;

        while (element != null) {
            listofValues.add(element.Value);
            element = element.nextEntry;
        }

        return listofValues;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {

        Set<Map.Entry<K,V>> setofEntrys = new HashSet<>();
        Entry<K,V> element = firstElement;

        while (element != null) {
            setofEntrys.add(element);
            element = element.nextEntry;
        }

        return setofEntrys;
    }
}
