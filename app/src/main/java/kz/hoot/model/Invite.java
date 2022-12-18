package kz.hoot.model;

public class Invite {
    String actorsUsername;
    Long castId;

    @Override
    public String toString() {
        return "Invite{" +
                "actorsUsername='" + actorsUsername + '\'' +
                ", castId=" + castId +
                '}';
    }

    public String getActorsUsername() {
        return actorsUsername;
    }

    public void setActorsUsername(String actorsUsername) {
        this.actorsUsername = actorsUsername;
    }

    public Long getCastId() {
        return castId;
    }

    public void setCastId(Long castId) {
        this.castId = castId;
    }
}
