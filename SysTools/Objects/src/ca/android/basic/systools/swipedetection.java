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

public class swipedetection extends Activity implements B4AActivity{
	public static swipedetection mostCurrent;
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
			processBA = new BA(this.getApplicationContext(), null, null, "ca.android.basic.systools", "ca.android.basic.systools.swipedetection");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (swipedetection).");
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
		activityBA = new BA(this, layout, processBA, "ca.android.basic.systools", "ca.android.basic.systools.swipedetection");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "ca.android.basic.systools.swipedetection", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (swipedetection) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (swipedetection) Resume **");
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
		return swipedetection.class;
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
        BA.LogInfo("** Activity (swipedetection) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
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
            swipedetection mc = mostCurrent;
			if (mc == null || mc != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (swipedetection) Resume **");
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
public static String _getapp = "";
public anywheresoftware.b4a.objects.PanelWrapper _pnldirection = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblup = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbldown = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblright = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblleft = null;
public static int _minimaldistance = 0;
public static float _x1 = 0f;
public static float _x2 = 0f;
public static float _y1 = 0f;
public static float _y2 = 0f;
public static boolean _runtime = false;
public anywheresoftware.b4a.samples.httputils2.httputils2service _httputils2service = null;
public ca.android.basic.systools.main _main = null;
public ca.android.basic.systools.showinfo _showinfo = null;
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
int _w = 0;
int _h = 0;
 //BA.debugLineNum = 20;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 22;BA.debugLine="If FirstTime = True Then";
if (_firsttime==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 23;BA.debugLine="runtime = True";
_runtime = anywheresoftware.b4a.keywords.Common.True;
 }else {
 //BA.debugLineNum = 25;BA.debugLine="runtime = False";
_runtime = anywheresoftware.b4a.keywords.Common.False;
 };
 //BA.debugLineNum = 28;BA.debugLine="Private W As Int = 100dip 'This is picture width";
_w = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (100));
 //BA.debugLineNum = 29;BA.debugLine="Private H As Int = 100dip ' and height";
_h = anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (100));
 //BA.debugLineNum = 31;BA.debugLine="Activity.Title = \"DB's SysTools\"";
mostCurrent._activity.setTitle(BA.ObjectToCharSequence("DB's SysTools"));
 //BA.debugLineNum = 32;BA.debugLine="Activity.Initialize(\"swipeDetection\")";
mostCurrent._activity.Initialize(mostCurrent.activityBA,"swipeDetection");
 //BA.debugLineNum = 33;BA.debugLine="Activity.Color = Colors.DarkGray";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.DarkGray);
 //BA.debugLineNum = 34;BA.debugLine="pnlDirection.Initialize(\"pnlDirection\")";
mostCurrent._pnldirection.Initialize(mostCurrent.activityBA,"pnlDirection");
 //BA.debugLineNum = 35;BA.debugLine="pnlDirection.SetBackgroundImage(LoadBitmap(File.D";
mostCurrent._pnldirection.SetBackgroundImageNew((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"swipeMenu.png").getObject()));
 //BA.debugLineNum = 36;BA.debugLine="lblUp.Initialize(\"lblUp\")";
mostCurrent._lblup.Initialize(mostCurrent.activityBA,"lblUp");
 //BA.debugLineNum = 37;BA.debugLine="lblDown.Initialize(\"lblDown\")";
mostCurrent._lbldown.Initialize(mostCurrent.activityBA,"lblDown");
 //BA.debugLineNum = 38;BA.debugLine="lblRight.Initialize(\"lblRight\")";
mostCurrent._lblright.Initialize(mostCurrent.activityBA,"lblRight");
 //BA.debugLineNum = 39;BA.debugLine="lblLeft.Initialize(\"lblLeft\")";
mostCurrent._lblleft.Initialize(mostCurrent.activityBA,"lblLeft");
 //BA.debugLineNum = 40;BA.debugLine="lblUp.Gravity = Bit.Or(Gravity.CENTER, Gravity.CE";
mostCurrent._lblup.setGravity(anywheresoftware.b4a.keywords.Common.Bit.Or(anywheresoftware.b4a.keywords.Common.Gravity.CENTER,anywheresoftware.b4a.keywords.Common.Gravity.CENTER_HORIZONTAL));
 //BA.debugLineNum = 41;BA.debugLine="lblUp.Height = 10dip";
mostCurrent._lblup.setHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 42;BA.debugLine="lblUp.Width = -2 ' -2 constante speciale pour l'a";
mostCurrent._lblup.setWidth((int) (-2));
 //BA.debugLineNum = 43;BA.debugLine="lblUp.TextSize = 20 'Description labels";
mostCurrent._lblup.setTextSize((float) (20));
 //BA.debugLineNum = 44;BA.debugLine="lblUp.TextColor = Colors.white";
mostCurrent._lblup.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 45;BA.debugLine="lblUp.Text = \"Internal\"";
mostCurrent._lblup.setText(BA.ObjectToCharSequence("Internal"));
 //BA.debugLineNum = 46;BA.debugLine="lblDown.Gravity = Bit.Or(Gravity.CENTER, Gravity.";
mostCurrent._lbldown.setGravity(anywheresoftware.b4a.keywords.Common.Bit.Or(anywheresoftware.b4a.keywords.Common.Gravity.CENTER,anywheresoftware.b4a.keywords.Common.Gravity.CENTER_HORIZONTAL));
 //BA.debugLineNum = 47;BA.debugLine="lblDown.Height = 10dip";
mostCurrent._lbldown.setHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 48;BA.debugLine="lblDown.Width = -2";
mostCurrent._lbldown.setWidth((int) (-2));
 //BA.debugLineNum = 49;BA.debugLine="lblDown.TextSize = 20";
mostCurrent._lbldown.setTextSize((float) (20));
 //BA.debugLineNum = 50;BA.debugLine="lblDown.TextColor = Colors.white";
mostCurrent._lbldown.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 51;BA.debugLine="lblDown.Text = \"System\"";
mostCurrent._lbldown.setText(BA.ObjectToCharSequence("System"));
 //BA.debugLineNum = 52;BA.debugLine="lblRight.Gravity = Bit.Or(Gravity.CENTER, Gravity";
mostCurrent._lblright.setGravity(anywheresoftware.b4a.keywords.Common.Bit.Or(anywheresoftware.b4a.keywords.Common.Gravity.CENTER,anywheresoftware.b4a.keywords.Common.Gravity.CENTER_HORIZONTAL));
 //BA.debugLineNum = 53;BA.debugLine="lblRight.Height = 10dip";
mostCurrent._lblright.setHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 54;BA.debugLine="lblRight.Width = -2";
mostCurrent._lblright.setWidth((int) (-2));
 //BA.debugLineNum = 55;BA.debugLine="lblRight.TextSize = 20";
mostCurrent._lblright.setTextSize((float) (20));
 //BA.debugLineNum = 56;BA.debugLine="lblRight.TextColor = Colors.white";
mostCurrent._lblright.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 57;BA.debugLine="lblRight.Text = \"File system\"";
mostCurrent._lblright.setText(BA.ObjectToCharSequence("File system"));
 //BA.debugLineNum = 58;BA.debugLine="lblLeft.Gravity = Bit.Or(Gravity.CENTER, Gravity.";
mostCurrent._lblleft.setGravity(anywheresoftware.b4a.keywords.Common.Bit.Or(anywheresoftware.b4a.keywords.Common.Gravity.CENTER,anywheresoftware.b4a.keywords.Common.Gravity.CENTER_HORIZONTAL));
 //BA.debugLineNum = 59;BA.debugLine="lblLeft.Height = 10dip";
mostCurrent._lblleft.setHeight(anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)));
 //BA.debugLineNum = 60;BA.debugLine="lblLeft.Width = -2";
mostCurrent._lblleft.setWidth((int) (-2));
 //BA.debugLineNum = 61;BA.debugLine="lblLeft.TextSize = 20";
mostCurrent._lblleft.setTextSize((float) (20));
 //BA.debugLineNum = 62;BA.debugLine="lblLeft.TextColor = Colors.white";
mostCurrent._lblleft.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 63;BA.debugLine="lblLeft.Text = \"Network\" 'Description labels end";
mostCurrent._lblleft.setText(BA.ObjectToCharSequence("Network"));
 //BA.debugLineNum = 64;BA.debugLine="Activity.AddView(pnlDirection, (100%x - W)/2, (10";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._pnldirection.getObject()),(int) ((anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)-_w)/(double)2),(int) ((anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)-_h)/(double)2),_w,_h);
 //BA.debugLineNum = 65;BA.debugLine="Activity.AddView(lblUp,(100%x - W)/2, 25%y , W, H";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lblup.getObject()),(int) ((anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)-_w)/(double)2),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (25),mostCurrent.activityBA),_w,_h);
 //BA.debugLineNum = 66;BA.debugLine="Activity.AddView(lblDown,(100%x - W)/2, 60%y , W,";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lbldown.getObject()),(int) ((anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA)-_w)/(double)2),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (60),mostCurrent.activityBA),_w,_h);
 //BA.debugLineNum = 67;BA.debugLine="Activity.AddView(lblRight,(95%x + W + H)/2, 42%y";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lblright.getObject()),(int) ((anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (95),mostCurrent.activityBA)+_w+_h)/(double)2),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (42),mostCurrent.activityBA),_w,_h);
 //BA.debugLineNum = 68;BA.debugLine="Activity.AddView(lblLeft,(33%x - W)/2, 42%y , W,";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lblleft.getObject()),(int) ((anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (33),mostCurrent.activityBA)-_w)/(double)2),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (42),mostCurrent.activityBA),_w,_h);
 //BA.debugLineNum = 69;BA.debugLine="ToastMessageShow(\"Swipe any direction to enter a";
anywheresoftware.b4a.keywords.Common.ToastMessageShow(BA.ObjectToCharSequence("Swipe any direction to enter a sub menu"),anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 70;BA.debugLine="End Sub";
return "";
}
public static boolean  _activity_keypress(int _keycode) throws Exception{
 //BA.debugLineNum = 121;BA.debugLine="Sub Activity_KeyPress(KeyCode As Int) As Boolean";
 //BA.debugLineNum = 123;BA.debugLine="If	runtime = True Then";
if (_runtime==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 124;BA.debugLine="Activity.finish";
mostCurrent._activity.Finish();
 }else {
 //BA.debugLineNum = 126;BA.debugLine="If KeyCode = KeyCodes.KEYCODE_BACK Then";
if (_keycode==anywheresoftware.b4a.keywords.Common.KeyCodes.KEYCODE_BACK) { 
 //BA.debugLineNum = 127;BA.debugLine="Activity.finish";
mostCurrent._activity.Finish();
 //BA.debugLineNum = 128;BA.debugLine="StartActivity(\"main\")";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)("main"));
 //BA.debugLineNum = 129;BA.debugLine="Return True";
if (true) return anywheresoftware.b4a.keywords.Common.True;
 //BA.debugLineNum = 130;BA.debugLine="StartActivity(\"main\")";
anywheresoftware.b4a.keywords.Common.StartActivity(processBA,(Object)("main"));
 };
 };
 //BA.debugLineNum = 133;BA.debugLine="End Sub";
return false;
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 72;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 74;BA.debugLine="If	runtime = True Then";
if (_runtime==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 76;BA.debugLine="If UserClosed = True Then";
if (_userclosed==anywheresoftware.b4a.keywords.Common.True) { 
 //BA.debugLineNum = 77;BA.debugLine="Activity.finish";
mostCurrent._activity.Finish();
 };
 };
 //BA.debugLineNum = 81;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 12;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 13;BA.debugLine="Private pnlDirection As Panel";
mostCurrent._pnldirection = new anywheresoftware.b4a.objects.PanelWrapper();
 //BA.debugLineNum = 14;BA.debugLine="Private lblUp,lblDown,lblRight,lblLeft As Label";
mostCurrent._lblup = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lbldown = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lblright = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lblleft = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 15;BA.debugLine="Private Minimaldistance As Int = 150";
_minimaldistance = (int) (150);
 //BA.debugLineNum = 16;BA.debugLine="Private x1,x2,y1,y2 As Float";
_x1 = 0f;
_x2 = 0f;
_y1 = 0f;
_y2 = 0f;
 //BA.debugLineNum = 17;BA.debugLine="Private runtime As Boolean";
_runtime = false;
 //BA.debugLineNum = 18;BA.debugLine="End Sub";
return "";
}
public static String  _pnldirection_touch(int _action,float _x,float _y) throws Exception{
float _deltax = 0f;
float _deltay = 0f;
 //BA.debugLineNum = 83;BA.debugLine="Sub pnlDirection_Touch (Action As Int, X As Float,";
 //BA.debugLineNum = 85;BA.debugLine="Select Action";
switch (BA.switchObjectToInt(_action,mostCurrent._activity.ACTION_DOWN,mostCurrent._activity.ACTION_UP)) {
case 0: {
 //BA.debugLineNum = 87;BA.debugLine="x1 = x";
_x1 = _x;
 //BA.debugLineNum = 88;BA.debugLine="y1 = y";
_y1 = _y;
 break; }
case 1: {
 //BA.debugLineNum = 90;BA.debugLine="x2 = x";
_x2 = _x;
 //BA.debugLineNum = 91;BA.debugLine="y2 = y";
_y2 = _y;
 //BA.debugLineNum = 93;BA.debugLine="Private deltaX As Float = x2 - x1";
_deltax = (float) (_x2-_x1);
 //BA.debugLineNum = 94;BA.debugLine="Private deltaY As Float = y2 - y1";
_deltay = (float) (_y2-_y1);
 //BA.debugLineNum = 96;BA.debugLine="If Abs(deltaX) > Minimaldistance Then";
if (anywheresoftware.b4a.keywords.Common.Abs(_deltax)>_minimaldistance) { 
 //BA.debugLineNum = 97;BA.debugLine="If x2 > x1 Then";
if (_x2>_x1) { 
 //BA.debugLineNum = 99;BA.debugLine="getApp = \"appFileSystem\" 'Left to Right";
_getapp = "appFileSystem";
 //BA.debugLineNum = 100;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 }else {
 //BA.debugLineNum = 103;BA.debugLine="getApp = \"appIPTools\" 'Right to Left";
_getapp = "appIPTools";
 //BA.debugLineNum = 104;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 };
 }else if(anywheresoftware.b4a.keywords.Common.Abs(_deltay)>_minimaldistance) { 
 //BA.debugLineNum = 108;BA.debugLine="If y2 > y1 Then";
if (_y2>_y1) { 
 //BA.debugLineNum = 110;BA.debugLine="getApp = \"appSysTools\" 'Top to Down";
_getapp = "appSysTools";
 //BA.debugLineNum = 111;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 }else {
 //BA.debugLineNum = 114;BA.debugLine="getApp = \"appInternalTools\" 'Down to Top";
_getapp = "appInternalTools";
 //BA.debugLineNum = 115;BA.debugLine="Activity.Finish";
mostCurrent._activity.Finish();
 };
 };
 break; }
}
;
 //BA.debugLineNum = 119;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Public getApp As String";
_getapp = "";
 //BA.debugLineNum = 10;BA.debugLine="End Sub";
return "";
}
}
