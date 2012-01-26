package com.facebook.android;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;


import android.app.Activity;
import android.content.Context;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

public class orientation extends Activity implements SensorListener{
	final String tag = "Shankar's sensors";
    SensorManager sm = null;
    TextView xViewA = null;
    TextView yViewA = null;
    TextView zViewA = null;
    TextView xViewO = null;
    TextView yViewO = null;
    TextView zViewO = null;
    public int MotionDirection;
    public float MotionAcceleration;
    public TextView tc;
    int up=0, down=0, hit = 0;
   // boolean up=false;
    private String TAG = "****Orientation!!!****";
    
    
    final int s_ctr = 10;
    static int x_ctr = 0;
    int ctr=0;
    Float[] buff = new Float[s_ctr];
        
    
	// This Class is responsible for communication with the server in
	// background.
	private class Communicator extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			HttpClient httpclient = new DefaultHttpClient(); // A new HttpClient
																// object
			HttpPost httppost = new HttpPost(Cloud.url); // A new HttpPost
															// object

			// Code to build the data packet to the server
			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
						2);
				
				if(Cloud.isAlreadyInserted.equals("False")){
					nameValuePairs.add(new BasicNameValuePair("qid", "INSERT"));
					Cloud.isAlreadyInserted="True";
				}else{
					nameValuePairs.add(new BasicNameValuePair("qid", "UPDATE"));
				}
				
				nameValuePairs.add(new BasicNameValuePair("pnumber", Cloud.teamid));
				nameValuePairs.add(new BasicNameValuePair("userid", Cloud.uid));
				nameValuePairs.add(new BasicNameValuePair("dirn", Cloud.dirn));
				nameValuePairs.add(new BasicNameValuePair("mag", Cloud.mag));
				nameValuePairs.add(new BasicNameValuePair("hit", Cloud.hit));
				nameValuePairs.add(new BasicNameValuePair("change", Cloud.change));
				
				try {
					httppost
							.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				try {
					httpclient.execute(httppost);
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (NullPointerException e) {
				Log.e("Error1", "null");

			}

			return null;
		}

	}
    
    
    
    
    public Example obj;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        obj= new Example();
        // Get the telephony manager
        //TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        
        //Using the facebook user id as the identifier
        
       // Cloud.uid = obj.userid;
        
        System.out.println(" User id is :: "+Cloud.uid);
        
       // get reference to SensorManager
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        setContentView(R.layout.orientation);
        tc = (TextView) findViewById(R.id.TextView01);
        
        xViewA = (TextView) findViewById(R.id.title1);
        yViewA = (TextView) findViewById(R.id.title2);
        zViewA = (TextView) findViewById(R.id.title3);
        xViewO = (TextView) findViewById(R.id.xboxo);
        yViewO = (TextView) findViewById(R.id.yboxo);
        zViewO = (TextView) findViewById(R.id.zboxo);
    }
    public void onSensorChanged(int sensor, float[] values) {
        synchronized (this) {
        	 	long time;
            Log.d(tag, "onSensorChanged: " + sensor + ", x: " + 
values[0] + ", y: " + values[1] + ", z: " + values[2]);
            
            
            
            
           /* if (sensor == SensorManager.SENSOR_ACCELEROMETER) {
                xViewA.setText("" + values[0]);
                yViewA.setText("" + values[1]);
                zViewA.setText("" + values[2]);
            }*/
            
            if(sensor == SensorManager.SENSOR_ORIENTATION){
            	Log.d(tag, "onSensorChanged: " + sensor + ", x: " + 
            			values[0] + ", y: " + values[1] + ", z: " + values[2]);
            			        
            	
            	
            						//determine hit
					                if(values[1]>100){
						            	hit ++;
						            	// 	 tp.setText("HIT counter is  "+ hit);
						            }
            	
            						if(this.ctr<this.s_ctr){
            							this.buff[this.ctr] = values[2];
            						}	
            						this.ctr++;
            						if(this.ctr>=this.s_ctr){
            							
            							//temp save old values
            							String old_dirn=Cloud.dirn;
            							String old_mag=Cloud.mag;
            							String old_hit=Cloud.hit;
            							
            							Cloud.change="0";
            							
            							float x = buff[s_ctr-1]-buff[0];
            							x /= 10;
            							
            							//find mag
            							Float y = Math.abs(x);
            							Cloud.mag = y.toString();
            							
            							//determine direction
            							
            							if(x<1 && x>-1){
            							//This is Static	
            								tc.setText("was STATIC");
            								Cloud.dirn = "-1";
            								
            							}else if( x<-1)
            							{          							
            							//This is going UP
            								tc.setText("went UP");
            								Cloud.dirn = "1";	
            								
            							}else{
            								//This is going Down
            								tc.setText("went DOWN");
            								Cloud.dirn = "0";
            								
            							}
            							
            							//determine hit
            							if(hit!=0){
            								Cloud.hit="1";
            							}else{
            								Cloud.hit="0";
            							}
            							
            							//DETERMINE IF THERE WAS A CHANGE
            							
            							if(Cloud.dirn.equals("-1")){
            								//this was static
            								Cloud.change=Cloud.hit;
            							}else{
            								//there was a motion
            								if(!old_dirn.equals(Cloud.dirn)||!old_mag.equals(Cloud.mag)||!old_hit.equals(Cloud.hit)){
                								Cloud.change="1";
                							}else{
                								Cloud.change="0";
                							}
            							}
            							
            							//Reset all values..
            							hit = 0;
            							this.ctr=0;
            							
            							//make a new communication thread	
            							if(Cloud.change.equals("1")){
            							new Communicator().execute();}
            							
            							
            							
            							//Log
            							//x_ctr++;	
            							//try { // catches IOException below
            			                    
            			                    // ##### Write a file to the disk #####
            			                    /* We have to use the openFileOutput()-method 
            			                     * the ActivityContext provides, to
            			                     * protect your file from others and 
            			                     * This is done for security-reasons. 
            			                     * We chose MODE_WORLD_READABLE, because
            			                     *  we have nothing to hide in our file*/ 
            			                
            			                	//File file = new File("//sdcard//test_orientation_xx.txt");

            			        			
//            			        			FileWriter fstream = new FileWriter(file, true);
//            			        			BufferedWriter out = new BufferedWriter(fstream);
//            			        			out.write(x_ctr+"#"+x+"\r\n");
 //           			        			out.close();
            			                	/*FileOutputStream fOut = openFileOutput("/sdcard/test_orientation.txt", 
            			                           Context.MODE_APPEND);
            			                                        
            			                    OutputStreamWriter osw = new OutputStreamWriter(fOut);  

            			                    // Write the string to the file
            			                    osw.write(time+"#"+values[0]+"#"+values[1]+"#"+values[3]+"\r\n");
            			                    
            			                     ensure that everything is 
            			                     * really written out and close 
            			                    osw.flush();
            			                    osw.close();*/
 //           			                } 
 //           			                catch(IOException e){e.printStackTrace();} 
            							
            							
            						}
            						
            					        xViewO.setText(""+values[0]);
            			                yViewO.setText(""+values[1]);
            			                zViewO.setText(""+values[2]);
            			                TextView tp= (TextView) findViewById(R.id.TextView02);
            			                time = System.currentTimeMillis();    
            			                
            			            
            			                    			                
            			                
            			        

            	
            	
            			            }         	
            	
            /*	if(MotionDirection==1 && MotionAcceleration>Math.abs(12)){
            		//up
            		up++;
            		//tc.setText(" Going Up"+ up );
            		MotionDirection=0;
            	}
            		
            	else if (MotionDirection==-1 && MotionAcceleration>Math.abs(12)){
            		//down
            		down++;
            		MotionDirection=0;
            		//tc.setText(" Going Down "+ down);
            	}
            	else{
            		//stationary
            		tc.setText(" Stationary  UP:: "+up+" DOWN:: "+down);
            	}
            	
            	*/
            }
        }
    
    public void onAccuracyChanged(int sensor, int accuracy) {
    	Log.d(tag,"onAccuracyChanged: " + sensor + ", accuracy: " + accuracy);
    }
    @Override
    protected void onResume() {
        super.onResume();
      // register this class as a listener for the orientation and accelerometer sensors
        sm.registerListener(this, 
                SensorManager.SENSOR_ORIENTATION |SensorManager.SENSOR_ACCELEROMETER,
                SensorManager.SENSOR_DELAY_FASTEST);
    }
    
    @Override
    protected void onStop() {
        // unregister listener
        sm.unregisterListener(this);
        super.onStop();
    }    


}
