package abc.project.projectcheckinapp.ui.StudentMainPage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class StudentMainPageViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public StudentMainPageViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is InputCourseCode fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}