package ro.ase.ism.passwordmanager.entities;

public class Token {

    private Meta meta;
    private TokenData data;

    public Token() {}

    public Token(Meta meta, TokenData data) {
        this.meta = meta;
        this.data = data;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public TokenData getData() {
        return data;
    }

    public void setData(TokenData data) {
        this.data = data;
    }
}
