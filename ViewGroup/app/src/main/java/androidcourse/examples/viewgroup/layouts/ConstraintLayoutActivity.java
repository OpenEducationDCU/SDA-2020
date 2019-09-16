package androidcourse.examples.viewgroup.layouts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import androidcourse.example.viewgroup.layouts.R;

/**
 * @author Chris Coughlan 2019
 */
public class ConstraintLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constraint_layout);

    }

    public void onReturnClicked(View v)
    {
        Intent intent = new Intent(ConstraintLayoutActivity.this,
                MainLayoutActivity.class);
        startActivity(intent);
    }
}
