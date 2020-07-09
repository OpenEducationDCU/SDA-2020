package sensor.oscail.edu.allsensorfrag;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;


public class MainActivityFragment extends ListFragment {

    public MainActivityFragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        SensorManager mSensorManager = (SensorManager) this.requireActivity().getSystemService(Context.SENSOR_SERVICE);

        List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        Iterator<Sensor> iter = deviceSensors.iterator();
        ArrayList<String> sensorList = new ArrayList<String>();


        while (iter.hasNext()) {
            Sensor s = iter.next();
            sensorList.add(s.getName());
        }

        int listSize = sensorList.size();
        String[] listString = sensorList.toArray(new String[listSize]);

        setListAdapter(new ArrayAdapter<>(inflater.getContext(), R.layout.fraglist_main,
                listString));

        return super.onCreateView(inflater, container, savedInstanceState);

    }

    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        Toast.makeText(requireActivity().getApplicationContext(),
                ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
    }

}




