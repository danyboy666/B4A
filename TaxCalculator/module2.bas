B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Activity
Version=8.3
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: True
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	Private Options As ListView
End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	'Activity.LoadLayout("Layout1")
	Private about As String
	Private opt1 = "Tax Calculator V 1.0"  As String
	Private opt2 As String
	Private opt3 As String
	Dim optArr() As String
	Private NbX = 3 As Int
	
	
	
	about = "Tax Calculator V 1.0"
	optArr = Array As String("opt1", "opt2", "opt3")
	Options.Initialize("Options")
	For i = 0 To NbX - 1
		Options.AddSingleLine(optArr(i))
		Activity.removeAllViews()
		
		Activity.AddView(Options, 0, 0, 100%x, 100%y)
	Next
End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub


Sub Options_ItemClick (Position As Int, Value As Object)
	Activity.Title = Value
End Sub

Sub Options_ItemLongClick (Position As Int, Value As Object)
	Activity.Finish
	
End Sub