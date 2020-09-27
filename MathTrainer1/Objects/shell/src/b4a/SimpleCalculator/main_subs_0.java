package b4a.SimpleCalculator;

import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.pc.*;

public class main_subs_0 {


public static RemoteObject  _activity_create(RemoteObject _firsttime) throws Exception{
try {
		Debug.PushSubsStack("Activity_Create (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,53);
if (RapidSub.canDelegate("activity_create")) { return b4a.SimpleCalculator.main.remoteMe.runUserSub(false, "main","activity_create", _firsttime);}
Debug.locals.put("FirstTime", _firsttime);
 BA.debugLineNum = 53;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
Debug.ShouldStop(1048576);
 BA.debugLineNum = 55;BA.debugLine="Activity.LoadLayout(\"Main\")";
Debug.ShouldStop(4194304);
main.mostCurrent._activity.runMethodAndSync(false,"LoadLayout",(Object)(RemoteObject.createImmutable("Main")),main.mostCurrent.activityBA);
 BA.debugLineNum = 56;BA.debugLine="NouveauProbleme";
Debug.ShouldStop(8388608);
_nouveauprobleme();
 BA.debugLineNum = 57;BA.debugLine="End Sub";
Debug.ShouldStop(16777216);
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
		Debug.PushSubsStack("Activity_Pause (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,63);
if (RapidSub.canDelegate("activity_pause")) { return b4a.SimpleCalculator.main.remoteMe.runUserSub(false, "main","activity_pause", _userclosed);}
Debug.locals.put("UserClosed", _userclosed);
 BA.debugLineNum = 63;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
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
public static RemoteObject  _activity_resume() throws Exception{
try {
		Debug.PushSubsStack("Activity_Resume (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,59);
if (RapidSub.canDelegate("activity_resume")) { return b4a.SimpleCalculator.main.remoteMe.runUserSub(false, "main","activity_resume");}
 BA.debugLineNum = 59;BA.debugLine="Sub Activity_Resume";
Debug.ShouldStop(67108864);
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
public static RemoteObject  _btnaction_click() throws Exception{
try {
		Debug.PushSubsStack("btnAction_Click (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,68);
if (RapidSub.canDelegate("btnaction_click")) { return b4a.SimpleCalculator.main.remoteMe.runUserSub(false, "main","btnaction_click");}
 BA.debugLineNum = 68;BA.debugLine="Sub btnAction_Click";
Debug.ShouldStop(8);
 BA.debugLineNum = 69;BA.debugLine="If btnAction.Text = \"O K\" Then";
Debug.ShouldStop(16);
if (RemoteObject.solveBoolean("=",main.mostCurrent._btnaction.runMethod(true,"getText"),BA.ObjectToString("O K"))) { 
 BA.debugLineNum = 70;BA.debugLine="If edtResultat.Text = \"\" Then";
Debug.ShouldStop(32);
if (RemoteObject.solveBoolean("=",main.mostCurrent._edtresultat.runMethod(true,"getText"),BA.ObjectToString(""))) { 
 BA.debugLineNum = 71;BA.debugLine="Msgbox(\"Il n’y a pas de résultat\",\"E R R E U R\"";
Debug.ShouldStop(64);
main.mostCurrent.__c.runVoidMethodAndSync ("Msgbox",(Object)(BA.ObjectToCharSequence("Il n’y a pas de résultat")),(Object)(BA.ObjectToCharSequence(RemoteObject.createImmutable("E R R E U R"))),main.mostCurrent.activityBA);
 }else {
 BA.debugLineNum = 73;BA.debugLine="TestResultat";
Debug.ShouldStop(256);
_testresultat();
 };
 }else {
 BA.debugLineNum = 76;BA.debugLine="NouveauProbleme";
Debug.ShouldStop(2048);
_nouveauprobleme();
 BA.debugLineNum = 77;BA.debugLine="btnAction.Text = \"O K\"";
Debug.ShouldStop(4096);
main.mostCurrent._btnaction.runMethod(true,"setText",BA.ObjectToCharSequence("O K"));
 };
 BA.debugLineNum = 79;BA.debugLine="End Sub";
Debug.ShouldStop(16384);
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
 //BA.debugLineNum = 25;BA.debugLine="Private btnAction As Button";
main.mostCurrent._btnaction = RemoteObject.createNew ("anywheresoftware.b4a.objects.ButtonWrapper");
 //BA.debugLineNum = 26;BA.debugLine="Private edtResultat As EditText";
main.mostCurrent._edtresultat = RemoteObject.createNew ("anywheresoftware.b4a.objects.EditTextWrapper");
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
 //BA.debugLineNum = 32;BA.debugLine="End Sub";
return RemoteObject.createImmutable("");
}
public static RemoteObject  _nouveauprobleme() throws Exception{
try {
		Debug.PushSubsStack("NouveauProbleme (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,34);
if (RapidSub.canDelegate("nouveauprobleme")) { return b4a.SimpleCalculator.main.remoteMe.runUserSub(false, "main","nouveauprobleme");}
 BA.debugLineNum = 34;BA.debugLine="Private Sub NouveauProbleme";
Debug.ShouldStop(2);
 BA.debugLineNum = 35;BA.debugLine="Nombre1 = Rnd(1, 10) 'Génère un nombre aléatoire";
Debug.ShouldStop(4);
main._nombre1 = main.mostCurrent.__c.runMethod(true,"Rnd",(Object)(BA.numberCast(int.class, 1)),(Object)(BA.numberCast(int.class, 10)));
 BA.debugLineNum = 36;BA.debugLine="Nombre2 = Rnd(1, 10) 'Génère un nombre aléatoire";
Debug.ShouldStop(8);
main._nombre2 = main.mostCurrent.__c.runMethod(true,"Rnd",(Object)(BA.numberCast(int.class, 1)),(Object)(BA.numberCast(int.class, 10)));
 BA.debugLineNum = 37;BA.debugLine="lblNombre1.Text = Nombre1 'Affiche Nombre1 dans l";
Debug.ShouldStop(16);
main.mostCurrent._lblnombre1.runMethod(true,"setText",BA.ObjectToCharSequence(main._nombre1));
 BA.debugLineNum = 38;BA.debugLine="lblNombre2.Text = Nombre2 'Affiche Nombre2 dans l";
Debug.ShouldStop(32);
main.mostCurrent._lblnombre2.runMethod(true,"setText",BA.ObjectToCharSequence(main._nombre2));
 BA.debugLineNum = 39;BA.debugLine="lblCommentaire.Text = \"Entrez le résultat\" & CRLF";
Debug.ShouldStop(64);
main.mostCurrent._lblcommentaire.runMethod(true,"setText",BA.ObjectToCharSequence(RemoteObject.concat(RemoteObject.createImmutable("Entrez le résultat"),main.mostCurrent.__c.getField(true,"CRLF"),RemoteObject.createImmutable("et cliquez sur OK"))));
 BA.debugLineNum = 40;BA.debugLine="edtResultat.Text = \"\" 'Vide edtResult.Text";
Debug.ShouldStop(128);
main.mostCurrent._edtresultat.runMethodAndSync(true,"setText",BA.ObjectToCharSequence(""));
 BA.debugLineNum = 42;BA.debugLine="End Sub";
Debug.ShouldStop(512);
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
main.myClass = BA.getDeviceClass ("b4a.SimpleCalculator.main");
		
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
		Debug.PushSubsStack("TestResultat (main) ","main",0,main.mostCurrent.activityBA,main.mostCurrent,44);
if (RapidSub.canDelegate("testresultat")) { return b4a.SimpleCalculator.main.remoteMe.runUserSub(false, "main","testresultat");}
 BA.debugLineNum = 44;BA.debugLine="Sub TestResultat";
Debug.ShouldStop(2048);
 BA.debugLineNum = 45;BA.debugLine="If edtResultat.Text = Nombre1 + Nombre2 Then";
Debug.ShouldStop(4096);
if (RemoteObject.solveBoolean("=",main.mostCurrent._edtresultat.runMethod(true,"getText"),BA.NumberToString(RemoteObject.solve(new RemoteObject[] {main._nombre1,main._nombre2}, "+",1, 1)))) { 
 BA.debugLineNum = 46;BA.debugLine="lblCommentaire.Text = \"B O N résultat\" & CRLF &";
Debug.ShouldStop(8192);
main.mostCurrent._lblcommentaire.runMethod(true,"setText",BA.ObjectToCharSequence(RemoteObject.concat(RemoteObject.createImmutable("B O N résultat"),main.mostCurrent.__c.getField(true,"CRLF"),RemoteObject.createImmutable("Cliquez sur Nouveau"))));
 BA.debugLineNum = 47;BA.debugLine="btnAction.Text = \"Nouveau\"";
Debug.ShouldStop(16384);
main.mostCurrent._btnaction.runMethod(true,"setText",BA.ObjectToCharSequence("Nouveau"));
 }else {
 BA.debugLineNum = 49;BA.debugLine="lblCommentaire.Text = \"M A U V A I S  résultat\"";
Debug.ShouldStop(65536);
main.mostCurrent._lblcommentaire.runMethod(true,"setText",BA.ObjectToCharSequence(RemoteObject.concat(RemoteObject.createImmutable("M A U V A I S  résultat"),main.mostCurrent.__c.getField(true,"CRLF"),RemoteObject.createImmutable("Entrez un nouveau résultat"),main.mostCurrent.__c.getField(true,"CRLF"),RemoteObject.createImmutable("et cliquez sur O K"))));
 };
 BA.debugLineNum = 51;BA.debugLine="End Sub";
Debug.ShouldStop(262144);
return RemoteObject.createImmutable("");
}
catch (Exception e) {
			throw Debug.ErrorCaught(e);
		} 
finally {
			Debug.PopSubsStack();
		}}
}