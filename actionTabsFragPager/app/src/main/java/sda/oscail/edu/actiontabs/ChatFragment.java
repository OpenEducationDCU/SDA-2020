package sda.oscail.edu.actiontabs;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ChatFragment extends Fragment
{

    int m_count=0;
    View.OnClickListener mOnClickListener;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.chatfragment, container, false);

        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar snackbar =Snackbar.make(view, "Count is "+  Integer.toString(m_count), Snackbar.LENGTH_LONG)
                        .setAction("Increment", mOnClickListener).setActionTextColor(Color.GREEN);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(Color.parseColor("#3f51b5"));
                TextView tv = sbView.findViewById(R.id.snackbar_text);
                tv.setTextColor(Color.WHITE);
                snackbar.show();
            }
        });

        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_count++;
            }
        };
        return root;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
           m_count =  savedInstanceState.getInt("COUNT");

        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("COUNT",m_count);
    }



}
