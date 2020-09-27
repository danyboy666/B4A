B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Service
Version=8.3
@EndOfDesignText@
#Region  Service Attributes 
	#StartAtBoot: False
	#ExcludeFromLibrary: True
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	Dim CheckScrOrientation, StoredOrientation As String
	Dim RotateTimer As Timer
End Sub

Sub Service_Create
	RotateTimer.Initialize("SDTime", 500)  '1000 Milliseconds = 1 second
	RotateTimer.Enabled = True
	
End Sub

Sub Service_Start (StartingIntent As Intent)
	

End Sub

Sub Service_TaskRemoved
	'This event will be raised when the user removes the app from the recent apps list.
End Sub

'Return true to allow the OS default exceptions handler to handle the uncaught exception.
Sub Application_Error (Error As Exception, StackTrace As String) As Boolean
	Return True
End Sub

Sub Service_Destroy

End Sub

Sub SDTime_Tick
	If GetDeviceLayoutValues.Width > GetDeviceLayoutValues.Height Then
		CheckScrOrientation = "Landscape"
	Else
		CheckScrOrientation = "Portrait"
	End If

	If CheckScrOrientation <> StoredOrientation Then
		StoredOrientation = CheckScrOrientation
		'PUT WHATEVER UPDATE/REFRESH ROUTINE OR CALLSUB YOU NEED HERE...
		'Activity.SetBackgroundImage(LoadBitmap(File.DirAssets, "8ballBng_portrait.png"))
		'StartActivity(Main)
	'Else
	'	Activity.SetBackgroundImage(LoadBitmap(File.DirAssets, "8ballBng_landscape.png"))
	End If
	
End Sub