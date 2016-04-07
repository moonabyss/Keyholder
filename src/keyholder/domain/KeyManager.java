package keyholder.domain;

import java.util.ArrayList;
import keyholder.db.KeyMapper;
import keyholder.db.KeyMovementMapper;
import keyholder.domain.Key;
import keyholder.domain.Person;

public class KeyManager {
    public KeyManager() {
    }

    public static boolean handKey(Key var0, Person var1) {
        if(var0 != null && var1 != null) {
            if(var0.getPerson() != Person.GUARDS) {
                return false;
            } else if(KeyMapper.updateKey(var0, var1)) {
                var0.setPerson(var1);
                KeyMovementMapper.insert(var0.getID(), var1.getFIO(), "", (short)0);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean handInKey(Key var0, Person var1) {
        if(var0 != null && var1 != Person.GUARDS) {
            if(var0.getPerson() != var1) {
                return false;
            } else if(KeyMapper.updateKey(var0, (Person)null)) {
                var0.setPerson((Person)null);
                KeyMovementMapper.insert(var0.getID(), var1.getFIO(), "", (short)1);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean handOverKey(Key var0, Person var1) {
        if(var0 != null && var1 != null) {
            if(var0.getPerson() == Person.GUARDS) {
                return false;
            } else if(var0.getPerson() == var1) {
                return false;
            } else if(KeyMapper.updateKey(var0, var1)) {
                KeyMovementMapper.insert(var0.getID(), var1.getFIO(), var0.getPerson().getFIO(), (short)2);
                var0.setPerson(var1);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static ArrayList<Key> selectHandedKey() {
        return KeyMapper.findKeysWhere("NOT PersonID IS NULL");
    }

    public static ArrayList<Key> selectFreeKey() {
        return KeyMapper.findKeysWhere("PersonID IS NULL");
    }

    public static void reloadKeys() {
        KeyMapper.reloadAll();
    }
}

