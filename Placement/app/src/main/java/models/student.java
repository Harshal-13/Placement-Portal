package models;

public class student {
    public String name, email, branch, cpi, profile_pic;

    public student(String name, String email, String branch, String cpi, String url) {
        this.name = name;
        this.email = email;
        this.branch = branch;
        this.cpi = cpi;
        this.profile_pic = url;
    }

    public student(){ }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getBranch() { return branch; }

    public void setBranch(String branch) { this.branch = branch; }

    public String getCpi() { return cpi; }

    public void setCpi(String cpi) { this.cpi = cpi; }

    public String getProfile_pic() { return profile_pic; }

    public void setProfile_pic(String profile_pic) { this.profile_pic = profile_pic; }
}
