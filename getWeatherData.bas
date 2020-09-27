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
	Dim ConnexioInternet As String
	Dim no_intenet As String = "<center><h1>No Internet</h1></center>"
	Private dataPanel As Panel 
End Sub

Sub Activity_Create(FirstTime As Boolean)
	'Do not forget to load the layout file created with the visual designer. For example:
	'Activity.LoadLayout("Layout1")

  
	Dim myw As Label
	Activity.Initialize(Me)
	
	
	myw.Initialize("")
	
	myw.Color = Colors.black
	myw.TextColor = Colors.white
	myw.Height = 1000dip 
	
	dataPanel.Initialize("dataPanel")
	
	Private su As StringUtils
	
	Activity.AddView(myw, 0, 0, 100%x, 100%y) 'Your options for size here
	Activity.AddView(dataPanel, 0, 0, 100%x, 100%y)
	Log("Will check internet now")
	CheckInternet
	If ConnexioInternet <> " " Then
		Log("Internet: 1")
		
'		myw.LoadUrl("http://db-tech.ddns.net/weather.txt")
		
		myw.text = ConnexioInternet
		dataPanel.Height = su.MeasureMultilineTextHeight(myw, myw.Text) 'resize the current scrollview panel to label.txt height
	Else
		Log("Internet: 0")
'		myw.LoadUrl($"no_internet"$)
	End If
End Sub

Sub CheckInternet
	Dim j As HttpJob
	j.Initialize("", Me)
	j.Download("http://db-tech.ddns.net/weather.txt")
	Wait For (j) JobDone(j As HttpJob)
	If j.Success Then
		ConnexioInternet = j.GetString
		Log(ConnexioInternet)
	End If
	j.Release
	
End Sub
Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub
