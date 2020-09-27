package b4a.Magic8Ball;


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
	public static final boolean fullScreen = true;
	public static final boolean includeTitle = false;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mostCurrent = this;
		if (processBA == null) {
			processBA = new BA(this.getApplicationContext(), null, null, "b4a.Magic8Ball", "b4a.Magic8Ball.main");
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
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(processBA, wl, false))
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
		activityBA = new BA(this, layout, processBA, "b4a.Magic8Ball", "b4a.Magic8Ball.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "b4a.Magic8Ball.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
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
public static anywheresoftware.b4a.phone.Phone.PhoneSensors _sensor = null;
public static anywheresoftware.b4a.audio.SoundPoolWrapper _sounds = null;
public static int _bounceid = 0;
public static int _i = 0;
public static int _j = 0;
public static int _k = 0;
public static int _x = 0;
public anywheresoftware.b4a.objects.LabelWrapper _label1 = null;
public b4a.Magic8Ball.starter _starter = null;
public b4a.Magic8Ball.shake _shake = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 28;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 29;BA.debugLine="i = i  + 1";
_i = (int) (_i+1);
 //BA.debugLineNum = 30;BA.debugLine="j = j  + 1";
_j = (int) (_j+1);
 //BA.debugLineNum = 31;BA.debugLine="k = k  + 1";
_k = (int) (_k+1);
 //BA.debugLineNum = 32;BA.debugLine="x = x + 1";
_x = (int) (_x+1);
 //BA.debugLineNum = 34;BA.debugLine="If FirstTime Then";
if (_firsttime) { 
 //BA.debugLineNum = 35;BA.debugLine="sensor.Initialize(sensor.TYPE_ACCELEROMETER)";
_sensor.Initialize(_sensor.TYPE_ACCELEROMETER);
 //BA.debugLineNum = 36;BA.debugLine="Shake.CallBackActivity = \"Main\" 'Set the activit";
mostCurrent._shake._callbackactivity = "Main";
 //BA.debugLineNum = 37;BA.debugLine="sounds.Initialize(1)";
_sounds.Initialize((int) (1));
 //BA.debugLineNum = 38;BA.debugLine="Label1.Initialize(1)";
mostCurrent._label1.Initialize(mostCurrent.activityBA,BA.NumberToString(1));
 //BA.debugLineNum = 39;BA.debugLine="Label1.Gravity = Gravity.CENTER";
mostCurrent._label1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 40;BA.debugLine="Label1.TextSize = 25";
mostCurrent._label1.setTextSize((float) (25));
 //BA.debugLineNum = 44;BA.debugLine="Activity.Color = Colors.RGB(150,150,150)";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (150),(int) (150),(int) (150)));
 //BA.debugLineNum = 46;BA.debugLine="Activity.SetBackgroundImage(LoadBitmap(File.DirA";
mostCurrent._activity.SetBackgroundImageNew((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"8ballBng_portrait.png").getObject()));
 //BA.debugLineNum = 49;BA.debugLine="Activity.AddView(Label1,38%x,33%y,25%x,30%y)";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._label1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (38),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (33),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (25),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (30),mostCurrent.activityBA));
 //BA.debugLineNum = 50;BA.debugLine="bounceId = sounds.Load(File.DirAssets, \"break.mp";
_bounceid = _sounds.Load(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"break.mp3");
 //BA.debugLineNum = 51;BA.debugLine="Label1.Text = \"Poses ta question\"";
mostCurrent._label1.setText(BA.ObjectToCharSequence("Poses ta question"));
 };
 //BA.debugLineNum = 53;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
int _rep = 0;
String _txt = "";
 //BA.debugLineNum = 95;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 97;BA.debugLine="Private Rep As Int";
_rep = 0;
 //BA.debugLineNum = 98;BA.debugLine="Private Txt As String";
_txt = "";
 //BA.debugLineNum = 100;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then ' Teste s";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 101;BA.debugLine="Txt = \"Voulez-vous vraiment quitter programme ?\"";
_txt = "Voulez-vous vraiment quitter programme ?";
 //BA.debugLineNum = 102;BA.debugLine="Rep = Msgbox2(Txt,\"A T T E N T I O N\",\"Oui\",\"\",\"";
_rep = anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence(_txt),BA.ObjectToCharSequence("A T T E N T I O N"),"Oui","","Non",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 103;BA.debugLine="If Rep = DialogResponse.POSITIVE Then ' Si Rep =";
if (_rep==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 104;BA.debugLine="Return False ' Return = False l’événement ne se";
if (true) return anywheresoftware.b4a.keywords.Common.False;
 }else {
 //BA.debugLineNum = 106;BA.debugLine="Return True ' Return = True l’événement sera co";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 };
 //BA.debugLineNum = 114;BA.debugLine="End Sub";
return false;
}
public static String  _activity_longclick() throws Exception{
 //BA.debugLineNum = 116;BA.debugLine="Sub Activity_LongClick";
 //BA.debugLineNum = 118;BA.debugLine="Msgbox(\"Fermeture du programme\",Activity.Title)";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Fermeture du programme"),mostCurrent._activity.getTitle(),mostCurrent.activityBA);
 //BA.debugLineNum = 119;BA.debugLine="ToastMessageShow(\"À la prochaine!\",True) : Activi";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("À la prochaine!"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 119;BA.debugLine="ToastMessageShow(\"À la prochaine!\",True) : Activi";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 122;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 59;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 60;BA.debugLine="sensor.StopListening";
_sensor.StopListening(processBA);
 //BA.debugLineNum = 61;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 55;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 56;BA.debugLine="sensor.StartListening(\"sensor\")";
_sensor.StartListening(processBA,"sensor");
 //BA.debugLineNum = 57;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 23;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 25;BA.debugLine="Private Label1 As Label";
mostCurrent._label1 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 26;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main._process_globals();
starter._process_globals();
shake._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 16;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 17;BA.debugLine="Dim sensor As PhoneSensors";
_sensor = new anywheresoftware.b4a.phone.Phone.PhoneSensors();
 //BA.debugLineNum = 18;BA.debugLine="Dim sounds As SoundPool";
_sounds = new anywheresoftware.b4a.audio.SoundPoolWrapper();
 //BA.debugLineNum = 19;BA.debugLine="Dim bounceId As Int";
_bounceid = 0;
 //BA.debugLineNum = 20;BA.debugLine="Private i = 0 , j = 0,  k = 0, x = 0 As Int";
_i = (int) (0);
_j = (int) (0);
_k = (int) (0);
_x = (int) (0);
 //BA.debugLineNum = 21;BA.debugLine="End Sub";
return "";
}
public static String  _sensor_sensorchanged(float[] _values) throws Exception{
 //BA.debugLineNum = 64;BA.debugLine="Sub sensor_SensorChanged (Values() As Float)";
 //BA.debugLineNum = 65;BA.debugLine="Shake.HandleSensorEvent(Values)";
mostCurrent._shake._handlesensorevent(mostCurrent.activityBA,_values);
 //BA.debugLineNum = 66;BA.debugLine="End Sub";
return "";
}
public static String  _shakeevent() throws Exception{
String[] _repevasives = null;
String[] _repaffirmatives = null;
String[] _repnegatives = null;
String[] _repstr = null;
 //BA.debugLineNum = 69;BA.debugLine="Sub ShakeEvent";
 //BA.debugLineNum = 71;BA.debugLine="Private RepEvasives() As String";
_repevasives = new String[(int) (0)];
java.util.Arrays.fill(_repevasives,"");
 //BA.debugLineNum = 72;BA.debugLine="Private RepAffirmatives() As String";
_repaffirmatives = new String[(int) (0)];
java.util.Arrays.fill(_repaffirmatives,"");
 //BA.debugLineNum = 73;BA.debugLine="Private RepNegatives() As String";
_repnegatives = new String[(int) (0)];
java.util.Arrays.fill(_repnegatives,"");
 //BA.debugLineNum = 74;BA.debugLine="Private RepStr() As String";
_repstr = new String[(int) (0)];
java.util.Arrays.fill(_repstr,"");
 //BA.debugLineNum = 78;BA.debugLine="RepEvasives = Array As String(\"Essaye plus tard\",";
_repevasives = new String[]{"Essaye plus tard","Essaye encore","Pas d'avis","C'est ton destin","Le sort en est jeté","Une chance sur deux","Repose ta question"};
 //BA.debugLineNum = 79;BA.debugLine="RepAffirmatives = Array As String(\"D'après moi ou";
_repaffirmatives = new String[]{"D'après moi oui","C'est certain","Oui absolument","Tu peux compter dessus","Sans aucun doute","Très probable","Oui","C'est bien parti"};
 //BA.debugLineNum = 80;BA.debugLine="RepNegatives = Array As String(\"C'est non\", \"Peu";
_repnegatives = new String[]{"C'est non","Peu probable","Faut pas rêver","N'y compte pas","Impossible"};
 //BA.debugLineNum = 82;BA.debugLine="i = Rnd(1,5)";
_i = anywheresoftware.b4a.keywords.Common.Rnd((int) (1),(int) (5));
 //BA.debugLineNum = 83;BA.debugLine="j = Rnd(1,8)";
_j = anywheresoftware.b4a.keywords.Common.Rnd((int) (1),(int) (8));
 //BA.debugLineNum = 84;BA.debugLine="k = Rnd(1,7)";
_k = anywheresoftware.b4a.keywords.Common.Rnd((int) (1),(int) (7));
 //BA.debugLineNum = 85;BA.debugLine="x = Rnd(1,3)";
_x = anywheresoftware.b4a.keywords.Common.Rnd((int) (1),(int) (3));
 //BA.debugLineNum = 87;BA.debugLine="RepStr = Array As String(RepNegatives(i), RepAffi";
_repstr = new String[]{_repnegatives[_i],_repaffirmatives[_j],_repevasives[_k]};
 //BA.debugLineNum = 89;BA.debugLine="Log(RepStr(x))";
anywheresoftware.b4a.keywords.Common.Log(_repstr[_x]);
 //BA.debugLineNum = 90;BA.debugLine="Label1.Text = RepStr(x)";
mostCurrent._label1.setText(BA.ObjectToCharSequence(_repstr[_x]));
 //BA.debugLineNum = 92;BA.debugLine="sounds.Play(bounceId, 1, 1, 1, 0, 1)";
_sounds.Play(_bounceid,(float) (1),(float) (1),(int) (1),(int) (0),(float) (1));
 //BA.debugLineNum = 93;BA.debugLine="End Sub";
return "";
}
}
