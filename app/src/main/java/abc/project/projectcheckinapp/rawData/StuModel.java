package abc.project.projectcheckinapp.rawData;

public class StuModel {
    private String stuname;
    private String studepart;
    private String stuid;

    public StuModel(String stuname, String studepart, String stuid) {
        this.stuname = stuname;
        this.studepart = studepart;
        this.stuid = stuid;
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
