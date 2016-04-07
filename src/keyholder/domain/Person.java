package keyholder.domain;

import java.util.ArrayList;
import keyholder.db.KeyMapper;
import keyholder.db.PersonMapper;
import keyholder.domain.Domain;
import keyholder.domain.Key;

public class Person extends Domain implements Comparable {
    private long cardNumber;
    private String name;
    private String surName;
    private String middleName;
    private String status;
    private String workPhone;
    private ArrayList<Key> keys;
    private String photoFile;
    public static final Person GUARDS = new Person(0L, 0L, "охрана", "-", "-", (String)null, (String)null, (String)null);

    public Person(long var1, long var3, String var5, String var6, String var7, String var8, String var9, String var10) {
        super(var1);
        this.cardNumber = this.cardNumber;
        this.name = var5;
        this.surName = var6;
        this.middleName = var7;
        this.status = var8;
        this.workPhone = var9;
        this.photoFile = var10;
    }

    public long getCardNumber() {
        return this.cardNumber;
    }

    public String getName() {
        return this.name;
    }

    public String getSurName() {
        return this.surName;
    }

    public String getMiddleName() {
        return this.middleName;
    }

    public String getStatus() {
        return this.status;
    }

    public String getWorkPhone() {
        return this.workPhone;
    }

    public String getPhotoFile() {
        return this.photoFile;
    }

    public ArrayList<Key> getKeys() {
        return KeyMapper.findForPerson(this.getID());
    }

    public String getFIO() {
        return this.getSurName() + " " + this.getName() + " " + this.getMiddleName();
    }

    public String toString() {
        return this.getFIO();
    }

    public boolean addKeyPermit(Key var1) {
        return this.getKeys().contains(var1)?false:KeyMapper.insertKeyPermit(this.getID(), var1.getID());
    }

    public boolean removeKeyPermit(Key var1) {
        return !this.getKeys().contains(var1)?false:KeyMapper.deleteKeyPermit(this.getID(), var1.getID());
    }

    public static ArrayList<Person> findAll() {
        return PersonMapper.findAllPersons();
    }

    public static Person find(long var0) {
        return PersonMapper.findPerson(var0);
    }

    static ArrayList findPersonsWithKeyPermit(Key var0) {
        return PersonMapper.findPersonsWithKeyPermit(var0.getID());
    }

    public int compareTo(Object var1) {
        if(!(var1 instanceof Person)) {
            return -1;
        } else {
            Person var2 = (Person)var1;
            return this.surName.compareTo(var2.surName);
        }
    }
}

