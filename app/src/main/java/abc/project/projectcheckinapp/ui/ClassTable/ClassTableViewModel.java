package abc.project.projectcheckinapp.ui.ClassTable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ClassTableViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ClassTableViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is InputCourseCode fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}