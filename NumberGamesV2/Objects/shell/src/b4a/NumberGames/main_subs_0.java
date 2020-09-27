package b4a.NumberGames;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.pc.*;

public class main_subs_0 {


public static RemoteObject  _activity_create(RemoteObject _firsttime) throws Exception{
try {
		Debug.PushSubsStack("Activity_Create (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,37);
if (RapidSub.canDelegate("activity_create")) { return b4a.NumberGames.main.remoteMe.runUserSub(false, "main","activity_create", _firsttime);}
Debug.locals.put("FirstTime", _firsttime);
 BA.debugLineNum = 37;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
Debug.ShouldStop(16);
 BA.debugLineNum = 39;BA.debugLine="Activity.LoadLayout(\"Layout1\")";
Debug.ShouldStop(64);
main.mostCurrent._activity.runMethodAndSync(false,"LoadLayout",(Object)(RemoteObject.createImmutable("Layout1")),main.mostCurrent.activityBA);
 BA.debugLineNum = 41;BA.debugLine="End Sub";
Debug.ShouldStop(256);
return RemoteObject.createImmutable("");
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
public static RemoteObject  _activity_pause(RemoteObject _userclosed) throws Exception{
try {
		Debug.PushSubsStack("Activity_Pause (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,47);
if (RapidSub.canDelegate("activity_pause")) { return b4a.NumberGames.main.remoteMe.runUserSub(false, "main","activity_pause", _userclosed);}
Debug.locals.put("UserClosed", _userclosed);
 BA.debugLineNum = 47;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
Debug.ShouldStop(16384);
 BA.debugLineNum = 49;BA.debugLine="End Sub";
Debug.ShouldStop(65536);
return RemoteObject.createImmutable("");
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
public static RemoteObject  _activity_resume() throws Exception{
try {
		Debug.PushSubsStack("Activity_Resume (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,43);
if (RapidSub.canDelegate("activity_resume")) { return b4a.NumberGames.main.remoteMe.runUserSub(false, "main","activity_resume");}
 BA.debugLineNum = 43;BA.debugLine="Sub Activity_Resume";
Debug.ShouldStop(1024);
 BA.debugLineNum = 45;BA.debugLine="End Sub";
Debug.ShouldStop(4096);
return RemoteObject.createImmutable("");
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
public static RemoteObject  _btnbegin_click() throws Exception{
try {
		Debug.PushSubsStack("btnBegin_Click (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,90);
if (RapidSub.canDelegate("btnbegin_click")) { return b4a.NumberGames.main.remoteMe.runUserSub(false, "main","btnbegin_click");}
 BA.debugLineNum = 90;BA.debugLine="Sub btnBegin_Click";
Debug.ShouldStop(33554432);
 BA.debugLineNum = 91;BA.debugLine="NoTry = 0";
Debug.ShouldStop(67108864);
main._notry = BA.numberCast(byte.class, 0);
 BA.debugLineNum = 92;BA.debugLine="lblTry.Text = NoTry";
Debug.ShouldStop(134217728);
main.mostCurrent._lbltry.runMethod(true,"setText",BA.ObjectToCharSequence(main._notry));
 BA.debugLineNum = 93;BA.debugLine="NbRandom = Rnd(0,100)";
Debug.ShouldStop(268435456);
main._nbrandom = BA.numberCast(byte.class, main.mostCurrent.__c.runMethod(true,"Rnd",(Object)(BA.numberCast(int.class, 0)),(Object)(BA.numberCast(int.class, 100))));
 BA.debugLineNum = 94;BA.debugLine="btnBegin.RequestFocus";
Debug.ShouldStop(536870912);
main.mostCurrent._btnbegin.runVoidMethod ("RequestFocus");
 BA.debugLineNum = 95;BA.debugLine="End Sub";
Debug.ShouldStop(1073741824);
return RemoteObject.createImmutable("");
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
public static RemoteObject  _btnclose_click() throws Exception{
try {
		Debug.PushSubsStack("btnClose_Click (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,86);
if (RapidSub.canDelegate("btnclose_click")) { return b4a.NumberGames.main.remoteMe.runUserSub(false, "main","btnclose_click");}
 BA.debugLineNum = 86;BA.debugLine="Sub btnClose_Click";
Debug.ShouldStop(2097152);
 BA.debugLineNum = 87;BA.debugLine="Activity.Finish";
Debug.ShouldStop(4194304);
main.mostCurrent._activity.runVoidMethod ("Finish");
 BA.debugLineNum = 88;BA.debugLine="End Sub";
Debug.ShouldStop(8388608);
return RemoteObject.createImmutable("");
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
public static void  _btntest_click() throws Exception{
try {
		Debug.PushSubsStack("btnTest_Click (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,68);
if (RapidSub.canDelegate("btntest_click")) { b4a.NumberGames.main.remoteMe.runUserSub(false, "main","btntest_click"); return;}
ResumableSub_btnTest_Click rsub = new ResumableSub_btnTest_Click(null);
rsub.resume(null, null);
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
public static class ResumableSub_btnTest_Click extends BA.ResumableSub {
public ResumableSub_btnTest_Click(b4a.NumberGames.main parent) {
this.parent = parent;
}
java.util.LinkedHashMap<String, Object> rsLocals = new java.util.LinkedHashMap<String, Object>();
b4a.NumberGames.main parent;

@Override
public void resume(BA ba, RemoteObject result) throws Exception{
try {
		Debug.PushSubsStack("btnTest_Click (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,68);
Debug.locals = rsLocals;Debug.currentSubFrame.locals = rsLocals;

    while (true) {
        switch (state) {
            case -1:
return;

case 0:
//C
this.state = 1;
 BA.debugLineNum = 69;BA.debugLine="NoTry = NoTry + 1";
Debug.ShouldStop(16);
parent._notry = BA.numberCast(byte.class, RemoteObject.solve(new RemoteObject[] {parent._notry,RemoteObject.createImmutable(1)}, "+",1, 1));
 BA.debugLineNum = 70;BA.debugLine="lblTry.Text = NoTry";
Debug.ShouldStop(32);
parent.mostCurrent._lbltry.runMethod(true,"setText",BA.ObjectToCharSequence(parent._notry));
 BA.debugLineNum = 72;BA.debugLine="If edtNumber.Text > NbRandom Then";
Debug.ShouldStop(128);
if (true) break;

case 1:
//if
this.state = 8;
if (RemoteObject.solveBoolean(">",BA.numberCast(double.class, parent.mostCurrent._edtnumber.runMethod(true,"getText")),BA.numberCast(double.class, parent._nbrandom))) { 
this.state = 3;
}else 
{ BA.debugLineNum = 75;BA.debugLine="Else if edtNumber.Text < NbRandom Then";
Debug.ShouldStop(1024);
if (RemoteObject.solveBoolean("<",BA.numberCast(double.class, parent.mostCurrent._edtnumber.runMethod(true,"getText")),BA.numberCast(double.class, parent._nbrandom))) { 
this.state = 5;
}else {
this.state = 7;
}}
if (true) break;

case 3:
//C
this.state = 8;
 BA.debugLineNum = 73;BA.debugLine="lblHint.Text = (\"Le nombre hazard est plus petit";
Debug.ShouldStop(256);
parent.mostCurrent._lblhint.runMethod(true,"setText",BA.ObjectToCharSequence((RemoteObject.createImmutable("Le nombre hazard est plus petit"))));
 BA.debugLineNum = 74;BA.debugLine="edtNumber.Text = (\"\")";
Debug.ShouldStop(512);
parent.mostCurrent._edtnumber.runMethodAndSync(true,"setText",BA.ObjectToCharSequence((RemoteObject.createImmutable(""))));
 if (true) break;

case 5:
//C
this.state = 8;
 BA.debugLineNum = 76;BA.debugLine="lblHint.Text = (\"Le nombre hazard est plus grand";
Debug.ShouldStop(2048);
parent.mostCurrent._lblhint.runMethod(true,"setText",BA.ObjectToCharSequence((RemoteObject.createImmutable("Le nombre hazard est plus grand"))));
 BA.debugLineNum = 77;BA.debugLine="edtNumber.Text = (\"\")";
Debug.ShouldStop(4096);
parent.mostCurrent._edtnumber.runMethodAndSync(true,"setText",BA.ObjectToCharSequence((RemoteObject.createImmutable(""))));
 if (true) break;

case 7:
//C
this.state = 8;
 BA.debugLineNum = 79;BA.debugLine="lblHint.Text = (\"Bravo! Vous avez trouvé le bon";
Debug.ShouldStop(16384);
parent.mostCurrent._lblhint.runMethod(true,"setText",BA.ObjectToCharSequence((RemoteObject.concat(RemoteObject.createImmutable("Bravo! Vous avez trouvé le bon nombre en "),parent._notry,RemoteObject.createImmutable(" coups!")))));
 BA.debugLineNum = 80;BA.debugLine="Sleep (2000)";
Debug.ShouldStop(32768);
parent.mostCurrent.__c.runVoidMethod ("Sleep",main.mostCurrent.activityBA,anywheresoftware.b4a.pc.PCResumableSub.createDebugResumeSub(this),BA.numberCast(int.class, 2000));
this.state = 9;
return;
case 9:
//C
this.state = 8;
;
 BA.debugLineNum = 81;BA.debugLine="edtNumber.Text = (\"\")";
Debug.ShouldStop(65536);
parent.mostCurrent._edtnumber.runMethodAndSync(true,"setText",BA.ObjectToCharSequence((RemoteObject.createImmutable(""))));
 BA.debugLineNum = 82;BA.debugLine="btnBegin.Enabled = True";
Debug.ShouldStop(131072);
parent.mostCurrent._btnbegin.runMethod(true,"setEnabled",parent.mostCurrent.__c.getField(true,"True"));
 if (true) break;

case 8:
//C
this.state = -1;
;
 BA.debugLineNum = 84;BA.debugLine="End Sub";
Debug.ShouldStop(524288);
if (true) break;

            }
        }
    }
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
}
public static RemoteObject  _edtnumber_enterpressed() throws Exception{
try {
		Debug.PushSubsStack("edtNumber_EnterPressed (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,64);
if (RapidSub.canDelegate("edtnumber_enterpressed")) { return b4a.NumberGames.main.remoteMe.runUserSub(false, "main","edtnumber_enterpressed");}
 BA.debugLineNum = 64;BA.debugLine="Sub edtNumber_EnterPressed";
Debug.ShouldStop(-2147483648);
 BA.debugLineNum = 66;BA.debugLine="End Sub";
Debug.ShouldStop(2);
return RemoteObject.createImmutable("");
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
public static RemoteObject  _edtnumber_textchanged(RemoteObject _old,RemoteObject _new) throws Exception{
try {
		Debug.PushSubsStack("edtNumber_TextChanged (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,60);
if (RapidSub.canDelegate("edtnumber_textchanged")) { return b4a.NumberGames.main.remoteMe.runUserSub(false, "main","edtnumber_textchanged", _old, _new);}
Debug.locals.put("Old", _old);
Debug.locals.put("New", _new);
 BA.debugLineNum = 60;BA.debugLine="Sub edtNumber_TextChanged (Old As String, New As S";
Debug.ShouldStop(134217728);
 BA.debugLineNum = 62;BA.debugLine="End Sub";
Debug.ShouldStop(536870912);
return RemoteObject.createImmutable("");
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
public static RemoteObject  _globals() throws Exception{
 //BA.debugLineNum = 21;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 25;BA.debugLine="Private btnBegin As Button";
main.mostCurrent._btnbegin = RemoteObject.createNew ("anywheresoftware.b4a.objects.ButtonWrapper");
 //BA.debugLineNum = 26;BA.debugLine="Private btnClose As Button";
main.mostCurrent._btnclose = RemoteObject.createNew ("anywheresoftware.b4a.objects.ButtonWrapper");
 //BA.debugLineNum = 27;BA.debugLine="Private btnTest As Button";
main.mostCurrent._btntest = RemoteObject.createNew ("anywheresoftware.b4a.objects.ButtonWrapper");
 //BA.debugLineNum = 28;BA.debugLine="Private edtNumber As EditText";
main.mostCurrent._edtnumber = RemoteObject.createNew ("anywheresoftware.b4a.objects.EditTextWrapper");
 //BA.debugLineNum = 29;BA.debugLine="Private lblHint As Label";
main.mostCurrent._lblhint = RemoteObject.createNew ("anywheresoftware.b4a.objects.LabelWrapper");
 //BA.debugLineNum = 30;BA.debugLine="Private lblTry As Label";
main.mostCurrent._lbltry = RemoteObject.createNew ("anywheresoftware.b4a.objects.LabelWrapper");
 //BA.debugLineNum = 31;BA.debugLine="Private lblDemand As Label";
main.mostCurrent._lbldemand = RemoteObject.createNew ("anywheresoftware.b4a.objects.LabelWrapper");
 //BA.debugLineNum = 32;BA.debugLine="Private lblTxtTry As Label";
main.mostCurrent._lbltxttry = RemoteObject.createNew ("anywheresoftware.b4a.objects.LabelWrapper");
 //BA.debugLineNum = 33;BA.debugLine="Private NoTry As Byte";
main._notry = RemoteObject.createImmutable((byte)0);
 //BA.debugLineNum = 34;BA.debugLine="Private NbRandom As Byte";
main._nbrandom = RemoteObject.createImmutable((byte)0);
 //BA.debugLineNum = 35;BA.debugLine="End Sub";
return RemoteObject.createImmutable("");
}
public static RemoteObject  _lblhint_click() throws Exception{
try {
		Debug.PushSubsStack("lblHint_Click (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,56);
if (RapidSub.canDelegate("lblhint_click")) { return b4a.NumberGames.main.remoteMe.runUserSub(false, "main","lblhint_click");}
 BA.debugLineNum = 56;BA.debugLine="Sub lblHint_Click";
Debug.ShouldStop(8388608);
 BA.debugLineNum = 58;BA.debugLine="End Sub";
Debug.ShouldStop(33554432);
return RemoteObject.createImmutable("");
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
public static RemoteObject  _lbltry_click() throws Exception{
try {
		Debug.PushSubsStack("lblTry_Click (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,52);
if (RapidSub.canDelegate("lbltry_click")) { return b4a.NumberGames.main.remoteMe.runUserSub(false, "main","lbltry_click");}
 BA.debugLineNum = 52;BA.debugLine="Sub lblTry_Click";
Debug.ShouldStop(524288);
 BA.debugLineNum = 54;BA.debugLine="End Sub";
Debug.ShouldStop(2097152);
return RemoteObject.createImmutable("");
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}

public static void initializeProcessGlobals() {
    
    if (main.processGlobalsRun == false) {
	    main.processGlobalsRun = true;
		try {
		        main_subs_0._process_globals();
main.myClass = BA.getDeviceClass ("b4a.NumberGames.main");
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static RemoteObject  _process_globals() throws Exception{
 //BA.debugLineNum = 15;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 19;BA.debugLine="End Sub";
return RemoteObject.createImmutable("");
}
}