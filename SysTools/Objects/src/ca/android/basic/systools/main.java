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

public class main extends Activity implements B4AActivity{
	public static main mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "ca.android.basic.systools", "ca.android.basic.systools.main");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (main).");
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
		activityBA = new BA(this, layout, processBA, "ca.android.basic.systools", "ca.android.basic.systools.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "ca.android.basic.systools.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (main) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (main) Resume **");
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
		return main.class;
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
        BA.LogInfo("** Activity (main) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            main mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (main) Resume **");
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
public static String _passcmd = "";
public anywheresoftware.b4a.objects.ListViewWrapper _lvapps = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnlmain = null;
public anywheresoftware.b4a.objects.RuntimePermissions _rp = null;
public static String _basecommand = "";
public static boolean _helpflag = false;
public anywheresoftware.b4a.samples.httputils2.httputils2service _httputils2service = null;
public ca.android.basic.systools.swipedetection _swipedetection = null;
public ca.android.basic.systools.showinfo _showinfo = null;
public ca.android.basic.systools.listpackages _listpackages = null;
public ca.android.basic.systools.libs _libs = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
vis = vis | (swipedetection.mostCurrent != null);
vis = vis | (showinfo.mostCurrent != null);
vis = vis | (listpackages.mostCurrent != null);
return vis;}
public static void  _activity_create(boolean _firsttime) throws Exception{
ResumableSub_Activity_Create rsub = new ResumableSub_Activity_Create(null,_firsttime);
rsub.resume(processBA, null);
}
public static class ResumableSub_Activity_Create extends BA.ResumableSub {
public ResumableSub_Activity_Create(ca.android.basic.systools.main parent,boolean _firsttime) {
this.parent = parent;
this._firsttime = _firsttime;
}
ca.android.basic.systools.main parent;
boolean _firsttime;
anywheresoftware.b4a.objects.ProgressBarWrapper _pb = null;
int _x = 0;
int step10;
int limit10;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 36;BA.debugLine="If FirstTime = True Then";
if (true) break;

case 1:
//if
this.state = 10;
if (_firsttime==anywheresoftware.b4a.keywords.Common.True) { 
this.state = 3;
}else {
this.state = 9;
}if (true) break;

case 3:
//C
this.state = 4;
 //BA.debugLineNum = 37;BA.debugLine="Private pb As ProgressBar";
_pb = new anywheresoftware.b4a.objects.ProgressBarWrapper();
 //BA.debugLineNum = 39;BA.debugLine="rp.GetSafeDirDefaultExternal(rp.PERMISSION_WRITE";
parent.mostCurrent._rp.GetSafeDirDefaultExternal(parent.mostCurrent._rp.PERMISSION_WRITE_EXTERNAL_STORAGE);
 //BA.debugLineNum = 41;BA.debugLine="Activity.Initialize(Me)";
parent.mostCurrent._activity.Initialize(mostCurrent.activityBA,BA.ObjectToString(main.getObject()));
 //BA.debugLineNum = 42;BA.debugLine="Activity.Title = \"Loading Magic App\"";
parent.mostCurrent._activity.setTitle(BA.ObjectToCharSequence("Loading Magic App"));
 //BA.debugLineNum = 43;BA.debugLine="pb.Initialize(\"pb\") 'initialize progress bar";
_pb.Initialize(mostCurrent.activityBA,"pb");
 //BA.debugLineNum = 44;BA.debugLine="pb.SetColorAnimated(5000, Colors.ARGB(0,255,255,";
_pb.SetColorAnimated((int) (5000),anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (0),(int) (255),(int) (255),(int) (254)),anywheresoftware.b4a.keywords.Common.Colors.ARGB((int) (255),(int) (0),(int) (0),(int) (0)));
 //BA.debugLineNum = 45;BA.debugLine="Activity.AddView(pb,0%x,0%y,100%x,100%y)";
parent.mostCurrent._activity.AddView((android.view.View)(_pb.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 46;BA.debugLine="ToastMessageShow(\"Magic 8 Ball is loading, pleas";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Magic 8 Ball is loading, please wait a moment"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 48;BA.debugLine="For x = 0 To 5000";
if (true) break;

case 4:
//for
this.state = 7;
step10 = 1;
limit10 = (int) (5000);
_x = (int) (0) ;
this.state = 11;
if (true) break;

case 11:
//C
this.state = 7;
if ((step10 > 0 && _x <= limit10) || (step10 < 0 && _x >= limit10)) this.state = 6;
if (true) break;

case 12:
//C
this.state = 11;
_x = ((int)(0 + _x + step10)) ;
if (true) break;

case 6:
//C
this.state = 12;
 //BA.debugLineNum = 49;BA.debugLine="x = x + 1";
_x = (int) (_x+1);
 //BA.debugLineNum = 50;BA.debugLine="Sleep(1) ' Do something!!";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1));
this.state = 13;
return;
case 13:
//C
this.state = 12;
;
 //BA.debugLineNum = 51;BA.debugLine="pb.Progress = x";
_pb.setProgress(_x);
 if (true) break;
if (true) break;

case 7:
//C
this.state = 10;
;
 //BA.debugLineNum = 53;BA.debugLine="Activity.removeAllViews() 'remove progress bar v";
parent.mostCurrent._activity.RemoveAllViews();
 //BA.debugLineNum = 54;BA.debugLine="StartActivity(\"swipeDetection\") 'start swipe det";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)("swipeDetection"));
 if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 56;BA.debugLine="StartActivity(\"swipeDetection\") 'if we have alre";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)("swipeDetection"));
 if (true) break;

case 10:
//C
this.state = -1;
;
 //BA.debugLineNum = 58;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
short _selection = (short)0;
 //BA.debugLineNum = 83;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 84;BA.debugLine="Private Selection As Short";
_selection = (short)0;
 //BA.debugLineNum = 86;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 87;BA.debugLine="Selection = Msgbox2(\"Go to main menu?\".ToUpperCa";
_selection = (short) (anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence("Go to main menu?".toUpperCase()),BA.ObjectToCharSequence("C o n f i r m a t i o n"),"Yes","","",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA));
 //BA.debugLineNum = 88;BA.debugLine="Select Selection";
switch (BA.switchObjectToInt(_selection,(short) (anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE),(short) (anywheresoftware.b4a.keywords.Common.DialogResponse.CANCEL),(short) (anywheresoftware.b4a.keywords.Common.DialogResponse.NEGATIVE))) {
case 0: {
 //BA.debugLineNum = 90;BA.debugLine="Return False";
if (true) return anywheresoftware.b4a.keywords.Common.False;
 break; }
case 1: 
case 2: {
 //BA.debugLineNum = 92;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 break; }
}
;
 };
 //BA.debugLineNum = 95;BA.debugLine="End Sub";
return false;
}
public static String  _activity_longclick() throws Exception{
 //BA.debugLineNum = 97;BA.debugLine="Sub Activity_LongClick";
 //BA.debugLineNum = 98;BA.debugLine="selectApp";
_selectapp();
 //BA.debugLineNum = 99;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 76;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 77;BA.debugLine="If UserClosed = True Then";
if (_userclosed==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 78;BA.debugLine="StartActivity(\"main\")";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)("main"));
 };
 //BA.debugLineNum = 80;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 62;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 63;BA.debugLine="selectApp";
_selectapp();
 //BA.debugLineNum = 64;BA.debugLine="End Sub";
return "";
}
public static String  _apploader() throws Exception{
anywheresoftware.b4a.objects.collections.List _alist = null;
anywheresoftware.b4a.objects.collections.List _adesc = null;
String[] _appsdesc = null;
String _appstring = "";
String _infotext = "";
String[] _lvapplist = null;
String[] _applist = null;
byte _arraylenght = (byte)0;
String[] _appsnames = null;
int _i = 0;
 //BA.debugLineNum = 151;BA.debugLine="Sub appLoader";
 //BA.debugLineNum = 152;BA.debugLine="Private aList,aDesc As List 'Declare list contain";
_alist = new anywheresoftware.b4a.objects.collections.List();
_adesc = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 153;BA.debugLine="Private appsDesc() As String 'Array container";
_appsdesc = new String[(int) (0)];
java.util.Arrays.fill(_appsdesc,"");
 //BA.debugLineNum = 154;BA.debugLine="Private appString = \"app\", infoText = \"Hold for i";
_appstring = "app";
_infotext = "Hold for info about ";
 //BA.debugLineNum = 155;BA.debugLine="Private lvAppList() As String 'array container";
_lvapplist = new String[(int) (0)];
java.util.Arrays.fill(_lvapplist,"");
 //BA.debugLineNum = 156;BA.debugLine="Private appList() As String 'array container";
_applist = new String[(int) (0)];
java.util.Arrays.fill(_applist,"");
 //BA.debugLineNum = 157;BA.debugLine="Private arrayLenght As Byte";
_arraylenght = (byte)0;
 //BA.debugLineNum = 158;BA.debugLine="arrayLenght = 6";
_arraylenght = (byte) (6);
 //BA.debugLineNum = 159;BA.debugLine="Private appsNames(arrayLenght) As String 'Array c";
_appsnames = new String[(int) (_arraylenght)];
java.util.Arrays.fill(_appsnames,"");
 //BA.debugLineNum = 161;BA.debugLine="Activity.Title = \"Application loader\"";
mostCurrent._activity.setTitle(BA.ObjectToCharSequence("Application loader"));
 //BA.debugLineNum = 162;BA.debugLine="aList = lvAppList";
_alist = anywheresoftware.b4a.keywords.Common.ArrayToList(_lvapplist);
 //BA.debugLineNum = 164;BA.debugLine="Select swipeDetection.getApp";
switch (BA.switchObjectToInt(mostCurrent._swipedetection._getapp,"appFileSystem","appIPTools","appSysTools","appInternalTools")) {
case 0: {
 //BA.debugLineNum = 166;BA.debugLine="appList = Array As String(\"Disk Free\", \"Mount\",";
_applist = new String[]{"Disk Free","Mount","Environment variables","Disk Free storage","Enter CMD","Package Manager"};
 break; }
case 1: {
 //BA.debugLineNum = 168;BA.debugLine="appList = Array As String(\"Ping google\", \"IP in";
_applist = new String[]{"Ping google","IP info","Route","Netstat","IP info all","Placeholder"};
 break; }
case 2: {
 //BA.debugLineNum = 170;BA.debugLine="appList = Array As String(\"Uptime\", \"Process\",";
_applist = new String[]{"Uptime","Process","CPU info","Memory info","Display running services","Linux Kernel info"};
 break; }
case 3: {
 //BA.debugLineNum = 172;BA.debugLine="appList = Array As String(\"Get Weather\", \"Globa";
_applist = new String[]{"Get Weather","Global device info","List shell Commands","Get Android system property","Reboot device","Shutdown device"};
 break; }
}
;
 //BA.debugLineNum = 175;BA.debugLine="appsDesc = Array As String(infoText & appList(0),";
_appsdesc = new String[]{_infotext+_applist[(int) (0)],_infotext+_applist[(int) (1)],_infotext+_applist[(int) (2)],_infotext+_applist[(int) (3)],_infotext+_applist[(int) (4)],_infotext+_applist[(int) (5)]};
 //BA.debugLineNum = 176;BA.debugLine="aDesc = appsDesc";
_adesc = anywheresoftware.b4a.keywords.Common.ArrayToList(_appsdesc);
 //BA.debugLineNum = 177;BA.debugLine="lvAppList = appList";
_lvapplist = _applist;
 //BA.debugLineNum = 179;BA.debugLine="For i = 0 To appList.Length - 1";
{
final int step24 = 1;
final int limit24 = (int) (_applist.length-1);
_i = (int) (0) ;
for (;_i <= limit24 ;_i = _i + step24 ) {
 //BA.debugLineNum = 180;BA.debugLine="appsNames(i) = appString & (i + 1)";
_appsnames[_i] = _appstring+BA.NumberToString((_i+1));
 //BA.debugLineNum = 181;BA.debugLine="appsNames(i) = lvAppList(i)";
_appsnames[_i] = _lvapplist[_i];
 //BA.debugLineNum = 182;BA.debugLine="lvApps.AddTwoLinesAndBitmap(appsNames(i), aDesc.";
mostCurrent._lvapps.AddTwoLinesAndBitmap(BA.ObjectToCharSequence(_appsnames[_i]),BA.ObjectToCharSequence(_adesc.Get(_i)),(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"sys.png").getObject()));
 }
};
 //BA.debugLineNum = 186;BA.debugLine="End Sub";
return "";
}
public static String  _assemblecmd(Object _value) throws Exception{
String _cmdarg = "";
String _helpcmdarg = "";
String _sendcurrentcmd = "";
 //BA.debugLineNum = 239;BA.debugLine="Sub assembleCMD(Value As Object) As String";
 //BA.debugLineNum = 241;BA.debugLine="Activity.Title = Value";
mostCurrent._activity.setTitle(BA.ObjectToCharSequence(_value));
 //BA.debugLineNum = 242;BA.debugLine="Private cmdArg, helpCMDArg As String";
_cmdarg = "";
_helpcmdarg = "";
 //BA.debugLineNum = 243;BA.debugLine="Private sendCurrentCMD As String";
_sendcurrentcmd = "";
 //BA.debugLineNum = 245;BA.debugLine="Select Value";
switch (BA.switchObjectToInt(_value,(Object)("Enter CMD"),(Object)("Get Weather"),(Object)("CPU info"),(Object)("Memory info"),(Object)("Global device info"),(Object)("Package Manager"),(Object)("Ping google"),(Object)("Disk Free"),(Object)("Mount"),(Object)("Display running services"),(Object)("Reboot device"),(Object)("Shutdown device"),(Object)("Uptime"),(Object)("Process"),(Object)("Get Android system property"),(Object)("Linux Kernel info"),(Object)("List shell Commands"),(Object)("Environment variables"),(Object)("Route"),(Object)("IP info"),(Object)("Netstat"),(Object)("IP info all"),(Object)("Disk Free storage"))) {
case 0: {
 //BA.debugLineNum = 248;BA.debugLine="baseCommand = \"enterCMD\"";
mostCurrent._basecommand = "enterCMD";
 //BA.debugLineNum = 249;BA.debugLine="cmdArg = \"\"";
_cmdarg = "";
 break; }
case 1: {
 //BA.debugLineNum = 251;BA.debugLine="baseCommand = \"getWeather\"";
mostCurrent._basecommand = "getWeather";
 //BA.debugLineNum = 252;BA.debugLine="cmdArg = \"\"";
_cmdarg = "";
 break; }
case 2: {
 //BA.debugLineNum = 254;BA.debugLine="baseCommand = \"cat /proc/cpuinfo\"";
mostCurrent._basecommand = "cat /proc/cpuinfo";
 //BA.debugLineNum = 255;BA.debugLine="cmdArg = \"\"";
_cmdarg = "";
 break; }
case 3: {
 //BA.debugLineNum = 257;BA.debugLine="baseCommand = \"cat\"";
mostCurrent._basecommand = "cat";
 //BA.debugLineNum = 258;BA.debugLine="cmdArg = \" /proc/meminfo\"";
_cmdarg = " /proc/meminfo";
 break; }
case 4: {
 //BA.debugLineNum = 260;BA.debugLine="baseCommand = \"devInfo\"";
mostCurrent._basecommand = "devInfo";
 //BA.debugLineNum = 261;BA.debugLine="cmdArg = \"\"";
_cmdarg = "";
 break; }
case 5: {
 //BA.debugLineNum = 263;BA.debugLine="baseCommand = \"packageManager\"";
mostCurrent._basecommand = "packageManager";
 break; }
case 6: {
 //BA.debugLineNum = 265;BA.debugLine="baseCommand = \"ping\"";
mostCurrent._basecommand = "ping";
 //BA.debugLineNum = 266;BA.debugLine="cmdArg = \" -c5 google.ca\"";
_cmdarg = " -c5 google.ca";
 //BA.debugLineNum = 267;BA.debugLine="helpCMDArg = \" -h\"";
_helpcmdarg = " -h";
 break; }
case 7: {
 //BA.debugLineNum = 269;BA.debugLine="baseCommand = \"df\"";
mostCurrent._basecommand = "df";
 //BA.debugLineNum = 270;BA.debugLine="cmdArg = \" -h\"";
_cmdarg = " -h";
 //BA.debugLineNum = 271;BA.debugLine="helpCMDArg = \" --help\"";
_helpcmdarg = " --help";
 break; }
case 8: {
 //BA.debugLineNum = 273;BA.debugLine="baseCommand = \"mount\"";
mostCurrent._basecommand = "mount";
 //BA.debugLineNum = 274;BA.debugLine="cmdArg = \"\"";
_cmdarg = "";
 //BA.debugLineNum = 275;BA.debugLine="helpCMDArg = \" --help\"";
_helpcmdarg = " --help";
 break; }
case 9: {
 //BA.debugLineNum = 278;BA.debugLine="baseCommand = \"dumpsys\"";
mostCurrent._basecommand = "dumpsys";
 //BA.debugLineNum = 279;BA.debugLine="cmdArg = \" -l\"";
_cmdarg = " -l";
 //BA.debugLineNum = 280;BA.debugLine="helpCMDArg = \" -h\"";
_helpcmdarg = " -h";
 break; }
case 10: {
 //BA.debugLineNum = 282;BA.debugLine="baseCommand = \"reboot\"";
mostCurrent._basecommand = "reboot";
 //BA.debugLineNum = 283;BA.debugLine="cmdArg = \"\"";
_cmdarg = "";
 //BA.debugLineNum = 284;BA.debugLine="helpCMDArg = \" -h\"";
_helpcmdarg = " -h";
 break; }
case 11: {
 //BA.debugLineNum = 286;BA.debugLine="baseCommand = \"reboot\"";
mostCurrent._basecommand = "reboot";
 //BA.debugLineNum = 287;BA.debugLine="cmdArg = \" -p\"";
_cmdarg = " -p";
 //BA.debugLineNum = 288;BA.debugLine="helpCMDArg = \" -h\"";
_helpcmdarg = " -h";
 break; }
case 12: {
 //BA.debugLineNum = 290;BA.debugLine="baseCommand = \"uptime\"";
mostCurrent._basecommand = "uptime";
 //BA.debugLineNum = 291;BA.debugLine="cmdArg = \"\"";
_cmdarg = "";
 //BA.debugLineNum = 292;BA.debugLine="helpCMDArg = \" --help\"";
_helpcmdarg = " --help";
 break; }
case 13: {
 //BA.debugLineNum = 294;BA.debugLine="baseCommand = \"ps\"";
mostCurrent._basecommand = "ps";
 //BA.debugLineNum = 295;BA.debugLine="cmdArg = \"\"";
_cmdarg = "";
 //BA.debugLineNum = 296;BA.debugLine="helpCMDArg = \" --help\"";
_helpcmdarg = " --help";
 break; }
case 14: {
 //BA.debugLineNum = 298;BA.debugLine="baseCommand = \"getprop\"";
mostCurrent._basecommand = "getprop";
 //BA.debugLineNum = 299;BA.debugLine="cmdArg = \"\"";
_cmdarg = "";
 //BA.debugLineNum = 300;BA.debugLine="helpCMDArg = \" --help\"";
_helpcmdarg = " --help";
 break; }
case 15: {
 //BA.debugLineNum = 302;BA.debugLine="baseCommand = \"uname\"";
mostCurrent._basecommand = "uname";
 //BA.debugLineNum = 303;BA.debugLine="cmdArg = \" -a\"";
_cmdarg = " -a";
 //BA.debugLineNum = 304;BA.debugLine="helpCMDArg = \" --help\"";
_helpcmdarg = " --help";
 break; }
case 16: {
 //BA.debugLineNum = 306;BA.debugLine="baseCommand = \"ls\"";
mostCurrent._basecommand = "ls";
 //BA.debugLineNum = 307;BA.debugLine="cmdArg = \" -1 /system/bin\"";
_cmdarg = " -1 /system/bin";
 break; }
case 17: {
 //BA.debugLineNum = 310;BA.debugLine="baseCommand = \"printenv\"";
mostCurrent._basecommand = "printenv";
 //BA.debugLineNum = 311;BA.debugLine="cmdArg = \"\"";
_cmdarg = "";
 //BA.debugLineNum = 312;BA.debugLine="helpCMDArg = \" --help\"";
_helpcmdarg = " --help";
 break; }
case 18: {
 //BA.debugLineNum = 314;BA.debugLine="baseCommand = \"ip\"";
mostCurrent._basecommand = "ip";
 //BA.debugLineNum = 315;BA.debugLine="cmdArg = \" route\"";
_cmdarg = " route";
 //BA.debugLineNum = 316;BA.debugLine="helpCMDArg = \" help\"";
_helpcmdarg = " help";
 break; }
case 19: {
 //BA.debugLineNum = 318;BA.debugLine="baseCommand = \"ip\"";
mostCurrent._basecommand = "ip";
 //BA.debugLineNum = 319;BA.debugLine="cmdArg = \" addr show wlan0\"";
_cmdarg = " addr show wlan0";
 //BA.debugLineNum = 320;BA.debugLine="helpCMDArg = \" addr help\"";
_helpcmdarg = " addr help";
 break; }
case 20: {
 //BA.debugLineNum = 322;BA.debugLine="baseCommand = \"netstat\"";
mostCurrent._basecommand = "netstat";
 //BA.debugLineNum = 323;BA.debugLine="cmdArg = \" -r -n\"";
_cmdarg = " -r -n";
 //BA.debugLineNum = 324;BA.debugLine="helpCMDArg = \" --help\"";
_helpcmdarg = " --help";
 break; }
case 21: {
 //BA.debugLineNum = 326;BA.debugLine="baseCommand = \"ip\"";
mostCurrent._basecommand = "ip";
 //BA.debugLineNum = 327;BA.debugLine="cmdArg = \" addr show\"";
_cmdarg = " addr show";
 //BA.debugLineNum = 328;BA.debugLine="helpCMDArg = \" addr help\"";
_helpcmdarg = " addr help";
 break; }
case 22: {
 //BA.debugLineNum = 330;BA.debugLine="baseCommand = \"df\"";
mostCurrent._basecommand = "df";
 //BA.debugLineNum = 331;BA.debugLine="cmdArg = \" -h /storage\"";
_cmdarg = " -h /storage";
 //BA.debugLineNum = 332;BA.debugLine="helpCMDArg = \" --help\"";
_helpcmdarg = " --help";
 break; }
default: {
 //BA.debugLineNum = 334;BA.debugLine="baseCommand = \"\"";
mostCurrent._basecommand = "";
 //BA.debugLineNum = 335;BA.debugLine="helpCMDArg = \"\"";
_helpcmdarg = "";
 break; }
}
;
 //BA.debugLineNum = 338;BA.debugLine="If helpFlag = True Then";
if (_helpflag==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 339;BA.debugLine="sendCurrentCMD = baseCommand&helpCMDArg";
_sendcurrentcmd = mostCurrent._basecommand+_helpcmdarg;
 }else if((_cmdarg).equals("") && (_helpcmdarg).equals("")) { 
 //BA.debugLineNum = 341;BA.debugLine="sendCurrentCMD = baseCommand";
_sendcurrentcmd = mostCurrent._basecommand;
 }else {
 //BA.debugLineNum = 343;BA.debugLine="sendCurrentCMD = baseCommand&cmdArg";
_sendcurrentcmd = mostCurrent._basecommand+_cmdarg;
 };
 //BA.debugLineNum = 346;BA.debugLine="helpFlag = False";
_helpflag = anywheresoftware.b4a.keywords.Common.False;
 //BA.debugLineNum = 347;BA.debugLine="Return sendCurrentCMD";
if (true) return _sendcurrentcmd;
 //BA.debugLineNum = 348;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 21;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 25;BA.debugLine="Private lvApps As ListView	 'Container for the ap";
mostCurrent._lvapps = new anywheresoftware.b4a.objects.ListViewWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Private pnlMain As Panel";
mostCurrent._pnlmain = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Private rp As RuntimePermissions";
mostCurrent._rp = new anywheresoftware.b4a.objects.RuntimePermissions();
 //BA.debugLineNum = 28;BA.debugLine="Private baseCommand As String";
mostCurrent._basecommand = "";
 //BA.debugLineNum = 29;BA.debugLine="Private helpFlag As Boolean";
_helpflag = false;
 //BA.debugLineNum = 30;BA.debugLine="End Sub";
return "";
}
public static String  _loadmainlayout() throws Exception{
anywheresoftware.b4a.objects.drawable.GradientDrawable _gradient1 = null;
int[] _clrs = null;
 //BA.debugLineNum = 104;BA.debugLine="Sub loadMainLayout()";
 //BA.debugLineNum = 106;BA.debugLine="Private Gradient1 As GradientDrawable";
_gradient1 = new anywheresoftware.b4a.objects.drawable.GradientDrawable();
 //BA.debugLineNum = 107;BA.debugLine="Private Clrs(2) As Int";
_clrs = new int[(int) (2)];
;
 //BA.debugLineNum = 109;BA.debugLine="Clrs(0) = Colors.RGB(28,28,28) 'define gradiant v";
_clrs[(int) (0)] = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (28),(int) (28),(int) (28));
 //BA.debugLineNum = 110;BA.debugLine="Clrs(1) = Colors.RGB(127,127,127)";
_clrs[(int) (1)] = anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (127),(int) (127),(int) (127));
 //BA.debugLineNum = 112;BA.debugLine="Activity.Title = \"Apps Menu\" 'Activity title";
mostCurrent._activity.setTitle(BA.ObjectToCharSequence("Apps Menu"));
 //BA.debugLineNum = 113;BA.debugLine="Activity.Initialize(Me) 'Initialize main activity";
mostCurrent._activity.Initialize(mostCurrent.activityBA,BA.ObjectToString(main.getObject()));
 //BA.debugLineNum = 114;BA.debugLine="Gradient1.Initialize(\"LEFT_RIGHT\", Clrs)";
_gradient1.Initialize(BA.getEnumFromString(android.graphics.drawable.GradientDrawable.Orientation.class,"LEFT_RIGHT"),_clrs);
 //BA.debugLineNum = 115;BA.debugLine="lvApps.Initialize(\"lvApps\") 'Initialize listview";
mostCurrent._lvapps.Initialize(mostCurrent.activityBA,"lvApps");
 //BA.debugLineNum = 116;BA.debugLine="Activity.Background = Gradient1";
mostCurrent._activity.setBackground((android.graphics.drawable.Drawable)(_gradient1.getObject()));
 //BA.debugLineNum = 117;BA.debugLine="pnlMain.Initialize(\"pnlMain\") 'Panel wich hold ou";
mostCurrent._pnlmain.Initialize(mostCurrent.activityBA,"pnlMain");
 //BA.debugLineNum = 118;BA.debugLine="pnlMain.SetBackgroundImage(LoadBitmap(File.DirAss";
mostCurrent._pnlmain.SetBackgroundImageNew((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"androidgears.jpg").getObject()));
 //BA.debugLineNum = 119;BA.debugLine="Activity.AddView(pnlMain,0%x,0%y,100%x,100%y) 'Pa";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._pnlmain.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 120;BA.debugLine="pnlMain.AddView(lvApps,50%x,50%y,100%x,100%y) 'Ad";
mostCurrent._pnlmain.AddView((android.view.View)(mostCurrent._lvapps.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (50),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 121;BA.debugLine="lvApps.SetLayoutAnimated(1000,0%x,0%y,100%x,100%y";
mostCurrent._lvapps.SetLayoutAnimated((int) (1000),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (0),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA));
 //BA.debugLineNum = 122;BA.debugLine="lvApps.TwoLinesAndBitmap.Label.Top = 0";
mostCurrent._lvapps.getTwoLinesAndBitmap().Label.setTop((int) (0));
 //BA.debugLineNum = 123;BA.debugLine="lvApps.TwoLinesAndBitmap.Label.Height = 8%y";
mostCurrent._lvapps.getTwoLinesAndBitmap().Label.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 124;BA.debugLine="lvApps.TwoLinesAndBitmap.Label.Left = 20%x";
mostCurrent._lvapps.getTwoLinesAndBitmap().Label.setLeft(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA));
 //BA.debugLineNum = 125;BA.debugLine="lvApps.TwoLinesAndBitmap.Label.TextSize = 20";
mostCurrent._lvapps.getTwoLinesAndBitmap().Label.setTextSize((float) (20));
 //BA.debugLineNum = 126;BA.debugLine="lvApps.TwoLinesAndBitmap.SecondLabel.TextSize = 1";
mostCurrent._lvapps.getTwoLinesAndBitmap().SecondLabel.setTextSize((float) (14));
 //BA.debugLineNum = 127;BA.debugLine="lvApps.TwoLinesAndBitmap.SecondLabel.Left = 20%x";
mostCurrent._lvapps.getTwoLinesAndBitmap().SecondLabel.setLeft(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA));
 //BA.debugLineNum = 128;BA.debugLine="lvApps.TwoLinesAndBitmap.SecondLabel.top = 8%y";
mostCurrent._lvapps.getTwoLinesAndBitmap().SecondLabel.setTop(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 129;BA.debugLine="lvApps.TwoLinesAndBitmap.SecondLabel.Height = 8%y";
mostCurrent._lvapps.getTwoLinesAndBitmap().SecondLabel.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (8),mostCurrent.activityBA));
 //BA.debugLineNum = 130;BA.debugLine="lvApps.TwoLinesAndBitmap.Label.TextColor = Colors";
mostCurrent._lvapps.getTwoLinesAndBitmap().Label.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 131;BA.debugLine="lvApps.TwoLinesAndBitmap.SecondLabel.TextColor =";
mostCurrent._lvapps.getTwoLinesAndBitmap().SecondLabel.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (255),(int) (255),(int) (255)));
 //BA.debugLineNum = 133;BA.debugLine="lvApps.TwoLinesAndBitmap.ImageView.top = 2%y";
mostCurrent._lvapps.getTwoLinesAndBitmap().ImageView.setTop(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA));
 //BA.debugLineNum = 134;BA.debugLine="lvApps.TwoLinesAndBitmap.ImageView.Height = 10%y";
mostCurrent._lvapps.getTwoLinesAndBitmap().ImageView.setHeight(anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (10),mostCurrent.activityBA));
 //BA.debugLineNum = 135;BA.debugLine="lvApps.TwoLinesAndBitmap.ImageView.Width = 15%x";
mostCurrent._lvapps.getTwoLinesAndBitmap().ImageView.setWidth(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (15),mostCurrent.activityBA));
 //BA.debugLineNum = 136;BA.debugLine="lvApps.TwoLinesAndBitmap.ItemHeight = 25%x 'This";
mostCurrent._lvapps.getTwoLinesAndBitmap().setItemHeight(anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (25),mostCurrent.activityBA));
 //BA.debugLineNum = 137;BA.debugLine="appLoader ' Call application loader block with th";
_apploader();
 //BA.debugLineNum = 138;BA.debugLine="End Sub";
return "";
}
public static void  _lvapps_itemclick(int _position,Object _value) throws Exception{
ResumableSub_lvApps_ItemClick rsub = new ResumableSub_lvApps_ItemClick(null,_position,_value);
rsub.resume(processBA, null);
}
public static class ResumableSub_lvApps_ItemClick extends BA.ResumableSub {
public ResumableSub_lvApps_ItemClick(ca.android.basic.systools.main parent,int _position,Object _value) {
this.parent = parent;
this._position = _position;
this._value = _value;
}
ca.android.basic.systools.main parent;
int _position;
Object _value;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 193;BA.debugLine="ProgressDialogShow2(\"Loading \" & Value & CRLF & \"";
anywheresoftware.b4a.keywords.Common.ProgressDialogShow2(mostCurrent.activityBA,BA.ObjectToCharSequence("Loading "+BA.ObjectToString(_value)+anywheresoftware.b4a.keywords.Common.CRLF+""+anywheresoftware.b4a.keywords.Common.CRLF+"Please wait"),anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 194;BA.debugLine="Sleep(2000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (2000));
this.state = 7;
return;
case 7:
//C
this.state = 1;
;
 //BA.debugLineNum = 196;BA.debugLine="Select Value";
if (true) break;

case 1:
//select
this.state = 6;
switch (BA.switchObjectToInt(_value,(Object)("Package Manager"))) {
case 0: {
this.state = 3;
if (true) break;
}
default: {
this.state = 5;
if (true) break;
}
}
if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 198;BA.debugLine="StartActivity(\"listpackages\")";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)("listpackages"));
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 200;BA.debugLine="passCMD = assembleCMD(Value)";
parent._passcmd = _assemblecmd(_value);
 //BA.debugLineNum = 201;BA.debugLine="StartActivity(\"showinfo\")";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)("showinfo"));
 if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 203;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static void  _lvapps_itemlongclick(int _position,Object _value) throws Exception{
ResumableSub_lvApps_ItemLongClick rsub = new ResumableSub_lvApps_ItemLongClick(null,_position,_value);
rsub.resume(processBA, null);
}
public static class ResumableSub_lvApps_ItemLongClick extends BA.ResumableSub {
public ResumableSub_lvApps_ItemLongClick(ca.android.basic.systools.main parent,int _position,Object _value) {
this.parent = parent;
this._position = _position;
this._value = _value;
}
ca.android.basic.systools.main parent;
int _position;
Object _value;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 210;BA.debugLine="Activity.Title = Value";
parent.mostCurrent._activity.setTitle(BA.ObjectToCharSequence(_value));
 //BA.debugLineNum = 212;BA.debugLine="helpFlag = True";
parent._helpflag = anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 213;BA.debugLine="passCMD = assembleCMD(Value)";
parent._passcmd = _assemblecmd(_value);
 //BA.debugLineNum = 214;BA.debugLine="Select Value";
if (true) break;

case 1:
//select
this.state = 20;
switch (BA.switchObjectToInt(_value,(Object)("Package Manager"),(Object)("CPU info"),(Object)("Enter CMD"),(Object)("Get Weather"),(Object)("Memory info"),(Object)("Global device info"),(Object)("List shell Commands"),(Object)("Placeholder"))) {
case 0: {
this.state = 3;
if (true) break;
}
case 1: {
this.state = 5;
if (true) break;
}
case 2: {
this.state = 7;
if (true) break;
}
case 3: {
this.state = 9;
if (true) break;
}
case 4: {
this.state = 11;
if (true) break;
}
case 5: {
this.state = 13;
if (true) break;
}
case 6: {
this.state = 15;
if (true) break;
}
case 7: {
this.state = 17;
if (true) break;
}
default: {
this.state = 19;
if (true) break;
}
}
if (true) break;

case 3:
//C
this.state = 20;
 //BA.debugLineNum = 216;BA.debugLine="Msgbox2Async(\"This is the system built-in pac";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("This is the system built-in package manager. This function will list all packages installed on this device."),BA.ObjectToCharSequence(BA.ObjectToString(_value)+" manual"),"","Cancel","",(anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper(), (android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null)),processBA,anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 5:
//C
this.state = 20;
 //BA.debugLineNum = 218;BA.debugLine="Msgbox2Async(\"This function will output cpu i";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("This function will output cpu info"),BA.ObjectToCharSequence(BA.ObjectToString(_value)+" manual"),"","Cancel","",(anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper(), (android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null)),processBA,anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 7:
//C
this.state = 20;
 //BA.debugLineNum = 220;BA.debugLine="Msgbox2Async(\"This function will accept input";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("This function will accept input from user via android keyboard"),BA.ObjectToCharSequence(BA.ObjectToString(_value)+" manual"),"","Cancel","",(anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper(), (android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null)),processBA,anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 9:
//C
this.state = 20;
 //BA.debugLineNum = 222;BA.debugLine="Msgbox2Async(\"This function will get the weat";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("This function will get the weather forecast for Rimouski"),BA.ObjectToCharSequence(BA.ObjectToString(_value)+" manual"),"","Cancel","",(anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper(), (android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null)),processBA,anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 11:
//C
this.state = 20;
 //BA.debugLineNum = 224;BA.debugLine="Msgbox2Async(\"This function will output memor";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("This function will output memory usage info"),BA.ObjectToCharSequence(BA.ObjectToString(_value)+" manual"),"","Cancel","",(anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper(), (android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null)),processBA,anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 13:
//C
this.state = 20;
 //BA.debugLineNum = 226;BA.debugLine="Msgbox2Async(\"This function will output Globa";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("This function will output Global device info"),BA.ObjectToCharSequence(BA.ObjectToString(_value)+" manual"),"","Cancel","",(anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper(), (android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null)),processBA,anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 15:
//C
this.state = 20;
 //BA.debugLineNum = 228;BA.debugLine="Msgbox2Async(\"This function will list all ava";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence("This function will list all available shell commands"),BA.ObjectToCharSequence(BA.ObjectToString(_value)+" manual"),"","Cancel","",(anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper(), (android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null)),processBA,anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 17:
//C
this.state = 20;
 //BA.debugLineNum = 230;BA.debugLine="ToastMessageShow(\"Cannot pass empty command.";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Cannot pass empty command. Returning to main"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 231;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 21;
return;
case 21:
//C
this.state = 20;
;
 //BA.debugLineNum = 232;BA.debugLine="Activity.Finish";
parent.mostCurrent._activity.Finish();
 if (true) break;

case 19:
//C
this.state = 20;
 //BA.debugLineNum = 235;BA.debugLine="Msgbox2Async(libs.sendToShell(passCMD), Value";
anywheresoftware.b4a.keywords.Common.Msgbox2Async(BA.ObjectToCharSequence(parent.mostCurrent._libs._sendtoshell(mostCurrent.activityBA,parent._passcmd)),BA.ObjectToCharSequence(BA.ObjectToString(_value)+" manual"),"","Cancel","",(anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper) anywheresoftware.b4a.AbsObjectWrapper.ConvertToWrapper(new anywheresoftware.b4a.objects.drawable.CanvasWrapper.BitmapWrapper(), (android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null)),processBA,anywheresoftware.b4a.keywords.Common.False);
 if (true) break;

case 20:
//C
this.state = -1;
;
 //BA.debugLineNum = 237;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        anywheresoftware.b4a.samples.httputils2.httputils2service._process_globals();
main._process_globals();
swipedetection._process_globals();
showinfo._process_globals();
listpackages._process_globals();
libs._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 15;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 18;BA.debugLine="Public passCMD As String 'store local module comm";
_passcmd = "";
 //BA.debugLineNum = 19;BA.debugLine="End Sub";
return "";
}
public static String  _selectapp() throws Exception{
 //BA.debugLineNum = 68;BA.debugLine="Sub selectApp()";
 //BA.debugLineNum = 69;BA.debugLine="If swipeDetection.getApp <> \"\" Then";
if ((mostCurrent._swipedetection._getapp).equals("") == false) { 
 //BA.debugLineNum = 70;BA.debugLine="loadMainLayout";
_loadmainlayout();
 }else {
 //BA.debugLineNum = 72;BA.debugLine="swipeDetection.getApp = \"\"	' catch eventual erro";
mostCurrent._swipedetection._getapp = "";
 };
 //BA.debugLineNum = 74;BA.debugLine="End Sub";
return "";
}
}
