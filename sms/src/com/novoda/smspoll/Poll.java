package com.novoda.smspoll;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.EditText;

public class Poll extends ListActivity {
	private EditText	mQuestion;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.poll);

		mQuestion = (EditText) findViewById(R.id.pollQuestion);
		mQuestion.setText("Why?");

		PollAnswerAdapter pollAnswerAdapter = new PollAnswerAdapter(this, "Why", new String[] { "becuase", "no reason" });
		this.setListAdapter(pollAnswerAdapter);

	}

}
