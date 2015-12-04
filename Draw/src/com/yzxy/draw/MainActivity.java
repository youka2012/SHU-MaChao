package com.yzxy.draw;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Timer;

import com.yzxy.draw.MyChartView.Mstyle;
import com.yzxy.draw.tools.Tools;

import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;

public class MainActivity extends Activity implements OnClickListener {

	TextView myTextView;
	TextView nowTempt;
	TextView setTempt;
	TextView nowStatus;
	Button sendButton, switchButton,connectbutton,helpbutton,up,down;
	MyReceiver receiver;
	IBinder serviceBinder;
	EditText tempSet;
	// MyService mService;
	Intent intent;
	int setClick = 0;
	int status = 0;
	int tempset = -1;
	 private int TIME = 1000; 
	 Double nowTemp=0.0;
	// int value = 0;

	/************** service 命令 *********/
	static final int CMD_STOP_SERVICE = 0x01;
	static final int CMD_SEND_DATA = 0x02;
	static final int CMD_SYSTEM_EXIT = 0x03;
	static final int CMD_SHOW_TOAST = 0x04;

	MyChartView tu;
	Button BT_Add;
	Timer mTimer = new Timer();
	HashMap<Double, Double> map;
	Double key = 8.0;
	Double value = 0.0;
	Tools tool = new Tools();
	Handler handler = new Handler();
	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			// handler自带方法实现定时器
			try {
				handler.postDelayed(this, TIME);
				randmap(map, nowTemp);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("exception...");
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// BT_Add=(Button)findViewById(R.id.bt_add);
		handler.postDelayed(runnable, TIME); //每隔1s执行
		tu = (MyChartView) findViewById(R.id.menulist);
		tu.SetTuView(map, 100, 12, "P", "℃", false);
		map = new HashMap<Double, Double>();
		map.put(-1.0, (double) 0);
		map.put(-2.0, (double) 0);
		map.put(-3.0, (double) 0);
		map.put(-4.0, (double) 0);
		map.put(-5.0, (double) 0);
		map.put(-6.0, (double) 0);
		map.put(-7.0, (double) 0);
		map.put(-9.0, (double) 0);
		map.put(-10.0, (double) 0);
		map.put(-11.0, (double) 0);
		map.put(-12.0, (double) 0);
		tu.setTotalvalue(30);
		tu.setPjvalue(3);
		tu.setMap(map);
		// tu.setXstr("");
		// tu.setYstr("");
		tu.setMargint(20);
		tu.setMarginb(50);
		tu.setMstyle(Mstyle.Line);

		// BT_Add.setOnClickListener(new click());
		nowTempt = (TextView) findViewById(R.id.nowTempt);
		setTempt = (TextView) findViewById(R.id.setTempt);
		nowStatus = (TextView) findViewById(R.id.status);
		tempSet = (EditText) findViewById(R.id.input_tempt);

		sendButton = (Button) findViewById(R.id.sendButton);
		sendButton.setOnClickListener(this);
		switchButton = (Button) findViewById(R.id.temSwitch);
		switchButton.setOnClickListener(this);
		connectbutton = (Button) findViewById(R.id.connect);
		connectbutton.setOnClickListener(this);
		helpbutton = (Button) findViewById(R.id.help);
		helpbutton.setOnClickListener(this);
		up = (Button) findViewById(R.id.up);
		up.setOnClickListener(this);
		down = (Button) findViewById(R.id.down);
		down.setOnClickListener(this);
		intent = new Intent(MainActivity.this, MyService.class);
		startService(intent);
	}

	// Random rd=new Random(System.currentTimeMillis());
	// Double temp= rd.nextDouble();
	// randmap(map, (double) 10);

	private void randmap(HashMap<Double, Double> mp, Double d) {
		ArrayList<Double> dz = tool.getintfrommap(mp);
		Double[] dvz = new Double[mp.size()];
		int t = 0;
		@SuppressWarnings("rawtypes")
		Set set = mp.entrySet();
		@SuppressWarnings("rawtypes")
		Iterator iterator = set.iterator();
		while (iterator.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry mapentry = (Map.Entry) iterator.next();
			dvz[t] = (Double) mapentry.getValue();
			t += 1;
		}
		for (int j = 0; j < dz.size() - 1; j++) {
			mp.put(dz.get(j), mp.get(dz.get(j + 1)));
		}
		mp.put((Double) dz.get(mp.size() - 1), d);
		tu.postInvalidate();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.lxx");
		MainActivity.this.registerReceiver(receiver, filter);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		if (receiver != null) {
			MainActivity.this.unregisterReceiver(receiver);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == R.id.menu_settings) {
			if (false == tu.isDrawingCacheEnabled()) {
				tu.setDrawingCacheEnabled(true);
			}
			Bitmap bitmap = tu.getDrawingCache();
			Tools tool = new Tools();
			try {
				Boolean b = tool.saveFile(bitmap, " ");
				if (b)
					Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (item.getItemId() == R.id.menu_ch) {
			tu.setMstyle(Mstyle.Curve);
			tu.setIsylineshow(true);
			tu.postInvalidate();
		}
		if (item.getItemId() == R.id.menu_ch2) {

			tu.setMstyle(Mstyle.Line);
			tu.setIsylineshow(false);
			tu.postInvalidate();
		}
		return true;
	}

	public void showToast(String str) {// 显示提示信息
		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
	}

	public class MyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			try{
			if (intent.getAction().equals("android.intent.action.lxx")) {
				Bundle bundle = intent.getExtras();
				int cmd = bundle.getInt("cmd");

				if (cmd == CMD_SHOW_TOAST) {
					String str = bundle.getString("str");
					showToast(str);
				}
				if (cmd == CMD_SEND_DATA) {
					String str = bundle.getString("str");
					
					Double wd = Double.parseDouble(str);
					nowTemp=wd;
					nowTempt.setText(str + "℃");
					if (tempset != -1) {
						if (wd < tempset - 1) {
							nowStatus.setText("加热中");
						} else if (wd > tempset + 1) {
							nowStatus.setText("制冷中");
						} else {
							nowStatus.setText("恒温");
						}
					}
				}
				if (cmd == CMD_SYSTEM_EXIT) {
					System.exit(0);
				}

			}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void sendCmd(byte command, int value) {
		Intent intent = new Intent();// 创建Intent对象
		intent.setAction("android.intent.action.cmd");
		intent.putExtra("cmd", CMD_SEND_DATA);
		intent.putExtra("command", command);
		intent.putExtra("value", value);
		sendBroadcast(intent);// 发送广播
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent ;
		switch (v.getId()) {
		case R.id.down:
			if(TIME>10){
			TIME=TIME/10;}
			break;
		case R.id.up:
			if(TIME<10000){
			TIME=TIME*10;}
			break;
		case R.id.sendButton:
			if (setClick == 0) {
				tempSet.setVisibility(View.VISIBLE);
				sendButton.setText("确定");
				setClick = 1;
			} else if (setClick == 1) {

				if (tempSet.getText().equals("")) {
					showToast("数据为空");
				} else {

					try {
						int wdz = Integer.parseInt(tempSet.getText().toString());
						tempset = wdz;
						byte command = (byte) wdz;
						int value = 0x12345;
						sendCmd(command, value);
						showToast("设定成功:" + command + "℃");
					} catch (Exception e) {
						return;
					}

					setClick = 0;
					sendButton.setText("设定温度");
					tempSet.setVisibility(View.GONE);
					setTempt.setText(tempSet.getText().toString() + "℃");
				}
			}

			break;
		case R.id.temSwitch:
			// randmap(map, (double) 10);
		intent = new Intent(MainActivity.this, SetActivity.class);  
		    startActivity(intent);
			break;
		case R.id.connect:
			// randmap(map, (double) 10);
			Intent intent1 = new Intent();// 创建Intent对象
			intent1.setAction("android.intent.action.cmd");
			intent1.putExtra("cmd", CMD_STOP_SERVICE);		
			sendBroadcast(intent1);// 发送广播
			finish();
			intent = new Intent(MainActivity.this, MainActivity.class);  
		    startActivity(intent);
			break;
		case R.id.help:
			intent = new Intent(MainActivity.this, HelpActivity.class);  
		    startActivity(intent);
			break;
		}

	}

}
