/*
This class implements the starting screen of the app
Uses several ui elements to get the flight itenary and
performs the prediction using TestAlgorithm.java class
*/
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import com.weka.trainandtest.TestAlgorithm;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;
import couk.jenxsol.parallaxscrollview.views.ParallaxScrollView;
import couk.jenxsol.parallaxscrollviewdemo1.R;


public class MainActivity extends Activity implements OnClickListener,OnTimeChangedListener, OnDateChangedListener 
{
	private ParallaxScrollView mScrollView;
	private Button submit;
	private DatePicker departureDatePicker;
	private TimePicker departureTimePicker;
	private TimePicker arrivalTimePicker;
	private EditText editTextCarrierCode;
	private EditText editTextOriginCode;
	private EditText editTextDestCode;
	private int departureHour;
	private int departureMinute;
	private int arrivalHour;
	private int arrivalMinute;
	private int departureYear;
	private int departureMonth;
	private int departureDayofMonth;
	private int departureDayofWeek;
	private String str_departureDayofWeek;
	HashMap<String, Integer> dayToInteger = new HashMap<String, Integer>();
	private boolean change = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mylayout);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		mScrollView = (ParallaxScrollView) findViewById(R.id.scroll_view);

		// initalise all ui elements 
		submit = (Button) findViewById(R.id.btn_submit);
		submit.setOnClickListener(this);

		departureDatePicker = (DatePicker) findViewById(R.id.departuredatepicker);

		departureTimePicker = (TimePicker) findViewById(R.id.departuretimepicker);
		departureTimePicker.setAddStatesFromChildren(true);

		arrivalTimePicker = (TimePicker) findViewById(R.id.arrivaltimepicker);
		arrivalTimePicker.setAddStatesFromChildren(true);

		editTextCarrierCode = (EditText) findViewById(R.id.edittext_carriercode);
		editTextCarrierCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
	        @Override
	        public void onFocusChange(View v, boolean hasFocus) {
	            if (!hasFocus) {
	                hideKeyboard(v); 
	            }
	        }
	    });
		editTextOriginCode = (EditText) findViewById(R.id.edittext_origincode);
		editTextDestCode = (EditText) findViewById(R.id.edittext_destinationcode);

		departureTimePicker.setOnTimeChangedListener(this);
		arrivalTimePicker.setOnTimeChangedListener(this);

		dayToInteger.put("Monday", 1);
		dayToInteger.put("Tuesday", 2);
		dayToInteger.put("Wednesday", 3);
		dayToInteger.put("Thursday", 4);
		dayToInteger.put("Friday", 5);
		dayToInteger.put("Saturday", 6);
		dayToInteger.put("Sunday", 7);

		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		departureDatePicker.init(year, month, day, this);

	}

	@Override
	public void onClick(View v) 
	{
		// validation of inputs on text elements
		if (editTextCarrierCode.getText().toString().equalsIgnoreCase("")) {
			Toast.makeText(this, "Please enter carrier code", Toast.LENGTH_LONG).show();
		} else if (editTextOriginCode.getText().toString().equalsIgnoreCase("")) {
			Toast.makeText(this, "Please enter origin airport code",Toast.LENGTH_LONG).show();
		} else if (editTextDestCode.getText().toString().equalsIgnoreCase("")) {
			Toast.makeText(this, "Please enter destination airport code",Toast.LENGTH_LONG).show();
		} 
		else {

			if (change) 
			{

				String testInput = departureYear + "," + (departureMonth + 1)
						+ "," + departureDayofMonth + "," + departureDayofWeek
						+ "," + editTextCarrierCode.getText() + ","
						+ editTextOriginCode.getText() + ","
						+ editTextDestCode.getText() + "," + departureHour
						+ "," + departureMinute + "," + arrivalHour + ","
						+ arrivalMinute + "," + "?";

				TestAlgorithm test = new TestAlgorithm();
				String result = test.load(testInput);

				Intent call_DR_Activity = new Intent(DemoActivity.this,DisplayResult.class);
				call_DR_Activity.putExtra("result", result);
				startActivity(call_DR_Activity);
			} 
			else 
			{
				departureYear = departureDatePicker.getYear();
				departureMonth = departureDatePicker.getMonth() + 1;
				departureDayofMonth = departureDatePicker.getDayOfMonth();
				str_departureDayofWeek = DateFormat.format("E",new Date(departureYear, departureMonth,departureDayofMonth)).toString();

				if (str_departureDayofWeek.equalsIgnoreCase("Tue")) {
					str_departureDayofWeek = "Tuesday";
				} else if (str_departureDayofWeek.equalsIgnoreCase("Wed")) {
					str_departureDayofWeek = "Wednesday";
				} else if (str_departureDayofWeek.equalsIgnoreCase("Thu")) {
					str_departureDayofWeek = "Thursday";
				} else if (str_departureDayofWeek.equalsIgnoreCase("Fri")) {
					str_departureDayofWeek = "Friday";
				} else if (str_departureDayofWeek.equalsIgnoreCase("Sat")) {
					str_departureDayofWeek = "Saturday";
				} else if (str_departureDayofWeek.equalsIgnoreCase("Sun")) {
					str_departureDayofWeek = "Sunday";
				} else if (str_departureDayofWeek.equalsIgnoreCase("Mon")) {
					str_departureDayofWeek = "Monday";
				}
				departureDayofWeek = (int) dayToInteger.get(str_departureDayofWeek);

				departureHour = departureTimePicker.getCurrentHour();
				departureMinute = departureTimePicker.getCurrentMinute();
				arrivalHour = arrivalTimePicker.getCurrentHour();
				arrivalMinute = arrivalTimePicker.getCurrentMinute();

				String testInput = departureYear + "," + (departureMonth + 1)
						+ "," + departureDayofMonth + "," + departureDayofWeek
						+ "," + editTextCarrierCode.getText() + ","
						+ editTextOriginCode.getText() + ","
						+ editTextDestCode.getText() + "," + departureHour
						+ "," + departureMinute + "," + arrivalHour + ","
						+ arrivalMinute + "," + "?";

				TestAlgorithm test = new TestAlgorithm();
				String result = test.load(testInput);

				Intent call_DR_Activity = new Intent(DemoActivity.this,DisplayResult.class);
				call_DR_Activity.putExtra("result", result);
				startActivity(call_DR_Activity);
			}
		}

	}

	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) 
	{
		if (view.getId() == R.id.departuretimepicker) 
		{
			departureTimePicker.clearFocus();
			departureHour = departureTimePicker.getCurrentHour();
			departureMinute = departureTimePicker.getCurrentMinute();
		} 
		else if (view.getId() == R.id.arrivaltimepicker) 
		{
			arrivalHour = arrivalTimePicker.getCurrentHour();
			arrivalMinute = arrivalTimePicker.getCurrentMinute();
		}
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear,int dayOfMonth) 
	{
		departureYear = departureDatePicker.getYear();
		departureMonth = departureDatePicker.getMonth();
		departureDayofMonth = departureDatePicker.getDayOfMonth();
		str_departureDayofWeek = DateFormat.format("E",new Date(departureYear, departureMonth, departureDayofMonth)).toString();

		if (str_departureDayofWeek.equalsIgnoreCase("Tue")) {
			str_departureDayofWeek = "Tuesday";
		} else if (str_departureDayofWeek.equalsIgnoreCase("Wed")) {
			str_departureDayofWeek = "Wednesday";
		} else if (str_departureDayofWeek.equalsIgnoreCase("Thu")) {
			str_departureDayofWeek = "Thursday";
		} else if (str_departureDayofWeek.equalsIgnoreCase("Fri")) {
			str_departureDayofWeek = "Friday";
		} else if (str_departureDayofWeek.equalsIgnoreCase("Sat")) {
			str_departureDayofWeek = "Saturday";
		} else if (str_departureDayofWeek.equalsIgnoreCase("Sun")) {
			str_departureDayofWeek = "Sunday";
		} else if (str_departureDayofWeek.equalsIgnoreCase("Mon")) {
			str_departureDayofWeek = "Monday";
		}

		departureDayofWeek = (int) dayToInteger.get(str_departureDayofWeek);
		change = true;
	}
	
	public void hideKeyboard(View view) 
	{
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
