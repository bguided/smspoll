package com.novoda.smspoll;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;

public class PollAnswerAdapter extends BaseAdapter {

	private Context						mContext;
	private String						mQuestion;
	private ArrayList<PollAnswer>		answers;
	private ArrayList<PollAnswerView>	answerViews;

	public PollAnswerAdapter(Context context) {
		this.mContext = context;
		this.answers = new ArrayList<PollAnswer>();
		this.answerViews = new ArrayList<PollAnswerView>();
		this.mQuestion = new String("What does it all mean?");
	}

	public PollAnswerAdapter(Context context, String question) {
		this.mContext = context;
		this.answers = new ArrayList<PollAnswer>();
		this.answerViews = new ArrayList<PollAnswerView>();
		this.mQuestion = new String(question);
	}

	public PollAnswerAdapter(Context context, String question, String[] answers) {
		this.mContext = context;
		mQuestion = new String(question);
		this.answers = new ArrayList<PollAnswer>();
		this.answerViews = new ArrayList<PollAnswerView>();

		PollAnswer answer;
		for (int i = 0; i < answers.length; i++) {
			answer = new PollAnswer(mQuestion, answers[i]);
			this.answers.add(answer);
			this.answerViews.add(new PollAnswerView(mContext, answers[i]));
		}

	}

	@Override
	public int getCount() {
		return this.answers.size();
	}

	@Override
	public PollAnswer getItem(int position) {
		final View itemInListView = answerViews.get(position);
		EditText question = (EditText) itemInListView.findViewById(R.id.question);

		return new PollAnswer(mQuestion, question.getText().toString());
	}

	@Override
	public long getItemId(int position) {
		return this.answerViews.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return this.answerViews.get(position);
	}

	public class PollAnswerView extends LinearLayout {

		public PollAnswerView(Context context) {
			super(context);

			LayoutInflater factory = LayoutInflater.from(context);
			final View itemInListView = factory.inflate(R.layout.row_question, null);

			addView(itemInListView);
		}

		public PollAnswerView(Context context, String string) {
			super(context);

			LayoutInflater factory = LayoutInflater.from(context);
			final View itemInListView = factory.inflate(R.layout.row_question, null);

			EditText question = (EditText) itemInListView.findViewById(R.id.question);
			question.setText(string);

			addView(itemInListView);
		}
	}

}
