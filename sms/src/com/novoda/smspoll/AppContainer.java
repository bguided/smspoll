/***
* 	
*	Entry Point. User is only presented with tasks they can actively carry out. 
* 
* 	Scenarios.
*	Previous poll exists & no poll active, start with 2 Activities (EditSms, PreviousPoll)
*	Previous poll exists & poll active, start with 2 Activities (CurrentPoll, PreviousPoll)
* 	No last poll, start one activity (EditSms)
* 	No last poll and there is a current poll then start with one activity (CurrentPoll)
*
 */

package com.novoda.smspoll;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class AppContainer extends TabActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		TabHost host = getTabHost();
		
//		if(previousPollExists){
//			if(pollIsActive){
//				start(currentPollActivity)
//			}else{
//				start(editSmsActivity)
//				addToTab(previousPollActivity)
//			}
//		}else{
//			if(pollIsActive){
//				start(currentPollActivity)
//			}else{
//				start(editSmsActivity)
//			}
//		}
		
		host.addTab(host.newTabSpec("one").setIndicator("Send").setContent(new Intent(this, EditSMS.class)));
		host.addTab(host.newTabSpec("two").setIndicator("New").setContent(new Intent(this, EditPoll.class)));
	}
}
