package kz.hoot.model;

public class User {
    private int id;
    private String username;
    private String fullName;
    private String userType;
    private boolean readyStatus;
    private String avatar;

    public User(int id, String username, String fullname, String userType, boolean readyStatus, String avatar) {
        this.id = id;
        this.username = username;
        this.fullName = fullname;
        this.userType = userType;
        this.readyStatus = readyStatus;
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullName;
    }

    public void setFullname(String fullname) {
        this.fullName = fullname;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public boolean isReadyStatus() {
        return readyStatus;
    }

    public void setReadyStatus(boolean readyStatus) {
        this.readyStatus = readyStatus;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
