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
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	
	Public data As String	
	
End Sub

Sub Globals
	'These global variables will be redeclared each time the activity is created.
	'These variables can only be accessed from this module.
	Private svConsole As ScrollView	
	Private lblConsole As Label
	Private edtUserInput As  EditText ' give user an input
	Private su As StringUtils	
	Private userInputSTR As String 'this will hold the edtTxt returned values

End Sub

Sub Activity_Create(FirstTime As Boolean)
	createPanel
End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

'load scrollview panel elements
Sub createPanel	
	Private cdBackground, ActBgnd As ColorDrawable
	Private TextSize As Int
		
	Activity.Title = "Loading apps"
	TextSize=14	
	svConsole.Initialize(-1) 'init scrollview container.  THis will holr our lblConsole, edtText , etc views.
	cdBackground.Initialize2(Colors.Transparent, 5dip, 2dip, Colors.RGB(255,255,255)) 'Draw a a white trim border around activity
	ActBgnd.Initialize(Colors.ARGB(127,255,255,255),100%y/100%x)
	Activity.Background = cdBackground 'Apply cd to actvity bgn
	Activity.Color = Colors.Black
	Activity.AddView(svConsole,2%x,2%y,95%x,95%y)
	lblConsole.Initialize("")'main display screen
	lblConsole.Color = Colors.black
	lblConsole.TextSize = TextSize
	lblConsole.TextColor = Colors.White
	lblConsole.Height = -1
	svConsole.Panel.Height = lblConsole.Height
	svConsole.Panel.AddView(lblConsole,1%x,1%y,90%x,lblConsole.Height)
	FillScrollView(Main.passCMD)
End Sub

Sub FillScrollView(getCMD As String)
	Private varDate, getUser As String

	getUser  = libs.sendToShell("whoami")
	varDate = libs.sendToShell("date")
	lblConsole.Text = "Using username " & getUser
	lblConsole.Text = lblConsole.Text & CRLF & "Welcome to db's SysTools utility" & CRLF
	lblConsole.Text = lblConsole.Text & CRLF & "	* Projet final module 6"
	lblConsole.Text = lblConsole.Text & CRLF & "	* Documentation https://developer.android.com"
	lblConsole.Text = lblConsole.Text & CRLF & "	* IDE B4A https://www.b4x.com" & CRLF
	lblConsole.Text = lblConsole.Text & CRLF & varDate 
	lblConsole.Text = lblConsole.Text & CRLF & " *** La boule magique dit : " & getAnswer & " ***" & CRLF
	lblConsole.Text = lblConsole.Text & CRLF & " -- # Loading..." & CRLF
	Sleep(1000)
	If getCMD = "devInfo"  Then	
		Private getSE, getStorage, getUptime  As String
		
		getSE  = libs.sendToShell("getenforce")
		getStorage  = libs.sendToShell("df -h /data/media")
		getUptime = libs.sendToShell("uptime")
		lblConsole.Text = lblConsole.Text & CRLF & "SELinux status is : " & getSE 
		lblConsole.Text = lblConsole.Text & CRLF & "Storage : " & getStorage
		lblConsole.Text = lblConsole.Text & CRLF & "Uptime : " & getUptime
		Sleep(2000)		
		'ASCii art!!!!!!!!!!!
		lblConsole.Text = lblConsole.Text & CRLF & "		 __  __  _____  ____  ____    ____  _____     ___  _____  __  __  ____"
		lblConsole.Text = lblConsole.Text & CRLF & "		(  \/  )(  _  )(  _ \( ___)  (_  _)(  _  )   / __)(  _  )(  \/  )( ___)"
		lblConsole.Text = lblConsole.Text & CRLF & "		 )    (  )(_)(  )   / )__)     )(   )(_)(   ( (__  )(_)(  )    (  )__) ()"
		lblConsole.Text = lblConsole.Text & CRLF & "		(_/\/\_)(_____)(_)\_)(____)   (__) (_____)   \___)(_____)(_/\/\_)(____)/"
		lblConsole.Text = lblConsole.Text & CRLF & "		 ___  ____   __   _  _    ____  __  __  _  _  ____  ____"
		lblConsole.Text = lblConsole.Text & CRLF & "		/ __)(_  _) /__\ ( \/ )  (_  _)(  )(  )( \( )( ___)(  _ \"
		lblConsole.Text = lblConsole.Text & CRLF & "		\__ \  )(  /(__)\ \  /     )(   )(__)(  )  (  )__)  )(_) )"
		lblConsole.Text = lblConsole.Text & CRLF & "		(___/ (__)(__)(__)(__)    (__) (______)(_)\_)(____)(____/()()()"
	Else if getCMD = "getWeather" Then
		
		SetScreenOrientation(0)
		DownloadAndSaveWeatherFile("http://db-tech.ddns.net/weather.txt")
		
	Else if getCMD = "enterCMD" Then
	
		edtUserInput.Initialize("edtUserInput") 'init edtUserInput
		edtUserInput.InputType = edtUserInput.INPUT_TYPE_TEXT
		edtUserInput.Gravity=Gravity.CENTER_HORIZONTAL
		svConsole.Panel.RemoveAllViews
		svConsole.RemoveView
		Activity.Invalidate
		Activity.AddView(svConsole,2%x,2%y,95%x,75%y)
		edtUserInput.Height = 100dip
	'	edtUserInput.Width = 90%x
		svConsole.Panel.AddView(edtUserInput,0, 30%y, 90%x,100%y)
		edtUserInput.BringToFront
		lblConsole.Height = -2		
		svConsole.Panel.AddView(lblConsole,1%x,1%y,90%x,lblConsole.Height)		
		'ToastMessageShow("Touch screen and type a command",True)
		ToastMessageShow("En development",True)
		edtUserInput.Color = Colors.Black
		edtUserInput.TextColor = Colors.white
		svConsole.Panel.Height = lblConsole.Height
		edtUserInput.BringToFront
	Else ' If no conditions met finish preparing our CLi
		lblConsole.Text = lblConsole.Text  & CRLF & " -- # Output of : " & getCMD & CRLF  & CRLF & "~­$ "
		lblConsole.Text = lblConsole.Text & libs.sendToShell(getCMD) 'send commands we got from main

	End If
	svConsole.Panel.Height = su.MeasureMultilineTextHeight(lblConsole, lblConsole.Text) 'resize the current scrollview panel to label.txt height
End Sub

Sub Activity_KeyPress(KeyCode As Int) As Boolean 
	If KeyCode = KeyCodes.KEYCODE_BACK Then ' Capture back key, kill current activity and return back to "main activity"
		Activity.finish
		StartActivity("main")
		Return True
	End If
End Sub

' Based on the game Magic 8-Ball
' get answer block
' fun stuff for user to read
Sub getAnswer As String
	Private RepEvasives() As String
	Private RepAffirmatives() As String
	Private RepNegatives() As String
	Private RepStr() As String
	Private i = 0 , j = 0,  k = 0, x = 0 As Byte
	
	i = i  + 1
	j = j  + 1
	k = k  + 1
	x = x + 1
	
	RepEvasives = Array As String("Essaye plus tard", "Essaye encore", "Pas d'avis", "C'est ton destin", "Le sort en est jeté", "Une chance sur deux", "Repose ta question")
	RepAffirmatives = Array As String("D'après moi oui", "C'est certain", "Oui absolument", "Tu peux compter dessus", "Sans aucun doute", "Très probable", "Oui", "C'est bien parti")
	RepNegatives = Array As String("C'est non", "Peu probable", "Faut pas rêver", "N'y compte pas", "Impossible")
	
	i = Rnd(1,5)
	j = Rnd(1,8)
	k = Rnd(1,7)
	x = Rnd(1,3)
					
	RepStr = Array As String(RepNegatives(i), RepAffirmatives(j), RepEvasives(k))

	Return RepStr(x)	
End Sub

' This function will download and return data as picture and display it to the console
Sub DownloadAndSaveWeatherFile (Link As String)
	Private j As HttpJob
	Private dataWeather As String
	Private ivWeather As ImageView

	j.Initialize("", Me)
	j.Download(Link)
	j.Download("http://db-tech.ddns.net/rimouski.png")

	Wait For (j) JobDone(j As HttpJob)
	If j.Success Then
		Private bmpWeatherData As Bitmap
		Private OutStream As OutputStream = File.OpenOutput(File.DirInternalCache, "weather.txt", False) ' download weather data as text file in cache directory
		Private OutStream2 As OutputStream = File.OpenOutput(File.DirInternalCache, "rimouski.png", False)' download weather data as image file in cache directory		

		lblConsole.Text = lblConsole.Text & CRLF & " -- # Weather services online"
		j.GetBitmap.WriteToStream(OutStream2, 100, "PNG")' convert downloaded image to a format and compression android like , from 0 - 100 max quality.
		lblConsole.TextSize = 11 ' resize so we can read the ASCii stuff!!!
		lblConsole.Text = lblConsole.Text & CRLF & " -- # Fetching data please wait..."
		Sleep(500)
		bmpWeatherData = j.GetBitmap
		ivWeather.Initialize("ivWeather")
		svConsole.Panel.AddView(ivWeather,0, lblConsole.Height, 90%x,100%y)
		ivWeather.Gravity = Gravity.FILL
		ivWeather.SetBackgroundImage(bmpWeatherData)
		lblConsole.Height = lblConsole.Height + ivWeather.Height
		svConsole.Panel.Height = lblConsole.Height
		OutStream.Close '<------ very important
		OutStream2.Close

	else If File.Exists (File.DirInternalCache,"weather.txt") Then

		Private bmpWeatherDataLocal As Bitmap
		Private weatherDataTxt As String

		If File.Exists(File.DirInternalCache,"rimouski.png") Then
			ToastMessageShow("Weather services offline, fetching cached data",True)
			lblConsole.Text = lblConsole.Text & CRLF & " -- # Weather services offline..."			
			weatherDataTxt = File.ReadString(File.DirInternalCache,"weather.txt") 'dump txt file into string variable			
			dataWeather = weatherDataTxt			
			lblConsole.TextSize = 11 ' resize so we can read the ASCii stuff!!!
			lblConsole.Text = lblConsole.Text & CRLF & " -- # Getting cached copy of weather data" & CRLF
			Sleep(1000)
			lblConsole.Text = lblConsole.Text & CRLF & dataWeather
			bmpWeatherDataLocal.Initialize(File.DirInternalCache,"rimouski.png") 'initialize our imageview stuff, set bg to it and add it to our scrollview panel.
			ivWeather.Initialize("ivWeather")
			svConsole.Panel.AddView(ivWeather,0, lblConsole.Height, 90%x,100%y)
			ivWeather.Gravity = Gravity.FILL
			ivWeather.SetBackgroundImage(bmpWeatherDataLocal)
			lblConsole.Height = lblConsole.Height + ivWeather.Height
			svConsole.Panel.Height = lblConsole.Height
			
			Else
				ToastMessageShow("Weather services offline, fetching cached data",True)
				Activity.Finish
			End If		
	Else 			
		ToastMessageShow("Could not contact weather services. Quitting",True)
		Activity.Finish
	End If
		j.Release 'free up ressources used by this process
End Sub

Sub SetScreenOrientation (Orientation As Int)
End Sub

' Fonction pour accepter une entré fait par l'utilisateur.
Sub edtUserInput_EnterPressed
	
	Private sendShellCMD As String
		
	userInputSTR = edtUserInput.Text	
	edtUserInput.Invalidate
	edtUserInput.ForceDoneButton = True
	edtUserInput.RemoveView
	If edtUserInput.Text <> "" Then
		sendShellCMD = libs.sendToShell(userInputSTR)		
		lblConsole.Text = lblConsole.Text  & CRLF & " -- # Output of : " & edtUserInput.Text & CRLF  & CRLF & "~­$ "
		lblConsole.Text = lblConsole.Text &  CRLF & sendShellCMD 'Update lblConsole with the user user command result
		svConsole.Panel.Height = su.MeasureMultilineTextHeight(lblConsole, lblConsole.Text) 'resize the current scrollview panel to label.txt height					
		
	Else
		Msgbox("You must enter a command before pressing ok",True)
	End If	
End Sub	

