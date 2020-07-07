/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.dartmouth.cs.customalertdialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import androidx.fragment.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

/**
 * Demonstrates how to show an AlertDialog that is managed by a Fragment.
 */

public class FragmentDialogAlarmActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment_dialog_alert);

		String myTitle = getString(R.string.dialog_title);
		View tv = findViewById(R.id.text);
		((TextView) tv)
				.setText(myTitle);

		// Watch for button clicks.
		Button button = findViewById(R.id.show);
		button.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog();
			}
		});
	}

	void showDialog() {
		DialogFragment newFragment = MyAlertDialogFragment
				.newInstance(R.string.alert_dialog_two_buttons_title);
		newFragment.show(getSupportFragmentManager(), "dialog");
	}

	public void doPositiveClick() {
		// Do stuff here.
		Log.i("FragmentAlertDialog", "Positive click!");
	}

	public void doNegativeClick() {
		// Do stuff here.
		Log.i("FragmentAlertDialog", "Negative click!");
	}

	//nested class for fragment
	public static class MyAlertDialogFragment extends DialogFragment {

		public static MyAlertDialogFragment newInstance(int title) {
			MyAlertDialogFragment frag = new MyAlertDialogFragment();
			Bundle args = new Bundle();
			args.putInt("title", title);
			frag.setArguments(args);
			return frag;
		}

		@NonNull
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			int title = 0;
			if (getArguments() != null) {
				title = getArguments().getInt("title");
			}

			//this creates the alertdialog
			return new AlertDialog.Builder(getActivity())
					.setView(R.layout.custom_dialog)
					.setPositiveButton(R.string.alert_dialog_ok,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									((FragmentDialogAlarmActivity) Objects.requireNonNull(getActivity()))
											.doPositiveClick();
								}
							})
					.setNegativeButton(R.string.alert_dialog_cancel,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									((FragmentDialogAlarmActivity) Objects.requireNonNull(getActivity()))
											.doNegativeClick();
								}
							})
					.create();
		}
	}

}
