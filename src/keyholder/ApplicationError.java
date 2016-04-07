package keyholder;

public class ApplicationError extends Exception {
    String[] message;

    public ApplicationError(String var1) {
        this(new String[]{var1});
    }

    public ApplicationError(String[] var1) {
        this.message = var1;
    }

    public String[] getErrorMessage() {
        return this.message;
    }
}

