package abc.project.projectcheckinapp.rawData;

public class RecordModel {
    private String date;
    private String stuInfo;

    public RecordModel(String date, String stuInfo) {
        this.date = date;
        this.stuInfo = stuInfo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStuInfo() {
        return stuInfo;
    }

    public void setStuInfo(String stuInfo) {
        this.stuInfo = stuInfo;
    }
}
