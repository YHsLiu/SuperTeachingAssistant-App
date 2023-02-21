package abc.project.projectcheckinapp.Other;

public class RoomModel {
    private int cid;
    private String classname;
    private String classcode;

    public RoomModel(int cid, String classname, String classcode) {
        this.cid = cid;
        this.classname = classname;
        this.classcode = classcode;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getClasscode() {
        return classcode;
    }

    public void setClasscode(String classcode) {
        this.classcode = classcode;
    }
}
