package abc.project.projectcheckinapp.ui.Student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import abc.project.projectcheckinapp.R;
import abc.project.projectcheckinapp.databinding.FragmentStudentmainpageBinding;

public class StudentMainPageFragment extends Fragment implements View.OnClickListener{

    private AppBarConfiguration AppBarConfiguration;
    FragmentStudentmainpageBinding binding;
    NavController navController;

    public StudentMainPageFragment() {
        // Required empty public constructor
    }

    public static StudentMainPageFragment newInstance(String param1, String param2) {
        StudentMainPageFragment fragment = new StudentMainPageFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentStudentmainpageBinding.inflate(inflater, container, false);
        binding.btnHome.setOnClickListener(this);
        binding.btnInputclasscode.setOnClickListener(this);
        binding.btnGotoclass.setOnClickListener(this);
        binding.btnClasstable.setOnClickListener(this);
        binding.btnRevisedata.setOnClickListener(this);

        DrawerLayout drawerStd = binding.layoutStddrawer;
        NavigationView navigationView = binding.navViewForStd;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_stdMainPage, R.id.nav_classTable, R.id.nav_EnterClass, R.id.nav_reviseStdData)   //影響設定是返回建or Menu
                .setOpenableLayout(drawerStd)
                .build();
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController((AppCompatActivity) getActivity(), navController, AppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        return binding.getRoot();
    }








    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            /*case R.id.layout_stdmainpage:
                navController.navigate(R.id.nav_EnterClass);
                break;*/
            case R.id.btn_home:
                getActivity().onBackPressed();
                break;
            case R.id.btn_inputclasscode:
                navController.navigate(R.id.nav_inputCourseCode);
                break;
            case R.id.btn_gotoclass:
                navController.navigate(R.id.nav_EnterClass);
                break;
            case R.id.btn_classtable:
                navController.navigate(R.id.nav_classTable);
                break;
            case R.id.btn_revisedata:
                navController.navigate(R.id.nav_reviseStdData);
                break;
        }
    }
}