/**
 * 作者：潘晓伟
 * 修改时间：2015-7-16
 * 备注:版权所有深圳市美的连医疗电子股份有限公司
 */
package com.nju.android.health.bswk;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import com.nju.android.health.views.activities.next.AddXyrecorder;

import java.util.UUID;


@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BluetoothLEService extends Service {
	private static UUID ACS_SERVICE_UUID;				//服务连接
	private static UUID DATA_LINE_UUID; 				//服务监听、写入
	private static UUID MAIN_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
	
	private static final int STATE_NONE = 0;
	private static final int STATE_SCANNING = 1;
	private static final int STATE_CONNECTING = 2;
	private static final int STATE_CONNECTED = 3;
	private static final int STATE_DISCONNECTED = 4;
	
	private static int bluetoothState = STATE_NONE;		
	
	private static boolean isConnected;					
	
	private static BluetoothManager mBluetoothManager;
	private static BluetoothAdapter mBluetoothAdapter;
	private static BluetoothGatt mBluetoothGatt;
	
	private static String deviceName;
	private static String deviceAddress;
	private static String needConnectDeviceName1 = "ZK";
	private static String needConnectDeviceName2 = "MEDXING-NIBP";

	private final IBinder binder = new LocalBinder();
	
	public final static String ACTION_BLUETOOTH_CONNECTED = "com.bluetooth.ACTION_BLUETOOTH_CONNECTED";
	public final static String ACTION_BLUETOOTH_DISCONNECTED = "com.bluetooth.ACTION_BLUETOOTH_DISCONNECTED";
	public final static String ACTION_BLUETOOTH_DATAARRIVED = "com.bluetooth.ACTION_BLUETOOTH_DATAARRIVED";
	public final static String ACTION_BLUETOOTH_DEVICE_FOUND = "com.bluetooth.ACTION_BLUETOOTH_DEVICE_FOUND";
	private static final int REQUEST_ENABLE_BT = 1;
	
	public String getDeviceAddress() {
		return deviceAddress;
	}
	
	public String getDeviceName() {
		return deviceName;
	}
	
	public void setDeviceName(String name) {
		deviceName = name;
	}
	
	public class LocalBinder extends Binder {
		public BluetoothLEService getService() {
			return BluetoothLEService.this;

		}
	}

	@Override
	public void onCreate() {
		Initialize();
		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return binder;
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		Close();
		return super.onUnbind(intent);
	}

	private void SendBroadcast(final String action) {
		final Intent intent = new Intent(action);
		sendBroadcast(intent);
	}
	
	private void SendDataArrivedBroadcast(final String action, byte[] data) {
		Intent intent = new Intent(action);
		intent.putExtra(ACTION_BLUETOOTH_DATAARRIVED, data);
		sendBroadcast(intent);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	private boolean Initialize() {
		
		if (mBluetoothManager == null) {
			mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
			if (mBluetoothManager == null) {
				return false;
			}
		}
		mBluetoothAdapter = mBluetoothManager.getAdapter();
		if (mBluetoothAdapter == null) {
			return false;
		}
		return true;
	}

	public boolean IsOpened(Activity activity) {
		if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			activity.startActivityForResult(enableBtIntent,REQUEST_ENABLE_BT);
			return false;
		} else {
			return true;
		}
	}
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	public void Scan(boolean status) {
		if (status && mBluetoothAdapter.isEnabled()) {
			if (!mBluetoothAdapter.isDiscovering() && bluetoothState != STATE_SCANNING && !isConnected) {
				bluetoothState = STATE_SCANNING;
				mBluetoothAdapter.startLeScan(mLeScanCallback);
			}
		} else {
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
		}
	}
	
	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
		// 将扫描到的设备发 关到前台
		@Override
		public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
			if(device.getName() != null){
					if (device.getName().equals(needConnectDeviceName1)) {
						deviceName = device.getName();
						deviceAddress = device.getAddress();
						ACS_SERVICE_UUID = UUID.fromString("0000FFE0-0000-1000-8000-00805f9b34fb");
						DATA_LINE_UUID = UUID.fromString("0000FFE1-0000-1000-8000-00805f9b34fb");
						AddXyrecorder.bluetoothCommand = "BEB001C036";
						SendBroadcast(ACTION_BLUETOOTH_DEVICE_FOUND);
					}else if(device.getName().equals(needConnectDeviceName2)){
						deviceName = device.getName();
						deviceAddress = device.getAddress();
						ACS_SERVICE_UUID = UUID.fromString("0000FFB0-0000-1000-8000-00805f9b34fb");
						DATA_LINE_UUID = UUID.fromString("0000FFB2-0000-1000-8000-00805f9b34fb");
						AddXyrecorder.bluetoothCommand = "FFFA04A501A0";
						SendBroadcast(ACTION_BLUETOOTH_DEVICE_FOUND);
					}
			}
		}
	};
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	public boolean Connect(final String address) {
		
		if(bluetoothState == STATE_CONNECTING || isConnected){
			return false;
		}
		bluetoothState = STATE_CONNECTING;
		
		Scan(false);
		if (mBluetoothAdapter == null || address == null || address.length() <= 0) {
			return false;
		}
		
		final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
		if (device == null) {
			return false;
		}
		
		if (mBluetoothGatt != null) {
			mBluetoothGatt.close();
			mBluetoothGatt = null;
		}
		
		mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
			
		return true;
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	public boolean writePackage(byte[] data) {
		boolean result = false;
		if (mBluetoothGatt == null) {
			return result;
		}
		
		BluetoothGattService ACSService = mBluetoothGatt.getService(ACS_SERVICE_UUID);
		if (ACSService == null) {
			return result;
		}
		BluetoothGattCharacteristic characteristic = ACSService.getCharacteristic(DATA_LINE_UUID);
		if (characteristic == null) {
			return result;
		}
		characteristic.setValue(data);
		result = mBluetoothGatt.writeCharacteristic(characteristic);

		return result;
	}
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	public void Disconnect() {
		if (mBluetoothGatt == null) {
			return;
		}
		mBluetoothGatt.disconnect();
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	private void Close() {
		if (mBluetoothGatt == null) {
			return;
		}
		Disconnect();
		mBluetoothGatt.close();
		mBluetoothGatt = null;
		
	}

	private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status,
											int newState) {
			if (newState == BluetoothProfile.STATE_CONNECTED) {
				mBluetoothGatt.discoverServices();
			} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
				isConnected = false;	
				bluetoothState = STATE_DISCONNECTED;
				Disconnect();
				SendBroadcast(ACTION_BLUETOOTH_DISCONNECTED);
			}
		}

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			if (status == BluetoothGatt.GATT_SUCCESS) {
				boolean isDiscoveredServices = setACSNotification(DATA_LINE_UUID);
				if (isDiscoveredServices) {
					Scan(false);
					deviceName = gatt.getDevice().getName();
					deviceAddress = gatt.getDevice().getAddress();
					isConnected = true;
					bluetoothState = STATE_CONNECTED;
					SendBroadcast(ACTION_BLUETOOTH_CONNECTED);
				}
			}
		}

		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
			byte[] data = characteristic.getValue();
			SendDataArrivedBroadcast(ACTION_BLUETOOTH_DATAARRIVED,data);
		}
	};
	
	private boolean setACSNotification(UUID charaUUID) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			return false;
		}
		BluetoothGattCharacteristic chara = getACSCharacteristic(charaUUID);
		if (chara == null) {
			return false;
		}
		return setCharacteristicNotification(chara, true);
	}

	private boolean setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			return false;
		}
		mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
		BluetoothGattDescriptor descriptor = characteristic.getDescriptor(MAIN_UUID);
		descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
		return mBluetoothGatt.writeDescriptor(descriptor);
	}

	private BluetoothGattCharacteristic getACSCharacteristic(UUID charaUUID) {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			return null;
		}
		BluetoothGattService service = mBluetoothGatt.getService(ACS_SERVICE_UUID);
		if (service == null) {
			return null;
		}
		BluetoothGattCharacteristic chara = service.getCharacteristic(charaUUID);
		return chara;
	}
}
