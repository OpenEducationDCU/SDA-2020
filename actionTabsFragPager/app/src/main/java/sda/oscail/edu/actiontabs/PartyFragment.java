package sda.oscail.edu.actiontabs;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class PartyFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.partyfragment, container, false);

        //find the button we added
        Button newButton = root.findViewById(R.id.button);

        //start a new activitty using an onclicklistener on the button inside the party fragment
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newActivity = new Intent(getActivity(), NewActivity.class);
                startActivity(newActivity);
            }
        });

        return root;
    }

}