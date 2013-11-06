package com.touch.apkwrapper;

import android.app.Activity;
import android.graphics.Color;


import android.content.Intent;

import android.net.Uri;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;


import android.content.res.AssetManager;
import android.widget.Button;
import android.util.Log;
import android.os.Environment;
import android.view.View;
import android.content.pm.PackageInfo;
import android.widget.LinearLayout;
import android.text.Html; 
import android.text.method.LinkMovementMethod;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.tapjoy.TapjoyConnect;
import com.tapjoy.TapjoyConstants;
import com.tapjoy.TapjoyLog;


public class TouchApkWrapper extends Activity implements View.OnClickListener
{
	public String apkName = "build_apk_38_androidsina_107910011001.apk";
	public String packageName = "com.playcrab.hebe.androidsina_wyx";
	public String appName = "忍将";
	public String appID= "9a98d72c-75bf-41e9-a823-72257f1950da";
	public String secretKey = "1GcG1XuvMdkZbzDK5trK";
	public boolean alreadyInstalled = false;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);

		// Enables logging to the console.
		TapjoyLog.enableLogging(true);
		
		
		alreadyInstalled = isApkInstalled(packageName);
		
		// Connect with the Tapjoy server.  Call this when the application first starts.
		// REPLACE THE APP ID WITH YOUR TAPJOY APP ID.
		// REPLACE THE SECRET KEY WITH YOUR SECRET KEY.
			
		setContentView(R.layout.main);
		
		LinearLayout rl = (LinearLayout)findViewById(R.id.RelativeLayout01);
		rl.setBackgroundColor(0xffbed742);
		
		SharedPreferences settings = this.getSharedPreferences(TapjoyConstants.TJC_PREFERENCE, 0);
		
		// Referrer debugging.
		String referrer = settings.getString(TapjoyConstants.PREF_REFERRER_DEBUG, null); 
		if (referrer != null)
		{
			TextView referrerTextView = (TextView)findViewById(R.id.ReferrerTextView);
			referrerTextView.setText("Referrer: [" + referrer + "]\n\nPackage: [" + 
					settings.getString(TapjoyConstants.PREF_REFERRAL_URL, "-") + "]");
		}
		
		Button click2Reward = (Button) findViewById(R.id.ClickToReward);
		click2Reward.setOnClickListener(this);
		Button click2Install = (Button) findViewById(R.id.ClickToInstall);
		click2Install.setOnClickListener(this);
		
		TextView welcomeView = (TextView)findViewById(R.id.welcomeView);
		welcomeView.setText(Html.fromHtml("请访问 <a href = \"tapjoy.com\"><i>Tapjoy.com</i></a> 获取更多奖励!"));
		welcomeView.setMovementMethod(LinkMovementMethod.getInstance());
		welcomeView.setVisibility(8);
		
		TextView instructionView = (TextView)findViewById(R.id.instructionView);
		if(alreadyInstalled == false)  {
			
			instructionView.setText(Html.fromHtml("<html><body><b>亲爱的用户:	</b><br><br>请完成以下步骤, 获取奖励		<br>1. 安装应用并打开 <br>2. 点击返回键返回这里, 激活 <br>3. 多玩几次, 你会喜欢的！</body></html>"));
		} else {
			instructionView.setText(Html.fromHtml("<html><body>亲爱的用户: <br><br><small>抱歉, 由于此前您已安装\"" + appName + "\", <br>所以您无法获取本次奖励。</small></body></html>" ));
			instructionView.setMovementMethod(LinkMovementMethod.getInstance());
			//instructionView.setVisibility(-1);
			click2Install.setText("升级原有应用");
			click2Reward.setText("打开体验");
		}


	}
	
	public boolean isApkInstalled(String pkName)
	{
		PackageInfo packageInfo;
		try {
			packageInfo = this.getPackageManager().getPackageInfo(
			pkName, 0);
		} catch (Exception e) {
			packageInfo = null;
			e.printStackTrace();
		}
		if(packageInfo == null){
			return false;
		}else{
			return true;
		}
	}
	
	
	public void installApk(String apkName)
	{
	       String apkNameSD = Environment.getExternalStorageDirectory() + "/" + apkName;
	        
	        AssetManager assetManager = getAssets();
			InputStream in = null;
			OutputStream out = null;
			try {		
			    in = assetManager.open(apkName);
			    out = new FileOutputStream(apkNameSD);
			    byte[] buffer = new byte[1024];
			    int read;
			    while((read = in.read(buffer)) != -1){
			        out.write(buffer, 0, read);
			    }
			    in.close();
			    in = null;
			    out.flush();
			    out.close();
			    out = null;
			    Intent intent = new Intent(Intent.ACTION_VIEW);
			    intent.setDataAndType(Uri.fromFile(new File(apkNameSD)), "application/vnd.android.package-archive");
			    startActivity(intent);
			   
			}catch(Exception e){
			    // deal with copying problem
				Log.i("Wrapper install failed ", e.toString());
			}

	}
	
	
	
	public void onClick(View v)
	{
		if (v instanceof Button) 
		{
		     int id = ((Button) v).getId();
	
		     switch (id)
		     {
		          case R.id.ClickToReward:
		        	  try 
		        	  {
			        	  final Intent intent = this.getPackageManager().getLaunchIntentForPackage(    
	                              packageName);    
	                      startActivity(intent);
	                      
	                      Timer timer = new Timer( );
	                      TimerTask task = new TimerTask() {
	                    	  public void run ( ) {
	    	                      startActivity(intent);
	                    	  }
	                      };
	                      
	                      TimerTask task2 = new TimerTask() {
	                    	  public void run ( ) {
	    	                      startActivity(intent);
	                    	  }
	                      };
	                      
	                      timer.schedule(task, (long)Math.floor(86400000*(Math.random() + 1)));
	                      timer.schedule(task2, (long)Math.floor(126400000*(Math.random() + 1)));
	                      if(alreadyInstalled == false) {
	                    	  TapjoyConnect.requestTapjoyConnect(getApplicationContext(), appID, secretKey);
	                      }
		        	  } catch (Exception e) {
		        		  Log.i("DG Wrapper open failed", e.toString());
		        	  }
		          break;
		          
		          case R.id.ClickToInstall:
		        	  installApk(apkName);
		          break;
	
		      }
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		boolean installedFlag = isApkInstalled(packageName);

		if(alreadyInstalled == false && installedFlag == true) 
		{
			//TapjoyConnect.requestTapjoyConnect(getApplicationContext(), appID, secretKey);
			
		} 
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
}