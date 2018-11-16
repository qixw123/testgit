package cn.redcdn.jec.user.dto;

public class UserSearchDto {
    private String nube;

    private String name;

    private String account;

    private Integer accountType;

    public String getNube() {
        return nube;
    }

    public void setNube(String nube) {
        this.nube = nube;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

}
