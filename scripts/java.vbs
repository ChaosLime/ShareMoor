wscript.echo java

' Returns Java version, both 32 and 64 bit
' by Jørgen Bigom
' Jan. 2016
Function Java
	Dim objWMIService, colItems, objItem
	Dim javadata, temp1, temp2
	Set objWMIService = GetObject("winmgmts:\\.\root\CIMV2")
	Set colItems = objWMIService.ExecQuery("SELECT * FROM Win32_ProductSoftwareFeatures")
	For Each objItem In colItems
		temp1 = objItem.Component
		If Instr (1, temp1, "ProductName=""Java", vbTextCompare) Then
			javadata = Split (temp1, ",")
			temp1 = javadata (Ubound (javadata)-1)
			temp1 = Replace (temp1, "ProductName=""", "", 1, -1, vbTextCompare)
			temp1 = Left (temp1 & Space (27), 27)
			temp2 = javadata (Ubound (javadata))
			temp2 = Replace (temp2, "Version=""", "Version: ", 1, -1, vbTextCompare)
			java = java + temp1 & " - " & temp2 & VbCrLf
			java = Replace (java, """", "")
		End If
	Next
	If java = "" Then
		java = " Java not installed!"
	Else
		java = Left (java, Len (java) -2)
	End If
End Function