package jp.egaonohon.android.sensorsample2;

import java.util.List;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

/*
 * 動かしているスマホに内蔵されているセンサが拾う数値を画面に表示。
 */

public class SensorActivity extends Activity implements SensorEventListener {// SensorEventListenerを実装している。
	/*
	 * まず、センサー関係を管理するSensorManagerをメンバ変数として宣言。 各メソッドから使えるようにしておく目的。
	 */
	private SensorManager sensorManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
		 * SensorManagerのインスタンスを取得 センサーを利用するには、まずこのSensorManagerインスタンスが必要。
		 * SensorManagerインスタンスの取得はnewではない。
		 */
		sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);// thisはcontext
		setContentView(R.layout.main);// レイアウト呼び出し
	}

	@Override
	protected void onResume() {
		super.onResume();
		// .getSensorList(Sensor.TYPE_ALL)ですべてのセンサーの種類を引っ張ってる。
		List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
		/*
		 * 取得したセンサーの種類別に【変化あったリスナー】を設定。for文で回しているのでセンサーの数だけ登録している。
		 *
		 * 【SensorManager.registerListenerの引数】
		 * 第1引数SensorEventListener。今回、このアクティビティ自体が実装しているのでthis。 第2引数Sensor
		 * sensor。イベントリスナーを登録するセンサー。ここでは、すべてのセンサーが拡張for文で次々に入る「s」。 第3引数int
		 * rateUs（センサー感度）
		 *
		 * 定数で入れる感度では微妙な場合はint値を直接入れられる（SensorManager.から書き換え）。
		 *
		 * ここで取得したセンサーすべてに、sensorManagerクラスのregisterListener()メソッドでリスナー登録する。
		 * そうすることですべてのセンサーの起動をかけている。
		 * Sensorに、このイベントリスナーを登録することで、そのセンサーの値が変更されるとイベントが呼び出され処理されるようになります。
		 *
		 * SENSOR_DELAY_NORMALはセンサー感度の設定。
		 *
		 * 【感度は次の4つから選べる】 int SENSOR_DELAY_FASTEST get sensor data as fast as
		 * possible （いちばん高感度・0ms） int SENSOR_DELAY_GAME rate suitable for games
		 * (20ms) int SENSOR_DELAY_UI rate suitable for the user interface
		 * （ゆっくり・60ms） int SENSOR_DELAY_NORMAL rate (default) suitable for
		 * screen orientation changes (一番ゆっくり200ms)
		 */
		for (Sensor s : sensors) {
			sensorManager.registerListener(this, s,
					SensorManager.SENSOR_DELAY_NORMAL);
		}
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// このメソッドはあまりオーバーライドしない。
	}

	/*
	 * このアクティビティが実装しているSensorEventListenerのメソッドであるonSensorChanged()メソッド。
	 * このメソッドは、登録したセンサーが変化する度に呼ばれるコールバックメソッド。 複数のセンサーがみなここを利用する設定にした。
	 * （SensorManager.registerListenerの第1引数をthisにしたから?　という訳でもなさそうだが…）
	 */
	public void onSensorChanged(SensorEvent e) {
		switch (e.sensor.getType()) {// .sensor.getType()でセンサーのタイプ別に振り分け。
		// 加速度
		case Sensor.TYPE_ACCELEROMETER: {
			TextView x = (TextView) findViewById(R.id.x);// valuesは配列。その配列から引っ張ってくる。
			x.setText("x:" + String.valueOf(e.values[0]));
			TextView y = (TextView) findViewById(R.id.y);
			y.setText("y:" + String.valueOf(e.values[1]));
			TextView z = (TextView) findViewById(R.id.z);
			z.setText("z:" + String.valueOf(e.values[2]));
			break;
		}

		//明るさ
		case Sensor.TYPE_LIGHT:{
			TextView x = (TextView) findViewById(R.id.brightness);
			x.setText("brightness:" + String.valueOf(e.values[0]));
			break;
		}

		// 傾き
//		case Sensor.TYPE_ORIENTATION: {
//			TextView x = (TextView) findViewById(R.id.brightness);
//			x.setText("Azimuth:" + String.valueOf(e.values[0]));
//			TextView y = (TextView) findViewById(R.id.pitch);
//			y.setText("Pitch:" + String.valueOf(e.values[1]));
//			TextView z = (TextView) findViewById(R.id.roll);
//			z.setText("Roll:" + String.valueOf(e.values[2]));
//			break;
//		}
		// 磁気
		case Sensor.TYPE_MAGNETIC_FIELD: {
			TextView x = (TextView) findViewById(R.id.magnetic_x);
			x.setText("x:" + String.valueOf(e.values[0]));
			TextView y = (TextView) findViewById(R.id.magnetic_y);
			y.setText("y:" + String.valueOf(e.values[1]));
			TextView z = (TextView) findViewById(R.id.magnetic_z);
			z.setText("z:" + String.valueOf(e.values[2]));
			break;
		}
		// 近接
		case Sensor.TYPE_PROXIMITY : {
			TextView x = (TextView) findViewById(R.id.proximity);
			x.setText("proximity:" + String.valueOf(e.values[0]));
		}
		}
	}

	@Override
	protected void onPause() {
		// TODO 自動生成されたメソッド・スタブ
		super.onPause();
		// unregisterListenerをすると、センサーのリスナー登録を解除。onPauseでセンサーを停止する癖をつけよう!
		sensorManager.unregisterListener(this);

	}

}