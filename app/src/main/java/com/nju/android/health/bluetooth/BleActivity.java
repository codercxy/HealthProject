package com.nju.android.health.bluetooth;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.nju.android.health.R;
import com.nju.android.health.providers.DbPressure;
import com.nju.android.health.providers.DbProvider;
import com.nju.android.health.utils.VolleyRequestImp;
import com.nju.android.health.views.activities.MainActivity;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BleActivity extends AppCompatActivity implements DeviceListFragment.OnDeviceListFragmentInteractionListener {
	public static final String TAG = "BluetoothLE";
	private final int ENABLE_BT = 1;

	private static Context context;
	private final Messenger mMessenger;
	private Intent mServiceIntent;
	private Messenger mService = null;
	private BleService.State mState = BleService.State.UNKNOWN;

	private MenuItem mRefreshItem = null;
	private MenuItem mExitItem = null;

	private DeviceListFragment mDeviceList = DeviceListFragment.newInstance();
	private DisplayFragment mDisplay = DisplayFragment.newInstance();

	private ServiceConnection mConnection = new ServiceConnection() {//得到远程信使
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = new Messenger(service);
			try {
				Message msg = Message.obtain(null, BleService.MSG_REGISTER);
				if (msg != null) {
					msg.replyTo = mMessenger;//将自己的信使设置到消息中，这样服务端接收到消息时同时也得到了客户端的信使对象了
					mService.send(msg);
				} else {
					mService = null;
				}
			} catch (Exception e) {
				Log.w(TAG, "Error connecting to BleService", e);
				mService = null;
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mService = null;
		}
	};

	public BleActivity() {
		super();
		mMessenger = new Messenger(new IncomingHandler(this));//创建一个信使对象
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ble);
		this.context = this;


		mServiceIntent = new Intent(this, BleService.class);
		FragmentTransaction tx = getFragmentManager().beginTransaction();
		tx.add(R.id.main_content, mDeviceList);
		tx.commit();
		
		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {//检测设备是否支持蓝牙
		     Toast.makeText(this, "ble_not_supported", Toast.LENGTH_SHORT).show();
		     Log.d("ble_not_supported", "ble_not_supported");
		     finish();
		 }
		

		if (!isServiceExisted(context, "com.nju.android.health.bluetooth.BleService")) {
			//add by wy in 2015.7.29
			Log.e("bleService_existed", "true");

		}

		//bindService(mServiceIntent, mConnection, BIND_AUTO_CREATE);//请求远程通信
	}

	@Override
	protected void onPause() {
		super.onPause();
		unbindService(mConnection);
		mService = null;
		mConnection = null;
		mServiceIntent = null;

	}

	@Override
	protected void onResume() {
		super.onResume();
		mConnection = new ServiceConnection() {//得到远程信使
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				mService = new Messenger(service);
				try {
					Message msg = Message.obtain(null, BleService.MSG_REGISTER);
					if (msg != null) {
						msg.replyTo = mMessenger;//将自己的信使设置到消息中，这样服务端接收到消息时同时也得到了客户端的信使对象了
						mService.send(msg);
					} else {
						mService = null;
					}
				} catch (Exception e) {
					Log.w(TAG, "Error connecting to BleService", e);
					mService = null;
				}

			}

			@Override
			public void onServiceDisconnected(ComponentName name) {
				mService = null;
			}
		};
		if (mService != null) {
			startScan();
		}
		bindService(mServiceIntent, mConnection, BIND_AUTO_CREATE);//请求远程通信

	}

	public static boolean isServiceExisted(Context context, String className) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList =
				activityManager.getRunningServices(Integer.MAX_VALUE);

		if (!(serviceList.size() > 0)) {
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++) {
			ActivityManager.RunningServiceInfo serviceInfo = serviceList.get(i);
			ComponentName serviceName = serviceInfo.service;
			Log.e("bleService", serviceName.getClassName().toString());
			if (serviceName.getClassName().equals(className)) {
				return true;
			}
		}
		return false;

	}

	@Override
	protected void onStop() {
		
		//delete by wy in 2015.7.29
//		if (mService != null) {
//			try {
//				Message msg = Message.obtain(null, BleService.MSG_UNREGISTER);
//				if (msg != null) {
//					msg.replyTo = mMessenger;
//					mService.send(msg);
//				}
//			} catch (Exception e) {
//				Log.w(TAG, "Error unregistering with BleService", e);
//				mService = null;
//			} finally {
//				unbindService(mConnection);
//			}
//		}
		super.onStop();
	}

	@Override
	protected void onStart() {
		super.onStart();

		//delete by wy in 2015.7.29
//		bindService(mServiceIntent, mConnection, BIND_AUTO_CREATE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		mRefreshItem = menu.findItem(R.id.action_refresh);
		mRefreshItem = menu.findItem(R.id.action_exit);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_refresh) {
			if (mService != null) {
				startScan();
			}
			return true;
		}
		if (id == R.id.action_exit){
			if (mService != null) {
				try {
					Message msg = Message.obtain(null, BleService.MSG_UNREGISTER);
					if (msg != null) {
						msg.replyTo = mMessenger;
						mService.send(msg);
					}
				} catch (Exception e) {
					Log.w(TAG, "Error unregistering with BleService", e);
					mService = null;
				} finally {
					unbindService(mConnection);
					Intent intent = new Intent(BleActivity.this, MainActivity.class);
					startActivity(intent);
					finish();
				}
			}
			
		}
		return super.onOptionsItemSelected(item);
	}

	private void startScan() {
		mRefreshItem.setEnabled(false);
		mDeviceList.setDevices(this, null);//null代表并没有设备
		mDeviceList.setScanning(true);
		Message msg = Message.obtain(null, BleService.MSG_START_SCAN);
		if (msg != null) {
			try {
				mService.send(msg);
			} catch (RemoteException e) {
				Log.w(TAG, "Lost connection to service", e);
				unbindService(mConnection);
			}
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (mRefreshItem != null) {
			mRefreshItem.setEnabled(mState == BleService.State.IDLE || mState == BleService.State.UNKNOWN);
			mRefreshItem.setVisible(mState == BleService.State.IDLE || mState == BleService.State.UNKNOWN);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onDeviceListFragmentInteraction(String macAddress) {
		Message msg = Message.obtain(null, BleService.MSG_DEVICE_CONNECT);
		if (msg != null) {
			msg.obj = macAddress;
			try {
				Log.d(TAG, macAddress);
				mService.send(msg);
			} catch (RemoteException e) {
				Log.w(TAG, "Lost connection to service", e);
				unbindService(mConnection);
			}
		}
	}



	private static class IncomingHandler extends Handler {
		private final WeakReference<BleActivity> mActivity;

		public IncomingHandler(BleActivity activity) {
			mActivity = new WeakReference<BleActivity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			BleActivity activity = mActivity.get();
			if (activity != null) {
				switch (msg.what) {
					case BleService.MSG_STATE_CHANGED:
						activity.stateChanged(BleService.State.values()[msg.arg1]);
						break;
					case BleService.MSG_DEVICE_FOUND:
						Bundle data = msg.getData();
						if (data != null && data.containsKey(BleService.KEY_NAME_ADDRESSES)) {
							activity.mDeviceList.setDevices(activity, data.getStringArray(BleService.KEY_NAME_ADDRESSES)); 
						}
						break;
					case BleService.MSG_DEVICE_DATA:
						int highpress = msg.getData().getInt("highpress");
						int lowpress = msg.getData().getInt("lowpress");
						int pulse = msg.getData().getInt("pulse");
						savePressureValue(highpress, lowpress, pulse);
						activity.mDisplay.setData(highpress, lowpress,pulse);
						break;
				}
			}
			super.handleMessage(msg);
		}
	}
	private static void savePressureValue(int high, int low, int pulse) {



		DbProvider provider = new DbProvider();
		provider.init(context);

		SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");

		ContentValues contentValues = new ContentValues();
		contentValues.put(DbPressure.Pressure.TIME, df.format(new Date()));
		contentValues.put(DbPressure.Pressure.HIGH, high);
		contentValues.put(DbPressure.Pressure.LOW, low);
		contentValues.put(DbPressure.Pressure.RATE, pulse);

		provider.insert(DbPressure.CONTENT_URI, contentValues);

		//volley
		Map<String, String> param = new HashMap<>();
		param.put("pretime",df.format(new Date()));
		param.put("highpre", String.valueOf(high));
		param.put("lowpre", String.valueOf(low));
		param.put("rate", String.valueOf(pulse));
		VolleyRequestImp volleyRequest = new VolleyRequestImp(param);
		volleyRequest.myVolleyRequestDemo_POST(context);
	}

	private void stateChanged(BleService.State newState) {
		boolean disconnected = mState == BleService.State.CONNECTED;
		mState = newState;
		switch (mState) {
			case SCANNING:
				mRefreshItem.setEnabled(true);
				mDeviceList.setScanning(true);
				break;
			case BLUETOOTH_OFF:
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, ENABLE_BT);
				break;
			case IDLE:
				if (disconnected) {
//					FragmentTransaction tx = getFragmentManager().beginTransaction();
//					tx.replace(R.id.main_content, mDeviceList);
//					tx.commit();
				}
				mRefreshItem.setEnabled(true);
				mDeviceList.setScanning(false);
				break;
			case CONNECTED:
				FragmentTransaction tx = getFragmentManager().beginTransaction();
				tx.replace(R.id.main_content, mDisplay);
				tx.commit();
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ENABLE_BT) {
			if (resultCode == RESULT_OK) {
				startScan();
			} else {
				//The user has elected not to turn on
				//Bluetooth. There's nothing we can do
				//without it, so let's finish().
				finish();
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
}