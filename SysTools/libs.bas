B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=StaticCode
Version=8.3
@EndOfDesignText@
'Code module
'Subs in this code module will be accessible from all modules.
Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	
End Sub


'This function will return shell command results
Sub sendToShell(str As String) As String
	
	
	Private CombinedStd As String	
	Private StdOut, StdErr As StringBuilder
	Private Ph As Phone

	StdOut.Initialize
	StdErr.Initialize
	Ph.Shell(str, Null, StdOut, StdErr)
	CombinedStd = StdOut.ToString&StdErr.ToString 'output everything even errors
	Return CombinedStd
'	Return StdOut.ToString
End Sub



