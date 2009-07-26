package com.novoda.smspoll;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;

public class Poll extends ListActivity {
	private static final String					ANSWER_KEY	= "answer";
	protected static final String				TAG			= "[Poll]";
	private ArrayList<HashMap<String, String>>	list		= new ArrayList<HashMap<String, String>>();
	private SimpleAdapter						adapter;
	private EditText							newAnswer;
	private EditText							question;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.poll);
		this.newAnswer = (EditText) findViewById(R.id.answer);
		this.question =  (EditText) findViewById(R.id.pollQuestion);
		
		this.adapter = new SimpleAdapter(this, list, R.layout.row_answer, new String[] { ANSWER_KEY }, new int[] { R.id.answer });
		setListAdapter(this.adapter);
		
		((EditText) findViewById(R.id.pollQuestion)).setOnClickListener(getQuestionOnClickListener());
		((ImageButton) findViewById(R.id.button)).setOnClickListener(getBtnClickListener());
	}

	private OnClickListener getQuestionOnClickListener() {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {
				question.setText("");
			}};
	}

	private OnClickListener getBtnClickListener() {
		return new OnClickListener() {
			public void onClick(View view) {
				try {
					addAnswer();
					adapter.notifyDataSetChanged();
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(newAnswer.getWindowToken(), 0);
					newAnswer.setText("");
				} catch (NullPointerException e) {
					Log.v(TAG, "Tried to add null value");
				}
			}
		};
	}

	private void addAnswer() {
		HashMap<String, String> item = new HashMap<String, String>();
		item.put(ANSWER_KEY, newAnswer.getText().toString());
		list.add(item);
	}

}
