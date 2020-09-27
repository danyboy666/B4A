B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Activity
Version=8.3
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: False
#End Region

Sub Process_Globals
	
End Sub

Sub Globals	
	Private lvPKG As ListView
	Private pm As PackageManager
	Private packages As List	
End Sub

'This function uses the PacketManger library. This will list all available packages and store them in an array. 
' We itterate thru this array and display the elements in a listview object.
Sub Activity_Create(FirstTime As Boolean)
	
	packages = pm.GetInstalledPackages
	Activity.Initialize(Me) 'Initialize main layout
	Activity.Title = "List packages" 'Activity main title
	lvPKG.Initialize("") 'Initialize main layout views
	lvPKG.SingleLineLayout.Label.TextSize = 5dip 'do not change this even if IDE complains!
	lvPKG.SingleLineLayout.ItemHeight = 40dip	'Item listed height
	ToastMessageShow("Use back key to go back to sub menu", False) 'info fuor user

	For i = 1 To packages.Size - 1	'iterate the array
		lvPKG.AddTwoLinesAndBitmap("Package "& i, packages.Get(i), LoadBitmap(File.DirAssets, "apk-icon-17.png")) 'store eleemnt into a list variable
		Activity.removeAllViews()
		Activity.AddView(lvPKG, 0, 0, 100%x, 100%y) 'display our elements
	Next	
End Sub





Sub Activity_KeyPress(KeyCode As Int) As Boolean
  
	If KeyCode = KeyCodes.KEYCODE_BACK Then 'detect back key press
		Activity.finish 'close this activity
'		StartActivity("main") 'send user back to main activity if the back key is pressed.
		Return True
	End If

End Sub