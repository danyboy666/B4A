package b4a.example;


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
			processBA = new BA(this.getApplicationContext(), null, null, "b4a.example", "b4a.example.main");
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
		activityBA = new BA(this, layout, processBA, "b4a.example", "b4a.example.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "b4a.example.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
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
public anywheresoftware.b4a.objects.PanelWrapper _pnl = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblpassscreen = null;
public anywheresoftware.b4a.objects.EditTextWrapper _getuserinput = null;
public static String[] _lettersmaj = null;
public static String[] _lettersmin = null;
public static String[] _digits = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnvalid = null;
public static String _password = "";
public static String _newpassword = "";
public anywheresoftware.b4a.objects.collections.List _allowedchars = null;
public static String _getvar = "";
public anywheresoftware.b4a.objects.ProgressBarWrapper _progressbar1 = null;
public static short _count = (short)0;
public b4a.example.starter _starter = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 38;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 40;BA.debugLine="Activity.LoadLayout(\"mlayout\")";
mostCurrent._activity.LoadLayout("mlayout",mostCurrent.activityBA);
 //BA.debugLineNum = 41;BA.debugLine="lblPassScreen.Text = \"\"";
mostCurrent._lblpassscreen.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 44;BA.debugLine="password = \"eleve\"";
mostCurrent._password = "eleve";
 //BA.debugLineNum = 46;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
int _rep = 0;
String _txt = "";
 //BA.debugLineNum = 56;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 58;BA.debugLine="Private Rep As Int";
_rep = 0;
 //BA.debugLineNum = 59;BA.debugLine="Private Txt As String";
_txt = "";
 //BA.debugLineNum = 61;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then ' Teste s";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 62;BA.debugLine="Txt = \"Voulez-vous vraiment quitter programme ?\"";
_txt = "Voulez-vous vraiment quitter programme ?";
 //BA.debugLineNum = 63;BA.debugLine="Rep = Msgbox2(Txt,\"A T T E N T I O N\",\"Oui\",\"\",\"";
_rep = anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence(_txt),BA.ObjectToCharSequence("A T T E N T I O N"),"Oui","","Non",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 64;BA.debugLine="If Rep = DialogResponse.POSITIVE Then ' Si Rep =";
if (_rep==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 65;BA.debugLine="Return False ' Return = False l’événement ne se";
if (true) return anywheresoftware.b4a.keywords.Common.False;
 }else {
 //BA.debugLineNum = 67;BA.debugLine="Return True ' Return = True l’événement sera co";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 };
 //BA.debugLineNum = 70;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 52;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 54;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 48;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 50;BA.debugLine="End Sub";
return "";
}
public static void  _btnvalid_click() throws Exception{
ResumableSub_btnValid_Click rsub = new ResumableSub_btnValid_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_btnValid_Click extends BA.ResumableSub {
public ResumableSub_btnValid_Click(b4a.example.main parent) {
this.parent = parent;
}
b4a.example.main parent;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 115;BA.debugLine="Log(newPassword)";
anywheresoftware.b4a.keywords.Common.Log(parent.mostCurrent._newpassword);
 //BA.debugLineNum = 116;BA.debugLine="If newPassword = password Then";
if (true) break;

case 1:
//if
this.state = 6;
if ((parent.mostCurrent._newpassword).equals(parent.mostCurrent._password)) { 
this.state = 3;
}else {
this.state = 5;
}if (true) break;

case 3:
//C
this.state = 6;
 //BA.debugLineNum = 117;BA.debugLine="lblPassScreen.Text = \"Correct password\"";
parent.mostCurrent._lblpassscreen.setText(BA.ObjectToCharSequence("Correct password"));
 //BA.debugLineNum = 118;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 7;
return;
case 7:
//C
this.state = 6;
;
 //BA.debugLineNum = 119;BA.debugLine="lblPassScreen.Text = \"\"";
parent.mostCurrent._lblpassscreen.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 120;BA.debugLine="getUserInput.Text = \"\"";
parent.mostCurrent._getuserinput.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 121;BA.debugLine="getVar = \"\"";
parent.mostCurrent._getvar = "";
 //BA.debugLineNum = 122;BA.debugLine="Log(\"Correct password\")";
anywheresoftware.b4a.keywords.Common.Log("Correct password");
 if (true) break;

case 5:
//C
this.state = 6;
 //BA.debugLineNum = 126;BA.debugLine="lblPassScreen.Text = \"Incorrect password\"";
parent.mostCurrent._lblpassscreen.setText(BA.ObjectToCharSequence("Incorrect password"));
 //BA.debugLineNum = 127;BA.debugLine="Sleep(1000)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (1000));
this.state = 8;
return;
case 8:
//C
this.state = 6;
;
 //BA.debugLineNum = 128;BA.debugLine="lblPassScreen.Text = \"\"";
parent.mostCurrent._lblpassscreen.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 129;BA.debugLine="getUserInput.Text = \"\"";
parent.mostCurrent._getuserinput.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 130;BA.debugLine="getVar = \"\"";
parent.mostCurrent._getvar = "";
 //BA.debugLineNum = 131;BA.debugLine="Log(\"Incorrect password\")";
anywheresoftware.b4a.keywords.Common.Log("Incorrect password");
 if (true) break;

case 6:
//C
this.state = -1;
;
 //BA.debugLineNum = 135;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _getuserinput_textchanged(String _old,String _new) throws Exception{
int _i = 0;
int _j = 0;
int _k = 0;
 //BA.debugLineNum = 73;BA.debugLine="Sub getUserInput_TextChanged (Old As String, New A";
 //BA.debugLineNum = 74;BA.debugLine="allowedChars.Initialize ' initialize allowed char";
mostCurrent._allowedchars.Initialize();
 //BA.debugLineNum = 75;BA.debugLine="lettersMin = Array As String (\"a\",\"b\",\"c\",\"d\",\"e\"";
mostCurrent._lettersmin = new String[]{"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
 //BA.debugLineNum = 76;BA.debugLine="lettersMaj = Array As String (\"A\",\"B\",\"C\",\"D\",\"E\"";
mostCurrent._lettersmaj = new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
 //BA.debugLineNum = 77;BA.debugLine="digits = Array As String (\"1\",\"2\",\"3\",\"4\",\"5\",\"6\"";
mostCurrent._digits = new String[]{"1","2","3","4","5","6","7","8","9","0"};
 //BA.debugLineNum = 85;BA.debugLine="For i = 0 To digits.Length - 1";
{
final int step5 = 1;
final int limit5 = (int) (mostCurrent._digits.length-1);
_i = (int) (0) ;
for (;_i <= limit5 ;_i = _i + step5 ) {
 //BA.debugLineNum = 86;BA.debugLine="allowedChars.Add(digits(i))";
mostCurrent._allowedchars.Add((Object)(mostCurrent._digits[_i]));
 }
};
 //BA.debugLineNum = 89;BA.debugLine="For j = 0 To lettersMin.Length -1";
{
final int step8 = 1;
final int limit8 = (int) (mostCurrent._lettersmin.length-1);
_j = (int) (0) ;
for (;_j <= limit8 ;_j = _j + step8 ) {
 //BA.debugLineNum = 90;BA.debugLine="allowedChars.Add(lettersMin(j))";
mostCurrent._allowedchars.Add((Object)(mostCurrent._lettersmin[_j]));
 //BA.debugLineNum = 91;BA.debugLine="allowedChars.Add(lettersMaj(j))";
mostCurrent._allowedchars.Add((Object)(mostCurrent._lettersmaj[_j]));
 }
};
 //BA.debugLineNum = 95;BA.debugLine="For k = 0 To allowedChars.Size - 1";
{
final int step12 = 1;
final int limit12 = (int) (mostCurrent._allowedchars.getSize()-1);
_k = (int) (0) ;
for (;_k <= limit12 ;_k = _k + step12 ) {
 //BA.debugLineNum = 96;BA.debugLine="If allowedChars.Get(k) <> getUserInput.Text Then";
if ((mostCurrent._allowedchars.Get(_k)).equals((Object)(mostCurrent._getuserinput.getText())) == false) { 
 //BA.debugLineNum = 97;BA.debugLine="Log(\"something\")";
anywheresoftware.b4a.keywords.Common.Log("something");
 }else {
 //BA.debugLineNum = 100;BA.debugLine="Log(\"ok passed\")";
anywheresoftware.b4a.keywords.Common.Log("ok passed");
 //BA.debugLineNum = 101;BA.debugLine="getVar = getVar & getUserInput.Text";
mostCurrent._getvar = mostCurrent._getvar+mostCurrent._getuserinput.getText();
 //BA.debugLineNum = 102;BA.debugLine="getUserInput.Text = \"\"";
mostCurrent._getuserinput.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 103;BA.debugLine="Log(\"getVar is \" & getVar)";
anywheresoftware.b4a.keywords.Common.Log("getVar is "+mostCurrent._getvar);
 };
 }
};
 //BA.debugLineNum = 107;BA.debugLine="newPassword = getVar";
mostCurrent._newpassword = mostCurrent._getvar;
 //BA.debugLineNum = 108;BA.debugLine="Log(\"text_changed \" & newPassword)";
anywheresoftware.b4a.keywords.Common.Log("text_changed "+mostCurrent._newpassword);
 //BA.debugLineNum = 109;BA.debugLine="lblPassScreen.Text = getVar";
mostCurrent._lblpassscreen.setText(BA.ObjectToCharSequence(mostCurrent._getvar));
 //BA.debugLineNum = 111;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 21;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 23;BA.debugLine="Private pnl As Panel";
mostCurrent._pnl = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Private lblPassScreen As Label";
mostCurrent._lblpassscreen = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Private getUserInput As EditText";
mostCurrent._getuserinput = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Private lettersMaj() As String";
mostCurrent._lettersmaj = new String[(int) (0)];
java.util.Arrays.fill(mostCurrent._lettersmaj,"");
 //BA.debugLineNum = 27;BA.debugLine="Private lettersMin() As String";
mostCurrent._lettersmin = new String[(int) (0)];
java.util.Arrays.fill(mostCurrent._lettersmin,"");
 //BA.debugLineNum = 28;BA.debugLine="Private digits() As String";
mostCurrent._digits = new String[(int) (0)];
java.util.Arrays.fill(mostCurrent._digits,"");
 //BA.debugLineNum = 29;BA.debugLine="Private btnValid As Button";
mostCurrent._btnvalid = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Private password As String";
mostCurrent._password = "";
 //BA.debugLineNum = 31;BA.debugLine="Private newPassword As String";
mostCurrent._newpassword = "";
 //BA.debugLineNum = 32;BA.debugLine="Private allowedChars As List";
mostCurrent._allowedchars = new anywheresoftware.b4a.objects.collections.List();
 //BA.debugLineNum = 33;BA.debugLine="Private getVar As String";
mostCurrent._getvar = "";
 //BA.debugLineNum = 34;BA.debugLine="Private ProgressBar1 As ProgressBar";
mostCurrent._progressbar1 = new anywheresoftware.b4a.objects.ProgressBarWrapper();
 //BA.debugLineNum = 35;BA.debugLine="Private count As Short";
_count = (short)0;
 //BA.debugLineNum = 36;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main._process_globals();
starter._process_globals();
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 15;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 19;BA.debugLine="End Sub";
return "";
}
}
