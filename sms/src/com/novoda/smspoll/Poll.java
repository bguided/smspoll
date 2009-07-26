package com.novoda.smspoll;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

public class Poll extends ListActivity {
	private static final String					ANSWER_KEY	= "answer";
	protected static final String				TAG			= "[Poll]";
	private ArrayList<HashMap<String, String>>	list		= new ArrayList<HashMap<String, String>>();
	private SimpleAdapter						adapter;
	private EditText							btn_newAnswer;
	private EditText							btn_question;
	private ImageButton							btn_addAnswer;
	private Button								btn_accept;
	private LinearLayout						layout_answer;
	private LinearLayout						layout_question;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.poll);

		this.layout_question = (LinearLayout) findViewById(R.id.layout_question);
		this.layout_answer = (LinearLayout) findViewById(R.id.layout_answer);

		this.btn_question = (EditText) findViewById(R.id.pollQuestion);
		this.btn_addAnswer = (ImageButton) findViewById(R.id.button);
		this.btn_newAnswer = (EditText) findViewById(R.id.answer);
		this.btn_accept = (Button) findViewById(R.id.btnAcceptPoll);

		this.adapter = new SimpleAdapter(this, list, R.layout.row_answer, new String[] { ANSWER_KEY }, new int[] { R.id.answer });
		setListAdapter(this.adapter);

		this.btn_question.setOnClickListener(getQuestionOnClickListener());
		this.btn_addAnswer.setOnClickListener(getAnswerAddBtnClickListener());
		this.btn_newAnswer.setOnClickListener(getNewAnswerOnCLickListener());
	}

	private OnClickListener getNewAnswerOnCLickListener() {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		};
	}

	private OnClickListener getQuestionOnClickListener() {
		return new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		};
	}

	private OnClickListener getAnswerAddBtnClickListener() {
		return new OnClickListener() {
			public void onClick(View view) {
				try {
					addAnswer();
					adapter.notifyDataSetChanged();
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(btn_newAnswer.getWindowToken(), 0);
					btn_newAnswer.setText("");
				} catch (NullPointerException e) {
					Log.v(TAG, "Tried to add null value");
				}
			}
		};
	}

	private void addAnswer() {
		HashMap<String, String> item = new HashMap<String, String>();
		item.put(ANSWER_KEY, btn_newAnswer.getText().toString());
		list.add(item);
	}

	// BaseInputConnection

//	@Override
//	public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
//		outAttrs.imeOptions |= EditorInfo.IME_FLAG_NO_EXTRACT_UI | EditorInfo.IME_ACTION_NONE;
//		return new BaseInputConnection(this, false);
//	}

}
