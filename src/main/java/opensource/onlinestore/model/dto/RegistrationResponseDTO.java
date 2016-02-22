package opensource.onlinestore.model.dto;

/**
 * Created by orbot on 29.01.16.
 */
public class RegistrationResponseDTO {
    private boolean success;
    private String token;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
