package abc.project.projectcheckinapp.Other;

public interface ClickListener {
    void onClickForAllStuList (int position,int sid, String stuname,String studepart,String stuid);
    void onClickForClassroom(int position,int cid,String classname);
    void onClickForNoRcStuList (int position,int sid);
}
