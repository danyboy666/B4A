package b4a.SimpleCalculator2;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.pc.*;

public class main_subs_0 {


public static RemoteObject  _activity_create(RemoteObject _firsttime) throws Exception{
try {
		Debug.PushSubsStack("Activity_Create (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,57);
if (RapidSub.canDelegate("activity_create")) { return b4a.SimpleCalculator2.main.remoteMe.runUserSub(false, "main","activity_create", _firsttime);}
Debug.locals.put("FirstTime", _firsttime);
 BA.debugLineNum = 57;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
Debug.ShouldStop(16777216);
 BA.debugLineNum = 59;BA.debugLine="Activity.LoadLayout(\"Main\")";
Debug.ShouldStop(67108864);
main.mostCurrent._activity.runMethodAndSync(false,"LoadLayout",(Object)(RemoteObject.createImmutable("Main")),main.mostCurrent.activityBA);
 BA.debugLineNum = 60;BA.debugLine="NouveauProbleme";
Debug.ShouldStop(134217728);
_nouveauprobleme();
 BA.debugLineNum = 61;BA.debugLine="End Sub";
Debug.ShouldStop(268435456);
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
		Debug.PushSubsStack("Activity_Pause (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,67);
if (RapidSub.canDelegate("activity_pause")) { return b4a.SimpleCalculator2.main.remoteMe.runUserSub(false, "main","activity_pause", _userclosed);}
Debug.locals.put("UserClosed", _userclosed);
 BA.debugLineNum = 67;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
Debug.ShouldStop(4);
 BA.debugLineNum = 69;BA.debugLine="End Sub";
Debug.ShouldStop(16);
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
		Debug.PushSubsStack("Activity_Resume (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,63);
if (RapidSub.canDelegate("activity_resume")) { return b4a.SimpleCalculator2.main.remoteMe.runUserSub(false, "main","activity_resume");}
 BA.debugLineNum = 63;BA.debugLine="Sub Activity_Resume";
Debug.ShouldStop(1073741824);
 BA.debugLineNum = 65;BA.debugLine="End Sub";
Debug.ShouldStop(1);
return RemoteObject.createImmutable("");
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
public static RemoteObject  _btnaction_click() throws Exception{
try {
		Debug.PushSubsStack("btnAction_Click (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,72);
if (RapidSub.canDelegate("btnaction_click")) { return b4a.SimpleCalculator2.main.remoteMe.runUserSub(false, "main","btnaction_click");}
 BA.debugLineNum = 72;BA.debugLine="Sub btnAction_Click";
Debug.ShouldStop(128);
 BA.debugLineNum = 73;BA.debugLine="If btnAction.Text = \"O K\" Then";
Debug.ShouldStop(256);
if (RemoteObject.solveBoolean("=",main.mostCurrent._btnaction.runMethod(true,"getText"),BA.ObjectToString("O K"))) { 
 BA.debugLineNum = 74;BA.debugLine="If lblResultat.Text = \"\" Then";
Debug.ShouldStop(512);
if (RemoteObject.solveBoolean("=",main.mostCurrent._lblresultat.runMethod(true,"getText"),BA.ObjectToString(""))) { 
 BA.debugLineNum = 75;BA.debugLine="Msgbox(\"Il n’y a pas de résultat\",\"E R R E U R\"";
Debug.ShouldStop(1024);
main.mostCurrent.__c.runVoidMethodAndSync ("Msgbox",(Object)(BA.ObjectToCharSequence("Il n’y a pas de résultat")),(Object)(BA.ObjectToCharSequence(RemoteObject.createImmutable("E R R E U R"))),main.mostCurrent.activityBA);
 }else {
 BA.debugLineNum = 77;BA.debugLine="TestResultat";
Debug.ShouldStop(4096);
_testresultat();
 };
 }else {
 BA.debugLineNum = 80;BA.debugLine="NouveauProbleme";
Debug.ShouldStop(32768);
_nouveauprobleme();
 BA.debugLineNum = 81;BA.debugLine="btnAction.Text = \"O K\"";
Debug.ShouldStop(65536);
main.mostCurrent._btnaction.runMethod(true,"setText",BA.ObjectToCharSequence("O K"));
 };
 BA.debugLineNum = 83;BA.debugLine="End Sub";
Debug.ShouldStop(262144);
return RemoteObject.createImmutable("");
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
public static RemoteObject  _btnevent_click() throws Exception{
try {
		Debug.PushSubsStack("btnEvent_Click (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,85);
if (RapidSub.canDelegate("btnevent_click")) { return b4a.SimpleCalculator2.main.remoteMe.runUserSub(false, "main","btnevent_click");}
RemoteObject _btnsender = RemoteObject.declareNull("anywheresoftware.b4a.objects.ButtonWrapper");
 BA.debugLineNum = 85;BA.debugLine="Private Sub btnEvent_Click";
Debug.ShouldStop(1048576);
 BA.debugLineNum = 86;BA.debugLine="Private btnSender As Button";
Debug.ShouldStop(2097152);
_btnsender = RemoteObject.createNew ("anywheresoftware.b4a.objects.ButtonWrapper");Debug.locals.put("btnSender", _btnsender);
 BA.debugLineNum = 88;BA.debugLine="btnSender = Sender";
Debug.ShouldStop(8388608);
_btnsender.setObject(main.mostCurrent.__c.runMethod(false,"Sender",main.mostCurrent.activityBA));
 BA.debugLineNum = 90;BA.debugLine="Select btnSender.Tag";
Debug.ShouldStop(33554432);
switch (BA.switchObjectToInt(_btnsender.runMethod(false,"getTag"),RemoteObject.createImmutable(("BS")))) {
case 0: {
 BA.debugLineNum = 92;BA.debugLine="If lblResultat.Text.Length > 0 Then";
Debug.ShouldStop(134217728);
if (RemoteObject.solveBoolean(">",main.mostCurrent._lblresultat.runMethod(true,"getText").runMethod(true,"length"),BA.numberCast(double.class, 0))) { 
 BA.debugLineNum = 93;BA.debugLine="lblResultat.Text = lblResultat.Text.SubString2(";
Debug.ShouldStop(268435456);
main.mostCurrent._lblresultat.runMethod(true,"setText",BA.ObjectToCharSequence(main.mostCurrent._lblresultat.runMethod(true,"getText").runMethod(true,"substring",(Object)(BA.numberCast(int.class, 0)),(Object)(RemoteObject.solve(new RemoteObject[] {main.mostCurrent._lblresultat.runMethod(true,"getText").runMethod(true,"length"),RemoteObject.createImmutable(1)}, "-",1, 1)))));
 };
 break; }
default: {
 BA.debugLineNum = 96;BA.debugLine="lblResultat.Text = lblResultat.Text & btnSender";
Debug.ShouldStop(-2147483648);
main.mostCurrent._lblresultat.runMethod(true,"setText",BA.ObjectToCharSequence(RemoteObject.concat(main.mostCurrent._lblresultat.runMethod(true,"getText"),_btnsender.runMethod(true,"getText"))));
 break; }
}
;
 BA.debugLineNum = 98;BA.debugLine="If lblResultat.Text.Length = 0 Then";
Debug.ShouldStop(2);
if (RemoteObject.solveBoolean("=",main.mostCurrent._lblresultat.runMethod(true,"getText").runMethod(true,"length"),BA.numberCast(double.class, 0))) { 
 BA.debugLineNum = 99;BA.debugLine="btn0.Visible = False";
Debug.ShouldStop(4);
main.mostCurrent._btn0.runMethod(true,"setVisible",main.mostCurrent.__c.getField(true,"False"));
 }else {
 BA.debugLineNum = 101;BA.debugLine="btn0.Visible = True";
Debug.ShouldStop(16);
main.mostCurrent._btn0.runMethod(true,"setVisible",main.mostCurrent.__c.getField(true,"True"));
 };
 BA.debugLineNum = 103;BA.debugLine="End Sub";
Debug.ShouldStop(64);
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
 //BA.debugLineNum = 25;BA.debugLine="Private btnAction, btn0 As Button";
main.mostCurrent._btnaction = RemoteObject.createNew ("anywheresoftware.b4a.objects.ButtonWrapper");
main.mostCurrent._btn0 = RemoteObject.createNew ("anywheresoftware.b4a.objects.ButtonWrapper");
 //BA.debugLineNum = 26;BA.debugLine="Private lblResultat As Label";
main.mostCurrent._lblresultat = RemoteObject.createNew ("anywheresoftware.b4a.objects.LabelWrapper");
 //BA.debugLineNum = 27;BA.debugLine="Private lblCommentaire As Label";
main.mostCurrent._lblcommentaire = RemoteObject.createNew ("anywheresoftware.b4a.objects.LabelWrapper");
 //BA.debugLineNum = 28;BA.debugLine="Private lblNombre1 As Label";
main.mostCurrent._lblnombre1 = RemoteObject.createNew ("anywheresoftware.b4a.objects.LabelWrapper");
 //BA.debugLineNum = 29;BA.debugLine="Private lblNombre2 As Label";
main.mostCurrent._lblnombre2 = RemoteObject.createNew ("anywheresoftware.b4a.objects.LabelWrapper");
 //BA.debugLineNum = 30;BA.debugLine="Private lblSigneMath As Label";
main.mostCurrent._lblsignemath = RemoteObject.createNew ("anywheresoftware.b4a.objects.LabelWrapper");
 //BA.debugLineNum = 31;BA.debugLine="Public Nombre1, Nombre2 As Int";
main._nombre1 = RemoteObject.createImmutable(0);
main._nombre2 = RemoteObject.createImmutable(0);
 //BA.debugLineNum = 33;BA.debugLine="End Sub";
return RemoteObject.createImmutable("");
}
public static RemoteObject  _nouveauprobleme() throws Exception{
try {
		Debug.PushSubsStack("NouveauProbleme (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,35);
if (RapidSub.canDelegate("nouveauprobleme")) { return b4a.SimpleCalculator2.main.remoteMe.runUserSub(false, "main","nouveauprobleme");}
 BA.debugLineNum = 35;BA.debugLine="Private Sub NouveauProbleme";
Debug.ShouldStop(4);
 BA.debugLineNum = 36;BA.debugLine="Nombre1 = Rnd(1, 10) 'Génère un nombre aléatoire";
Debug.ShouldStop(8);
main._nombre1 = main.mostCurrent.__c.runMethod(true,"Rnd",(Object)(BA.numberCast(int.class, 1)),(Object)(BA.numberCast(int.class, 10)));
 BA.debugLineNum = 37;BA.debugLine="Nombre2 = Rnd(1, 10) 'Génère un nombre aléatoire";
Debug.ShouldStop(16);
main._nombre2 = main.mostCurrent.__c.runMethod(true,"Rnd",(Object)(BA.numberCast(int.class, 1)),(Object)(BA.numberCast(int.class, 10)));
 BA.debugLineNum = 38;BA.debugLine="lblNombre1.Text = Nombre1 'Affiche Nombre1 dans l";
Debug.ShouldStop(32);
main.mostCurrent._lblnombre1.runMethod(true,"setText",BA.ObjectToCharSequence(main._nombre1));
 BA.debugLineNum = 39;BA.debugLine="lblNombre2.Text = Nombre2 'Affiche Nombre2 dans l";
Debug.ShouldStop(64);
main.mostCurrent._lblnombre2.runMethod(true,"setText",BA.ObjectToCharSequence(main._nombre2));
 BA.debugLineNum = 40;BA.debugLine="lblCommentaire.Text = \"Entrez le résultat\" & CRLF";
Debug.ShouldStop(128);
main.mostCurrent._lblcommentaire.runMethod(true,"setText",BA.ObjectToCharSequence(RemoteObject.concat(RemoteObject.createImmutable("Entrez le résultat"),main.mostCurrent.__c.getField(true,"CRLF"),RemoteObject.createImmutable("et cliquez sur OK"))));
 BA.debugLineNum = 41;BA.debugLine="lblCommentaire.Color = Colors.RGB(255,235,128) 'c";
Debug.ShouldStop(256);
main.mostCurrent._lblcommentaire.runVoidMethod ("setColor",main.mostCurrent.__c.getField(false,"Colors").runMethod(true,"RGB",(Object)(BA.numberCast(int.class, 255)),(Object)(BA.numberCast(int.class, 235)),(Object)(BA.numberCast(int.class, 128))));
 BA.debugLineNum = 42;BA.debugLine="lblResultat.Text = \"\" 'Vide edtResult.Text";
Debug.ShouldStop(512);
main.mostCurrent._lblresultat.runMethod(true,"setText",BA.ObjectToCharSequence(""));
 BA.debugLineNum = 43;BA.debugLine="btn0.Visible = False";
Debug.ShouldStop(1024);
main.mostCurrent._btn0.runMethod(true,"setVisible",main.mostCurrent.__c.getField(true,"False"));
 BA.debugLineNum = 44;BA.debugLine="End Sub";
Debug.ShouldStop(2048);
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
main.myClass = BA.getDeviceClass ("b4a.SimpleCalculator2.main");
		
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
}public static RemoteObject  _process_globals() throws Exception{
 //BA.debugLineNum = 15;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 19;BA.debugLine="End Sub";
return RemoteObject.createImmutable("");
}
public static RemoteObject  _testresultat() throws Exception{
try {
		Debug.PushSubsStack("TestResultat (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,46);
if (RapidSub.canDelegate("testresultat")) { return b4a.SimpleCalculator2.main.remoteMe.runUserSub(false, "main","testresultat");}
 BA.debugLineNum = 46;BA.debugLine="Sub TestResultat";
Debug.ShouldStop(8192);
 BA.debugLineNum = 47;BA.debugLine="If lblResultat.Text = Nombre1 + Nombre2 Then";
Debug.ShouldStop(16384);
if (RemoteObject.solveBoolean("=",main.mostCurrent._lblresultat.runMethod(true,"getText"),BA.NumberToString(RemoteObject.solve(new RemoteObject[] {main._nombre1,main._nombre2}, "+",1, 1)))) { 
 BA.debugLineNum = 48;BA.debugLine="lblCommentaire.Text = \"B O N résultat\" & CRLF &";
Debug.ShouldStop(32768);
main.mostCurrent._lblcommentaire.runMethod(true,"setText",BA.ObjectToCharSequence(RemoteObject.concat(RemoteObject.createImmutable("B O N résultat"),main.mostCurrent.__c.getField(true,"CRLF"),RemoteObject.createImmutable("Cliquez sur Nouveau"))));
 BA.debugLineNum = 49;BA.debugLine="btnAction.Text = \"Nouveau\"";
Debug.ShouldStop(65536);
main.mostCurrent._btnaction.runMethod(true,"setText",BA.ObjectToCharSequence("Nouveau"));
 BA.debugLineNum = 50;BA.debugLine="lblCommentaire.Color = Colors.RGB(128,255,128) '";
Debug.ShouldStop(131072);
main.mostCurrent._lblcommentaire.runVoidMethod ("setColor",main.mostCurrent.__c.getField(false,"Colors").runMethod(true,"RGB",(Object)(BA.numberCast(int.class, 128)),(Object)(BA.numberCast(int.class, 255)),(Object)(BA.numberCast(int.class, 128))));
 }else {
 BA.debugLineNum = 52;BA.debugLine="lblCommentaire.Text = \"M A U V A I S  résultat\"";
Debug.ShouldStop(524288);
main.mostCurrent._lblcommentaire.runMethod(true,"setText",BA.ObjectToCharSequence(RemoteObject.concat(RemoteObject.createImmutable("M A U V A I S  résultat"),main.mostCurrent.__c.getField(true,"CRLF"),RemoteObject.createImmutable("Entrez un nouveau résultat"),main.mostCurrent.__c.getField(true,"CRLF"),RemoteObject.createImmutable("et cliquez sur O K"))));
 BA.debugLineNum = 53;BA.debugLine="lblCommentaire.Color = Colors.RGB(255,128,128)";
Debug.ShouldStop(1048576);
main.mostCurrent._lblcommentaire.runVoidMethod ("setColor",main.mostCurrent.__c.getField(false,"Colors").runMethod(true,"RGB",(Object)(BA.numberCast(int.class, 255)),(Object)(BA.numberCast(int.class, 128)),(Object)(BA.numberCast(int.class, 128))));
 };
 BA.debugLineNum = 55;BA.debugLine="End Sub";
Debug.ShouldStop(4194304);
return RemoteObject.createImmutable("");
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
}