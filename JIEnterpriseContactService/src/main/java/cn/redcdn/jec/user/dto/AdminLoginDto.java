package cn.redcdn.jec.user.dto;

public class AdminLoginDto {
    /**
     * token
     */
    private String token;
    private String type;

    /**
     * getter method
     *
     * @return Returns the token.
     */
    public String getToken() {
        return token;
    }

    /**
     * setter method
     *
     * @param token The token to set.
     */
    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
