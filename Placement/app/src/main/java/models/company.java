package models;

public class company{
    public String name, email, branch, cpi;

    public company(String name, String email, String branch, String cpi) {
        this.name = name;
        this.email = email;
        this.branch = branch;
        this.cpi = cpi;
    }

    public company(){

    }

    public String company_getName() {
        return name;
    }

    public String company_getEmail() {
        return email;
    }

    public String company_getBranch() {
        return branch;
    }

    public String company_getCpi() {
        return cpi;
    }

    public void company_setName(String name) {
        this.name = name;
    }

    public void company_setEmail(String email) {
        this.email = email;
    }

    public void company_setBranch(String branch) {
        this.branch = branch;
    }

    public void company_setCpi(String cpi) {
        this.cpi = cpi;
    }
}
