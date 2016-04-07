package keyholder.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import keyholder.db.KeyMovementMapper;
import keyholder.domain.Domain;
import keyholder.domain.Key;

public class KeyMovement extends Domain {
    private Key key;
    private String person;
    private String prevPerson;
    private short event;
    private Timestamp time;
    public static final short HAND_EVNT = 0;
    public static final short HANDIN_EVNT = 1;
    public static final short HANDOVER_EVNT = 2;

    public KeyMovement(long var1, Key var3, String var4, String var5, short var6, Timestamp var7) {
        super(var1);
        this.key = var3;
        this.person = var4;
        this.prevPerson = var5;
        this.event = var6;
        this.time = var7;
    }

    public Key getKey() {
        return this.key;
    }

    public String getPerson() {
        return this.person;
    }

    public String getPrevPerson() {
        return this.prevPerson;
    }

    public String getEvent() {
        switch(this.event) {
            case 0:
                return "выдан";
            case 1:
                return "сдан";
            case 2:
                return "передан";
            default:
                return "ошибка";
        }
    }

    public Timestamp getTime() {
        return this.time;
    }

    public short getEventNumber() {
        return this.event;
    }

    public static ArrayList<KeyMovement> findAll() {
        return KeyMovementMapper.findAll();
    }

    public static boolean deleteAllLater(int var0) {
        return KeyMovementMapper.deleteAllLater(var0);
    }
}

