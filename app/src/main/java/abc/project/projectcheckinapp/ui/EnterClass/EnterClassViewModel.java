package abc.project.projectcheckinapp.ui.EnterClass;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EnterClassViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public EnterClassViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is InputCourseCode fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}