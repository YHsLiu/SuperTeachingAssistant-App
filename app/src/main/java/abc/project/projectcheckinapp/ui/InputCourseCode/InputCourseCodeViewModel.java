package abc.project.projectcheckinapp.ui.InputCourseCode;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class InputCourseCodeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public InputCourseCodeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is InputCourseCode fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}