package com.novoda.smspoll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class Poll extends ListActivity {
	private static final String					INTENT_DATA_OPTION			= "option1";
	private static final String					INTENT_DATA_TOTAL_ANSWERS	= "no_of_optiions";
	private static final String					INTENT_DATA_QUESTION		= "question";
	private static final String					ANSWER_KEY					= "answer";
	protected static final String				TAG							= "[Poll]";
	private ArrayList<HashMap<String, String>>	list						= new ArrayList<HashMap<String, String>>();
	private SimpleAdapter						answerAdapter;
	private EditText							btn_newAnswer;
	private EditText							txt_question;
	private ImageButton							btn_addAnswer;
	private Button								btn_accept;
	private ArrayList<String>					answers;
	private int									mChosenPosition;
	protected Object							mNewFileName;
	private Object								sCurrFileName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.poll);

		this.txt_question = (EditText) findViewById(R.id.pollQuestion);
		this.btn_addAnswer = (ImageButton) findViewById(R.id.button);
		this.btn_newAnswer = (EditText) findViewById(R.id.answer);
		this.btn_accept = (Button) findViewById(R.id.btnAcceptPoll);

		this.answerAdapter = new SimpleAdapter(this, list, R.layout.row_answer, new String[] { ANSWER_KEY }, new int[] { R.id.answer });
		setListAdapter(this.answerAdapter);

		this.txt_question.setOnClickListener(getQuestionOnClickListener());
		this.btn_addAnswer.setOnClickListener(getAnswerAddBtnClickListener());
		this.btn_accept.setOnClickListener(getAcceptBtnOnClickListener());

		setListClickListeners();

		addAnswer("Two");
		addAnswer("Twenty");

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.poll_actions, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.accept:
				acceptPoll();
				return true;
		}
		return false;
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.delete:
				removeAnswer(mChosenPosition);
				return true;
		}
		return true;
	}

	private void setListClickListeners() {
		ListView lv = getListView();

		lv.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
				MenuInflater inflater = getMenuInflater();
				inflater.inflate(R.menu.poll_item_actions, menu);
			}
		});

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mChosenPosition = position;
				getListView().showContextMenu();
			}
		});
	}

	private Intent getCannedPoll() {
		Intent intent = new Intent();
		int answers_total = 2;
		txt_question.setText("How many fingers?");
		intent.putExtra(INTENT_DATA_QUESTION, "How many fingers?");
		intent.putExtra(INTENT_DATA_TOTAL_ANSWERS, answers_total);
		answers = new ArrayList<String>();
		answers.add(0, "1");
		answers.add(1, "20");
		answers.add(2, "40");
		return intent;

	}

	private OnClickListener getAcceptBtnOnClickListener() {
		return new OnClickListener() {
			public void onClick(View v) {
				acceptPoll();
			}
		};
	}

	private void acceptPoll() {
		Intent intent = getCannedPoll();

		String smsTxt = txt_question.getText() + "     ";
		int answerNumber;
		for (int i = 0; i < list.size(); i++) {
			answerNumber = i + 1;
			smsTxt = smsTxt + "\n" + answerNumber + "." + answers.get(i);
		}

		smsTxt = smsTxt + "\nVote: # <option> ie. #1 or #2";

		intent.putExtra(Constants.INTENT_DATA_SMS_CONTENTS, smsTxt);
		setResult(RESULT_OK, intent);
		finish();
	}

	public static JSONObject getJSON(String path, Map params) {
		Iterator iter = params.entrySet().iterator();

		JSONObject holder = new JSONObject();

		while (iter.hasNext()) {
			Map.Entry pairs = (Map.Entry) iter.next();
			String key = (String) pairs.getKey();
			Map m = (Map) pairs.getValue();

			JSONObject data = new JSONObject();
			try {
				Iterator iter2 = m.entrySet().iterator();
				while (iter2.hasNext()) {
					Map.Entry pairs2 = (Map.Entry) iter2.next();
					data.put((String) pairs2.getKey(), (String) pairs2.getValue());
				}
				holder.put(key, data);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return holder;
	}

	private OnClickListener getQuestionOnClickListener() {
		return new OnClickListener() {
			public void onClick(View v) {

			}
		};
	}

	private OnClickListener getAnswerAddBtnClickListener() {
		return new OnClickListener() {
			public void onClick(View view) {
				try {

					if (btn_newAnswer.getText().toString().length() >= 1) {
						Log.i(TAG, "text contains : " + btn_newAnswer.getText().toString());
						addAnswer();
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(btn_newAnswer.getWindowToken(), 0);
						btn_newAnswer.setText("");
					}

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
		answerAdapter.notifyDataSetChanged();
	}

	private void addAnswer(String answer) {
		HashMap<String, String> item = new HashMap<String, String>();
		item.put(ANSWER_KEY, answer);
		list.add(item);
		answerAdapter.notifyDataSetChanged();
	}

	@SuppressWarnings("unchecked")
	private void removeAnswer(int itemPosition) {
		list.remove((HashMap<String, String>) answerAdapter.getItem(itemPosition));
		answerAdapter.notifyDataSetChanged();
	}

}
