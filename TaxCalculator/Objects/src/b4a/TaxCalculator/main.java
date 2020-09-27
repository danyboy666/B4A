package b4a.TaxCalculator;


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
			processBA = new BA(this.getApplicationContext(), null, null, "b4a.TaxCalculator", "b4a.TaxCalculator.main");
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
		activityBA = new BA(this, layout, processBA, "b4a.TaxCalculator", "b4a.TaxCalculator.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "b4a.TaxCalculator.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
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
public anywheresoftware.b4a.objects.ButtonWrapper _btncalcul = null;
public anywheresoftware.b4a.objects.ButtonWrapper _bntinv = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnbs = null;
public anywheresoftware.b4a.objects.EditTextWrapper _edtresultat = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbltotal = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbltvq = null;
public anywheresoftware.b4a.objects.LabelWrapper _lbltps = null;
public static float _montant = 0f;
public static float _montantap = 0f;
public static float _gtotal = 0f;

public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
return vis;}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 33;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 35;BA.debugLine="Activity.LoadLayout(\"Main\")";
mostCurrent._activity.LoadLayout("Main",mostCurrent.activityBA);
 //BA.debugLineNum = 37;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 43;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 45;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 39;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 41;BA.debugLine="End Sub";
return "";
}
public static String  _btnbs_click() throws Exception{
 //BA.debugLineNum = 94;BA.debugLine="Sub btnBS_Click";
 //BA.debugLineNum = 95;BA.debugLine="edtResultat.Text = \"\"";
mostCurrent._edtresultat.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 96;BA.debugLine="lblTVQ.Text = \"\"";
mostCurrent._lbltvq.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 97;BA.debugLine="lblTPS.Text = \"\"";
mostCurrent._lbltps.setText(BA.ObjectToCharSequence(""));
 //BA.debugLineNum = 98;BA.debugLine="lblTotal.Text = 0";
mostCurrent._lbltotal.setText(BA.ObjectToCharSequence(0));
 //BA.debugLineNum = 99;BA.debugLine="edtResultat.RequestFocus";
mostCurrent._edtresultat.RequestFocus();
 //BA.debugLineNum = 101;BA.debugLine="End Sub";
return "";
}
public static String  _btncalcul_click() throws Exception{
 //BA.debugLineNum = 47;BA.debugLine="Sub btnCalcul_Click";
 //BA.debugLineNum = 48;BA.debugLine="Log(\"btnAction_click\")";
anywheresoftware.b4a.keywords.Common.Log("btnAction_click");
 //BA.debugLineNum = 49;BA.debugLine="If edtResultat.Text = \"\" Then";
if ((mostCurrent._edtresultat.getText()).equals("")) { 
 //BA.debugLineNum = 50;BA.debugLine="Msgbox(\"erreur\",\"\")";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("erreur"),BA.ObjectToCharSequence(""),mostCurrent.activityBA);
 }else {
 //BA.debugLineNum = 52;BA.debugLine="Montant = edtResultat.Text";
_montant = (float)(Double.parseDouble(mostCurrent._edtresultat.getText()));
 //BA.debugLineNum = 53;BA.debugLine="calcul";
_calcul();
 };
 //BA.debugLineNum = 56;BA.debugLine="End Sub";
return "";
}
public static String  _btninv_click() throws Exception{
 //BA.debugLineNum = 58;BA.debugLine="Private Sub btnInv_Click";
 //BA.debugLineNum = 59;BA.debugLine="Log(\"btnInv_click\")";
anywheresoftware.b4a.keywords.Common.Log("btnInv_click");
 //BA.debugLineNum = 60;BA.debugLine="If edtResultat.Text = \"\" Then";
if ((mostCurrent._edtresultat.getText()).equals("")) { 
 //BA.debugLineNum = 61;BA.debugLine="Msgbox(\"erreur enter un montant apres les taxes\"";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("erreur enter un montant apres les taxes"),BA.ObjectToCharSequence(""),mostCurrent.activityBA);
 }else {
 //BA.debugLineNum = 63;BA.debugLine="Montant = edtResultat.Text";
_montant = (float)(Double.parseDouble(mostCurrent._edtresultat.getText()));
 //BA.debugLineNum = 64;BA.debugLine="Montant = MontantAp / 1.14975";
_montant = (float) (_montantap/(double)1.14975);
 //BA.debugLineNum = 65;BA.debugLine="calcul";
_calcul();
 //BA.debugLineNum = 66;BA.debugLine="If gtotal <> MontantAp Then";
if (_gtotal!=_montantap) { 
 //BA.debugLineNum = 67;BA.debugLine="Msgbox(\"erreur de cenne\",\"IMPORTANT\")";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("erreur de cenne"),BA.ObjectToCharSequence("IMPORTANT"),mostCurrent.activityBA);
 //BA.debugLineNum = 68;BA.debugLine="Log(calcul)";
anywheresoftware.b4a.keywords.Common.Log(_calcul());
 };
 };
 //BA.debugLineNum = 72;BA.debugLine="End Sub";
return "";
}
public static String  _calcul() throws Exception{
 //BA.debugLineNum = 82;BA.debugLine="Sub calcul()";
 //BA.debugLineNum = 83;BA.debugLine="Log(\"calcul sub\")";
anywheresoftware.b4a.keywords.Common.Log("calcul sub");
 //BA.debugLineNum = 84;BA.debugLine="edtResultat.Text = Round2(Montant,2)";
mostCurrent._edtresultat.setText(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.Round2(_montant,(int) (2))));
 //BA.debugLineNum = 85;BA.debugLine="lblTPS.Text = Round2(pourcent(5, Montant),2)";
mostCurrent._lbltps.setText(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.Round2(_pourcent((float) (5),_montant),(int) (2))));
 //BA.debugLineNum = 86;BA.debugLine="Log(lblTPS.Text)";
anywheresoftware.b4a.keywords.Common.Log(mostCurrent._lbltps.getText());
 //BA.debugLineNum = 87;BA.debugLine="lblTVQ.Text = Round2(pourcent(9.975, Montant),2)";
mostCurrent._lbltvq.setText(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.Round2(_pourcent((float) (9.975),_montant),(int) (2))));
 //BA.debugLineNum = 88;BA.debugLine="Log(lblTVQ.Text)";
anywheresoftware.b4a.keywords.Common.Log(mostCurrent._lbltvq.getText());
 //BA.debugLineNum = 89;BA.debugLine="gtotal = Montant + lblTPS.Text + lblTVQ.Text";
_gtotal = (float) (_montant+(double)(Double.parseDouble(mostCurrent._lbltps.getText()))+(double)(Double.parseDouble(mostCurrent._lbltvq.getText())));
 //BA.debugLineNum = 90;BA.debugLine="lblTotal.Text = Round2(gtotal,2)";
mostCurrent._lbltotal.setText(BA.ObjectToCharSequence(anywheresoftware.b4a.keywords.Common.Round2(_gtotal,(int) (2))));
 //BA.debugLineNum = 92;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 21;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 24;BA.debugLine="Private btnCalcul, bntInv, btnBS As Button";
mostCurrent._btncalcul = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._bntinv = new anywheresoftware.b4a.objects.ButtonWrapper();
mostCurrent._btnbs = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 25;BA.debugLine="Private edtResultat As EditText";
mostCurrent._edtresultat = new anywheresoftware.b4a.objects.EditTextWrapper();
 //BA.debugLineNum = 26;BA.debugLine="Private lblTotal As Label";
mostCurrent._lbltotal = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 27;BA.debugLine="Private lblTVQ As Label";
mostCurrent._lbltvq = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 28;BA.debugLine="Private lblTPS As Label";
mostCurrent._lbltps = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 29;BA.debugLine="Public Montant = 0, MontantAp = 0, gtotal = 0 As";
_montant = (float) (0);
_montantap = (float) (0);
_gtotal = (float) (0);
 //BA.debugLineNum = 31;BA.debugLine="End Sub";
return "";
}
public static float  _pourcent(float _taux,float _total) throws Exception{
float _taxes = 0f;
 //BA.debugLineNum = 74;BA.debugLine="Sub pourcent(Taux As Float, Total As Float) As Flo";
 //BA.debugLineNum = 75;BA.debugLine="Private Taxes As Float";
_taxes = 0f;
 //BA.debugLineNum = 76;BA.debugLine="Log(\"test\")";
anywheresoftware.b4a.keywords.Common.Log("test");
 //BA.debugLineNum = 77;BA.debugLine="Taxes = Total * (Taux / 100)";
_taxes = (float) (_total*(_taux/(double)100));
 //BA.debugLineNum = 78;BA.debugLine="Return Taxes";
if (true) return _taxes;
 //BA.debugLineNum = 80;BA.debugLine="End Sub";
return 0f;
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
