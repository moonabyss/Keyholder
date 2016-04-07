package keyholder.db;

import keyholder.db.SoftHashMap;
import keyholder.domain.Key;
import keyholder.domain.KeyMovement;
import keyholder.domain.Person;

public class Registry<TYPE> {
    private static SoftHashMap personLoadedMap = new SoftHashMap();
    private static SoftHashMap keyLoadedMap = new SoftHashMap();
    private static SoftHashMap keyMovementLoadedMap = new SoftHashMap();

    public Registry() {
    }

    public static Person getPerson(long var0) {
        return (Person)personLoadedMap.get(Long.valueOf(var0));
    }

    public static Key getKey(long var0) {
        return (Key)keyLoadedMap.get(Long.valueOf(var0));
    }

    public static KeyMovement getKeyMovement(long var0) {
        return (KeyMovement)keyMovementLoadedMap.get(Long.valueOf(var0));
    }

    public static void put(Person var0) {
        personLoadedMap.put(new Long(var0.getID()), var0);
    }

    public static void put(Key var0) {
        keyLoadedMap.put(new Long(var0.getID()), var0);
    }

    public static void put(KeyMovement var0) {
        keyMovementLoadedMap.put(new Long(var0.getID()), var0);
    }

    public static void remove(Person var0) {
        personLoadedMap.remove(new Long(var0.getID()));
    }

    public static void remove(Key var0) {
        keyLoadedMap.remove(new Long(var0.getID()));
    }
}

