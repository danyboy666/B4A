package b4a.SimpleCalculator2.designerscripts;
import anywheresoftware.b4a.objects.TextViewWrapper;
import anywheresoftware.b4a.objects.ImageViewWrapper;
import anywheresoftware.b4a.BA;


public class LS_main{

public static void LS_general(java.util.LinkedHashMap<String, anywheresoftware.b4a.keywords.LayoutBuilder.ViewWrapperAndAnchor> views, int width, int height, float scale) {
anywheresoftware.b4a.keywords.LayoutBuilder.setScaleRate(0.3);
//BA.debugLineNum = 2;BA.debugLine="AutoScaleAll"[Main/General script]
anywheresoftware.b4a.keywords.LayoutBuilder.scaleAll(views);
//BA.debugLineNum = 3;BA.debugLine="lblSigneMath.HorizontalCenter = 50%x  'centre la view au milieu de l'écran"[Main/General script]
views.get("lblsignemath").vw.setLeft((int)((50d / 100 * width) - (views.get("lblsignemath").vw.getWidth() / 2)));
//BA.debugLineNum = 4;BA.debugLine="lblNombre1.Right = lblSigneMath.Left  'aligne le bord droit sur le bord gauche"[Main/General script]
views.get("lblnombre1").vw.setLeft((int)((views.get("lblsignemath").vw.getLeft()) - (views.get("lblnombre1").vw.getWidth())));
//BA.debugLineNum = 5;BA.debugLine="lblNombre2.Left = lblSigneMath.Right  'aligne le bord gauche sur le bord droit"[Main/General script]
views.get("lblnombre2").vw.setLeft((int)((views.get("lblsignemath").vw.getLeft() + views.get("lblsignemath").vw.getWidth())));
//BA.debugLineNum = 6;BA.debugLine="lblResultat.HorizontalCenter = 50%x   'centre la view au milieu de l'écran"[Main/General script]
views.get("lblresultat").vw.setLeft((int)((50d / 100 * width) - (views.get("lblresultat").vw.getWidth() / 2)));
//BA.debugLineNum = 7;BA.debugLine="lblCommentaire.HorizontalCenter = 50%x 'centre la view au milieu de l'écran"[Main/General script]
views.get("lblcommentaire").vw.setLeft((int)((50d / 100 * width) - (views.get("lblcommentaire").vw.getWidth() / 2)));
//BA.debugLineNum = 8;BA.debugLine="pnlClavier.HorizontalCenter = 50%x    'centre la view au milieu de l'écran"[Main/General script]
views.get("pnlclavier").vw.setLeft((int)((50d / 100 * width) - (views.get("pnlclavier").vw.getWidth() / 2)));

}
}