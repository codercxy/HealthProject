package com.nju.android.health.bluetooth;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
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
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

@SuppressLint("NewApi")
public class BleService extends Service implements BluetoothAdapter.LeScanCallback {
	public static final String TAG = "BleService";
	static final int MSG_REGISTER = 1;
	static final int MSG_UNREGISTER = 2;
	static final int MSG_START_SCAN = 3;
	static final int MSG_STATE_CHANGED = 4;
	static final int MSG_DEVICE_FOUND = 5;
	static final int MSG_DEVICE_CONNECT = 6;
	static final int MSG_DEVICE_DISCONNECT = 7;
	static final int MSG_DEVICE_DATA = 8;

	private static final long SCAN_PERIOD = 3000;

	public static final String KEY_NAME_ADDRESSES = "KEY_NAME_ADDRESSES";

//	private static final String DEVICE_NAME = "SensorTag";
	private static final UUID UUID_BLOODPRESSURE_SERVICE  = UUID.fromString("00001000-0000-1000-8000-00805f9b34fb");
	private static final UUID UUID_BLOODPRESSURE_READABLE = UUID.fromString("00001003-0000-1000-8000-00805f9b34fb");
	private static final UUID UUID_BLOODPRESSURE_WRITEABLE = UUID.fromString("00001001-0000-1000-8000-00805f9b34fb");
	private static final UUID UUID_BLOODPRESURE_CONF = UUID.fromString("00001002-0000-1000-8000-00805f9b34fb");
	private static final UUID UUID_CLIENT_CHARACTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

	private static final Queue<Object> sWriteQueue = new ConcurrentLinkedQueue<Object>();
	private static boolean sIsWriting = false;

	private final IncomingHandler mHandler;
	private final Messenger mMessenger;
	private final List<Messenger> mClients = new LinkedList<Messenger>();
	private final Map<String, BluetoothDevice> mDevices = new HashMap<String, BluetoothDevice>();
	private BluetoothGatt mGatt = null;
	
	private BluetoothGattService bloodpressService;
	private BluetoothGattCharacteristic bloodpressReadCharacteristic;
	private BluetoothGattCharacteristic bloodpressWriteCharacteristic;
	private BluetoothGattCharacteristic bloodpressConf;
	
	private boolean packetStart = false;			//新包是否开始标志变量
	private int packetLength=0;						//包长
	private ArrayList<Byte> tempPacket = new ArrayList<Byte>();		//组包过程中的临时包
	private boolean data_available= false;			//是否收到结果
	
	private String macaddress;

	public enum State {
		UNKNOWN,
		IDLE,
		SCANNING,
		BLUETOOTH_OFF,
		CONNECTING,
		CONNECTED,
		DISCONNECTING
	}

	private BluetoothAdapter mBluetooth = null;
	private State mState = State.UNKNOWN;

	private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
			super.onConnectionStateChange(gatt, status, newState);
			Log.v(TAG, "Connection State Changed: " + (newState == BluetoothProfile.STATE_CONNECTED ? "Connected" : "Disconnected"));
			if (newState == BluetoothProfile.STATE_CONNECTED) {
				setState(State.CONNECTED);
				gatt.discoverServices();
			} else {
				setState(State.IDLE);
			}
		}

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			Log.v(TAG, "onServicesDiscovered: " + status);
			if (status == BluetoothGatt.GATT_SUCCESS) {
				subscribe(gatt);
			}
		}

		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
//			Log.v(TAG, "onCharacteristicWrite: " + status);
			sIsWriting = false;
			nextWrite();
		}

		@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
		@SuppressLint("NewApi")
		@Override
		public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
			Log.v(TAG, "onDescriptorWrite: " + status);
			sIsWriting = false;
			nextWrite();
		}
		
		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
			Log.v(TAG, "onCharacteristicChanged UUID is: " + characteristic.getUuid());
			if (characteristic.getUuid().equals(UUID_BLOODPRESURE_CONF)) {
				
				byte [] data = characteristic.getValue();
				pack(data);
				
			}
		}
	};
	
	/**
	 * 
	 * ��notify���Ի�õ��������
	 * 
	 */
	private void pack(byte[] data){
		byte[] packet;
		int i,j;

		//获取包长
		if(!packetStart){									//开始一个新包
			tempPacket.clear();
			//设备发出的所有包：信息、开始、过程、结果、结束包的起始码都是0x55
			if(data[0] != 0x55){
				Log.e(TAG, "packet error:"+"��ʼ�����");
				return;
//				finish();
			}
			if(data.length>=2)
				packetLength=data[1];
			else
				packetLength=17;						//数据太少，无法获得包长，设一个比较大的值
		}
		else{											//一个包还没结束
			if(tempPacket.size()==1)					//目前只有一个字节
				packetLength=data[0];
			else
				packetLength = tempPacket.get(1);
		}
		
		//���
		if(tempPacket.size()+data.length == packetLength){			//刚好一个完整的包
			packet = new byte[packetLength];
			for(i=0; i<tempPacket.size(); i++)
				packet[i] = tempPacket.get(i);
			for(j=0; i<packetLength; i++,j++)
				packet[i] = data[j];
			unpack(packet);
			packetStart = false;
		}
		else if(tempPacket.size()+data.length < packetLength){		//不够一个包
			for(byte d:data)
				tempPacket.add(d);
			packetStart = true;
		}
		else{														//多于一个包
			packet = new byte[packetLength];
			for(i=0; i<tempPacket.size(); i++)
				packet[i] = tempPacket.get(i);
			for(j=0; i<packetLength; i++,j++)
				packet[i] = data[j];
			unpack(packet);
			packetStart = true;
			
			tempPacket.clear();
			for(; j<data.length; j++)
				tempPacket.add(data[j]);
		    //ʣ�ಿ�ֻ�����һ�������İ�
			if(tempPacket.size()>2 && tempPacket.size()==tempPacket.get(1)){
				packet = new byte[tempPacket.size()];
				for(i=0; i<tempPacket.size(); i++)
					packet[i] = tempPacket.get(i);
				unpack(packet);
				packetStart = false;
			}
			else if(tempPacket.size()>2 && tempPacket.size()>tempPacket.get(1)){
				Log.e(TAG, "packet error:"+"notify特性获取的数据含有两个以上的包");						//notify特性获取的数据含有两个以上的包
			}
			
		}
	}
	
	/**
	 * 
	 * 
	 * 包的解析
	 * 
	 */
	private void unpack(byte[] packet){
		
		Log.d(TAG, "the type code of recieved packet is 0"+packet[2]);
		if(packet[2]==0x00){								//information packet				
			sendResponse((byte)5);
		}
		else if(packet[2]==0x01){							//start packet				
			sendResponse((byte)5);
		}
		else if(packet[2]==0x02){							//procedure packet
//			sendResponse((byte)5);
			
		}
		else if(packet[2]==0x03 && !data_available){							//result packet
			Log.d(TAG, "packet length is:"+packet[1]);
			data_available = true;
			int highpress = shortUnsignedAtOffset(bloodpressConf, 8);
			int lowpress = bloodpressConf.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 10);
			int pulse = bloodpressConf.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 11);
			Log.d(TAG, "value: "+highpress+" "+lowpress+" "+pulse);
			Message msg = Message.obtain(null, MSG_DEVICE_DATA);
			Bundle bundle = new Bundle();
			bundle.putInt("highpress", highpress);
			bundle.putInt("lowpress", lowpress);
			bundle.putInt("pulse", pulse);
			msg.setData(bundle);
			sendMessage(msg);
			sendResponse((byte)5);
		}
		else if(packet[2]==0x04){						//结束包
			sendResponse((byte)6);
			if (mState == State.CONNECTED && mGatt != null) {
				mGatt.disconnect();
			}
			Log.d(TAG, "断开蓝牙连接");
		}
		else{
			Log.e(TAG, "packet error:"+"组包或解析错误");
		}
		
	}

	public BleService() {
		mHandler = new IncomingHandler(this);
		mMessenger = new Messenger(mHandler);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mMessenger.getBinder();
	}

	private static class IncomingHandler extends Handler {
		private final WeakReference<BleService> mService;

		public IncomingHandler(BleService service) {
			mService = new WeakReference<BleService>(service);
		}

		@Override
		public void handleMessage(Message msg) {
			BleService service = mService.get();
			if (service != null) {
				switch (msg.what) {
					case MSG_REGISTER:
						service.mClients.add(msg.replyTo);
						Log.d(TAG, "Registered");
						break;
					case MSG_UNREGISTER:
						service.mClients.remove(msg.replyTo);
						Log.d(TAG, "UnRegistered");
						break;
					case MSG_START_SCAN:
						service.startScan();
						Log.d(TAG, "Start Scan");
						break;
					case MSG_DEVICE_CONNECT:
						service.connect((String) msg.obj);
						break;
					case MSG_DEVICE_DISCONNECT:
						if (service.mState == State.CONNECTED && service.mGatt != null) {
							service.mGatt.disconnect();
						}
						break;
					default:
						super.handleMessage(msg);
				}
			}
		}
	}

	private void startScan() {
		mDevices.clear();
		setState(State.SCANNING);
		if (mBluetooth == null) {
			BluetoothManager bluetoothMgr = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
			mBluetooth = bluetoothMgr.getAdapter();
		}
		if (mBluetooth == null || !mBluetooth.isEnabled()) {
			setState(State.BLUETOOTH_OFF);
		} else {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					if (mState == State.SCANNING) {
						mBluetooth.stopLeScan(BleService.this);
						setState(State.IDLE);
					}
				}
			}, SCAN_PERIOD);
			mBluetooth.startLeScan(this);
		}
	}

	@Override
	public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
		if (device != null && !mDevices.containsValue(device) && device.getName() != null) {//筛选条件，发现符合条件的设备
			mDevices.put(device.getAddress(), device);
			macaddress = device.getAddress();
			Message msg = Message.obtain(null, MSG_DEVICE_FOUND);
			if (msg != null) {
				Bundle bundle = new Bundle();
				String[] name_addresses = mDevices.keySet().toArray(new String[mDevices.size()]);
				bundle.putStringArray(KEY_NAME_ADDRESSES, name_addresses);
				msg.setData(bundle);
				sendMessage(msg);
			}
			Log.d(TAG, "Added " + device.getName() + ": " + device.getAddress());
		}
	}

	public void connect(String macAddress) {
		if(!macAddress.equals(macaddress))
			Log.d(TAG, macaddress+"!="+macAddress);
		Log.d(TAG, macAddress);
		BluetoothDevice device = mDevices.get(macAddress);
		if (device != null) {
			mGatt = device.connectGatt(this, false, mGattCallback);
		}
	}

	private void subscribe(BluetoothGatt gatt) {
		bloodpressService = gatt.getService(UUID_BLOODPRESSURE_SERVICE);
		if (bloodpressService != null) {
			bloodpressReadCharacteristic = bloodpressService.getCharacteristic(UUID_BLOODPRESSURE_READABLE);
			bloodpressWriteCharacteristic = bloodpressService.getCharacteristic(UUID_BLOODPRESSURE_WRITEABLE);
			bloodpressConf = bloodpressService.getCharacteristic(UUID_BLOODPRESURE_CONF);
			if (bloodpressReadCharacteristic != null && bloodpressConf != null && bloodpressWriteCharacteristic != null) {
				BluetoothGattDescriptor config = bloodpressConf.getDescriptor(UUID_CLIENT_CHARACTERISTIC_CONFIG);
				if (config != null  ) {
					gatt.setCharacteristicNotification(bloodpressConf, true);
					config.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
					write(config);
					
					//send 05 response packet
					sendResponse((byte)5);
					
				}
				else{
					Log.d("subscribe", "fail");
				}
			}
		}
	}
	
	private void sendResponse(byte type){
		
		//get date
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int date = c.get(Calendar.DATE);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		
		byte[] response = new byte[11];
		response[0] = 0x5a;				
		response[1] = 0x0b;			
		response[2] = type;					
		response[3] = (byte)(year%100);	
		response[4] = (byte)month;				
		response[5] = (byte)date;				
		response[6] = (byte)hour;				
		response[7] = (byte)minute;				
		int sum=0;
		for(int i=0; i<8; i++)
			sum += response[i];
		response[8] = (byte) (sum & 0xff);
		response[9] = (byte) ((sum>>8) & 0xff);
		response[10] = (byte) ((sum>>16) & 0xff);
		
		bloodpressWriteCharacteristic.setValue(response);
		Log.d(TAG, "write 05 response packet ");
		write(bloodpressWriteCharacteristic);
	}

	private synchronized void write(Object o) {
		if (sWriteQueue.isEmpty() && !sIsWriting) {
			doWrite(o);
		} else {
			sWriteQueue.add(o);
		}
	}

	private synchronized void nextWrite() {
		if (!sWriteQueue.isEmpty() && !sIsWriting) {
			doWrite(sWriteQueue.poll());
		}
	}

	private synchronized void doWrite(Object o) {
		if (o instanceof BluetoothGattCharacteristic) {
			sIsWriting = true;
			mGatt.writeCharacteristic((BluetoothGattCharacteristic) o);
		} else if (o instanceof BluetoothGattDescriptor) {
			sIsWriting = true;
			mGatt.writeDescriptor((BluetoothGattDescriptor) o);
		} else {
			nextWrite();
		}
	}

	private void setState(State newState) {
		if (mState != newState) {
			mState = newState;
			Message msg = getStateMessage();
			if (msg != null) {
				sendMessage(msg);
			}
		}
	}

	private Message getStateMessage() {
		Message msg = Message.obtain(null, MSG_STATE_CHANGED);
		if (msg != null) {
			msg.arg1 = mState.ordinal();
		}
		return msg;
	}

	private void sendMessage(Message msg) {
		for (int i = mClients.size() - 1; i >= 0; i--) {
			Messenger messenger = mClients.get(i);
			if (!sendMessage(messenger, msg)) {
				mClients.remove(messenger);
			}
		}
	}

	private boolean sendMessage(Messenger messenger, Message msg) {
		boolean success = true;
		try {
			messenger.send(msg);
		} catch (RemoteException e) {
			Log.w(TAG, "Lost connection to client", e);
			success = false;
		}
		return success;
	}

	private static Integer shortUnsignedAtOffset(BluetoothGattCharacteristic characteristic, int offset) {
		Integer lowerByte = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, offset);
		Integer upperByte = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, offset + 1);

		return (upperByte << 8) + lowerByte;
	}
}