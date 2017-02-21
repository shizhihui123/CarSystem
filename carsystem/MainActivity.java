package com.topintelligent.carsystem;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements OnClickListener
{
	private SharedPreferences			sharedPrefHost, sharedPrefPort;
	private SharedPreferences.Editor	editorHost, editorPort;
	private Bitmap						photo[]								= new Bitmap[8];
	private final int					NOPORT								= -1;
	private final int					CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE	= 100;
	private String						HOST;
	private int							PORT;
	private int							clickedImgNum;
	private Spinner						spinnerSeatNum, spinnerTriangle;
	private ImageButton					imageButton1, imageButton2,
			imageButton3, imageButton4, imageButton5, imageButton6,
			imageButton7, imageButton8;
	private Button						btnSubmit;
	private EditText					editCarType, editVIN, editEngine,
			editTyre, editIpAdrr, editPort, editCarNum, editCarLength,
			editCarHeight, editCarWidth;
	private TextView					textCurrentIp, textCurrentPort;

	private SettingDialog				dialog;
	private long						exitTime;
	private final String				version								= "1";//0演示版1客户版

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		outState.putInt("clickedImgNum", clickedImgNum);

		for (int i = 0; i < photo.length; i++)
		{
			if (null != photo[i])
			{
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				photo[i].compress(Bitmap.CompressFormat.JPEG, 100, out);
				outState.putByteArray("bitmap" + String.valueOf(i),
						out.toByteArray());
				try
				{
					out.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		Log.d("LOG_TAG", "OnCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sharedPrefHost = getPreferences(Context.MODE_PRIVATE);
		sharedPrefPort = getPreferences(Context.MODE_PRIVATE);
		editorHost = sharedPrefHost.edit();
		editorPort = sharedPrefPort.edit();
		HOST = sharedPrefHost.getString(
				getString(R.string.preference_host_key), null);
		String temp = sharedPrefPort.getString(
				getString(R.string.preference_port_key), null);
		if (null == temp)
		{
			PORT = NOPORT;
		}
		else
		{
			PORT = Integer.valueOf(temp);
		}

		spinnerSeatNum = (Spinner) findViewById(R.id.spinnerSeatNum);
		spinnerTriangle = (Spinner) findViewById(R.id.spinnerTriangle);
		imageButton1 = (ImageButton) findViewById(R.id.imageButton1);
		imageButton1.setOnClickListener(this);
		imageButton2 = (ImageButton) findViewById(R.id.imageButton2);
		imageButton2.setOnClickListener(this);
		imageButton3 = (ImageButton) findViewById(R.id.imageButton3);
		imageButton3.setOnClickListener(this);
		imageButton4 = (ImageButton) findViewById(R.id.imageButton4);
		imageButton4.setOnClickListener(this);
		imageButton5 = (ImageButton) findViewById(R.id.imageButton5);
		imageButton5.setOnClickListener(this);
		imageButton6 = (ImageButton) findViewById(R.id.imageButton6);
		imageButton6.setOnClickListener(this);
		imageButton7 = (ImageButton) findViewById(R.id.imageButton7);
		imageButton7.setOnClickListener(this);
		imageButton8 = (ImageButton) findViewById(R.id.imageButton8);
		imageButton8.setOnClickListener(this);
		editCarType = (EditText) findViewById(R.id.editCarType);
		editCarNum = (EditText) findViewById(R.id.editCarNum);
		editEngine = (EditText) findViewById(R.id.editEngine);
		editTyre = (EditText) findViewById(R.id.editTyre);
		editVIN = (EditText) findViewById(R.id.editVIN);
		editCarHeight = (EditText) findViewById(R.id.editCarHeight);
		editCarLength = (EditText) findViewById(R.id.editCarLength);
		editCarWidth = (EditText) findViewById(R.id.editCarWidth);
		btnSubmit = (Button) findViewById(R.id.btnSubmit);
		btnSubmit.setOnClickListener(this);
		if (null != savedInstanceState)
		{
			clickedImgNum = savedInstanceState.getInt("clickedImgNum");
			for (int i = 0; i < photo.length; i++)
			{
				byte[] bitmapTemp = savedInstanceState.getByteArray("bitmap"
						+ String.valueOf(i));
				if (null != bitmapTemp)
				{
					photo[i] = BitmapFactory.decodeByteArray(bitmapTemp, 0,
							bitmapTemp.length);
					switch (i)
					{
					case 0:
						imageButton1.setImageBitmap(photo[i]);
						break;
					case 1:
						imageButton2.setImageBitmap(photo[i]);
						break;
					case 2:
						imageButton3.setImageBitmap(photo[i]);
						break;
					case 3:
						imageButton4.setImageBitmap(photo[i]);
						break;
					case 4:
						imageButton5.setImageBitmap(photo[i]);
						break;
					case 5:
						imageButton6.setImageBitmap(photo[i]);
						break;
					case 6:
						imageButton7.setImageBitmap(photo[i]);
						break;
					case 7:
						imageButton8.setImageBitmap(photo[i]);
						break;
					}
				}
			}
		}
	}

	@Override
	protected void onRestart()
	{
		Log.d("LOG_TAG", "OnRestart");
		super.onRestart();
	}

	@Override
	protected void onStart()
	{
		Log.d("LOG_TAG", "OnStart");
		super.onStart();
	}

	@Override
	protected void onResume()
	{
		Log.d("LOG_TAG", "OnResume");
		super.onResume();
	}

	@Override
	protected void onPause()
	{
		Log.d("LOG_TAG", "OnPause");
		super.onPause();
	}

	@Override
	protected void onStop()
	{
		Log.d("LOG_TAG", "OnStop");
		super.onStop();
	}

	@Override
	protected void onDestroy()
	{
		Log.d("LOG_TAG", "OnDestory");
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		menu.addSubMenu(0, Menu.FIRST, 0, getString(R.string.action_settings));
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case Menu.FIRST:
			dialog = new SettingDialog();
			dialog.show();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN)
		{
			if ((System.currentTimeMillis() - exitTime) > 2000) // System.currentTimeMillis()无论何时调用，肯定大于2000
			{
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			}
			else
			{
				finish();
				System.exit(0);
			}

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View arg0)
	{
		// start the image capture Intent
		switch (arg0.getId())
		{
		case R.id.btnSubmit:
			if (null == HOST || NOPORT == PORT)
			{
				dialog = new SettingDialog();
				dialog.show();
				if (null == HOST && NOPORT == PORT)
				{
					if (null == HOST)
					{
						Toast.makeText(getApplicationContext(), "必须指定一个发送地址",
								Toast.LENGTH_SHORT).show();
					}
					else
					{
						Toast.makeText(getApplicationContext(), "必须指定一个发送端口",
								Toast.LENGTH_SHORT).show();
					}
				}
			}
			else
			{
				try
				{
					Net netPacket = new Net();
					netPacket.createNetPacketData();
					Thread thd = new Thread(netPacket.sendNetPacket());
					thd.start();
				}
				catch (UnknownHostException e)
				{
					e.printStackTrace();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				BitmapDrawable db = (BitmapDrawable) getResources()
						.getDrawable(R.drawable.pic);
				Bitmap bm = db.getBitmap();
				imageButton1.setImageBitmap(bm);
				imageButton2.setImageBitmap(bm);
				imageButton3.setImageBitmap(bm);
				imageButton4.setImageBitmap(bm);
				imageButton5.setImageBitmap(bm);
				imageButton6.setImageBitmap(bm);
				imageButton7.setImageBitmap(bm);
				imageButton8.setImageBitmap(bm);
				editCarType.setText("");
				editEngine.setText("");
				editVIN.setText("");
				editTyre.setText("");
				editCarNum.setText("");
				editCarLength.setText("");
				editCarWidth.setText("");
				editCarHeight.setText("");
				for (int i = 0; i < photo.length; i++)
				{
					photo[i] = null;
				}
			}
			return;
		case R.id.imageButton1:
			clickedImgNum = R.id.imageButton1;
			break;
		case R.id.imageButton2:
			clickedImgNum = R.id.imageButton2;
			break;
		case R.id.imageButton3:
			clickedImgNum = R.id.imageButton3;
			break;
		case R.id.imageButton4:
			clickedImgNum = R.id.imageButton4;
			break;
		case R.id.imageButton5:
			clickedImgNum = R.id.imageButton5;
			break;
		case R.id.imageButton6:
			clickedImgNum = R.id.imageButton6;
			break;
		case R.id.imageButton7:
			clickedImgNum = R.id.imageButton7;
			break;
		case R.id.imageButton8:
			clickedImgNum = R.id.imageButton8;
			break;
		}
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		Log.d("LOG_TAG", "Take Picture Button Click");
		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode)
	{
		super.startActivityForResult(intent, requestCode);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
		{
			if (resultCode == RESULT_OK)
			{
				Bitmap thumbnail = data.getParcelableExtra("data");
				switch (clickedImgNum)
				{
				case R.id.imageButton1:
					photo[0] = thumbnail;
					imageButton1.setImageBitmap(thumbnail);
					break;
				case R.id.imageButton2:
					photo[1] = thumbnail;
					imageButton2.setImageBitmap(thumbnail);
					if ("0" == version)
					{
						editVIN.setText("LZZAEJND3BC138572");
					}
					break;
				case R.id.imageButton3:
					photo[2] = thumbnail;
					imageButton3.setImageBitmap(thumbnail);
					if ("0" == version)
					{
						editEngine.setText("LC168F-2T53208060003341");
					}
					break;
				case R.id.imageButton4:
					photo[3] = thumbnail;
					imageButton4.setImageBitmap(thumbnail);
					break;
				case R.id.imageButton5:
					photo[4] = thumbnail;
					imageButton5.setImageBitmap(thumbnail);
					break;
				case R.id.imageButton6:
					photo[5] = thumbnail;
					imageButton6.setImageBitmap(thumbnail);
					break;
				case R.id.imageButton7:
					photo[6] = thumbnail;
					imageButton7.setImageBitmap(thumbnail);
					if ("0" == version)
					{
						editCarNum.setText("桂AXK575");
					}
					break;
				case R.id.imageButton8:
					photo[7] = thumbnail;
					imageButton8.setImageBitmap(thumbnail);
					break;
				default:
					break;
				}
				Toast.makeText(this, "图片保存成功", Toast.LENGTH_SHORT).show();
			}
			else if (resultCode == RESULT_CANCELED)
			{
				// User cancelled the image capture
			}
			else
			{
				// Image capture failed, advise user
			}
		}
	}

	/**
	 * 网络中要传送的数据包
	 */
	public class Net
	{
		private int		packetSize;
		private byte[]	netPacketData;
		private byte[]	netPacketHead;
		private byte[]	netPacket;

		public Net()
		{
			netPacketHead = new byte[5];
		}

		private void makeTcpPacket()
		{
			netPacket = new byte[netPacketData.length + netPacketHead.length];
			for (int i = 0; i < netPacketHead.length; i++)
			{
				netPacket[i] = netPacketHead[i];
			}
			for (int i = 0; i < netPacketData.length; i++)
			{
				netPacket[netPacketHead.length + i] = netPacketData[i];
			}
		}

		/**
		 * 制作网包头部段
		 * 
		 * @throws IOException
		 */
		private void makeNetPacketHead() throws IOException
		{
			byte[] tempHead = new byte[4];
			int tempSize = getPacketSize();
			Log.d("LOG_TAG", "packet size:" + String.valueOf(tempSize));
			tempHead = intToByte(tempSize);

			for (int i = 0; i < 4; i++)
			{
				netPacketHead[1 + i] = tempHead[i];
			}
			netPacketHead[0] = '@';
			System.out.println(netPacketHead[0]);
			System.out.println(netPacketHead[1]);
			System.out.println(netPacketHead[2]);
			System.out.println(netPacketHead[3]);
			System.out.println(netPacketHead[4]);
		}

		/**
		 * 获取网包数据段的大小
		 */
		public int getPacketSize()
		{
			packetSize = netPacketData.length;
			return packetSize;
		}

		/**
		 * 发送数据包
		 */
		public Runnable sendNetPacket() throws UnknownHostException,
				IOException
		{
			makeNetPacketHead();
			makeTcpPacket();
			Runnable sendMessage = new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						Log.d("LOG_TAG", "socket:send netpacket");
						Socket s = new Socket(HOST, PORT);
						Log.d("LOG_TAG",
								"remote socket " + s.getRemoteSocketAddress());
						OutputStream out = s.getOutputStream();
						InputStream in = s.getInputStream();
						out.write(netPacket);
						byte[] tempBuffer = new byte[300];
						in.read(tempBuffer);
						Log.d("LOG_TAG",
								"from server: " + tempBuffer.toString());
						in.read(tempBuffer);
						Log.d("LOG_TAG",
								"from server: " + tempBuffer.toString());
						String tempStr = new String(tempBuffer, "UTF-8");
						if (tempStr
								.contains("<detail>\'无车车任何记录\' detail</result>"))
						{
							Looper.prepare();
							new AlertDialog.Builder(MainActivity.this)
									.setTitle("提示").setMessage("无此车信息")
									.setPositiveButton("确定", null).show();
							Looper.loop();
						}
						else
						{
							Looper.prepare();
							new AlertDialog.Builder(MainActivity.this)
									.setTitle("提示").setMessage("发送成功")
									.setPositiveButton("确定", null).show();
							Looper.loop();
						}
						out.flush();
						out.close();
						in.close();
						s.close();
					}
					catch (Exception e)
					{
						Looper.prepare();
						Toast.makeText(MainActivity.this, "发送失败,网络连接失败",
								Toast.LENGTH_LONG).show();
						Looper.loop();
						e.printStackTrace();
					}
				}
			};
			return sendMessage;
		}

		/**
		 * 在网包中创建XML格式数据化数据段
		 * */
		public void createNetPacketData() throws Exception
		{
			Xml xml = new Xml();
			xml.startTag("?xml version=\"1.0\" encoding=\"utf-8\"?");
			xml.startTag("Message ID=\"1\" type=\"submit\" version=\"1.0\" encodeType=\"base64\"");

			xml.startTag("carType");
			xml.text(editCarType.getText().toString());
			xml.endTag("carType");

			xml.startTag("img1");
			if (photo[0] != null)
			{
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				photo[0].compress(Bitmap.CompressFormat.JPEG, 100, out);
				xml.text(Base64.encodeToString(out.toByteArray(),
						Base64.DEFAULT));
				out.close();
			}
			xml.endTag("img1");

			xml.startTag("VIN");
			xml.text(editVIN.getText().toString());
			xml.endTag("VIN");

			xml.startTag("img2");
			if (photo[1] != null)
			{
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				photo[1].compress(Bitmap.CompressFormat.JPEG, 100, out);
				xml.text(Base64.encodeToString(out.toByteArray(),
						Base64.DEFAULT));
				out.close();
			}
			xml.endTag("img2");

			xml.startTag("engine");
			xml.text(editEngine.getText().toString());
			xml.endTag("engine");

			xml.startTag("img3");
			if (photo[2] != null)
			{
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				photo[2].compress(Bitmap.CompressFormat.JPEG, 100, out);
				xml.text(Base64.encodeToString(out.toByteArray(),
						Base64.DEFAULT));
				out.close();
			}
			xml.endTag("img3");

			xml.startTag("seatNum");
			xml.text(spinnerSeatNum.getSelectedItem().toString());
			xml.endTag("seatNum");

			xml.startTag("img4");
			if (photo[3] != null)
			{
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				photo[3].compress(Bitmap.CompressFormat.JPEG, 100, out);
				xml.text(Base64.encodeToString(out.toByteArray(),
						Base64.DEFAULT));
				out.close();
			}
			xml.endTag("img4");

			xml.startTag("tyre");
			xml.text(editTyre.getText().toString());
			xml.endTag("tyre");

			xml.startTag("img5");
			if (photo[4] != null)
			{
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				photo[4].compress(Bitmap.CompressFormat.JPEG, 100, out);
				xml.text(Base64.encodeToString(out.toByteArray(),
						Base64.DEFAULT));
				out.close();
			}
			xml.endTag("img5");

			xml.startTag("triangle");
			if (spinnerTriangle.getSelectedItem().toString().equals("有"))
			{
				xml.text("have");
			}
			else
			{
				xml.text("no");
			}
			xml.endTag("triangle");

			xml.startTag("img6");
			if (photo[5] != null)
			{
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				photo[5].compress(Bitmap.CompressFormat.JPEG, 100, out);
				xml.text(Base64.encodeToString(out.toByteArray(),
						Base64.DEFAULT));
				out.close();
			}
			xml.endTag("img6");

			xml.startTag("carNum");
			xml.text(editCarNum.getText().toString());
			xml.endTag("carNum");

			xml.startTag("img7");
			if (photo[6] != null)
			{
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				photo[6].compress(Bitmap.CompressFormat.JPEG, 100, out);
				xml.text(Base64.encodeToString(out.toByteArray(),
						Base64.DEFAULT));
				out.close();
			}
			xml.endTag("img7");

			xml.startTag("carSize");
			xml.text(editCarLength.getText().toString() + "X" + editCarWidth.getText().toString() + "X" + editCarHeight.getText().toString());
			xml.endTag("carSize");

			xml.startTag("img8");
			if (photo[7] != null)
			{
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				photo[7].compress(Bitmap.CompressFormat.JPEG, 100, out);
				xml.text(Base64.encodeToString(out.toByteArray(),
						Base64.DEFAULT));
				out.close();
			}
			xml.endTag("img8");

			xml.endTag("Message");
			byte[] xmlData = xml.toByteArr();
			netPacketData = xmlData;
		}
	}

	/**
	 * 创建XML流
	 * */
	public class Xml
	{
		private String	str;

		public Xml()
		{
			str = new String();
		}

		public void startTag(String tag)
		{
			str += '<' + tag + '>';
		}

		public void endTag(String tag)
		{
			str = str + '<' + '/' + tag + '>';
		}

		public void text(String temp)
		{
			str += temp;
		}

		public byte[] toByteArr()
		{
			return str.getBytes();
		}

		public void encode(String encodeWay)
				throws UnsupportedEncodingException
		{
			str = new String(str.getBytes(), encodeWay);
		}
	}

	/**
	 * 自定义化的对话框类
	 */
	public class SettingDialog
	{
		View	diaLogView;

		public SettingDialog()
		{
			diaLogView = LayoutInflater.from(MainActivity.this).inflate(
					R.layout.dialog_main, null);
			textCurrentIp = (TextView) diaLogView.findViewById(R.id.currentIp);
			textCurrentPort = (TextView) diaLogView
					.findViewById(R.id.currentPort);
			editIpAdrr = (EditText) diaLogView.findViewById(R.id.editIpAddr);
			editPort = (EditText) diaLogView.findViewById(R.id.editPort);

		}

		/**
		 * 显示设置IP/端口对话框
		 */
		public void show()
		{
			if (null == HOST)
			{
				textCurrentIp.setText("当前Ip地址:" + "无");
			}
			else
			{
				textCurrentIp.setText("当前Ip地址:" + HOST);
			}
			if (-1 == PORT)
			{
				textCurrentPort.setText("当前端口:" + "无");
			}
			else
			{
				textCurrentPort.setText("当前端口:" + PORT);
			}
			new AlertDialog.Builder(MainActivity.this)
					.setTitle(getString(R.string.dialogTitle))
					.setView(diaLogView)
					.setPositiveButton(getString(R.string.dialogBtnSure),
							new DialogInterface.OnClickListener()
							{

								@Override
								public void onClick(DialogInterface dialog,
										int which)
								{
									if (editIpAdrr.getText().toString()
											.equals("")
											|| editPort.getText().toString()
													.equals(""))
									{
										if (editIpAdrr.getText().toString()
												.equals(""))
										{
											Toast.makeText(
													getApplicationContext(),
													"Ip地址不能为空",
													Toast.LENGTH_LONG).show();
										}
										else
										{
											Toast.makeText(
													getApplicationContext(),
													"端口不能为空", Toast.LENGTH_LONG)
													.show();
										}
									}
									else
									{
										HOST = editIpAdrr.getText().toString();
										PORT = Integer.parseInt(editPort
												.getText().toString());
										editorHost
												.putString(
														getString(R.string.preference_host_key),
														HOST);
										editorPort
												.putString(
														getString(R.string.preference_port_key),
														String.valueOf(PORT));
										editorHost.commit();
										editorPort.commit();
										Toast.makeText(getApplicationContext(),
												"设置成功", Toast.LENGTH_LONG)
												.show();
									}
								}
							})
					.setNegativeButton(getString(R.string.dialogBtnCancel),
							null).show();
		}
	}

	/**
	 * int转换为byte数组,tcp data段转换为byte写入tcp头在server端进行识别
	 * */
	public static byte[] intToByte(int number)
	{

		byte[] abyte = new byte[4];
		abyte[0] = (byte) ((0xff000000 & number) >> 24);
		abyte[1] = (byte) ((0xff0000 & number) >> 16);
		abyte[2] = (byte) ((0xff00 & number) >> 8);
		abyte[3] = (byte) (0xff & number);
		return abyte;
	}

}
