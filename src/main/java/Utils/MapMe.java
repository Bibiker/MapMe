package Utils;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class MapMe<K, V> extends  AbstractMap<K, V> {

    private int size;
    private Entry<K, V> firstEntry, lastEntry;
    private Set<K> keySet;
    private Collection<V> values;
    private EntrySet entrySet;

    private Comparator<? super K> comparator;
    private ElementOfTree rootOfTree;

    public MapMe() {
        this.size = 0;
        this.firstEntry = null;
        this.lastEntry = null;
        this.comparator = comparator;
    }

    public MapMe(Comparator<? super K> comparator) {
        this.comparator = comparator;
    }

    private class ElementOfTree {
        private MapMe.Entry<K, V> entry;
        ElementOfTree leftElement, rightElement;

        public MapMe.Entry<K, V> getEntry() {
            return entry;
        }

        public K getEntryKey() {
            return entry.getKey();
        }

        public boolean hasNextLeft() {
            return leftElement != null;
        }

        public boolean hasNextRight() {
            return rightElement != null;
        }

        public ElementOfTree getLeftElement() {
            if (this.hasNextLeft()) {
                return leftElement;
            }
            return null;
        }

        public ElementOfTree getRightElement() {
            if (this.hasNextRight()) {
                return rightElement;
            }
            return null;
        }
    }

    public Comparator<? super K> getComparator() {
        if (this.comparator == null) {
            this.comparator = (Comparator<? super K>) (e1, e2) -> {
                Comparable<? super K> k1 = (Comparable<? super K>) e1;
                return k1.compareTo(e2);
            };
        }
        return this.comparator;
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

    public Map.Entry<K, V> getFirstEntry() {
        return firstEntry;
    }

    public Map.Entry<K, V> getLastEntry() {
        return lastEntry;
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

        if (comparator == null) {
            getComparator();
        }

        boolean isLowerThanFirst = false;
        boolean isBiggerThanLast = false;
        if (comparator.compare(lastEntry.getKey(), key) < 0) {
            isBiggerThanLast = true;
        }

        if (comparator.compare(firstEntry.getKey(), key) > 0) {
            isLowerThanFirst = true;
        }

        if (isLowerThanFirst) {
            Entry<K, V> entry = new Entry<K, V>(key, value, firstEntry, null);
            firstEntry.prevEntry = entry;
            firstEntry = entry;
            size++;
            return null;
        } else if (isBiggerThanLast) {
            Entry<K, V> entry = new Entry<K, V>(key, value, null, lastEntry);
            lastEntry.nextEntry = entry;
            lastEntry = entry;
            size++;
            return null;
        } else {

            Entry<K, V> entry = getFirstOrNear(key);
            Entry<K, V> entryToAdd;

            if (entry.getKey() == key) {
                return entry.setValue(value);
            } else {
                int cmp = comparator.compare(entry.getKey(), key);
                if (cmp > 0) {
                    entryToAdd = new Entry<K, V>(key, value, entry, entry.prevEntry);
                    entry.prevEntry.nextEntry = entryToAdd;
                    entry.prevEntry = entryToAdd;
                    size++;
                    return null;
                }
                if (cmp < 0) {
                    entryToAdd = new Entry<K, V>(key, value, entry.nextEntry, entry);
                    entry.nextEntry.prevEntry = entryToAdd;
                    entry.nextEntry = entryToAdd;
                    size++;
                    return null;
                }
            }
        }
        return null;
    }

    private Entry<K, V> getFirstOrNear(K key) throws NullPointerException {
        if (key == null) { throw new NullPointerException(); }

        if (isEmpty()) {
            return null;
        }

        Entry<K, V> entry = firstEntry,
                reversEntry = lastEntry;

        while (entry != null){

            int cmp = comparator.compare(entry.getKey(), key);
            if (cmp == 0) {
                return entry;
            }

            if (cmp < 0) {
                entry = entry.nextEntry;
            } else if (cmp > 0) {
                return entry;
            }

            cmp = comparator.compare(reversEntry.getKey(), key);
            if (cmp == 0) {
                return reversEntry;
            }

            if (cmp > 0) {
                reversEntry = reversEntry.prevEntry;
            } else if (cmp < 0) {
                return reversEntry;
            }

        }
        return null;
    }

    private Entry<K, V> getEntryByKey(Object key) throws NullPointerException {
        if (key == null) { throw new NullPointerException(); }

        if (isEmpty()) {
            return null;
        }

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
            return new MySpliterator<>(Map.Entry::getKey, firstEntry, lastEntry, size);
        }
    }

    @Override
    public Collection<V> values() {
        if (values == null) {
            values = new ValuesCollection();
        }
        return values;
    }

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

        @Override
        public Spliterator<V> spliterator() {
            return new MySpliterator<>(Map.Entry::getValue, firstEntry, lastEntry, size);
        }
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        if (entrySet == null) {
            entrySet = new EntrySet();
        }
        return entrySet;
    }


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

    @Override
    public Spliterator<Map.Entry<K, V>> spliterator() {
        return new MySpliterator<>((a) -> a, firstEntry, lastEntry, size);
    }

    public boolean removeAll(Collection<?> collectionToRemove) {

            //(optionaly)

            return false;
        }
    }

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
                            entryOfBegin,
                            entryOfEnd;
        private int size;
        private Function<Map.Entry<K, V>, S> mapper;

        MySpliterator(Function<Map.Entry<K, V>, S> mapper,
                      Entry<K, V> entryOfBegin,
                      Entry<K, V> entryOfEnd,
                      int size) {
            this.mapper = mapper;
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

            if (currentEntry == entryOfEnd.nextEntry || currentEntry == null) {
                return false;
            }

            action.accept(mapper.apply(currentEntry));
            currentEntry = currentEntry.nextEntry;
            return true;
        }

        @Override
        public Spliterator<S> trySplit() {
            if (entryOfEnd.nextEntry == null) { return null; }
            if (entryOfBegin == entryOfEnd || entryOfBegin.nextEntry == entryOfEnd) {
                return null;
            }
            int halfSize = size >>> 1;

            if (halfSize <= 1) {
                return null;
            }

            Entry<K, V> curr = entryOfBegin;
            for (int i = 0; i < halfSize - 1; i++) {
                curr = curr.nextEntry;
            }

            if (size % 2 == 1) halfSize++;
            Entry<K, V> oldEntryOfBegin = entryOfBegin;
            entryOfBegin = curr.nextEntry;

            return new MySpliterator<S>(mapper, oldEntryOfBegin, curr, halfSize);
        }

        @Override
        public long estimateSize() {
            return size;
        }

    }
}
