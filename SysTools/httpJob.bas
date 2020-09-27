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

	Public urlResult As String
End Sub

Sub Globals
    'These global variables will be redeclared each time the activity is created.
    'These variables can only be accessed from this module.
    Dim ConnexioInternet As String
    Dim no_intenet As String = "<center><h1>No Internet</h1></center>"
End Sub

Sub Activity_Create(FirstTime As Boolean)
    'Do not forget to load the layout file created with the visual designer. For example:
	'Activity.LoadLayout("Layout1")
   
'	Dim ConnexioInternet As String
	Dim j As HttpJob
'	Private sendData As String
	
	j.Initialize("", Me)
	j.Download("http://db-tech.ddns.net/weather.txt")
	Wait For (j) JobDone(j As HttpJob)
	If j.Success Then
		ConnexioInternet = j.GetString
		Log(ConnexioInternet)
		
		'libs.cmd(getCMD)
'			urlResult = j.GetString
'			Activity.Finish
'		StartActivity("showinfo")
	End If
	j.Release
'	Return sendData
End Sub

