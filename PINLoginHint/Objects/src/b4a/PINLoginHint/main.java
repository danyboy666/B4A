package b4a.PINLoginHint;


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
	public static final boolean includeTitle = true;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        mostCurrent = this;
		if (processBA == null) {
			processBA = new BA(this.getApplicationContext(), null, null, "b4a.PINLoginHint", "b4a.PINLoginHint.main");
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
		activityBA = new BA(this, layout, processBA, "b4a.PINLoginHint", "b4a.PINLoginHint.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "b4a.PINLoginHint.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
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
public anywheresoftware.b4a.objects.EditTextWrapper _edtpwd = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnce = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn0 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn1 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn2 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn3 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn4 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn5 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn6 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn7 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn8 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn9 = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnlkey1 = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnlkey2 = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnlkey3 = null;
public anywheresoftware.b4a.objects.PanelWrapper _pnlkey4 = null;
public static String _btnvalue = "";
public static int _count = 0;
public static int _attempt = 0;
public static String _master = "";
public static String _temp = "";
public anywheresoftware.b4a.objects.ButtonWrapper _btnhint = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnchange = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbltxt = null;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 42;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 44;BA.debugLine="Activity.LoadLayout(\"Layout1\")";
mostCurrent._activity.LoadLayout("Layout1",mostCurrent.activityBA);
 //BA.debugLineNum = 46;BA.debugLine="pnlKey1.Color = Colors.White";
mostCurrent._pnlkey1.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 47;BA.debugLine="pnlKey2.Color = Colors.White";
mostCurrent._pnlkey2.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 48;BA.debugLine="pnlKey3.Color = Colors.White";
mostCurrent._pnlkey3.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 49;BA.debugLine="pnlKey4.Color = Colors.White";
mostCurrent._pnlkey4.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 51;BA.debugLine="edtPWD.Visible = False";
mostCurrent._edtpwd.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 52;BA.debugLine="lblTxt.Visible = False";
mostCurrent._lbltxt.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 54;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 60;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 62;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 56;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 58;BA.debugLine="End Sub";
return "";
}
public static String  _btnchange_click() throws Exception{
 //BA.debugLineNum = 138;BA.debugLine="Sub btnChange_Click";
 //BA.debugLineNum = 139;BA.debugLine="master = edtPWD.Text";
mostCurrent._master = mostCurrent._edtpwd.getText();
 //BA.debugLineNum = 140;BA.debugLine="lblTxt.Visible = True";
mostCurrent._lbltxt.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 142;BA.debugLine="edtPWD.Visible = True";
mostCurrent._edtpwd.setVisible(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 143;BA.debugLine="btnHint.Enabled = False";
mostCurrent._btnhint.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 144;BA.debugLine="btn0.Enabled = False";
mostCurrent._btn0.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 145;BA.debugLine="btn1.Enabled = False";
mostCurrent._btn1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 146;BA.debugLine="btn2.Enabled = False";
mostCurrent._btn2.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 147;BA.debugLine="btn3.Enabled = False";
mostCurrent._btn3.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 148;BA.debugLine="btn4.Enabled = False";
mostCurrent._btn4.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 149;BA.debugLine="btn5.Enabled = False";
mostCurrent._btn5.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 150;BA.debugLine="btn6.Enabled = False";
mostCurrent._btn6.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 151;BA.debugLine="btn7.Enabled = False";
mostCurrent._btn7.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 152;BA.debugLine="btn8.Enabled = False";
mostCurrent._btn8.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 153;BA.debugLine="btn9.Enabled = False";
mostCurrent._btn9.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 155;BA.debugLine="End Sub";
return "";
}
public static void  _btnevent_click() throws Exception{
ResumableSub_btnEvent_Click rsub = new ResumableSub_btnEvent_Click(null);
rsub.resume(processBA, null);
}
public static class ResumableSub_btnEvent_Click extends BA.ResumableSub {
public ResumableSub_btnEvent_Click(b4a.PINLoginHint.main parent) {
this.parent = parent;
}
b4a.PINLoginHint.main parent;
anywheresoftware.b4a.objects.ButtonWrapper _btn = null;

@Override
public void resume(BA ba, Object[] result) throws Exception{

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 //BA.debugLineNum = 88;BA.debugLine="Private btn As Button";
_btn = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 89;BA.debugLine="count = count + 1";
parent._count = (int) (parent._count+1);
 //BA.debugLineNum = 91;BA.debugLine="btn = Sender";
_btn.setObject((android.widget.Button)(anywheresoftware.b4a.keywords.Common.Sender(mostCurrent.activityBA)));
 //BA.debugLineNum = 93;BA.debugLine="Select count";
if (true) break;

case 1:
//select
this.state = 22;
switch (parent._count) {
case 1: {
this.state = 3;
if (true) break;
}
case 2: {
this.state = 5;
if (true) break;
}
case 3: {
this.state = 7;
if (true) break;
}
case 4: {
this.state = 9;
if (true) break;
}
}
if (true) break;

case 3:
//C
this.state = 22;
 //BA.debugLineNum = 95;BA.debugLine="pnlKey1.Color = Colors.Red";
parent.mostCurrent._pnlkey1.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 96;BA.debugLine="temp = temp & btn.Tag";
parent.mostCurrent._temp = parent.mostCurrent._temp+BA.ObjectToString(_btn.getTag());
 if (true) break;

case 5:
//C
this.state = 22;
 //BA.debugLineNum = 99;BA.debugLine="pnlKey2.Color = Colors.Red";
parent.mostCurrent._pnlkey2.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 100;BA.debugLine="temp = temp & btn.Tag";
parent.mostCurrent._temp = parent.mostCurrent._temp+BA.ObjectToString(_btn.getTag());
 if (true) break;

case 7:
//C
this.state = 22;
 //BA.debugLineNum = 103;BA.debugLine="pnlKey3.Color = Colors.Red";
parent.mostCurrent._pnlkey3.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 104;BA.debugLine="temp = temp & btn.Tag";
parent.mostCurrent._temp = parent.mostCurrent._temp+BA.ObjectToString(_btn.getTag());
 if (true) break;

case 9:
//C
this.state = 10;
 //BA.debugLineNum = 107;BA.debugLine="pnlKey4.Color = Colors.Red";
parent.mostCurrent._pnlkey4.setColor(anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 108;BA.debugLine="temp = temp & btn.Tag";
parent.mostCurrent._temp = parent.mostCurrent._temp+BA.ObjectToString(_btn.getTag());
 //BA.debugLineNum = 110;BA.debugLine="Sleep(500)";
anywheresoftware.b4a.keywords.Common.Sleep(mostCurrent.activityBA,this,(int) (500));
this.state = 23;
return;
case 23:
//C
this.state = 10;
;
 //BA.debugLineNum = 111;BA.debugLine="If temp = master Then";
if (true) break;

case 10:
//if
this.state = 21;
if ((parent.mostCurrent._temp).equals(parent.mostCurrent._master)) { 
this.state = 12;
}else {
this.state = 14;
}if (true) break;

case 12:
//C
this.state = 21;
 //BA.debugLineNum = 112;BA.debugLine="clearScreen";
_clearscreen();
 //BA.debugLineNum = 113;BA.debugLine="Activity.Finish";
parent.mostCurrent._activity.Finish();
 //BA.debugLineNum = 114;BA.debugLine="StartActivity(Me)";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,main.getObject());
 if (true) break;

case 14:
//C
this.state = 15;
 //BA.debugLineNum = 116;BA.debugLine="attempt = attempt + 1";
parent._attempt = (int) (parent._attempt+1);
 //BA.debugLineNum = 117;BA.debugLine="If attempt = 3 Then";
if (true) break;

case 15:
//if
this.state = 20;
if (parent._attempt==3) { 
this.state = 17;
}else {
this.state = 19;
}if (true) break;

case 17:
//C
this.state = 20;
 //BA.debugLineNum = 118;BA.debugLine="Activity.Finish";
parent.mostCurrent._activity.Finish();
 if (true) break;

case 19:
//C
this.state = 20;
 //BA.debugLineNum = 120;BA.debugLine="clearScreen";
_clearscreen();
 if (true) break;

case 20:
//C
this.state = 21;
;
 if (true) break;

case 21:
//C
this.state = 22;
;
 if (true) break;

case 22:
//C
this.state = -1;
;
 //BA.debugLineNum = 125;BA.debugLine="End Sub";
if (true) break;

            }
        }
    }
}
public static String  _btnhint_click() throws Exception{
 //BA.debugLineNum = 157;BA.debugLine="Sub btnHint_Click";
 //BA.debugLineNum = 158;BA.debugLine="Msgbox(\"Le mot de passe est :\" & master,\"Mot de p";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Le mot de passe est :"+mostCurrent._master),BA.ObjectToCharSequence("Mot de passe"),mostCurrent.activityBA);
 //BA.debugLineNum = 159;BA.debugLine="End Sub";
return "";
}
public static String  _clearscreen() throws Exception{
 //BA.debugLineNum = 127;BA.debugLine="Sub clearScreen()";
 //BA.debugLineNum = 128;BA.debugLine="pnlKey1.Color = Colors.White";
mostCurrent._pnlkey1.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 129;BA.debugLine="pnlKey2.Color = Colors.White";
mostCurrent._pnlkey2.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 130;BA.debugLine="pnlKey3.Color = Colors.White";
mostCurrent._pnlkey3.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 131;BA.debugLine="pnlKey4.Color = Colors.White";
mostCurrent._pnlkey4.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 132;BA.debugLine="edtPWD.Text = \"\"";
mostCurrent._edtpwd.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 133;BA.debugLine="temp = \"\"";
mostCurrent._temp = "";
 //BA.debugLineNum = 134;BA.debugLine="count = 0";
_count = (int) (0);
 //BA.debugLineNum = 135;BA.debugLine="End Sub";
return "";
}
public static String  _edtpwd_textchanged(String _old,String _new) throws Exception{
 //BA.debugLineNum = 65;BA.debugLine="Sub edtPWD_TextChanged (Old As String, New As Stri";
 //BA.debugLineNum = 67;BA.debugLine="If edtPWD.Text.Length = 4 Then";
if (mostCurrent._edtpwd.getText().length()==4) { 
 //BA.debugLineNum = 68;BA.debugLine="master = edtPWD.Text";
mostCurrent._master = mostCurrent._edtpwd.getText();
 //BA.debugLineNum = 69;BA.debugLine="lblTxt.Visible = False";
mostCurrent._lbltxt.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 70;BA.debugLine="edtPWD.Text = \"\"";
mostCurrent._edtpwd.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 71;BA.debugLine="edtPWD.Visible = False";
mostCurrent._edtpwd.setVisible(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 72;BA.debugLine="btnHint.Enabled = True";
mostCurrent._btnhint.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 73;BA.debugLine="btn0.Enabled = True";
mostCurrent._btn0.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 74;BA.debugLine="btn1.Enabled = True";
mostCurrent._btn1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 75;BA.debugLine="btn2.Enabled = True";
mostCurrent._btn2.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 76;BA.debugLine="btn3.Enabled = True";
mostCurrent._btn3.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 77;BA.debugLine="btn4.Enabled = True";
mostCurrent._btn4.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 78;BA.debugLine="btn5.Enabled = True";
mostCurrent._btn5.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 79;BA.debugLine="btn6.Enabled = True";
mostCurrent._btn6.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 80;BA.debugLine="btn7.Enabled = True";
mostCurrent._btn7.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 81;BA.debugLine="btn8.Enabled = True";
mostCurrent._btn8.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 82;BA.debugLine="btn9.Enabled = True";
mostCurrent._btn9.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 85;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 21;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 25;BA.debugLine="Private edtPWD As EditText";
mostCurrent._edtpwd = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Private btnCE, btn0, btn1, btn2, btn3, btn4, btn5";
mostCurrent._btnce = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btn0 = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btn1 = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btn2 = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btn3 = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btn4 = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btn5 = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btn6 = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btn7 = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btn8 = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btn9 = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Private pnlKey1 As Panel";
mostCurrent._pnlkey1 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Private pnlKey2 As Panel";
mostCurrent._pnlkey2 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Private pnlKey3 As Panel";
mostCurrent._pnlkey3 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 30;BA.debugLine="Private pnlKey4 As Panel";
mostCurrent._pnlkey4 = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 32;BA.debugLine="Private btnValue As String";
mostCurrent._btnvalue = "";
 //BA.debugLineNum = 33;BA.debugLine="Private count = 0, attempt = 0 As Int";
_count = (int) (0);
_attempt = (int) (0);
 //BA.debugLineNum = 34;BA.debugLine="Private master As String = \"1234\"";
mostCurrent._master = "1234";
 //BA.debugLineNum = 35;BA.debugLine="Private temp As String";
mostCurrent._temp = "";
 //BA.debugLineNum = 37;BA.debugLine="Private btnHint As Button";
mostCurrent._btnhint = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 38;BA.debugLine="Private btnChange As Button";
mostCurrent._btnchange = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 39;BA.debugLine="Private lblTxt As Label";
mostCurrent._lbltxt = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 40;BA.debugLine="End Sub";
return "";
}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main._process_globals();
		
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
