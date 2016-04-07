package keyholder.domain;

import com.jgoodies.binding.beans.Model;

public class Domain extends Model {
    long id;

    public Domain(long var1) {
        this.id = var1;
    }

    public long getID() {
        return this.id;
    }

    public void setID(long var1) {
        if(this.id == 0L) {
            this.id = var1;
        }

    }
}
