package Utils;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class MapMe<K, V> implements Map<K, V> {

    private int size;
    private Entry<K, V> firstEntry, lastEntry;
    private Set<K> keySet;
    private Collection<V> values;
    private EntrySet entrySet;

    public MapMe() {
        this.size = 0;
        this.firstEntry = null;
        this.lastEntry = null;
    }

    static class Entry<K,V> implements Map.Entry<K,V> {

        private K Key;
        private V Value;
        Entry<K, V> nextEntry, prevEntry;

        public Entry() {
            this(null, null, null, null);
        }

        Entry (K key, V value, Entry<K, V> nextEntry, Entry<K,V> prevEntry) {
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
        public V setValue(V value) {
            V oldval = Value;
            Value = value;
            return oldval;
        }

        @Override
        public boolean equals(Object object) {
            return super.equals(object);
        }
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public boolean containsKey(Object key) {
        return (getEntryByKey(key) != null);
    }

    @Override
    public boolean containsValue(Object value) {
        return (getEntryByValue(value) != null);
    }

    private Entry<K, V> getEntryByValue(Object value) {
        Entry<K, V> entry = firstEntry,
            reversEntry = lastEntry;

        if (this.isEmpty()) return null;

        while (entry != null) {
            //Идём с двух сторон по списку
            if (entry.getValue() == value)
                return entry;
            if (reversEntry.getValue() == value)
                return reversEntry;

            //Если указатели сошлись, то элемент не найден, выврлим null
            if (entry.equals(reversEntry)
                    || entry.nextEntry.equals(reversEntry))
                return null;

            entry = entry.nextEntry;
            reversEntry = reversEntry.prevEntry;
        }
        return null;
    }

    @Override
    public V get(Object key) {
        Entry<K, V> entry = getEntryByKey(key);
        return entry != null ? entry.Value : null;
    }

    @Override
    public V put(K key, V value) throws NullPointerException{
        if (key == null) { throw new NullPointerException(); }

        if (isEmpty()) {
            firstEntry = new Entry<K, V>(key, value, null, null);
            lastEntry = firstEntry;
            size++;
            return null;
        }

        Entry<K, V> val = getEntryByKey(key);

        if (val != null) {
            V oldValue = val.getValue();
            val.setValue(value);
            return oldValue;
        }

        Entry<K, V> entry = new Entry<K, V>(key, value, null, lastEntry);
        lastEntry.nextEntry = entry;
        lastEntry = entry;
        size++;
        return null;
    }

    private Entry<K, V> getEntryByKey(Object key) throws NullPointerException {
        if (key == null) { throw new NullPointerException(); }

        if (isEmpty())
            return null;

        Entry<K, V> entry = firstEntry,
                reversEntry = lastEntry;

        while (entry != null){
            if (entry.getKey().equals(key))
                return entry;
            if (reversEntry.getKey().equals(key))
                return reversEntry;

            if (entry.equals(reversEntry)
                    || entry.nextEntry.equals(reversEntry))
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

        Entry<K, V> entry = getEntryByKey(key);

        if (entry == null) {
            return null;
        }

        deleteEntry(entry);
        size--;
        return entry.getValue();
    }

    private void deleteEntry(Entry<K, V> entry) throws NullPointerException {

        if (entry.equals(firstEntry)) {
            firstEntry = firstEntry.nextEntry;
            firstEntry.prevEntry = null;
            return;
        }
        if (entry.equals(lastEntry)) {
            lastEntry = lastEntry.prevEntry;
            lastEntry.nextEntry = null;
            return;
        }

        entry.nextEntry.prevEntry = entry.prevEntry;
        entry.prevEntry.nextEntry = entry.nextEntry;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) throws NullPointerException{
        if (m == null) { throw new NullPointerException(); }
        if(m == this || m.isEmpty()) { return; }

        for(Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        size = 0;
        firstEntry = lastEntry = null;
        values = null;
        keySet = null;
        entrySet = null;
    }

    @Override
    public Set<K> keySet() {
        if (keySet == null) {
            keySet = new KeySet();
        }
        return keySet;
    }

/////////////////////////////////////////////////////////////////////
    class KeySet extends AbstractSet<K> {

        @Override
        public int size() {
            return size;
        }

        @Override
        public void clear() {
            MapMe.this.clear();
        }

        @Override
        public Iterator<K> iterator() {
            return new KeyIterator();
        }

        @Override
        public final boolean contains(Object object) {
            return containsKey(object);
        }

        @Override
        public final boolean remove(Object key) {
            return MapMe.this.remove(key) != null;
        }

        @Override
        public final Spliterator<K> spliterator() {
            //TO DO!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            return null;
        }
    }
/////////////////////////////////////////////////////////////////////
    @Override
    public Collection<V> values() {
        if (values == null) {
            values = new ValuesCollection();
        }
        return values;
    }

/////////////////////////////////////////////////////////////////////
    class ValuesCollection extends AbstractCollection<V> {

        @Override
        public Iterator<V> iterator() {
            return new ValueIterator();
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public void clear() {
            MapMe.this.clear();
        }

        @Override
        public boolean contains(Object object) {
            return containsValue(object);
        }

        @Override
        public boolean remove(Object object) {
            Entry<K, V> objectToDelete = getEntryByValue(object);
            if (objectToDelete != null) {
                deleteEntry(objectToDelete);
                size--;
                return true;
            }
            return false;
        }

        //TO DO SPLITERATOR!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    }
/////////////////////////////////////////////////////////////////////
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        if (entrySet == null) {
            entrySet = new EntrySet();
        }
        return entrySet;
    }

/////////////////////////////////////////////////////////////////////
    class EntrySet extends AbstractSet<Map.Entry<K, V>> {

        public int size() {
            return size;
        }
        public void clear() {MapMe.this.clear(); }
        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator();
        }
        public boolean contains(Object object) {
            if (!(object instanceof Map.Entry))
                return false;

            Entry<K,V> entry = (Entry<K,V>) object;
            Entry<K,V> entryFromObject = getEntryByKey(((Entry<K,V>) object).getKey());
            return entryFromObject != null && entryFromObject.equals(entry);
        }

        public boolean remove(Object object) {
            if (!(object instanceof Map.Entry)) {
                return false;
            }

            K key = ((Map.Entry<K, V>) object).getKey();
            V value = ((Map.Entry<K, V>) object).getValue();
            Entry<K, V> entryFromObject = getEntryByKey(key);

            if (entryFromObject != null && entryFromObject.getValue() == value) {
                deleteEntry(entryFromObject);
                size--;
                return true;
            }
            return false;
        }

        //TO DO SPLITERATOR!!!!!!!!!!!!!!!!!!!!

        public boolean removeAll(Collection<?> collectionToRemove) {

            //TODO (optionaly)

            return false;
        }
    }
/////////////////////////////////////////////////////////////////////

    abstract class MyIterator  {

        Entry<K, V> entry, entryNext;

        MyIterator() {
            entry = null;
            entryNext = firstEntry;
        }

        public boolean hasNext() {
            return entryNext != null;
        }

        public Map.Entry<K,V> nextEntry() throws NoSuchElementException {

            if (!hasNext()) {
                throw new NoSuchElementException();
            }

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

    class EntryIterator extends MyIterator implements Iterator<Map.Entry<K, V>> {
        public Map.Entry<K, V> next() {
            return nextEntry();
        }
    }

    class KeyIterator extends MyIterator implements Iterator<K> {
        public K next() {
            return nextEntry().getKey();
        }
    }

    class ValueIterator extends MyIterator implements Iterator<V> {
        public V next() {
            return nextEntry().getValue();
        }
    }

/////SPLITERATOR

    class MySpliterator<S> implements Spliterator<S> {

        private Entry<K, V> currentEntry,
                           // lastOfPrevious,
                            entryOfBegin,
                            entryOfEnd;
        private int size;
        private Function<Map.Entry<K, V>, S> mapper;

        MySpliterator(//Entry<K, V> lastOfPrevious,
                      Entry<K, V> entryOfBegin,
                      Entry<K, V> entryOfEnd,
                      int size) {
            //this.lastOfPrevious = lastOfPrevious;
            this.entryOfBegin = currentEntry = entryOfBegin;
            this.entryOfEnd = entryOfEnd;
            this.size = size;
        }

        @Override
        public int characteristics() {
            return (SIZED | SUBSIZED | ORDERED);
        }

        @Override
        public boolean tryAdvance(Consumer<? super S> action) {
            if (action == null) {
                throw new NullPointerException();
            }

            if (currentEntry == entryOfEnd || currentEntry == null) {
                return false;
            }

            action.accept(mapper.apply(currentEntry));
            currentEntry = currentEntry.nextEntry;
            return true;
        }

        @Override
        public Spliterator<S> trySplit() {
            if (entryOfBegin == entryOfEnd || entryOfBegin.nextEntry == entryOfEnd) {
                return null;
            }
            int halfSize = size >>> 1;
            if (size % 2 == 1) halfSize++;

            if (halfSize <= 1) {
                return null;
            }

            Entry<K, V> curr = entryOfBegin;
            for (int i = 0; i < size; i++) {
                curr = curr.nextEntry;
            }

            Entry<K, V> oldEntryOfBegin = entryOfBegin;
            entryOfBegin = curr.nextEntry;

            return new MySpliterator<S>(oldEntryOfBegin, curr, halfSize);
        }

        @Override
        public long estimateSize() {
            return size;
        }

    }
}
