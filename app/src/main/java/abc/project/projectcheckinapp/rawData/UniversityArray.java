package abc.project.projectcheckinapp.rawData;

import android.util.Log;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class UniversityArray {
    InputStream inputStream;

    public UniversityArray() {
    }

    public UniversityArray(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public ArrayList<String> getArrayToSpinner(InputStream inputStream)  {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb =new StringBuilder();
        String line;
        ArrayList< String > universityArray =new ArrayList<>();
        try {
            while ((line = br.readLine()) != null)
                sb.append(line);
            br.close();
            JSONArray jsonArray = new JSONArray(sb.toString());

            for (int i=0; i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                universityArray.add(jsonObject.getString("學校名稱"));
            }
        } catch (Exception e){
            Log.w("json", "讀取異常");
        }
        return universityArray;
    }
}
