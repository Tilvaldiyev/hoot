package kz.hoot.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RegistrationModel implements Parcelable {
    private String username;
    private String password;
    private String userType;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String countryCode;
    private int city;

    public RegistrationModel(String login, String password, String userType, String first_name, String last_name, String middle_name, String country, int city) {
        this.username = login;
        this.password = password;
        this.userType = userType;
        this.firstName = first_name;
        this.lastName = last_name;
        this.patronymic = middle_name;
        this.countryCode = country;
        this.city = city;
    }

    protected RegistrationModel(Parcel in) {
        username = in.readString();
        password = in.readString();
        userType = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        patronymic = in.readString();
        countryCode = in.readString();
        city = in.readInt();
    }

    public static final Creator<RegistrationModel> CREATOR = new Creator<RegistrationModel>() {
        @Override
        public RegistrationModel createFromParcel(Parcel in) {
            return new RegistrationModel(in);
        }

        @Override
        public RegistrationModel[] newArray(int size) {
            return new RegistrationModel[size];
        }
    };

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(userType);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(patronymic);
        dest.writeString(countryCode);
        dest.writeInt(city);
    }
}