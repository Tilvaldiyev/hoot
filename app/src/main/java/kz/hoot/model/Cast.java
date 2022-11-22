package kz.hoot.model;


import androidx.annotation.NonNull;

public class Cast {

    private String castName;
    private String poster;
    private String castCreatorName;
    private CastType castType;
    private String description;

    public String getCastName() {
        return castName;
    }

    public void setCastName(String castName) {
        this.castName = castName;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getCastCreatorName() {
        return castCreatorName;
    }

    public void setCastCreatorName(String castCreatorName) {
        this.castCreatorName = castCreatorName;
    }

    public CastType getCastType() {
        return castType;
    }

    public void setCastType(CastType castType) {
        this.castType = castType;
    }

    public String getDescription() {
        return description;
    }

    public enum CastType{
        MOVIE,
        CLIP,
        SERIES;
        public static final Cast.CastType[] ALL = {
                MOVIE,
                CLIP,
                SERIES
        };
    }

    @NonNull
    @Override
    public String toString() {
        return "Cast:" +
                " castName=" + castName +
                ", castType=" + castType +
                ", castCreatorName=" + castCreatorName +
                "";
    }
}
