package models;

public class company{
    public String name, email, profile_pic;

    public company(String name, String email, String url) {
        this.name = name;
        this.email = email;
        this.profile_pic = url;
    }
    public company(){ }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getProfile_pic() { return profile_pic; }

    public void setProfile_pic(String profile_pic) { this.profile_pic = profile_pic; }
}
