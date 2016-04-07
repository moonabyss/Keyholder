package keyholder.domain;

import java.util.ArrayList;
import keyholder.db.KeyMapper;
import keyholder.domain.Domain;
import keyholder.domain.Person;

public class Key extends Domain implements Comparable {
    public String number;
    public Person person;

    public Key(String var1) {
        super(0L);
        this.number = var1;
    }

    public Key(long var1, String var3, Person var4) {
        super(var1);
        this.number = var3;
        this.person = var4;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String var1) {
        if(!this.number.equals(var1)) {
            if(KeyMapper.updateKeyNumber(this, var1)) {
                String var2 = this.number;
                this.number = var1;
                this.firePropertyChange("number", var2, var1);
            }

        }
    }

    public Person getPerson() {
        return this.person == null?Person.GUARDS:this.person;
    }

    public void setPerson(Person var1) {
        this.person = var1;
    }

    public String toString() {
        return this.getNumber();
    }

    public boolean delete() {
        ArrayList var1 = Person.findPersonsWithKeyPermit(this);
        if(!KeyMapper.deleteKey(this)) {
            return false;
        } else {
            for(int var2 = 0; var2 < var1.size(); ++var2) {
                ((Person)var1.get(var2)).getKeys().remove(this);
            }

            return true;
        }
    }

    public boolean insert() {
        return KeyMapper.insertKey(this);
    }

    public static ArrayList<Key> findAll() {
        return KeyMapper.findAll();
    }

    public int compareTo(Object var1) {
        if(!(var1 instanceof Key)) {
            return -1;
        } else {
            Key var2 = (Key)var1;
            return this.number.compareTo(var2.number);
        }
    }
}

