package kz.hoot.model;

public class Actor {
    private int actorId;
    private String nickname;
    private String city;
    private String country;
    private String gender;
    private String lastname;
    private String name;
    private String patronymic;
    private String photo;
    private String about;
    private int age;

    public Actor(String nickname, String city, String country, String gender, String surname, String name, String patronymic, String img, String about, int age, int id) {
        this.nickname = nickname;
        this.city = city;
        this.country = country;
        this.gender = gender;
        this.lastname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.photo = img;
        this.age = age;
        this.about = about;
        this.actorId = id;
    }

    public Actor(String nickname, String country,  String surname, String name, String patronymic, String img, int age, int id) {
        this.actorId = id;
        this.nickname = nickname;
        this.name = name;
        this.lastname = surname;
        this.age = age;
        this.country = country;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public int getActorId() {
        return actorId;
    }

    public void setActorId(int actorId) {
        this.actorId = actorId;
    }
}
