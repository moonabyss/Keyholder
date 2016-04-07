package keyholder.db;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class SoftHashMap extends AbstractMap {
    private final Map hash;
    private final int HARD_SIZE;
    private final LinkedList hardCache;
    private static final ReferenceQueue queue = new ReferenceQueue();

    public SoftHashMap() {
        this(100);
    }

    public SoftHashMap(int var1) {
        this.hash = new HashMap();
        this.hardCache = new LinkedList();
        this.HARD_SIZE = var1;
    }

    public Object get(Object var1) {
        Object var2 = null;
        SoftReference var3 = (SoftReference)this.hash.get(var1);
        if(var3 != null) {
            var2 = var3.get();
            if(var2 == null) {
                this.hash.remove(var1);
            } else {
                this.hardCache.addFirst(var2);
                if(this.hardCache.size() > this.HARD_SIZE) {
                    this.hardCache.removeLast();
                }
            }
        }

        return var2;
    }

    public void processQueue() {
        SoftHashMap.SoftValue var1;
        while((var1 = (SoftHashMap.SoftValue)queue.poll()) != null) {
            this.hash.remove(var1.key);
        }

    }

    public Object put(Object var1, Object var2) {
        this.processQueue();
        return this.hash.put(var1, new SoftHashMap.SoftValue(var2, var1, queue));
    }

    public Object remove(Object var1) {
        this.processQueue();
        return this.hash.remove(var1);
    }

    public void clear() {
        this.hardCache.clear();
        this.processQueue();
        this.hash.clear();
    }

    public int size() {
        this.processQueue();
        return this.hash.size();
    }

    public Set entrySet() {
        throw new UnsupportedOperationException();
    }

    private static class SoftValue extends SoftReference {
        private final Object key;

        private SoftValue(Object var1, Object var2, ReferenceQueue var3) {
            super(var1, var3);
            this.key = var2;
        }
    }
}

