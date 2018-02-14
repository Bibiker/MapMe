package main.java.com.MP.Nikita;

import javax.swing.text.html.HTMLDocument;
import java.util.*;

public class MapMe<K,V> implements Map<K,V> {

    int size;
    Entry<K,V> firstEntry, lastEntry;

    MapMe() {
        this.size = 0;
        this.firstEntry = null;
        this.lastEntry = null;
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

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
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
        Entry<K,V> entry = firstEntry,
            reversEntry = lastEntry;

        if (this.isEmpty()) return null;

        while (entry != null) {
            //Идём с двух сторон по списку
            if (entry.Value == value)
                return entry;
            if (reversEntry.Value == value)
                return reversEntry;

            //Если указатели сошлись, то элемент не найден, выврлим null
            if (entry == reversEntry
                    || entry.nextEntry == reversEntry)
                return null;

            entry = entry.nextEntry;
            reversEntry = reversEntry.prevEntry;
        }
        return null;
    }

    @Override
    public V get(Object key) {
        return getEntryByKey(key).Value;
    }

    @Override
    public V put(K key, V value) {

        if (isEmpty()) {
            firstEntry = new Entry<K, V>(key, value, null, null);
            lastEntry = firstEntry;
            size++;
            return null;
        }

        Entry<K,V> val = getEntryByKey(key);

        if (val != null) {
            V oldValue = val.Value;
            val.Value = value;
            return oldValue;
        }

        Entry<K, V> entry = new Entry<K, V>(key, value, null, lastEntry);
        lastEntry.nextEntry = entry;
        lastEntry = entry;
        size++;
        return null;
    }

    private Entry<K,V> getEntryByKey(Object key) {
        if (isEmpty())
            return null;

        Entry<K,V> entry = firstEntry,
                reversEntry = lastEntry;

        while (firstEntry != null){
            if (entry.Key == key) return entry;
            if (reversEntry.Key == key) return reversEntry;

            if (entry == reversEntry
                    || entry.nextEntry == reversEntry)
                return null;

            entry = entry.nextEntry;
            reversEntry = reversEntry.prevEntry;
        }
        return null;
    }

    @Override
    public V remove(Object key) {

        if (isEmpty()) {
            return null;
        }

        Entry<K,V> entry = getEntryByKey(key);

        if (entry == null) { return null; }

        deleteEntry(entry);
        size--;
        return entry.Value;
    }

    private void deleteEntry(Entry<K,V> entry) throws NullPointerException {

        if (entry == firstEntry) {
            firstEntry = firstEntry.nextEntry;
            firstEntry.prevEntry = null;
            return;
        }
        if (entry == lastEntry) {
            lastEntry = lastEntry.prevEntry;
            lastEntry.nextEntry = null;
            return;
        }

        entry.nextEntry.prevEntry = entry.prevEntry;
        entry.prevEntry.nextEntry = entry.nextEntry;
        return;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        //TODO
    }

    @Override
    public void clear() {
        size = 0;
        firstEntry = lastEntry = null;
    }

    @Override
    public Set keySet() {
        Set<K> setofKey = new HashSet<>();
        Entry<K,V> entry = firstEntry;

        while (entry != null) {
            setofKey.add(entry.Key);
            entry = entry.nextEntry;
        }
        return setofKey;
    }

    @Override
    public Collection values() {

        List<V> listofValues = new ArrayList<>();
        Entry<K,V> entry = firstEntry;

        while (entry != null) {
            listofValues.add(entry.Value);
            entry = entry.nextEntry;
        }

        return listofValues;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return new EntrySet();
    }

    class EntrySet extends AbstractSet<Map.Entry<K, V>> {

        public int size() {
            return size;
        }
        public void clear() {this.clear(); }
        public Iterator<Map.Entry<K,V>> iterator() {
            return new EntryIterator();
        }
        public boolean contains(Object object) {
            if (!(object instanceof Entry))
                return false;

            Entry<K,V> entry = (Entry<K,V>) object;
            Entry<K,V> entryFromObject = getEntryByKey(((Entry<K,V>) object).getKey());
            return entryFromObject != null && entryFromObject.equals(entry);
        }

        public boolean remove(Object object) {
            if (!(object instanceof Entry)) {
                return false;
            }

            Entry<K,V> entry = (Entry<K,V>) object;
            Object key = ((Entry<K,V>) object).getKey();
            Entry<K,V> entryFromObject = getEntryByKey(key);

            if (entryFromObject != null) {
                deleteEntry(entryFromObject);
                size--;
                return true;
            }
            return false;
        }

        public void removeAll() {
            MapMe.this.clear();
        }
    }

    class EntryIterator implements Iterator<Map.Entry<K,V>> {

        Entry<K, V> entry, entryNext;

        EntryIterator() {
            entry = firstEntry;
            entryNext = entry.nextEntry;
        }

        @Override
        public boolean hasNext() {
            return entryNext == null? false : true;
        }

        @Override
        public Map.Entry<K,V> next() {
            entry = entryNext;
            entryNext = entryNext.nextEntry;
            return entry;
        }

        public void remove() {
            MapMe.this.deleteEntry(entry);
            size--;
            entry = entryNext;
        }
    }
}
