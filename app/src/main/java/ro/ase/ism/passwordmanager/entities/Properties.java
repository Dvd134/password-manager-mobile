package ro.ase.ism.passwordmanager.entities;

public class Properties {

    private String clientId;
    private String clientSecret;
    private String bearerToken;
    private String passphrase;

    public Properties() {}

    public Properties(String clientId, String clientSecret, String bearerToken, String passphrase) {

        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.bearerToken = bearerToken;
        this.passphrase = passphrase;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getBearerToken() {
        return bearerToken;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public void setBearerToken(String bearerToken) {
        this.bearerToken = bearerToken;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }
}
