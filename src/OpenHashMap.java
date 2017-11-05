
public class OpenHashMap {

    private int capacity;
    static final float LOAD_FACTOR = 0.75f;
    private int threshold;

    private int size;
    private Entry[] entries;

    public OpenHashMap() {
        this.capacity = 10;
        this.threshold = (int)(this.capacity * LOAD_FACTOR);
        this.entries = new Entry[this.capacity];
    }

    public void put(int key, long value){
        Entry entry = new Entry(key, value);
        int index = getIndex(key);
        if (entries[index] != null){
            index = findEmptyCell(index, key);
        }

        entries[index] = entry;
        size++;
        if (size == threshold || entries[entries.length - 1] != null){
            rebuildTable();
        }
    }

    public Long get(int key) {
        Long result = null;
        int index = getIndex(key);

        try {
            int foundKey = entries[index].getKey();
            while (foundKey != key) {
                index++;
                foundKey = entries[index].getKey();
            }
            result = entries[index].getValue();
        } catch (NullPointerException e){}

        return result;
    }


    public int size() {
        return size;
    }

    private int getIndex(int key){
        int index = key % capacity;
        if (index < 0){
            index *= -1;
        }
        return index;
    }

    private int findEmptyCell(int start, int key){
        int index = -1;
        for (int i = start; i < entries.length; i++) {
            if (entries[i] == null || entries[i].getKey() == key){
                index = i;
                break;
            }
        }
        return index;
    }

    private void rebuildTable(){
        size = 0;
        capacity = (int) (capacity * 1.5f);
        threshold = (int)(capacity * LOAD_FACTOR);
        Entry[] old = entries;
        entries = new Entry[capacity];
        for (int i = 0; i < old.length; i++) {
            if (old[i] != null){
                put(old[i].getKey(), old[i].getValue());
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < entries.length; i++) {
            if (entries[i] != null){
                sb.append(entries[i] + ", ");
            }
        }
        if (sb.length() > 0){
            sb.delete(sb.length() - 2, sb.length());
        }
        return "{" + sb + "}";
    }
}
