package com.yzxy.draw;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SetActivity extends Activity implements OnClickListener {

	EditText macTextView;
	Button macButton, macback,macinit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_act);
		// myTextView = (TextView)findViewById(R.id.testTextView);
		macButton = (Button) findViewById(R.id.macbutton);
		macButton.setOnClickListener(this);
		macinit=(Button) findViewById(R.id.macinit);
		macinit.setOnClickListener(this);
		;
		macTextView = (EditText) findViewById(R.id.mactext);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (v.getId()) {
		case R.id.macbutton:
			InitPre.setBlueMAC(getApplicationContext(), macTextView.getText()
					.toString());
			Toast.makeText(getApplicationContext(), "设置完成", Toast.LENGTH_SHORT).show();
			break;
		case R.id.macinit:
			InitPre.setBlueMAC(getApplicationContext(), "30:14:09:30:20:70");
			Toast.makeText(getApplicationContext(), "初始化完成", Toast.LENGTH_SHORT).show();
			break;
	

		}

	}
}
