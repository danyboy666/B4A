package ca.android.basic.systools;


import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.debug.*;

public class libs {
private static libs mostCurrent = new libs();
public static Object getObject() {
    throw new RuntimeException("Code module does not support this method.");
}
 public anywheresoftware.b4a.keywords.Common __c = null;
public anywheresoftware.b4a.samples.httputils2.httputils2service _httputils2service = null;
public ca.android.basic.systools.main _main = null;
public ca.android.basic.systools.swipedetection _swipedetection = null;
public ca.android.basic.systools.showinfo _showinfo = null;
public ca.android.basic.systools.listpackages _listpackages = null;
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 3;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 7;BA.debugLine="End Sub";
return "";
}
public static String  _sendtoshell(anywheresoftware.b4a.BA _ba,String _str) throws Exception{
String _combinedstd = "";
anywheresoftware.b4a.keywords.StringBuilderWrapper _stdout = null;
anywheresoftware.b4a.keywords.StringBuilderWrapper _stderr = null;
anywheresoftware.b4a.phone.Phone _ph = null;
 //BA.debugLineNum = 11;BA.debugLine="Sub sendToShell(str As String) As String";
 //BA.debugLineNum = 14;BA.debugLine="Private CombinedStd As String";
_combinedstd = "";
 //BA.debugLineNum = 15;BA.debugLine="Private StdOut, StdErr As StringBuilder";
_stdout = new anywheresoftware.b4a.keywords.StringBuilderWrapper();
_stderr = new anywheresoftware.b4a.keywords.StringBuilderWrapper();
 //BA.debugLineNum = 16;BA.debugLine="Private Ph As Phone";
_ph = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 18;BA.debugLine="StdOut.Initialize";
_stdout.Initialize();
 //BA.debugLineNum = 19;BA.debugLine="StdErr.Initialize";
_stderr.Initialize();
 //BA.debugLineNum = 20;BA.debugLine="Ph.Shell(str, Null, StdOut, StdErr)";
_ph.Shell(_str,(String[])(anywheresoftware.b4a.keywords.Common.Null),(java.lang.StringBuilder)(_stdout.getObject()),(java.lang.StringBuilder)(_stderr.getObject()));
 //BA.debugLineNum = 21;BA.debugLine="CombinedStd = StdOut.ToString&StdErr.ToString 'ou";
_combinedstd = _stdout.ToString()+_stderr.ToString();
 //BA.debugLineNum = 22;BA.debugLine="Return CombinedStd";
if (true) return _combinedstd;
 //BA.debugLineNum = 24;BA.debugLine="End Sub";
return "";
}
}
