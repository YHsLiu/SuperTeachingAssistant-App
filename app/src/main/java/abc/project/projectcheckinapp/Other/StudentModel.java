package abc.project.projectcheckinapp.Other;

public class StudentModel {
    private int sid;
    private String stuname;
    private String studepart;
    private String stuid;

    public StudentModel() {
    }

    public StudentModel(int sid, String stuname, String studepart, String stuid) {
        this.sid = sid;
        this.stuname = stuname;
        this.studepart = studepart;
        this.stuid = stuid;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getStuname() {
        return stuname;
    }

    public void setStuname(String stuname) {
        this.stuname = stuname;
    }

    public String getStudepart() {
        return studepart;
    }

    public void setStudepart(String studepart) {
        this.studepart = studepart;
    }

    public String getStuid() {
        return stuid;
    }

    public void setStuid(String stuid) {
        this.stuid = stuid;
    }
}
