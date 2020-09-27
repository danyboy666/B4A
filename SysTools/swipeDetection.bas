B4A=true
Group=Default Group
ModulesStructureVersion=1
Type=Activity
Version=8.3
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: true
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	Public getApp As String
End Sub

Sub Globals
	Private pnlDirection As Panel
	Private lblUp,lblDown,lblRight,lblLeft As Label
	Private Minimaldistance As Int = 150
	Private x1,x2,y1,y2 As Float
	Private runtime As Boolean
End Sub

Sub Activity_Create(FirstTime As Boolean)
	
	If FirstTime = True Then
	runtime = True	
	Else 
		runtime = False		
	End If
	
	Private W As Int = 100dip 'This is picture width
	Private H As Int = 100dip ' and height

	Activity.Title = "DB's SysTools"
	Activity.Initialize("swipeDetection")
	Activity.Color = Colors.DarkGray	
	pnlDirection.Initialize("pnlDirection")	
	pnlDirection.SetBackgroundImage(LoadBitmap(File.DirAssets, "swipeMenu.png")) 'set bgn image
	lblUp.Initialize("lblUp")
	lblDown.Initialize("lblDown")
	lblRight.Initialize("lblRight")
	lblLeft.Initialize("lblLeft")
	lblUp.Gravity = Bit.Or(Gravity.CENTER, Gravity.CENTER_HORIZONTAL)
	lblUp.Height = 10dip 
	lblUp.Width = -2 ' -2 constante speciale pour l'ajustement automatique du texte.
	lblUp.TextSize = 20 'Description labels 
	lblUp.TextColor = Colors.white
	lblUp.Text = "Internal"
	lblDown.Gravity = Bit.Or(Gravity.CENTER, Gravity.CENTER_HORIZONTAL)
	lblDown.Height = 10dip
	lblDown.Width = -2	
	lblDown.TextSize = 20
	lblDown.TextColor = Colors.white
	lblDown.Text = "System"	
	lblRight.Gravity = Bit.Or(Gravity.CENTER, Gravity.CENTER_HORIZONTAL)
	lblRight.Height = 10dip
	lblRight.Width = -2	
	lblRight.TextSize = 20
	lblRight.TextColor = Colors.white
	lblRight.Text = "File system"	
	lblLeft.Gravity = Bit.Or(Gravity.CENTER, Gravity.CENTER_HORIZONTAL)
	lblLeft.Height = 10dip
	lblLeft.Width = -2	
	lblLeft.TextSize = 20
	lblLeft.TextColor = Colors.white
	lblLeft.Text = "Network" 'Description labels end
	Activity.AddView(pnlDirection, (100%x - W)/2, (100%y - H)/2,  W, H)
	Activity.AddView(lblUp,(100%x - W)/2, 25%y , W, H)
	Activity.AddView(lblDown,(100%x - W)/2, 60%y , W, H)
	Activity.AddView(lblRight,(95%x + W + H)/2, 42%y , W, H)
	Activity.AddView(lblLeft,(33%x - W)/2, 42%y , W, H)	
	ToastMessageShow("Swipe any direction to enter a sub menu",True)
End Sub

Sub Activity_Pause (UserClosed As Boolean)
		
	If	runtime = True Then
'	Activity.finish	
	If UserClosed = True Then
			Activity.finish
'		ToastMessageShow("test activity paused swipe",True)
	End If	
	End If
End Sub

Sub pnlDirection_Touch (Action As Int, X As Float, Y As Float)
	
	Select Action		
		Case Activity.ACTION_DOWN
			x1 = x
			y1 = y
		Case Activity.ACTION_UP
			x2 = x
			y2 = y
			
			Private deltaX As Float = x2 - x1
			Private deltaY As Float = y2 - y1
            
			If Abs(deltaX) > Minimaldistance Then
				If x2 > x1 Then

					getApp = "appFileSystem" 'Left to Right
					Activity.Finish	
				Else

					getApp = "appIPTools" 'Right to Left
					Activity.Finish
				End If

			else If Abs(deltaY) > Minimaldistance Then
				If y2 > y1 Then

					getApp = "appSysTools" 'Top to Down
					Activity.Finish
				Else

					getApp = "appInternalTools" 'Down to Top
					Activity.Finish
				End If            
			End If          
	End Select
End Sub

Sub Activity_KeyPress(KeyCode As Int) As Boolean
   
If	runtime = True Then
	Activity.finish
	Else
		If KeyCode = KeyCodes.KEYCODE_BACK Then
		Activity.finish
		StartActivity("main")
		Return True
		StartActivity("main")
	End If
End If
End Sub