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
public anywheresoftware.b4a.objects.LabelWrapper _lblpassscreen = null;
public static String _lettersmaj = "";
public static String _lettersmin = "";
public static String _digits = "";
public static String _combinedstring = "";
public anywheresoftware.b4a.objects.ButtonWrapper _btnvalid = null;
public static String _password = "";
public anywheresoftware.b4a.objects.EditTextWrapper _edtqty = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblqty = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _chkdig = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _chkmaj = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _chkmin = null;
public anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper _chkchar = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edtchar = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblchar = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbltxtlist = null;
public b4a.example.starter _starter = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 47;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 49;BA.debugLine="Activity.LoadLayout(\"mlayout\")";
mostCurrent._activity.LoadLayout("mlayout",mostCurrent.activityBA);
 //BA.debugLineNum = 50;BA.debugLine="btnValid.Enabled = False";
mostCurrent._btnvalid.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 52;BA.debugLine="edtQTY.InputType = edtQTY.INPUT_TYPE_NUMBERS";
mostCurrent._edtqty.setInputType(mostCurrent._edtqty.INPUT_TYPE_NUMBERS);
 //BA.debugLineNum = 55;BA.debugLine="edtQTY.RequestFocus";
mostCurrent._edtqty.RequestFocus();
 //BA.debugLineNum = 57;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
int _rep = 0;
String _txt = "";
 //BA.debugLineNum = 67;BA.debugLine="Sub Activity_KeyPress (KeyCode As Int) As Boolean";
 //BA.debugLineNum = 69;BA.debugLine="Private Rep As Int";
_rep = 0;
 //BA.debugLineNum = 70;BA.debugLine="Private Txt As String";
_txt = "";
 //BA.debugLineNum = 72;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then ' Teste s";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 73;BA.debugLine="Txt = \"Voulez-vous vraiment quitter programme ?\"";
_txt = "Voulez-vous vraiment quitter programme ?";
 //BA.debugLineNum = 74;BA.debugLine="Rep = Msgbox2(Txt,\"A T T E N T I O N\",\"Oui\",\"\",\"";
_rep = anywheresoftware.b4a.keywords.Common.Msgbox2(BA.ObjectToCharSequence(_txt),BA.ObjectToCharSequence("A T T E N T I O N"),"Oui","","Non",(android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.Null),mostCurrent.activityBA);
 //BA.debugLineNum = 75;BA.debugLine="If Rep = DialogResponse.POSITIVE Then ' Si Rep =";
if (_rep==anywheresoftware.b4a.keywords.Common.DialogResponse.POSITIVE) { 
 //BA.debugLineNum = 76;BA.debugLine="Return False ' Return = False l’événement ne se";
if (true) return anywheresoftware.b4a.keywords.Common.False;
 }else {
 //BA.debugLineNum = 78;BA.debugLine="Return True ' Return = True l’événement sera co";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 };
 };
 //BA.debugLineNum = 84;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 63;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 65;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 59;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 61;BA.debugLine="End Sub";
return "";
}
public static String  _btnvalid_click() throws Exception{
byte _getpasslength = (byte)0;
int _i = 0;
 //BA.debugLineNum = 110;BA.debugLine="Sub btnValid_Click";
 //BA.debugLineNum = 111;BA.debugLine="Private getPassLength As Byte";
_getpasslength = (byte)0;
 //BA.debugLineNum = 120;BA.debugLine="getCombinedString";
_getcombinedstring();
 //BA.debugLineNum = 124;BA.debugLine="getPassLength = 0";
_getpasslength = (byte) (0);
 //BA.debugLineNum = 125;BA.debugLine="Log(edtQTY.Text)";
anywheresoftware.b4a.keywords.Common.Log(mostCurrent._edtqty.getText());
 //BA.debugLineNum = 126;BA.debugLine="getPassLength = edtQTY.Text";
_getpasslength = (byte)(Double.parseDouble(mostCurrent._edtqty.getText()));
 //BA.debugLineNum = 127;BA.debugLine="Log(getPassLength)";
anywheresoftware.b4a.keywords.Common.Log(BA.NumberToString(_getpasslength));
 //BA.debugLineNum = 129;BA.debugLine="Log(combinedString)";
anywheresoftware.b4a.keywords.Common.Log(mostCurrent._combinedstring);
 //BA.debugLineNum = 132;BA.debugLine="For i = 1  To getPassLength";
{
final int step8 = 1;
final int limit8 = (int) (_getpasslength);
_i = (int) (1) ;
for (;_i <= limit8 ;_i = _i + step8 ) {
 //BA.debugLineNum = 133;BA.debugLine="password = password & combinedString.CharAt(Rnd(";
mostCurrent._password = mostCurrent._password+BA.ObjectToString(mostCurrent._combinedstring.charAt(anywheresoftware.b4a.keywords.Common.Rnd((int) (0),mostCurrent._combinedstring.length())));
 }
};
 //BA.debugLineNum = 136;BA.debugLine="Log(password)";
anywheresoftware.b4a.keywords.Common.Log(mostCurrent._password);
 //BA.debugLineNum = 137;BA.debugLine="lblPassScreen.Text = password";
mostCurrent._lblpassscreen.setText(BA.ObjectToCharSequence(mostCurrent._password));
 //BA.debugLineNum = 139;BA.debugLine="Log(password)";
anywheresoftware.b4a.keywords.Common.Log(mostCurrent._password);
 //BA.debugLineNum = 140;BA.debugLine="password = \"\"";
mostCurrent._password = "";
 //BA.debugLineNum = 141;BA.debugLine="End Sub";
return "";
}
public static String  _chkchar_checkedchange(boolean _checked) throws Exception{
 //BA.debugLineNum = 190;BA.debugLine="Sub chkChar_CheckedChange(Checked As Boolean)";
 //BA.debugLineNum = 191;BA.debugLine="If (chkMin.Checked = True) Or (chkMaj.Checked = T";
if ((mostCurrent._chkmin.getChecked()==anywheresoftware.b4a.keywords.Common.True) || (mostCurrent._chkmaj.getChecked()==anywheresoftware.b4a.keywords.Common.True) || (mostCurrent._chkdig.getChecked()==anywheresoftware.b4a.keywords.Common.True) || (mostCurrent._chkchar.getChecked()==anywheresoftware.b4a.keywords.Common.True)) { 
 //BA.debugLineNum = 192;BA.debugLine="If chkChar.Checked = True Then";
if (mostCurrent._chkchar.getChecked()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 193;BA.debugLine="lblTxtList.Visible = True";
mostCurrent._lbltxtlist.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 195;BA.debugLine="edtChar.Visible = True";
mostCurrent._edtchar.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 197;BA.debugLine="edtChar.RequestFocus";
mostCurrent._edtchar.RequestFocus();
 }else {
 //BA.debugLineNum = 200;BA.debugLine="lblTxtList.Visible = False";
mostCurrent._lbltxtlist.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 201;BA.debugLine="edtChar.Visible = False";
mostCurrent._edtchar.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 202;BA.debugLine="lblChar.Visible = False";
mostCurrent._lblchar.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 203;BA.debugLine="edtQTY.RequestFocus";
mostCurrent._edtqty.RequestFocus();
 };
 }else {
 //BA.debugLineNum = 208;BA.debugLine="btnValid.Enabled = False";
mostCurrent._btnvalid.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 209;BA.debugLine="edtChar.Text = \"\"";
mostCurrent._edtchar.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 210;BA.debugLine="getCombinedString";
_getcombinedstring();
 };
 //BA.debugLineNum = 213;BA.debugLine="End Sub";
return "";
}
public static String  _chkdig_checkedchange(boolean _checked) throws Exception{
 //BA.debugLineNum = 177;BA.debugLine="Sub chkDig_CheckedChange(Checked As Boolean)";
 //BA.debugLineNum = 178;BA.debugLine="If (chkMin.Checked = True) Or (chkMaj.Checked = T";
if ((mostCurrent._chkmin.getChecked()==anywheresoftware.b4a.keywords.Common.True) || (mostCurrent._chkmaj.getChecked()==anywheresoftware.b4a.keywords.Common.True) || (mostCurrent._chkdig.getChecked()==anywheresoftware.b4a.keywords.Common.True) || (mostCurrent._chkchar.getChecked()==anywheresoftware.b4a.keywords.Common.True)) { 
 //BA.debugLineNum = 179;BA.debugLine="btnValid.Enabled = True";
mostCurrent._btnvalid.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 181;BA.debugLine="btnValid.Enabled = False";
mostCurrent._btnvalid.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 182;BA.debugLine="getCombinedString";
_getcombinedstring();
 };
 //BA.debugLineNum = 186;BA.debugLine="End Sub";
return "";
}
public static String  _chkmaj_checkedchange(boolean _checked) throws Exception{
 //BA.debugLineNum = 166;BA.debugLine="Sub chkMaj_CheckedChange(Checked As Boolean)";
 //BA.debugLineNum = 167;BA.debugLine="If (chkMin.Checked = True) Or (chkMaj.Checked = T";
if ((mostCurrent._chkmin.getChecked()==anywheresoftware.b4a.keywords.Common.True) || (mostCurrent._chkmaj.getChecked()==anywheresoftware.b4a.keywords.Common.True) || (mostCurrent._chkdig.getChecked()==anywheresoftware.b4a.keywords.Common.True) || (mostCurrent._chkchar.getChecked()==anywheresoftware.b4a.keywords.Common.True)) { 
 //BA.debugLineNum = 168;BA.debugLine="btnValid.Enabled = True";
mostCurrent._btnvalid.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 170;BA.debugLine="btnValid.Enabled = False";
mostCurrent._btnvalid.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 171;BA.debugLine="getCombinedString";
_getcombinedstring();
 };
 //BA.debugLineNum = 175;BA.debugLine="End Sub";
return "";
}
public static String  _chkmin_checkedchange(boolean _checked) throws Exception{
 //BA.debugLineNum = 156;BA.debugLine="Sub chkMin_CheckedChange(Checked As Boolean)";
 //BA.debugLineNum = 157;BA.debugLine="If (chkMin.Checked = True) Or (chkMaj.Checked = T";
if ((mostCurrent._chkmin.getChecked()==anywheresoftware.b4a.keywords.Common.True) || (mostCurrent._chkmaj.getChecked()==anywheresoftware.b4a.keywords.Common.True) || (mostCurrent._chkdig.getChecked()==anywheresoftware.b4a.keywords.Common.True) || (mostCurrent._chkchar.getChecked()==anywheresoftware.b4a.keywords.Common.True)) { 
 //BA.debugLineNum = 158;BA.debugLine="btnValid.Enabled = True";
mostCurrent._btnvalid.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 160;BA.debugLine="btnValid.Enabled = False";
mostCurrent._btnvalid.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 161;BA.debugLine="getCombinedString";
_getcombinedstring();
 };
 //BA.debugLineNum = 164;BA.debugLine="End Sub";
return "";
}
public static String  _edtchar_textchanged(String _old,String _new) throws Exception{
 //BA.debugLineNum = 216;BA.debugLine="Sub edtChar_TextChanged (Old As String, New As Str";
 //BA.debugLineNum = 217;BA.debugLine="If (edtChar.Text <> \"\" ) Or (chkMin.Checked = Tru";
if (((mostCurrent._edtchar.getText()).equals("") == false) || (mostCurrent._chkmin.getChecked()==anywheresoftware.b4a.keywords.Common.True) || (mostCurrent._chkmaj.getChecked()==anywheresoftware.b4a.keywords.Common.True) || (mostCurrent._chkdig.getChecked()==anywheresoftware.b4a.keywords.Common.True)) { 
 //BA.debugLineNum = 218;BA.debugLine="btnValid.Enabled = True";
mostCurrent._btnvalid.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 220;BA.debugLine="btnValid.Enabled = False";
mostCurrent._btnvalid.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 224;BA.debugLine="End Sub";
return "";
}
public static String  _edtqty_textchanged(String _old,String _new) throws Exception{
 //BA.debugLineNum = 143;BA.debugLine="Sub edtQTY_TextChanged (Old As String, New As Stri";
 //BA.debugLineNum = 144;BA.debugLine="If (edtQTY.Text <> \"\") And ((chkMin.Checked = Tru";
if (((mostCurrent._edtqty.getText()).equals("") == false) && ((mostCurrent._chkmin.getChecked()==anywheresoftware.b4a.keywords.Common.True) || (mostCurrent._chkmaj.getChecked()==anywheresoftware.b4a.keywords.Common.True) || (mostCurrent._chkdig.getChecked()==anywheresoftware.b4a.keywords.Common.True))) { 
 //BA.debugLineNum = 145;BA.debugLine="btnValid.Enabled = True";
mostCurrent._btnvalid.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 }else {
 //BA.debugLineNum = 147;BA.debugLine="btnValid.Enabled = False";
mostCurrent._btnvalid.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 };
 //BA.debugLineNum = 150;BA.debugLine="End Sub";
return "";
}
public static String  _getcombinedstring() throws Exception{
 //BA.debugLineNum = 91;BA.debugLine="Sub getCombinedString";
 //BA.debugLineNum = 92;BA.debugLine="combinedString = \"\"";
mostCurrent._combinedstring = "";
 //BA.debugLineNum = 94;BA.debugLine="If chkMin.Checked = True Then";
if (mostCurrent._chkmin.getChecked()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 95;BA.debugLine="combinedString = combinedString & lettersMin";
mostCurrent._combinedstring = mostCurrent._combinedstring+mostCurrent._lettersmin;
 };
 //BA.debugLineNum = 97;BA.debugLine="If chkMaj.Checked = True Then";
if (mostCurrent._chkmaj.getChecked()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 98;BA.debugLine="combinedString = combinedString & lettersMaj";
mostCurrent._combinedstring = mostCurrent._combinedstring+mostCurrent._lettersmaj;
 };
 //BA.debugLineNum = 100;BA.debugLine="If chkDig.Checked = True Then";
if (mostCurrent._chkdig.getChecked()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 101;BA.debugLine="combinedString = combinedString & digits";
mostCurrent._combinedstring = mostCurrent._combinedstring+mostCurrent._digits;
 };
 //BA.debugLineNum = 103;BA.debugLine="If chkChar.Checked = True Then";
if (mostCurrent._chkchar.getChecked()==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 104;BA.debugLine="combinedString = combinedString & edtChar.Text";
mostCurrent._combinedstring = mostCurrent._combinedstring+mostCurrent._edtchar.getText();
 };
 //BA.debugLineNum = 107;BA.debugLine="End Sub";
return "";
}
public static String  _getuserinput_textchanged(String _old,String _new) throws Exception{
 //BA.debugLineNum = 86;BA.debugLine="Sub getUserInput_TextChanged (Old As String, New A";
 //BA.debugLineNum = 89;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 21;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 25;BA.debugLine="Private lblPassScreen As Label";
mostCurrent._lblpassscreen = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Private lettersMaj = \"ABCDEFGHIJKLMNOPQRSTUVWXYZ\"";
mostCurrent._lettersmaj = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
 //BA.debugLineNum = 28;BA.debugLine="Private lettersMin = \"abcdefghijklmnopqrstuvwxyz\"";
mostCurrent._lettersmin = "abcdefghijklmnopqrstuvwxyz";
 //BA.debugLineNum = 29;BA.debugLine="Private digits = \"1234567890\" As String";
mostCurrent._digits = "1234567890";
 //BA.debugLineNum = 31;BA.debugLine="Private combinedString As String";
mostCurrent._combinedstring = "";
 //BA.debugLineNum = 32;BA.debugLine="Private btnValid As Button";
mostCurrent._btnvalid = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 33;BA.debugLine="Private password As String";
mostCurrent._password = "";
 //BA.debugLineNum = 36;BA.debugLine="Private edtQTY As EditText";
mostCurrent._edtqty = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 37;BA.debugLine="Private lblQTY As Label";
mostCurrent._lblqty = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 38;BA.debugLine="Private chkDig As CheckBox";
mostCurrent._chkdig = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
 //BA.debugLineNum = 39;BA.debugLine="Private chkMaj As CheckBox";
mostCurrent._chkmaj = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
 //BA.debugLineNum = 40;BA.debugLine="Private chkMin As CheckBox";
mostCurrent._chkmin = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
 //BA.debugLineNum = 41;BA.debugLine="Private chkChar As CheckBox";
mostCurrent._chkchar = new anywheresoftware.b4a.objects.CompoundButtonWrapper.CheckBoxWrapper();
 //BA.debugLineNum = 42;BA.debugLine="Private edtChar As EditText";
mostCurrent._edtchar = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 43;BA.debugLine="Private lblChar As Label";
mostCurrent._lblchar = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 44;BA.debugLine="Private lblTxtList As Label";
mostCurrent._lbltxtlist = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 45;BA.debugLine="End Sub";
return "";
}
public static String  _lbltxtlist_click() throws Exception{
 //BA.debugLineNum = 226;BA.debugLine="Sub lblTxtList_Click";
 //BA.debugLineNum = 228;BA.debugLine="End Sub";
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
