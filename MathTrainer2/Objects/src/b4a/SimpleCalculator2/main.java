package b4a.SimpleCalculator2;


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
			processBA = new anywheresoftware.b4a.ShellBA(this.getApplicationContext(), null, null, "b4a.SimpleCalculator2", "b4a.SimpleCalculator2.main");
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
		activityBA = new BA(this, layout, processBA, "b4a.SimpleCalculator2", "b4a.SimpleCalculator2.main");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "b4a.SimpleCalculator2.main", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
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



public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}
public static boolean isAnyActivityVisible() {
    boolean vis = false;
vis = vis | (main.mostCurrent != null);
return vis;}

private static BA killProgramHelper(BA ba) {
    if (ba == null)
        return null;
    anywheresoftware.b4a.BA.SharedProcessBA sharedProcessBA = ba.sharedProcessBA;
    if (sharedProcessBA == null || sharedProcessBA.activityBA == null)
        return null;
    return sharedProcessBA.activityBA.get();
}
public static void killProgram() {
     {
            Activity __a = null;
            if (main.previousOne != null) {
				__a = main.previousOne.get();
			}
            else {
                BA ba = killProgramHelper(main.mostCurrent == null ? null : main.mostCurrent.processBA);
                if (ba != null) __a = ba.activity;
            }
            if (__a != null)
				__a.finish();}

}
public anywheresoftware.b4a.keywords.Common __c = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btnaction = null;
public anywheresoftware.b4a.objects.ButtonWrapper _btn0 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblresultat = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblcommentaire = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblnombre1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblnombre2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lblsignemath = null;
public static int _nombre1 = 0;
public static int _nombre2 = 0;
public static String  _activity_create(boolean _firsttime) throws Exception{
RDebugUtils.currentModule="main";
if (Debug.shouldDelegate(mostCurrent.activityBA, "activity_create"))
	 {return ((String) Debug.delegate(mostCurrent.activityBA, "activity_create", new Object[] {_firsttime}));}
RDebugUtils.currentLine=262144;
 //BA.debugLineNum = 262144;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
RDebugUtils.currentLine=262146;
 //BA.debugLineNum = 262146;BA.debugLine="Activity.LoadLayout(\"Main\")";
mostCurrent._activity.LoadLayout("Main",mostCurrent.activityBA);
RDebugUtils.currentLine=262147;
 //BA.debugLineNum = 262147;BA.debugLine="NouveauProbleme";
_nouveauprobleme();
RDebugUtils.currentLine=262148;
 //BA.debugLineNum = 262148;BA.debugLine="End Sub";
return "";
}
public static String  _nouveauprobleme() throws Exception{
RDebugUtils.currentModule="main";
if (Debug.shouldDelegate(mostCurrent.activityBA, "nouveauprobleme"))
	 {return ((String) Debug.delegate(mostCurrent.activityBA, "nouveauprobleme", null));}
RDebugUtils.currentLine=131072;
 //BA.debugLineNum = 131072;BA.debugLine="Private Sub NouveauProbleme";
RDebugUtils.currentLine=131073;
 //BA.debugLineNum = 131073;BA.debugLine="Nombre1 = Rnd(1, 10) 'Génère un nombre aléatoire";
_nombre1 = anywheresoftware.b4a.keywords.Common.Rnd((int) (1),(int) (10));
RDebugUtils.currentLine=131074;
 //BA.debugLineNum = 131074;BA.debugLine="Nombre2 = Rnd(1, 10) 'Génère un nombre aléatoire";
_nombre2 = anywheresoftware.b4a.keywords.Common.Rnd((int) (1),(int) (10));
RDebugUtils.currentLine=131075;
 //BA.debugLineNum = 131075;BA.debugLine="lblNombre1.Text = Nombre1 'Affiche Nombre1 dans l";
mostCurrent._lblnombre1.setText(BA.ObjectToCharSequence(_nombre1));
RDebugUtils.currentLine=131076;
 //BA.debugLineNum = 131076;BA.debugLine="lblNombre2.Text = Nombre2 'Affiche Nombre2 dans l";
mostCurrent._lblnombre2.setText(BA.ObjectToCharSequence(_nombre2));
RDebugUtils.currentLine=131077;
 //BA.debugLineNum = 131077;BA.debugLine="lblCommentaire.Text = \"Entrez le résultat\" & CRLF";
mostCurrent._lblcommentaire.setText(BA.ObjectToCharSequence("Entrez le résultat"+anywheresoftware.b4a.keywords.Common.CRLF+"et cliquez sur OK"));
RDebugUtils.currentLine=131078;
 //BA.debugLineNum = 131078;BA.debugLine="lblCommentaire.Color = Colors.RGB(255,235,128) 'c";
mostCurrent._lblcommentaire.setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (255),(int) (235),(int) (128)));
RDebugUtils.currentLine=131079;
 //BA.debugLineNum = 131079;BA.debugLine="lblResultat.Text = \"\" 'Vide edtResult.Text";
mostCurrent._lblresultat.setText(BA.ObjectToCharSequence(""));
RDebugUtils.currentLine=131080;
 //BA.debugLineNum = 131080;BA.debugLine="btn0.Visible = False";
mostCurrent._btn0.setVisible(anywheresoftware.b4a.keywords.Common.False);
RDebugUtils.currentLine=131081;
 //BA.debugLineNum = 131081;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
RDebugUtils.currentModule="main";
RDebugUtils.currentLine=393216;
 //BA.debugLineNum = 393216;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
RDebugUtils.currentLine=393218;
 //BA.debugLineNum = 393218;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
RDebugUtils.currentModule="main";
if (Debug.shouldDelegate(mostCurrent.activityBA, "activity_resume"))
	 {return ((String) Debug.delegate(mostCurrent.activityBA, "activity_resume", null));}
RDebugUtils.currentLine=327680;
 //BA.debugLineNum = 327680;BA.debugLine="Sub Activity_Resume";
RDebugUtils.currentLine=327682;
 //BA.debugLineNum = 327682;BA.debugLine="End Sub";
return "";
}
public static String  _btnaction_click() throws Exception{
RDebugUtils.currentModule="main";
if (Debug.shouldDelegate(mostCurrent.activityBA, "btnaction_click"))
	 {return ((String) Debug.delegate(mostCurrent.activityBA, "btnaction_click", null));}
RDebugUtils.currentLine=458752;
 //BA.debugLineNum = 458752;BA.debugLine="Sub btnAction_Click";
RDebugUtils.currentLine=458753;
 //BA.debugLineNum = 458753;BA.debugLine="If btnAction.Text = \"O K\" Then";
if ((mostCurrent._btnaction.getText()).equals("O K")) { 
RDebugUtils.currentLine=458754;
 //BA.debugLineNum = 458754;BA.debugLine="If lblResultat.Text = \"\" Then";
if ((mostCurrent._lblresultat.getText()).equals("")) { 
RDebugUtils.currentLine=458755;
 //BA.debugLineNum = 458755;BA.debugLine="Msgbox(\"Il n’y a pas de résultat\",\"E R R E U R\"";
anywheresoftware.b4a.keywords.Common.Msgbox(BA.ObjectToCharSequence("Il n’y a pas de résultat"),BA.ObjectToCharSequence("E R R E U R"),mostCurrent.activityBA);
 }else {
RDebugUtils.currentLine=458757;
 //BA.debugLineNum = 458757;BA.debugLine="TestResultat";
_testresultat();
 };
 }else {
RDebugUtils.currentLine=458760;
 //BA.debugLineNum = 458760;BA.debugLine="NouveauProbleme";
_nouveauprobleme();
RDebugUtils.currentLine=458761;
 //BA.debugLineNum = 458761;BA.debugLine="btnAction.Text = \"O K\"";
mostCurrent._btnaction.setText(BA.ObjectToCharSequence("O K"));
 };
RDebugUtils.currentLine=458763;
 //BA.debugLineNum = 458763;BA.debugLine="End Sub";
return "";
}
public static String  _testresultat() throws Exception{
RDebugUtils.currentModule="main";
if (Debug.shouldDelegate(mostCurrent.activityBA, "testresultat"))
	 {return ((String) Debug.delegate(mostCurrent.activityBA, "testresultat", null));}
RDebugUtils.currentLine=196608;
 //BA.debugLineNum = 196608;BA.debugLine="Sub TestResultat";
RDebugUtils.currentLine=196609;
 //BA.debugLineNum = 196609;BA.debugLine="If lblResultat.Text = Nombre1 + Nombre2 Then";
if ((mostCurrent._lblresultat.getText()).equals(BA.NumberToString(_nombre1+_nombre2))) { 
RDebugUtils.currentLine=196610;
 //BA.debugLineNum = 196610;BA.debugLine="lblCommentaire.Text = \"B O N résultat\" & CRLF &";
mostCurrent._lblcommentaire.setText(BA.ObjectToCharSequence("B O N résultat"+anywheresoftware.b4a.keywords.Common.CRLF+"Cliquez sur Nouveau"));
RDebugUtils.currentLine=196611;
 //BA.debugLineNum = 196611;BA.debugLine="btnAction.Text = \"Nouveau\"";
mostCurrent._btnaction.setText(BA.ObjectToCharSequence("Nouveau"));
RDebugUtils.currentLine=196612;
 //BA.debugLineNum = 196612;BA.debugLine="lblCommentaire.Color = Colors.RGB(128,255,128) '";
mostCurrent._lblcommentaire.setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (128),(int) (255),(int) (128)));
 }else {
RDebugUtils.currentLine=196614;
 //BA.debugLineNum = 196614;BA.debugLine="lblCommentaire.Text = \"M A U V A I S  résultat\"";
mostCurrent._lblcommentaire.setText(BA.ObjectToCharSequence("M A U V A I S  résultat"+anywheresoftware.b4a.keywords.Common.CRLF+"Entrez un nouveau résultat"+anywheresoftware.b4a.keywords.Common.CRLF+"et cliquez sur O K"));
RDebugUtils.currentLine=196615;
 //BA.debugLineNum = 196615;BA.debugLine="lblCommentaire.Color = Colors.RGB(255,128,128)";
mostCurrent._lblcommentaire.setColor(anywheresoftware.b4a.keywords.Common.Colors.RGB((int) (255),(int) (128),(int) (128)));
 };
RDebugUtils.currentLine=196617;
 //BA.debugLineNum = 196617;BA.debugLine="End Sub";
return "";
}
public static String  _btnevent_click() throws Exception{
RDebugUtils.currentModule="main";
if (Debug.shouldDelegate(mostCurrent.activityBA, "btnevent_click"))
	 {return ((String) Debug.delegate(mostCurrent.activityBA, "btnevent_click", null));}
anywheresoftware.b4a.objects.ButtonWrapper _btnsender = null;
RDebugUtils.currentLine=589824;
 //BA.debugLineNum = 589824;BA.debugLine="Private Sub btnEvent_Click";
RDebugUtils.currentLine=589825;
 //BA.debugLineNum = 589825;BA.debugLine="Private btnSender As Button";
_btnsender = new anywheresoftware.b4a.objects.ButtonWrapper();
RDebugUtils.currentLine=589827;
 //BA.debugLineNum = 589827;BA.debugLine="btnSender = Sender";
_btnsender.setObject((android.widget.Button)(anywheresoftware.b4a.keywords.Common.Sender(mostCurrent.activityBA)));
RDebugUtils.currentLine=589829;
 //BA.debugLineNum = 589829;BA.debugLine="Select btnSender.Tag";
switch (BA.switchObjectToInt(_btnsender.getTag(),(Object)("BS"))) {
case 0: {
RDebugUtils.currentLine=589831;
 //BA.debugLineNum = 589831;BA.debugLine="If lblResultat.Text.Length > 0 Then";
if (mostCurrent._lblresultat.getText().length()>0) { 
RDebugUtils.currentLine=589832;
 //BA.debugLineNum = 589832;BA.debugLine="lblResultat.Text = lblResultat.Text.SubString2(";
mostCurrent._lblresultat.setText(BA.ObjectToCharSequence(mostCurrent._lblresultat.getText().substring((int) (0),(int) (mostCurrent._lblresultat.getText().length()-1))));
 };
 break; }
default: {
RDebugUtils.currentLine=589835;
 //BA.debugLineNum = 589835;BA.debugLine="lblResultat.Text = lblResultat.Text & btnSender";
mostCurrent._lblresultat.setText(BA.ObjectToCharSequence(mostCurrent._lblresultat.getText()+_btnsender.getText()));
 break; }
}
;
RDebugUtils.currentLine=589837;
 //BA.debugLineNum = 589837;BA.debugLine="If lblResultat.Text.Length = 0 Then";
if (mostCurrent._lblresultat.getText().length()==0) { 
RDebugUtils.currentLine=589838;
 //BA.debugLineNum = 589838;BA.debugLine="btn0.Visible = False";
mostCurrent._btn0.setVisible(anywheresoftware.b4a.keywords.Common.False);
 }else {
RDebugUtils.currentLine=589840;
 //BA.debugLineNum = 589840;BA.debugLine="btn0.Visible = True";
mostCurrent._btn0.setVisible(anywheresoftware.b4a.keywords.Common.True);
 };
RDebugUtils.currentLine=589842;
 //BA.debugLineNum = 589842;BA.debugLine="End Sub";
return "";
}
}