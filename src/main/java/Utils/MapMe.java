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
    private MapMe.Entry<K, V> entryOfEnd, entryOfBegin;
    private int range;
    private final int MAX_RANGE = 5000;

    public MapMe() {
        this(false);
    }

    public MapMe(Comparator<? super K> comparator) {
        this(true);
        this.comparator = comparator;
    }

    private MapMe(boolean isComparator) {
        this.size = 0;
        this.firstEntry = null;
        this.lastEntry = null;
        this.range = -2;
        this.rootOfTree = null;
        if (!isComparator) {
            this.comparator = createComparator();
        }
    }

    private class ElementOfTree {
        private MapMe.Entry<K, V> entry;
        private ElementOfTree leftElement, rightElement;
        private final boolean isZeroLevel;
        private final int indexOfElement;

        public ElementOfTree() {
            this(null, null, null, false);
        }

        ElementOfTree(MapMe.Entry<K, V> entry,
                      ElementOfTree leftElement,
                      ElementOfTree rightElement,
                      boolean isZeroLevel) {
            this.entry = entry;
            this.leftElement = leftElement;
            this.rightElement = rightElement;
            this.isZeroLevel = isZeroLevel;
            this.indexOfElement = size / MAX_RANGE;
        }

        int getIndexOfElement() {
            return indexOfElement;
        }

        void setEntry(MapMe.Entry<K, V> entry) {
            this.entry = entry;
        }

        void setRightElement(ElementOfTree element) {
            this.rightElement = element;
        }

        public void setLeftElement(ElementOfTree element) {
            this.leftElement = element;
        }

        MapMe.Entry<K, V> getEntry() {
            return entry;
        }

        boolean isZeroLevel() {
            return isZeroLevel;
        }

        K getEntryKey() {
            return entry.getKey();
        }

        boolean hasNextLeft() {
            return leftElement != null;
        }

        boolean hasNextRight() {
            return rightElement != null;
        }

        ElementOfTree getLeftElement() {
            if (this.hasNextLeft()) {
                return leftElement;
            }
            return null;
        }

        ElementOfTree getRightElement() {
            return rightElement;
        }
    }

    private void putElementToTree(MapMe.Entry<K, V> entry) {

        if (rootOfTree == null) {
            rootOfTree = new ElementOfTree(entry, null, null, true);
            return;
        }

        ElementOfTree currentElement = rootOfTree;
        ElementOfTree createdElement;

        if (rootOfTree.isZeroLevel()) {
            createdElement = new ElementOfTree(entry, rootOfTree, null, false);
            rootOfTree = createdElement;
            return;
        }

        while (currentElement != null) {

            if (!currentElement.hasNextRight()
                    && !currentElement.isZeroLevel()) {
                createdElement = new ElementOfTree(entry, null, null, true);
                currentElement.setRightElement(createdElement);
                return;
            }

            if (isFull(currentElement.getRightElement())) {
                createdElement = new ElementOfTree(entry, currentElement.getRightElement(), null, false);
                currentElement.setRightElement(createdElement);
                return;
            }

            currentElement = currentElement.getRightElement();
        }
    }

    private void removeLastElementFromTree() {

        if (rootOfTree == null) {
            return;
        }

        if (!rootOfTree.hasNextRight()) {
            rootOfTree = rootOfTree.getLeftElement();
        }

        ElementOfTree currentElement = rootOfTree;

        while (currentElement.getRightElement().hasNextRight()) {
            currentElement = currentElement.getRightElement();
        }

        if (currentElement.getRightElement().hasNextLeft()) {
            currentElement.setRightElement(currentElement.getRightElement().getLeftElement());
            return;
        }

        currentElement.setRightElement(null);
    }

    private void getNearestEntriesFromTree(K key) {

        if (rootOfTree == null) {
            entryOfBegin = firstEntry;
            entryOfEnd = lastEntry;
            return;
        }

        ElementOfTree currentEntry = rootOfTree;

        entryOfBegin = entryOfEnd = null;

        while (currentEntry != null) {
            try {
                if (comparator.compare(key, currentEntry.getEntryKey()) >= 1) {
                    entryOfBegin = currentEntry.getEntry();
                    currentEntry = currentEntry.getRightElement();
                } else {
                    entryOfEnd = currentEntry.getEntry();
                    currentEntry = currentEntry.getLeftElement();
                }
            } catch (NullPointerException ex) {
                return;
            }
        }

        if (entryOfBegin == null) {
            entryOfBegin = firstEntry;
        }

        if (entryOfEnd == null) {
            entryOfEnd = lastEntry;
        }
    }

    private boolean isFull(ElementOfTree element) {
        while (element.hasNextRight()) {
            element = element.getRightElement();
        }
        return element.isZeroLevel();
    }

    public Comparator<? super K> createComparator() {
        if (this.comparator == null) {
            this.comparator = (Comparator<? super K>) (e1, e2) -> {
                Comparable<? super K> k1 = (Comparable<? super K>) e1;
                return k1.compareTo(e2);
            };
        }
        return this.comparator;
    }

    static class Entry<K, V> implements Map.Entry<K, V> {

        private K Key;
        private V Value;
        Entry<K, V> nextEntry, prevEntry;

        public Entry() {
            this(null, null, null, null);
        }

        Entry(K key, V value, Entry<K, V> nextEntry, Entry<K, V> prevEntry) {
            this.Key = key;
            this.Value = value;
            this.nextEntry = nextEntry;
            this.prevEntry = prevEntry;
        }

        @Override
        public K getKey() {
            return Key;
        }

        @Override
        public final V getValue() {
            return Value;
        }

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

    private Map.Entry<K, V> getFirstEntry() {
        return firstEntry;
    }

    private Map.Entry<K, V> getLastEntry() {
        return lastEntry;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

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
    public V put(K key, V value) throws NullPointerException {
        if (key == null) {
            throw new NullPointerException();
        }

        if (isEmpty()) {
            firstEntry = new Entry<K, V>(key, value, null, null);
            lastEntry = firstEntry;
            size++;
            range++;
            return null;
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
            range++;
        } else if (isBiggerThanLast) {
            Entry<K, V> entry = new Entry<K, V>(key, value, null, lastEntry);
            lastEntry.nextEntry = entry;
            lastEntry = entry;
            size++;
            range++;
        } else {
            Entry<K, V> entry = getFirstOrNearest(key);
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

                    shiftToSide(key, true, rootOfTree);
                    range++;
                    reSizeTree();

                    return null;
                }
                if (cmp < 0) {
                    entryToAdd = new Entry<K, V>(key, value, entry.nextEntry, entry);
                    entry.nextEntry.prevEntry = entryToAdd;
                    entry.nextEntry = entryToAdd;
                    size++;

                    shiftToSide(key, true, rootOfTree);
                    range++;
                    reSizeTree();

                    return null;
                }
            }
        }
        return null;
    }

    private void shiftToSide(K key, boolean isLeftShift, ElementOfTree element) {
        if (element == null) {
            return;
        }
        if (element.hasNextLeft()) {
            shiftToSide(key, isLeftShift, element.getLeftElement());
        }

        if (element.hasNextRight()) {
            shiftToSide(key, isLeftShift, element.getRightElement());
        }

        if (comparator.compare((element.getEntryKey()), key) >= 1) {
            if (isLeftShift) {
                element.setEntry(element.getEntry().prevEntry);
            } else {
                element.setEntry(element.getEntry().nextEntry);
            }
        }
    }

    private void reSizeTree() {

        if (range == MAX_RANGE + 1) {
            putElementToTree(lastEntry.prevEntry);
            range = 0;
        }

        if (range < 0) {
            removeLastElementFromTree();
            range = MAX_RANGE;
        }
    }

    private Entry<K, V> getFirstOrNearest(K key) throws NullPointerException {
        if (key == null) {
            throw new NullPointerException();
        }

        if (isEmpty()) {
            return null;
        }

        getNearestEntriesFromTree(key);

        Entry<K, V> entry = entryOfBegin,
                reversEntry = entryOfEnd;

        while (entry != null) {

            int cmp = comparator.compare(entry.getKey(), key);
            if (cmp == 0) {
                return entry;
            }

            if (cmp < 0) {
                entry = entry.nextEntry;
            } else {
                return entry;
            }

            if (entry.nextEntry == reversEntry) {
                continue;
            }
            if (entry.prevEntry == reversEntry) {
                return null;
            }

            cmp = comparator.compare(reversEntry.getKey(), key);
            if (cmp == 0) {
                return reversEntry;
            }

            if (cmp > 0) {
                reversEntry = reversEntry.prevEntry;
            } else {
                return reversEntry;
            }

        }
        return null;
    }

    private Entry<K, V> getEntryByKey(Object key) {

        MapMe.Entry<K, V> foundedEntry = getFirstOrNearest((K) key);

        if (foundedEntry.getKey() == (K) key) {
            return foundedEntry;
        } else {
            return null;
        }
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
        return entry.getValue();
    }

    private void deleteEntry(Entry<K, V> entry) throws NullPointerException {
        if (entry == null) {
            throw new NullPointerException();
        }

        if (entry.equals(firstEntry)) {
            firstEntry = firstEntry.nextEntry;
            firstEntry.prevEntry = null;
            size--;
            return;
        }
        if (entry.equals(lastEntry)) {
            lastEntry = lastEntry.prevEntry;
            lastEntry.nextEntry = null;
            size--;
            return;
        }

        entry.nextEntry.prevEntry = entry.prevEntry;
        entry.prevEntry.nextEntry = entry.nextEntry;
        size--;
        shiftToSide(entry.getKey(), false, rootOfTree);
        reSizeTree();
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) throws NullPointerException {
        if (m == null) {
            throw new NullPointerException();
        }
        if (m == this || m.isEmpty()) {
            return;
        }

        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
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
        rootOfTree = null;
        entryOfEnd = null;
        entryOfBegin = null;
        range = -2;
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

        public void clear() {
            MapMe.this.clear();
        }

        public Iterator<Map.Entry<K, V>> iterator() {
            return new EntryIterator();
        }

        public boolean contains(Object object) {
            if (!(object instanceof Map.Entry))
                return false;

            Entry<K, V> entry = (Entry<K, V>) object;
            Entry<K, V> entryFromObject = getEntryByKey(((Entry<K, V>) object).getKey());
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
                return true;
            }
            return false;
        }

        @Override
        public MySpliterator<Map.Entry<K, V>> spliterator() {
            return new MySpliterator<>((el) -> el, firstEntry, lastEntry, size);
        }

        public boolean removeAll(Collection<?> collectionToRemove) {

            //(optionaly)

            return false;
        }
    }

    abstract class MyIterator {

        Entry<K, V> entry, entryNext;

        MyIterator() {
            entry = null;
            entryNext = firstEntry;
        }

        public boolean hasNext() {
            return entryNext != null;
        }

        public Map.Entry<K, V> nextEntry() throws NoSuchElementException {

            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            entry = entryNext;
            entryNext = entryNext.nextEntry;
            return entry;
        }

        public void remove() {
            MapMe.this.deleteEntry(entry);
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

    private int getNearestEntriesByIndex(int indexToSearch) {
        if (rootOfTree == null) {
            return -1;
        }

        ElementOfTree currentEntry = rootOfTree;

        entryOfBegin = entryOfEnd = null;
        int indexOfBegin = -1;

        while (currentEntry != null) {
            if (Integer.compare(indexToSearch, currentEntry.getIndexOfElement()) >= 1) {
                entryOfBegin = currentEntry.getEntry();
                indexOfBegin = currentEntry.indexOfElement;
                currentEntry = currentEntry.getRightElement();
            } else {
                entryOfEnd = currentEntry.getEntry();
                currentEntry = currentEntry.getLeftElement();
            }
        }

        if (entryOfBegin == null) {
            entryOfBegin = firstEntry;
            return 0;
        }

        if (entryOfEnd == null) {
            entryOfEnd = lastEntry;
        }

        return indexOfBegin;
    }

/////SPLITERATOR

    class MySpliterator<S> implements Spliterator<S> {

        private MapMe.Entry<K, V> currentEntry,
                firstEntrySplit,
                lastEntrySplit;
        private int size;
        private Function<Map.Entry<K, V>, S> mapper;

        MySpliterator() {
            this.mapper = null;
            this.firstEntrySplit = currentEntry = null;
            this.lastEntrySplit = null;
            this.size = -1;
        }

        MySpliterator(Function<Map.Entry<K, V>, S> mapper,
                      MapMe.Entry<K, V> firstEntrySplit,
                      MapMe.Entry<K, V> lastEntrySplit,
                      int size) {
            this.mapper = mapper;
            this.firstEntrySplit = currentEntry = firstEntrySplit;
            this.lastEntrySplit = lastEntrySplit;
            this.size = size;
        }

        @Override
        public int characteristics() {
            return (SORTED | SIZED | SUBSIZED | ORDERED);
        }

        @Override
        public boolean tryAdvance(Consumer<? super S> action) {
            if (action == null) {
                throw new NullPointerException();
            }

            if (currentEntry == lastEntrySplit.nextEntry || currentEntry == null) {
                return false;
            }

            action.accept(mapper.apply(currentEntry));
            currentEntry = currentEntry.nextEntry;
            return true;
        }

        @Override
        public long estimateSize() {
            return size;
        }

        @Override
        public Comparator<? super S> getComparator() {
            return (Comparator<? super S>) (e1, e2) -> {
                Comparable<? super S> k1 = (Comparable<? super S>) e1;
                return k1.compareTo(e2);
            };
        }

        @Override
        public MySpliterator<S> trySplit() {
            if (lastEntrySplit.nextEntry == null) {
                return null;
            }
            if (firstEntrySplit == lastEntrySplit || firstEntrySplit.nextEntry == lastEntrySplit) {
                return null;
            }
            int halfSize = size >>> 1;

            if (halfSize <= 1) {
                return null;
            }

            MapMe.Entry<K, V> currEntry = (Entry<K, V>) entryOfBegin;

            int indexOfBegin = getNearestEntriesByIndex(halfSize);

            for (int i = 0; i < halfSize - indexOfBegin; i++) {
                currEntry = currEntry.nextEntry;
            }

            if (size % 2 == 1) halfSize++;
            Entry<K, V> oldEntryOfBegin = firstEntrySplit;
            firstEntrySplit = currEntry.nextEntry;

            return new MySpliterator<S>(mapper, oldEntryOfBegin, currEntry, halfSize);
        }

    }

}