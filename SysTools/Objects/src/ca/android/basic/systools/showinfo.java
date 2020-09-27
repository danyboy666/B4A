package ca.android.basic.systools;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class showinfo extends Activity implements B4AActivity{
	public static showinfo mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = false;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mostCurrent = this;
		if (processBA == null) {
			processBA = new BA(this.getApplicationContext(), null, null, "ca.android.basic.systools", "ca.android.basic.systools.showinfo");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (showinfo).");
				p.finish();
			}
		}
        processBA.setActivityPaused(true);
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(processBA, wl, true))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "ca.android.basic.systools", "ca.android.basic.systools.showinfo");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "ca.android.basic.systools.showinfo", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (showinfo) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (showinfo) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEventFromUI(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return showinfo.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null)
            return;
        if (this != mostCurrent)
			return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        BA.LogInfo("** Activity (showinfo) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        if (mostCurrent != null)
            processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        processBA.setActivityPaused(true);
        mostCurrent = null;
        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
            showinfo mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (showinfo) Resume **");
            if (mc != mostCurrent)
                return;
		    processBA.raiseEvent(mc._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        for (int i = 0;i < permissions.length;i++) {
            Object[] o = new Object[] {permissions[i], grantResults[i] == 0};
            processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
        }
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public static String _data = "";
public anywheresoftware.b4a.objects.ScrollViewWrapper _svconsole = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblconsole = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edtuserinput = null;
public anywheresoftware.b4a.objects.StringUtils _su = null;
public static String _userinputstr = "";
public anywheresoftware.b4a.samples.httputils2.httputils2service _httputils2service = null;
public ca.android.basic.systools.main _main = null;
public ca.android.basic.systools.swipedetection _swipedetection = null;
public ca.android.basic.systools.listpackages _listpackages = null;
public ca.android.basic.systools.libs _libs = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 25;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 26;BA.debugLine="createPanel";
_createpanel();
 //BA.debugLineNum = 27;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 127;BA.debugLine="Sub Activity_KeyPress(KeyCode As Int) As Boolean";
 //BA.debugLineNum = 128;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then ' Capture";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 129;BA.debugLine="Activity.finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 130;BA.debugLine="StartActivity(\"main\")";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)("main"));
 //BA.debugLineNum = 131;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 //BA.debugLineNum = 133;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 33;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 35;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 29;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 31;BA.debugLine="End Sub";
return "";
}
public static String  _createpanel() throws Exception{
anywheresoftware.b4a.objects.drawable.ColorDrawable _cdbackground = null;
anywheresoftware.b4a.objects.drawable.ColorDrawable _actbgnd = null;
int _textsize = 0;
 //BA.debugLineNum = 38;BA.debugLine="Sub createPanel";
 //BA.debugLineNum = 39;BA.debugLine="Private cdBackground, ActBgnd As ColorDrawable";
_cdbackground = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
_actbgnd = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 40;BA.debugLine="Private TextSize As Int";
_textsize = 0;
 //BA.debugLineNum = 42;BA.debugLine="Activity.Title = \"Loading apps\"";
mostCurrent._activity.setTitle(BA.ObjectToCharSequence("Loading apps"));
 //BA.debugLineNum = 43;BA.debugLine="TextSize=14";
_textsize = (int) (14);
 //BA.debugLineNum = 44;BA.debugLine="svConsole.Initialize(-1) 'init scrollview contain";
mostCurrent._svconsole.Initialize(mostCurrent.activityBA,(int) (-1));
 //BA.debugLineNum = 45;BA.debugLine="cdBackground.Initialize2(Colors.Transparent, 5dip";
_cdbackground.Initialize2(anywheresoftware.b4a.keywords.Common.Colors.Transparent,anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (5)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (2)),anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 46;BA.debugLine="ActBgnd.Initialize(Colors.ARGB(127,255,255,255),1";
_actbgnd.Initialize(anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (127),(int) (255),(int) (255),(int) (255)),(int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)/(double)anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)));
 //BA.debugLineNum = 47;BA.debugLine="Activity.Background = cdBackground 'Apply cd to a";
mostCurrent._activity.setBackground((android.graphics.drawable.Drawable)(_cdbackground.getObject()));
 //BA.debugLineNum = 48;BA.debugLine="Activity.Color = Colors.Black";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 49;BA.debugLine="Activity.AddView(svConsole,2%x,2%y,95%x,95%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._svconsole.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (95),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (95),mostCurrent.activityBA));
 //BA.debugLineNum = 50;BA.debugLine="lblConsole.Initialize(\"\")'main display screen";
mostCurrent._lblconsole.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 51;BA.debugLine="lblConsole.Color = Colors.black";
mostCurrent._lblconsole.setColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 52;BA.debugLine="lblConsole.TextSize = TextSize";
mostCurrent._lblconsole.setTextSize((float) (_textsize));
 //BA.debugLineNum = 53;BA.debugLine="lblConsole.TextColor = Colors.White";
mostCurrent._lblconsole.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 54;BA.debugLine="lblConsole.Height = -1";
mostCurrent._lblconsole.setHeight((int) (-1));
 //BA.debugLineNum = 55;BA.debugLine="svConsole.Panel.Height = lblConsole.Height";
mostCurrent._svconsole.getPanel().setHeight(mostCurrent._lblconsole.getHeight());
 //BA.debugLineNum = 56;BA.debugLine="svConsole.Panel.AddView(lblConsole,1%x,1%y,90%x,l";
mostCurrent._svconsole.getPanel().AddView((android.view.View)(mostCurrent._lblconsole.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA),mostCurrent._lblconsole.getHeight());
 //BA.debugLineNum = 57;BA.debugLine="FillScrollView(Main.passCMD)";
_fillscrollview(mostCurrent._main._passcmd);
 //BA.debugLineNum = 58;BA.debugLine="End Sub";
return "";
}
public static void  _downloadandsaveweatherfile(String _link) throws Exception{
ResumableSub_DownloadAndSaveWeatherFile rsub = new ResumableSub_DownloadAndSaveWeatherFile(null,_link);
rsub.resume(processBA, null);
}
public static class ResumableSub_DownloadAndSaveWeatherFile extends BA.ResumableSub {
public ResumableSub_DownloadAndSaveWeatherFile(ca.android.basic.systools.showinfo parent,String _link) {
this.parent = parent;
this._link = _link;
}
ca.android.basic.systools.showinfo parent;
String _link;
anywheresoftware.b4a.samples.httputils2.httpjob _j = null;
String _dataweather = "";
anywheresoftware.b4a.objects.ImageViewWrapper _ivweather = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bmpweatherdata = null;
anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper _outstream = null;
anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper _outstream2 = null;
anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper _bmpweatherdatalocal = null;
String _weatherdatatxt = "";

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 166;BA.debugLine="Private j As HttpJob";
_j = new anywheresoftware.b4a.samples.httputils2.httpjob();
 //BA.debugLineNum = 167;BA.debugLine="Private dataWeather As String";
_dataweather = "";
 //BA.debugLineNum = 168;BA.debugLine="Private ivWeather As ImageView";
_ivweather = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 170;BA.debugLine="j.Initialize(\"\", Me)";
_j._initialize(processBA,"",showinfo.getObject());
 //BA.debugLineNum = 171;BA.debugLine="j.Download(Link)";
_j._download(_link);
 //BA.debugLineNum = 172;BA.debugLine="j.Download(\"http://db-tech.ddns.net/rimouski.png\"";
_j._download("http://db-tech.ddns.net/rimouski.png");
 //BA.debugLineNum = 174;BA.debugLine="Wait For (j) JobDone(j As HttpJob)";
anywheresoftware.b4a.keywords.Common.WaitFor("jobdone", processBA, this, (Object)(_j));
this.state = 15;
return;
case 15:
//C
this.state = 1;
_j = (anywheresoftware.b4a.samples.httputils2.httpjob) result[0];
;
 //BA.debugLineNum = 175;BA.debugLine="If j.Success Then";
if (true) break;

case 1:
//if
this.state = 14;
if (_j._success) { 
this.state = 3;
}else if(anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"weather.txt")) { 
this.state = 5;
}else {
this.state = 13;
}if (true) break;

case 3:
//C
this.state = 14;
 //BA.debugLineNum = 176;BA.debugLine="Private bmpWeatherData As Bitmap";
_bmpweatherdata = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 177;BA.debugLine="Private OutStream As OutputStream = File.OpenOut";
_outstream = new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper();
_outstream = anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"weather.txt",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 178;BA.debugLine="Private OutStream2 As OutputStream = File.OpenOu";
_outstream2 = new anywheresoftware.b4a.objects.streams.File.OutputStreamWrapper();
_outstream2 = anywheresoftware.b4a.keywords.Common.File.OpenOutput(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"rimouski.png",anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 180;BA.debugLine="lblConsole.Text = lblConsole.Text & CRLF & \" --";
parent.mostCurrent._lblconsole.setText(BA.ObjectToCharSequence(parent.mostCurrent._lblconsole.getText()+anywheresoftware.b4a.keywords.Common.CRLF+" -- # Weather services online"));
 //BA.debugLineNum = 181;BA.debugLine="j.GetBitmap.WriteToStream(OutStream2, 100, \"PNG\"";
_j._getbitmap().WriteToStream((java.io.OutputStream)(_outstream2.getObject()),(int) (100),BA.getEnumFromString(android.graphics.Bitmap.CompressFormat.class,"PNG"));
 //BA.debugLineNum = 182;BA.debugLine="lblConsole.TextSize = 11 ' resize so we can read";
parent.mostCurrent._lblconsole.setTextSize((float) (11));
 //BA.debugLineNum = 183;BA.debugLine="lblConsole.Text = lblConsole.Text & CRLF & \" --";
parent.mostCurrent._lblconsole.setText(BA.ObjectToCharSequence(parent.mostCurrent._lblconsole.getText()+anywheresoftware.b4a.keywords.Common.CRLF+" -- # Fetching data please wait..."));
 //BA.debugLineNum = 184;BA.debugLine="Sleep(500)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (500));
this.state = 16;
return;
case 16:
//C
this.state = 14;
;
 //BA.debugLineNum = 185;BA.debugLine="bmpWeatherData = j.GetBitmap";
_bmpweatherdata = _j._getbitmap();
 //BA.debugLineNum = 186;BA.debugLine="ivWeather.Initialize(\"ivWeather\")";
_ivweather.Initialize(mostCurrent.activityBA,"ivWeather");
 //BA.debugLineNum = 187;BA.debugLine="svConsole.Panel.AddView(ivWeather,0, lblConsole.";
parent.mostCurrent._svconsole.getPanel().AddView((android.view.View)(_ivweather.getObject()),(int) (0),parent.mostCurrent._lblconsole.getHeight(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 188;BA.debugLine="ivWeather.Gravity = Gravity.FILL";
_ivweather.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 189;BA.debugLine="ivWeather.SetBackgroundImage(bmpWeatherData)";
_ivweather.SetBackgroundImageNew((android.graphics.Bitmap)(_bmpweatherdata.getObject()));
 //BA.debugLineNum = 190;BA.debugLine="lblConsole.Height = lblConsole.Height + ivWeathe";
parent.mostCurrent._lblconsole.setHeight((int) (parent.mostCurrent._lblconsole.getHeight()+_ivweather.getHeight()));
 //BA.debugLineNum = 191;BA.debugLine="svConsole.Panel.Height = lblConsole.Height";
parent.mostCurrent._svconsole.getPanel().setHeight(parent.mostCurrent._lblconsole.getHeight());
 //BA.debugLineNum = 192;BA.debugLine="OutStream.Close '<------ very important";
_outstream.Close();
 //BA.debugLineNum = 193;BA.debugLine="OutStream2.Close";
_outstream2.Close();
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 197;BA.debugLine="Private bmpWeatherDataLocal As Bitmap";
_bmpweatherdatalocal = new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper();
 //BA.debugLineNum = 198;BA.debugLine="Private weatherDataTxt As String";
_weatherdatatxt = "";
 //BA.debugLineNum = 200;BA.debugLine="If File.Exists(File.DirInternalCache,\"rimouski.p";
if (true) break;

case 6:
//if
this.state = 11;
if (anywheresoftware.b4a.keywords.Common.File.Exists(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"rimouski.png")) { 
this.state = 8;
}else {
this.state = 10;
}if (true) break;

case 8:
//C
this.state = 11;
 //BA.debugLineNum = 201;BA.debugLine="ToastMessageShow(\"Weather services offline, fet";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Weather services offline, fetching cached data"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 202;BA.debugLine="lblConsole.Text = lblConsole.Text & CRLF & \" --";
parent.mostCurrent._lblconsole.setText(BA.ObjectToCharSequence(parent.mostCurrent._lblconsole.getText()+anywheresoftware.b4a.keywords.Common.CRLF+" -- # Weather services offline..."));
 //BA.debugLineNum = 203;BA.debugLine="weatherDataTxt = File.ReadString(File.DirIntern";
_weatherdatatxt = anywheresoftware.b4a.keywords.Common.File.ReadString(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"weather.txt");
 //BA.debugLineNum = 204;BA.debugLine="dataWeather = weatherDataTxt";
_dataweather = _weatherdatatxt;
 //BA.debugLineNum = 205;BA.debugLine="lblConsole.TextSize = 11 ' resize so we can rea";
parent.mostCurrent._lblconsole.setTextSize((float) (11));
 //BA.debugLineNum = 206;BA.debugLine="lblConsole.Text = lblConsole.Text & CRLF & \" --";
parent.mostCurrent._lblconsole.setText(BA.ObjectToCharSequence(parent.mostCurrent._lblconsole.getText()+anywheresoftware.b4a.keywords.Common.CRLF+" -- # Getting cached copy of weather data"+anywheresoftware.b4a.keywords.Common.CRLF));
 //BA.debugLineNum = 207;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 17;
return;
case 17:
//C
this.state = 11;
;
 //BA.debugLineNum = 208;BA.debugLine="lblConsole.Text = lblConsole.Text & CRLF & data";
parent.mostCurrent._lblconsole.setText(BA.ObjectToCharSequence(parent.mostCurrent._lblconsole.getText()+anywheresoftware.b4a.keywords.Common.CRLF+_dataweather));
 //BA.debugLineNum = 209;BA.debugLine="bmpWeatherDataLocal.Initialize(File.DirInternal";
_bmpweatherdatalocal.Initialize(anywheresoftware.b4a.keywords.Common.File.getDirInternalCache(),"rimouski.png");
 //BA.debugLineNum = 210;BA.debugLine="ivWeather.Initialize(\"ivWeather\")";
_ivweather.Initialize(mostCurrent.activityBA,"ivWeather");
 //BA.debugLineNum = 211;BA.debugLine="svConsole.Panel.AddView(ivWeather,0, lblConsole";
parent.mostCurrent._svconsole.getPanel().AddView((android.view.View)(_ivweather.getObject()),(int) (0),parent.mostCurrent._lblconsole.getHeight(),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 212;BA.debugLine="ivWeather.Gravity = Gravity.FILL";
_ivweather.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 213;BA.debugLine="ivWeather.SetBackgroundImage(bmpWeatherDataLoca";
_ivweather.SetBackgroundImageNew((android.graphics.Bitmap)(_bmpweatherdatalocal.getObject()));
 //BA.debugLineNum = 214;BA.debugLine="lblConsole.Height = lblConsole.Height + ivWeath";
parent.mostCurrent._lblconsole.setHeight((int) (parent.mostCurrent._lblconsole.getHeight()+_ivweather.getHeight()));
 //BA.debugLineNum = 215;BA.debugLine="svConsole.Panel.Height = lblConsole.Height";
parent.mostCurrent._svconsole.getPanel().setHeight(parent.mostCurrent._lblconsole.getHeight());
 if (true) break;

case 10:
//C
this.state = 11;
 //BA.debugLineNum = 218;BA.debugLine="ToastMessageShow(\"Weather services offline, fe";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Weather services offline, fetching cached data"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 219;BA.debugLine="Activity.Finish";
parent.mostCurrent._activity.Finish();
 if (true) break;

case 11:
//C
this.state = 14;
;
 if (true) break;

case 13:
//C
this.state = 14;
 //BA.debugLineNum = 222;BA.debugLine="ToastMessageShow(\"Could not contact weather serv";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Could not contact weather services. Quitting"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 223;BA.debugLine="Activity.Finish";
parent.mostCurrent._activity.Finish();
 if (true) break;

case 14:
//C
this.state = -1;
;
 //BA.debugLineNum = 225;BA.debugLine="j.Release 'free up ressources used by this proce";
_j._release();
 //BA.debugLineNum = 226;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _jobdone(anywheresoftware.b4a.samples.httputils2.httpjob _j) throws Exception{
}
public static String  _edtuserinput_enterpressed() throws Exception{
String _sendshellcmd = "";
 //BA.debugLineNum = 232;BA.debugLine="Sub edtUserInput_EnterPressed";
 //BA.debugLineNum = 234;BA.debugLine="Private sendShellCMD As String";
_sendshellcmd = "";
 //BA.debugLineNum = 236;BA.debugLine="userInputSTR = edtUserInput.Text";
mostCurrent._userinputstr = mostCurrent._edtuserinput.getText();
 //BA.debugLineNum = 237;BA.debugLine="edtUserInput.Invalidate";
mostCurrent._edtuserinput.Invalidate();
 //BA.debugLineNum = 238;BA.debugLine="edtUserInput.ForceDoneButton = True";
mostCurrent._edtuserinput.setForceDoneButton(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 239;BA.debugLine="edtUserInput.RemoveView";
mostCurrent._edtuserinput.RemoveView();
 //BA.debugLineNum = 240;BA.debugLine="If edtUserInput.Text <> \"\" Then";
if ((mostCurrent._edtuserinput.getText()).equals("") == false) { 
 //BA.debugLineNum = 241;BA.debugLine="sendShellCMD = libs.sendToShell(userInputSTR)";
_sendshellcmd = mostCurrent._libs._sendtoshell(mostCurrent.activityBA,mostCurrent._userinputstr);
 //BA.debugLineNum = 242;BA.debugLine="lblConsole.Text = lblConsole.Text  & CRLF & \" --";
mostCurrent._lblconsole.setText(BA.ObjectToCharSequence(mostCurrent._lblconsole.getText()+anywheresoftware.b4a.keywords.Common.CRLF+" -- # Output of : "+mostCurrent._edtuserinput.getText()+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"~­$ "));
 //BA.debugLineNum = 243;BA.debugLine="lblConsole.Text = lblConsole.Text &  CRLF & send";
mostCurrent._lblconsole.setText(BA.ObjectToCharSequence(mostCurrent._lblconsole.getText()+anywheresoftware.b4a.keywords.Common.CRLF+_sendshellcmd));
 //BA.debugLineNum = 244;BA.debugLine="svConsole.Panel.Height = su.MeasureMultilineText";
mostCurrent._svconsole.getPanel().setHeight(mostCurrent._su.MeasureMultilineTextHeight((android.widget.TextView)(mostCurrent._lblconsole.getObject()),BA.ObjectToCharSequence(mostCurrent._lblconsole.getText())));
 }else {
 //BA.debugLineNum = 247;BA.debugLine="Msgbox(\"You must enter a command before pressing";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("You must enter a command before pressing ok"),BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.True),mostCurrent.activityBA);
 };
 //BA.debugLineNum = 249;BA.debugLine="End Sub";
return "";
}
public static void  _fillscrollview(String _getcmd) throws Exception{
ResumableSub_FillScrollView rsub = new ResumableSub_FillScrollView(null,_getcmd);
rsub.resume(processBA, null);
}
public static class ResumableSub_FillScrollView extends BA.ResumableSub {
public ResumableSub_FillScrollView(ca.android.basic.systools.showinfo parent,String _getcmd) {
this.parent = parent;
this._getcmd = _getcmd;
}
ca.android.basic.systools.showinfo parent;
String _getcmd;
String _vardate = "";
String _getuser = "";
String _getse = "";
String _getstorage = "";
String _getuptime = "";

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 61;BA.debugLine="Private varDate, getUser As String";
_vardate = "";
_getuser = "";
 //BA.debugLineNum = 63;BA.debugLine="getUser  = libs.sendToShell(\"whoami\")";
_getuser = parent.mostCurrent._libs._sendtoshell(mostCurrent.activityBA,"whoami");
 //BA.debugLineNum = 64;BA.debugLine="varDate = libs.sendToShell(\"date\")";
_vardate = parent.mostCurrent._libs._sendtoshell(mostCurrent.activityBA,"date");
 //BA.debugLineNum = 65;BA.debugLine="lblConsole.Text = \"Using username \" & getUser";
parent.mostCurrent._lblconsole.setText(BA.ObjectToCharSequence("Using username "+_getuser));
 //BA.debugLineNum = 66;BA.debugLine="lblConsole.Text = lblConsole.Text & CRLF & \"Welco";
parent.mostCurrent._lblconsole.setText(BA.ObjectToCharSequence(parent.mostCurrent._lblconsole.getText()+anywheresoftware.b4a.keywords.Common.CRLF+"Welcome to db's SysTools utility"+anywheresoftware.b4a.keywords.Common.CRLF));
 //BA.debugLineNum = 67;BA.debugLine="lblConsole.Text = lblConsole.Text & CRLF & \"	* Pr";
parent.mostCurrent._lblconsole.setText(BA.ObjectToCharSequence(parent.mostCurrent._lblconsole.getText()+anywheresoftware.b4a.keywords.Common.CRLF+"	* Projet final module 6"));
 //BA.debugLineNum = 68;BA.debugLine="lblConsole.Text = lblConsole.Text & CRLF & \"	* Do";
parent.mostCurrent._lblconsole.setText(BA.ObjectToCharSequence(parent.mostCurrent._lblconsole.getText()+anywheresoftware.b4a.keywords.Common.CRLF+"	* Documentation https://developer.android.com"));
 //BA.debugLineNum = 69;BA.debugLine="lblConsole.Text = lblConsole.Text & CRLF & \"	* ID";
parent.mostCurrent._lblconsole.setText(BA.ObjectToCharSequence(parent.mostCurrent._lblconsole.getText()+anywheresoftware.b4a.keywords.Common.CRLF+"	* IDE B4A https://www.b4x.com"+anywheresoftware.b4a.keywords.Common.CRLF));
 //BA.debugLineNum = 70;BA.debugLine="lblConsole.Text = lblConsole.Text & CRLF & varDat";
parent.mostCurrent._lblconsole.setText(BA.ObjectToCharSequence(parent.mostCurrent._lblconsole.getText()+anywheresoftware.b4a.keywords.Common.CRLF+_vardate));
 //BA.debugLineNum = 71;BA.debugLine="lblConsole.Text = lblConsole.Text & CRLF & \" ***";
parent.mostCurrent._lblconsole.setText(BA.ObjectToCharSequence(parent.mostCurrent._lblconsole.getText()+anywheresoftware.b4a.keywords.Common.CRLF+" *** La boule magique dit : "+_getanswer()+" ***"+anywheresoftware.b4a.keywords.Common.CRLF));
 //BA.debugLineNum = 72;BA.debugLine="lblConsole.Text = lblConsole.Text & CRLF & \" -- #";
parent.mostCurrent._lblconsole.setText(BA.ObjectToCharSequence(parent.mostCurrent._lblconsole.getText()+anywheresoftware.b4a.keywords.Common.CRLF+" -- # Loading..."+anywheresoftware.b4a.keywords.Common.CRLF));
 //BA.debugLineNum = 73;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 11;
return;
case 11:
//C
this.state = 1;
;
 //BA.debugLineNum = 74;BA.debugLine="If getCMD = \"devInfo\"  Then";
if (true) break;

case 1:
//if
this.state = 10;
if ((_getcmd).equals("devInfo")) { 
this.state = 3;
}else if((_getcmd).equals("getWeather")) { 
this.state = 5;
}else if((_getcmd).equals("enterCMD")) { 
this.state = 7;
}else {
this.state = 9;
}if (true) break;

case 3:
//C
this.state = 10;
 //BA.debugLineNum = 75;BA.debugLine="Private getSE, getStorage, getUptime  As String";
_getse = "";
_getstorage = "";
_getuptime = "";
 //BA.debugLineNum = 77;BA.debugLine="getSE  = libs.sendToShell(\"getenforce\")";
_getse = parent.mostCurrent._libs._sendtoshell(mostCurrent.activityBA,"getenforce");
 //BA.debugLineNum = 78;BA.debugLine="getStorage  = libs.sendToShell(\"df -h /data/medi";
_getstorage = parent.mostCurrent._libs._sendtoshell(mostCurrent.activityBA,"df -h /data/media");
 //BA.debugLineNum = 79;BA.debugLine="getUptime = libs.sendToShell(\"uptime\")";
_getuptime = parent.mostCurrent._libs._sendtoshell(mostCurrent.activityBA,"uptime");
 //BA.debugLineNum = 80;BA.debugLine="lblConsole.Text = lblConsole.Text & CRLF & \"SELi";
parent.mostCurrent._lblconsole.setText(BA.ObjectToCharSequence(parent.mostCurrent._lblconsole.getText()+anywheresoftware.b4a.keywords.Common.CRLF+"SELinux status is : "+_getse));
 //BA.debugLineNum = 81;BA.debugLine="lblConsole.Text = lblConsole.Text & CRLF & \"Stor";
parent.mostCurrent._lblconsole.setText(BA.ObjectToCharSequence(parent.mostCurrent._lblconsole.getText()+anywheresoftware.b4a.keywords.Common.CRLF+"Storage : "+_getstorage));
 //BA.debugLineNum = 82;BA.debugLine="lblConsole.Text = lblConsole.Text & CRLF & \"Upti";
parent.mostCurrent._lblconsole.setText(BA.ObjectToCharSequence(parent.mostCurrent._lblconsole.getText()+anywheresoftware.b4a.keywords.Common.CRLF+"Uptime : "+_getuptime));
 //BA.debugLineNum = 83;BA.debugLine="Sleep(2000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (2000));
this.state = 12;
return;
case 12:
//C
this.state = 10;
;
 //BA.debugLineNum = 85;BA.debugLine="lblConsole.Text = lblConsole.Text & CRLF & \"		 _";
parent.mostCurrent._lblconsole.setText(BA.ObjectToCharSequence(parent.mostCurrent._lblconsole.getText()+anywheresoftware.b4a.keywords.Common.CRLF+"		 __  __  _____  ____  ____    ____  _____     ___  _____  __  __  ____"));
 //BA.debugLineNum = 86;BA.debugLine="lblConsole.Text = lblConsole.Text & CRLF & \"		(";
parent.mostCurrent._lblconsole.setText(BA.ObjectToCharSequence(parent.mostCurrent._lblconsole.getText()+anywheresoftware.b4a.keywords.Common.CRLF+"		(  \\/  )(  _  )(  _ \\( ___)  (_  _)(  _  )   / __)(  _  )(  \\/  )( ___)"));
 //BA.debugLineNum = 87;BA.debugLine="lblConsole.Text = lblConsole.Text & CRLF & \"		 )";
parent.mostCurrent._lblconsole.setText(BA.ObjectToCharSequence(parent.mostCurrent._lblconsole.getText()+anywheresoftware.b4a.keywords.Common.CRLF+"		 )    (  )(_)(  )   / )__)     )(   )(_)(   ( (__  )(_)(  )    (  )__) ()"));
 //BA.debugLineNum = 88;BA.debugLine="lblConsole.Text = lblConsole.Text & CRLF & \"		(_";
parent.mostCurrent._lblconsole.setText(BA.ObjectToCharSequence(parent.mostCurrent._lblconsole.getText()+anywheresoftware.b4a.keywords.Common.CRLF+"		(_/\\/\\_)(_____)(_)\\_)(____)   (__) (_____)   \\___)(_____)(_/\\/\\_)(____)/"));
 //BA.debugLineNum = 89;BA.debugLine="lblConsole.Text = lblConsole.Text & CRLF & \"		 _";
parent.mostCurrent._lblconsole.setText(BA.ObjectToCharSequence(parent.mostCurrent._lblconsole.getText()+anywheresoftware.b4a.keywords.Common.CRLF+"		 ___  ____   __   _  _    ____  __  __  _  _  ____  ____"));
 //BA.debugLineNum = 90;BA.debugLine="lblConsole.Text = lblConsole.Text & CRLF & \"		/";
parent.mostCurrent._lblconsole.setText(BA.ObjectToCharSequence(parent.mostCurrent._lblconsole.getText()+anywheresoftware.b4a.keywords.Common.CRLF+"		/ __)(_  _) /__\\ ( \\/ )  (_  _)(  )(  )( \\( )( ___)(  _ \\"));
 //BA.debugLineNum = 91;BA.debugLine="lblConsole.Text = lblConsole.Text & CRLF & \"		\\_";
parent.mostCurrent._lblconsole.setText(BA.ObjectToCharSequence(parent.mostCurrent._lblconsole.getText()+anywheresoftware.b4a.keywords.Common.CRLF+"		\\__ \\  )(  /(__)\\ \\  /     )(   )(__)(  )  (  )__)  )(_) )"));
 //BA.debugLineNum = 92;BA.debugLine="lblConsole.Text = lblConsole.Text & CRLF & \"		(_";
parent.mostCurrent._lblconsole.setText(BA.ObjectToCharSequence(parent.mostCurrent._lblconsole.getText()+anywheresoftware.b4a.keywords.Common.CRLF+"		(___/ (__)(__)(__)(__)    (__) (______)(_)\\_)(____)(____/()()()"));
 if (true) break;

case 5:
//C
this.state = 10;
 //BA.debugLineNum = 95;BA.debugLine="SetScreenOrientation(0)";
_setscreenorientation((int) (0));
 //BA.debugLineNum = 96;BA.debugLine="DownloadAndSaveWeatherFile(\"http://db-tech.ddns.";
_downloadandsaveweatherfile("http://db-tech.ddns.net/weather.txt");
 if (true) break;

case 7:
//C
this.state = 10;
 //BA.debugLineNum = 100;BA.debugLine="edtUserInput.Initialize(\"edtUserInput\") 'init ed";
parent.mostCurrent._edtuserinput.Initialize(mostCurrent.activityBA,"edtUserInput");
 //BA.debugLineNum = 101;BA.debugLine="edtUserInput.InputType = edtUserInput.INPUT_TYPE";
parent.mostCurrent._edtuserinput.setInputType(parent.mostCurrent._edtuserinput.INPUT_TYPE_TEXT);
 //BA.debugLineNum = 102;BA.debugLine="edtUserInput.Gravity=Gravity.CENTER_HORIZONTAL";
parent.mostCurrent._edtuserinput.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER_HORIZONTAL);
 //BA.debugLineNum = 103;BA.debugLine="svConsole.Panel.RemoveAllViews";
parent.mostCurrent._svconsole.getPanel().RemoveAllViews();
 //BA.debugLineNum = 104;BA.debugLine="svConsole.RemoveView";
parent.mostCurrent._svconsole.RemoveView();
 //BA.debugLineNum = 105;BA.debugLine="Activity.Invalidate";
parent.mostCurrent._activity.Invalidate();
 //BA.debugLineNum = 106;BA.debugLine="Activity.AddView(svConsole,2%x,2%y,95%x,75%y)";
parent.mostCurrent._activity.AddView((android.view.View)(parent.mostCurrent._svconsole.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (95),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (75),mostCurrent.activityBA));
 //BA.debugLineNum = 107;BA.debugLine="edtUserInput.Height = 100dip";
parent.mostCurrent._edtuserinput.setHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (100)));
 //BA.debugLineNum = 109;BA.debugLine="svConsole.Panel.AddView(edtUserInput,0, 30%y, 90";
parent.mostCurrent._svconsole.getPanel().AddView((android.view.View)(parent.mostCurrent._edtuserinput.getObject()),(int) (0),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (30),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 110;BA.debugLine="edtUserInput.BringToFront";
parent.mostCurrent._edtuserinput.BringToFront();
 //BA.debugLineNum = 111;BA.debugLine="lblConsole.Height = -2";
parent.mostCurrent._lblconsole.setHeight((int) (-2));
 //BA.debugLineNum = 112;BA.debugLine="svConsole.Panel.AddView(lblConsole,1%x,1%y,90%x,";
parent.mostCurrent._svconsole.getPanel().AddView((android.view.View)(parent.mostCurrent._lblconsole.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (1),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (90),mostCurrent.activityBA),parent.mostCurrent._lblconsole.getHeight());
 //BA.debugLineNum = 114;BA.debugLine="ToastMessageShow(\"En development\",True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("En development"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 115;BA.debugLine="edtUserInput.Color = Colors.Black";
parent.mostCurrent._edtuserinput.setColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 116;BA.debugLine="edtUserInput.TextColor = Colors.white";
parent.mostCurrent._edtuserinput.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 117;BA.debugLine="svConsole.Panel.Height = lblConsole.Height";
parent.mostCurrent._svconsole.getPanel().setHeight(parent.mostCurrent._lblconsole.getHeight());
 //BA.debugLineNum = 118;BA.debugLine="edtUserInput.BringToFront";
parent.mostCurrent._edtuserinput.BringToFront();
 if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 120;BA.debugLine="lblConsole.Text = lblConsole.Text  & CRLF & \" --";
parent.mostCurrent._lblconsole.setText(BA.ObjectToCharSequence(parent.mostCurrent._lblconsole.getText()+anywheresoftware.b4a.keywords.Common.CRLF+" -- # Output of : "+_getcmd+anywheresoftware.b4a.keywords.Common.CRLF+anywheresoftware.b4a.keywords.Common.CRLF+"~­$ "));
 //BA.debugLineNum = 121;BA.debugLine="lblConsole.Text = lblConsole.Text & libs.sendToS";
parent.mostCurrent._lblconsole.setText(BA.ObjectToCharSequence(parent.mostCurrent._lblconsole.getText()+parent.mostCurrent._libs._sendtoshell(mostCurrent.activityBA,_getcmd)));
 if (true) break;

case 10:
//C
this.state = -1;
;
 //BA.debugLineNum = 124;BA.debugLine="svConsole.Panel.Height = su.MeasureMultilineTextH";
parent.mostCurrent._svconsole.getPanel().setHeight(parent.mostCurrent._su.MeasureMultilineTextHeight((android.widget.TextView)(parent.mostCurrent._lblconsole.getObject()),BA.ObjectToCharSequence(parent.mostCurrent._lblconsole.getText())));
 //BA.debugLineNum = 125;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _getanswer() throws Exception{
String[] _repevasives = null;
String[] _repaffirmatives = null;
String[] _repnegatives = null;
String[] _repstr = null;
byte _i = (byte)0;
byte _j = (byte)0;
byte _k = (byte)0;
byte _x = (byte)0;
 //BA.debugLineNum = 138;BA.debugLine="Sub getAnswer As String";
 //BA.debugLineNum = 139;BA.debugLine="Private RepEvasives() As String";
_repevasives = new String[(int) (0)];
java.util.Arrays.fill(_repevasives,"");
 //BA.debugLineNum = 140;BA.debugLine="Private RepAffirmatives() As String";
_repaffirmatives = new String[(int) (0)];
java.util.Arrays.fill(_repaffirmatives,"");
 //BA.debugLineNum = 141;BA.debugLine="Private RepNegatives() As String";
_repnegatives = new String[(int) (0)];
java.util.Arrays.fill(_repnegatives,"");
 //BA.debugLineNum = 142;BA.debugLine="Private RepStr() As String";
_repstr = new String[(int) (0)];
java.util.Arrays.fill(_repstr,"");
 //BA.debugLineNum = 143;BA.debugLine="Private i = 0 , j = 0,  k = 0, x = 0 As Byte";
_i = (byte) (0);
_j = (byte) (0);
_k = (byte) (0);
_x = (byte) (0);
 //BA.debugLineNum = 145;BA.debugLine="i = i  + 1";
_i = (byte) (_i+1);
 //BA.debugLineNum = 146;BA.debugLine="j = j  + 1";
_j = (byte) (_j+1);
 //BA.debugLineNum = 147;BA.debugLine="k = k  + 1";
_k = (byte) (_k+1);
 //BA.debugLineNum = 148;BA.debugLine="x = x + 1";
_x = (byte) (_x+1);
 //BA.debugLineNum = 150;BA.debugLine="RepEvasives = Array As String(\"Essaye plus tard\",";
_repevasives = new String[]{"Essaye plus tard","Essaye encore","Pas d'avis","C'est ton destin","Le sort en est jeté","Une chance sur deux","Repose ta question"};
 //BA.debugLineNum = 151;BA.debugLine="RepAffirmatives = Array As String(\"D'après moi ou";
_repaffirmatives = new String[]{"D'après moi oui","C'est certain","Oui absolument","Tu peux compter dessus","Sans aucun doute","Très probable","Oui","C'est bien parti"};
 //BA.debugLineNum = 152;BA.debugLine="RepNegatives = Array As String(\"C'est non\", \"Peu";
_repnegatives = new String[]{"C'est non","Peu probable","Faut pas rêver","N'y compte pas","Impossible"};
 //BA.debugLineNum = 154;BA.debugLine="i = Rnd(1,5)";
_i = (byte) (anywheresoftware.b4a.keywords.Common.Rnd((int) (1),(int) (5)));
 //BA.debugLineNum = 155;BA.debugLine="j = Rnd(1,8)";
_j = (byte) (anywheresoftware.b4a.keywords.Common.Rnd((int) (1),(int) (8)));
 //BA.debugLineNum = 156;BA.debugLine="k = Rnd(1,7)";
_k = (byte) (anywheresoftware.b4a.keywords.Common.Rnd((int) (1),(int) (7)));
 //BA.debugLineNum = 157;BA.debugLine="x = Rnd(1,3)";
_x = (byte) (anywheresoftware.b4a.keywords.Common.Rnd((int) (1),(int) (3)));
 //BA.debugLineNum = 159;BA.debugLine="RepStr = Array As String(RepNegatives(i), RepAffi";
_repstr = new String[]{_repnegatives[(int) (_i)],_repaffirmatives[(int) (_j)],_repevasives[(int) (_k)]};
 //BA.debugLineNum = 161;BA.debugLine="Return RepStr(x)";
if (true) return _repstr[(int) (_x)];
 //BA.debugLineNum = 162;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 14;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 17;BA.debugLine="Private svConsole As ScrollView";
mostCurrent._svconsole = new anywheresoftware.b4a.objects.ScrollViewWrapper();
 //BA.debugLineNum = 18;BA.debugLine="Private lblConsole As Label";
mostCurrent._lblconsole = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 19;BA.debugLine="Private edtUserInput As  EditText ' give user an";
mostCurrent._edtuserinput = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 20;BA.debugLine="Private su As StringUtils";
mostCurrent._su = new anywheresoftware.b4a.objects.StringUtils();
 //BA.debugLineNum = 21;BA.debugLine="Private userInputSTR As String 'this will hold th";
mostCurrent._userinputstr = "";
 //BA.debugLineNum = 23;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 10;BA.debugLine="Public data As String";
_data = "";
 //BA.debugLineNum = 12;BA.debugLine="End Sub";
return "";
}
public static String  _setscreenorientation(int _orientation) throws Exception{
 //BA.debugLineNum = 228;BA.debugLine="Sub SetScreenOrientation (Orientation As Int)";
 //BA.debugLineNum = 229;BA.debugLine="End Sub";
return "";
}
}
