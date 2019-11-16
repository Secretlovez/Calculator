import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Calculator extends JFrame {
	public static void main(String[] args) {
		new Calculator();
	}
	static ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript"); 
    static String trueFormula="";         //计算的真实字符串，最后只计算这串字符串
    //中断字符串的栈
    static Stack<String>  strInterrupt=new Stack();
    //中断变量栈
    static Stack<Integer>    intInterrupt=new Stack(); 
 
   
    /*伽马函数的实现，主要用于求广义的阶乘*/
    public double gamma(double x) { 
    	return Math.exp(logGamma(x)); 
    	}
    public double logGamma(double x) {
		      double tmp = (x - 0.5) * Math.log(x + 4.5) - (x + 4.5);
		      double ser = 1.0 + 76.18009173    / (x + 0)   - 86.50532033    / (x + 1)
		                       + 24.01409822    / (x + 2)   -  1.231739516   / (x + 3)
		                       +  0.00120858003 / (x + 4)   -  0.00000536382 / (x + 5);
		      return tmp + Math.log(ser * Math.sqrt(2 * Math.PI));
		 }
    
    /*防止连续的运算符出现*/
	public String mSymbols(String str,String symbol) {		
		int length = str.length();
		String strFormula,strSymbol;
		strFormula="";
		strSymbol="";
		if (length == 1) 
			return str+symbol;
		if (length > 1) {
			strFormula = str.substring(0, length - 1);
			strSymbol=str.substring(length - 1, length);
		}
		switch(strSymbol) {
		case "+":	
			return strFormula+symbol;
			
		case "-":
			return strFormula+symbol;
		 
		case "/":
			return strFormula+symbol;
		    
		case "*":
			return strFormula+symbol;
		   
		case ".":
			return strFormula+symbol;
					 
		default:
			return str+symbol;  
		
		}
	}
	/*数字键逻辑*/
	public void numberKeyLogic(JTextField jTextField,String symbol){
		if(jTextField.getText().indexOf("=") != -1){
			//String[]  lastOperation = jTextField.getText().split("=");
			if(symbol.equals("0")) {
				trueFormula="";
			}else {
			    trueFormula=symbol;
			}
			//strInterrupt="";
			jTextField.setText(symbol);
		}
		else if(jTextField.getText().indexOf("错误") != -1||jTextField.getText().indexOf("Infinity") != -1) {
			trueFormula=symbol;
			//strInterrupt="";
			jTextField.setText(symbol);
		}
		else if (jTextField.getText().equals("0")) {                 /*与0对比，确保不会出现00的现象*/
			//jTextField.setText("");
			if(symbol.equals("0")) {
				trueFormula="";
			}else {
			    trueFormula=symbol;
			}
			//strInterrupt="";
			jTextField.setText(symbol);
			//jTextField.requestFocus();  /*把输入焦点放在调用这个方法的控件上*/ 
		}
		else {
			if(intInterrupt.isEmpty()) {
				if(jTextField.getText().equals("0")) {
					trueFormula="";
				}else {
				trueFormula=trueFormula + symbol;	
				}
			}else {
				if(strInterrupt.isEmpty()) {
					strInterrupt.push((String)symbol);
				}else {
				strInterrupt.push((String)strInterrupt.pop()+symbol);
				}
			}
			String string = jTextField.getText();
			jTextField.setText(string + symbol);
		}
	}
	/*运算键逻辑*/
	public void operationKeyLogic(JTextField jTextField,String symbol) {
		if(jTextField.getText().indexOf("=") != -1){
			String[]  lastOperation = jTextField.getText().split("=");
			trueFormula=lastOperation[1]+symbol;
			jTextField.setText(lastOperation[1]+symbol);
		}
		else if(jTextField.getText().indexOf("错误") != -1||jTextField.getText().indexOf("Infinity") != -1) {
            trueFormula="";
			jTextField.setText("0");
		}
		else if (jTextField.getText().equals("0")) {                 /*与0对比，确保不会出现00的现象*/
			//jTextField.setText("");
			trueFormula="0"+symbol;
			jTextField.setText("0"+symbol);
			jTextField.requestFocus();  /*把输入焦点放在调用这个方法的控件上*/ 
		}
		else {
			if(intInterrupt.isEmpty()) {
				//trueFormula=trueFormula + mSymbols(trueFormula,symbol);
				trueFormula= mSymbols(trueFormula,symbol);
			}else {
				if(intInterrupt.isEmpty()) {
					if(trueFormula.isEmpty()) {
					trueFormula="0" + symbol;
					}else {
						trueFormula=trueFormula + symbol;
					}
				}else {
					if(strInterrupt.isEmpty()) {
					   return;
					}else {
					strInterrupt.push((String)strInterrupt.pop()+symbol);
					}
				}
				//strInterrupt=strInterrupt+ mSymbols(strInterrupt,symbol);
				//strInterrupt.push(mSymbols((String)strInterrupt.pop(),symbol));
			}
			String string = jTextField.getText();
			jTextField.setText(mSymbols(string,symbol));
		}
	}
	
	public void squareKeyLogic(JTextField jTextField,String symbol){
		String str=jTextField.getText();
		int length = str.length();
		int currentLength=length;
		String strFormula,strSymbol,strResult;
		strSymbol=str.substring(currentLength - 1, currentLength);
		strResult="0";
		currentLength--;
		//jTextField.setText(str.substring(currentLength - 1, currentLength));
		if(strSymbol.indexOf("+") != -1||strSymbol.indexOf("-") != -1||strSymbol.indexOf("*") != -1||strSymbol.indexOf("/") != -1||strSymbol.indexOf(".") != -1) {
			jTextField.setText(str);
			return;
		}
		
		if(str.indexOf("=") != -1) {
			String[]  lastOperation = jTextField.getText().split("=");
			if(symbol=="root") {
	        	if(strSymbol.indexOf(".") != -1) {
	     	    Double double1 = Double.parseDouble(lastOperation[1]);// 将字符串转换为double
			    Double double2 =double1 * double1; 
			     strResult = String.valueOf(double2);// 将double转换为string
			     }else {
			    	 int int1=Integer.parseInt(lastOperation[1]);
			    	 int int2=int1*int1;
			    	 strResult = Integer.toString(int2);  	
			     }
			}
	        if(symbol=="sqrt") {
	        	Double double1 = Double.parseDouble(lastOperation[1]);// 将字符串转换为double
				Double double2 =(Double) Math.sqrt(double1);
				int  int1=(new Double(double2)).intValue();
				Double double3=Double.valueOf(int1);
				double dis=1e-6;                               //确定一个精度，方便比较
				if(Math.abs(double2-double3)<dis) {
					strResult = Integer.toString(int1);
				}else {
				   strResult = String.valueOf(double2);
				}
	        }
	        trueFormula=strResult;
	        jTextField.setText(strResult);
		}else {
		          while(currentLength!=0) {
			      strSymbol=str.substring(currentLength - 1, currentLength);
			      if(strSymbol.indexOf("+") != -1||strSymbol.indexOf("-") != -1||strSymbol.indexOf("*") != -1||strSymbol.indexOf("/") != -1) break;
			      else     currentLength--;
		          }
		        strFormula = str.substring(currentLength, length);
		        if(symbol=="root") {
		        	if(strFormula.indexOf(".") != -1) {
		     	    Double double1 = Double.parseDouble(strFormula);// 将字符串转换为double
				    Double double2 =double1 * double1; 
				     strResult = String.valueOf(double2);// 将double转换为string
				     }else {
				    	 int int1=Integer.parseInt(strFormula);
				    	 int int2=int1*int1;
				    	 strResult = Integer.toString(int2);  	 
				     }
				}
		        if(symbol=="sqrt") {
		        	Double double1 = Double.parseDouble(strFormula);// 将字符串转换为double
					Double double2 =(Double) Math.sqrt(double1);
					int  int1=(new Double(double2)).intValue();
					Double double3=Double.valueOf(int1);
					double dis=1e-6;                               //确定一个精度，方便比较
					if(Math.abs(double2-double3)<dis) {
						strResult = Integer.toString(int1);
					}else {
					   strResult = String.valueOf(double2);
					}
		        }
				if(currentLength!=0) {
					trueFormula=str.substring(0, currentLength)+strResult;
				jTextField.setText(str.substring(0, currentLength)+strResult);
				}else {
					trueFormula=strResult;
					jTextField.setText(strResult);
				}
		
		}		
	}
	
	public void interruptKeyLogic(JTextField jTextField,String symbol) throws ScriptException {
		String str=jTextField.getText();
		if(str.indexOf("=") != -1) {
			String[]  lastOperation = jTextField.getText().split("=");
			trueFormula=lastOperation[1];
			//System.out.println(trueFormula);
			str=lastOperation[1];
		}
		Double doubleCalculation;   //用于计算转化的变量
		int length = str.length();
		int currentLength=length;
		String strFormula,strSymbol;
		strSymbol=str.substring(currentLength - 1, currentLength);  //获得字符串最后一位
		// System.out.println(strSymbol);
		currentLength--;
		  if(strSymbol.equals("0")||strSymbol.equals("1")||strSymbol.equals("2")||strSymbol.equals("3")||strSymbol.equals("4")||strSymbol.equals("5")||strSymbol.equals("6")||strSymbol.equals("7")||strSymbol.equals("8")||strSymbol.equals("9")) {
			  //System.out.println(strSymbol);
			 // System.out.println("haha");
			  if(symbol.equals("(")||symbol.equals("sin")||symbol.equals("cos")||symbol.equals("tan")||symbol.equals("ln")||symbol.equals("e^")||symbol.equals("10^")||symbol.equals("√")) {
				   //避免0*sin之类的无意义运算
				  if(trueFormula.isEmpty()) {
					str=symbol+"(";
					trueFormula="";
				}else {
					trueFormula=trueFormula+"*";
					if(symbol.equals("(")) {
					str=str+"*"+symbol;
					}else {
					str=str+"*"+symbol+"(";
					}
				}
				switch(symbol) {
				case "(" :
					intInterrupt.push(0);
					break;
				case "sin":
					intInterrupt.push(1);
					break;
				case "cos":
					intInterrupt.push(2);
					break;
				case "tan":
					intInterrupt.push(3);
					break;
				case "ln":
					intInterrupt.push(4);
					break;
				case "e^":
					intInterrupt.push(5);
					break;
				case "10^":
					intInterrupt.push(6);
					break;
				case "√":
					intInterrupt.push(7);
					break;
			    default:
			    	break;
				}
				
				jTextField.setText(str);
			}
			else if(symbol.equals(")")) {
				// System.out.println(intInterrupt.peek()+"()");
				// intInterrupt.pop();
				 //if(intInterrupt.isEmpty()) {
					// System.out.println(intInterrupt.isEmpty()+"()");
				// return;}
				if(strInterrupt.isEmpty()) {
					//System.out.println((String)strInterrupt.peek());
					return;
				}
				if(/*str.indexOf("(") != -1*/!intInterrupt.isEmpty()) {
				  jTextField.setText(str+")");
				  int switchInterrupt=0;   //获得运算符
				  String caseInterrupt="";  //帮助运算的字符串
				  //System.out.println((String)strInterrupt.peek()+"aaaaaaaaaaaaa");
				  switchInterrupt=(int)intInterrupt.pop();
				  switch(switchInterrupt) {				  
				  case 0:
				      caseInterrupt=jse.eval((String)strInterrupt.pop()).toString();
					  doubleCalculation=Double.parseDouble(caseInterrupt);
					  //doubleCalculation=Math.asin(doubleCalculation);
				      caseInterrupt=String.valueOf(doubleCalculation);
					  trueFormula=trueFormula+caseInterrupt;
				      break;
				  case 1:
					  caseInterrupt=jse.eval((String)strInterrupt.pop()).toString();
					  doubleCalculation=Double.parseDouble(caseInterrupt);
					  doubleCalculation=Math.asin(doubleCalculation);
					  caseInterrupt=String.valueOf(doubleCalculation);
					  trueFormula=trueFormula+caseInterrupt;
				      break;
				  case 2:
					  caseInterrupt=jse.eval((String)strInterrupt.pop()).toString();
					  doubleCalculation=Double.parseDouble(caseInterrupt);
					  doubleCalculation=Math.acos(doubleCalculation);
					  caseInterrupt=String.valueOf(doubleCalculation);
					  trueFormula=trueFormula+caseInterrupt;
				      break;
				  case 3:
					  caseInterrupt=jse.eval((String)strInterrupt.pop()).toString();
					  doubleCalculation=Double.parseDouble(caseInterrupt);
					  doubleCalculation=Math.atan(doubleCalculation);
					  caseInterrupt=String.valueOf(doubleCalculation);
					  trueFormula=trueFormula+caseInterrupt;
				      break;
				  case 4:
					  caseInterrupt=jse.eval((String)strInterrupt.pop()).toString();
					  doubleCalculation=Double.parseDouble(caseInterrupt);
					  if(doubleCalculation<=0) {
						  return;
					  }else {
					  doubleCalculation=Math.log(doubleCalculation);
					  caseInterrupt=String.valueOf(doubleCalculation);
					  trueFormula=trueFormula+caseInterrupt;
					  }
				      break;
				  case 5:
					  caseInterrupt=jse.eval((String)strInterrupt.pop()).toString();
					  doubleCalculation=Double.parseDouble(caseInterrupt);
					  doubleCalculation=Math.exp(doubleCalculation);
					  caseInterrupt=String.valueOf(doubleCalculation);
					  trueFormula=trueFormula+caseInterrupt;
				      break;  
				  case 6:
					  caseInterrupt=jse.eval((String)strInterrupt.pop()).toString();
					  doubleCalculation=Double.parseDouble(caseInterrupt);
					  doubleCalculation=Math.pow(10,doubleCalculation);
					  caseInterrupt=String.valueOf(doubleCalculation);
					  trueFormula=trueFormula+caseInterrupt;
				      break;  
				  case 7:
					  caseInterrupt=jse.eval((String)strInterrupt.pop()).toString();
					  doubleCalculation=Double.parseDouble(caseInterrupt);
					  if(doubleCalculation<0) {
						  return;
					  }else {
					  doubleCalculation=Math.sqrt(doubleCalculation);
					  caseInterrupt=String.valueOf(doubleCalculation);
					  trueFormula=trueFormula+caseInterrupt;
					  }
				      break; 
				  default:
				    	break;
				  }
				}
				else {
					return;
				}		     	
			}else if(symbol.equals("%")||symbol.equals("*10^-1")||symbol.equals("^2")||symbol.equals("^3")||symbol.equals("!")){
				/*strSymbol=str.substring(currentLength - 1, currentLength);
				if(strSymbol=="0"||strSymbol=="1"||strSymbol=="2"||strSymbol=="3"||strSymbol=="4"||strSymbol=="5"||strSymbol=="6"||strSymbol=="7"||strSymbol=="8"||strSymbol=="9")  {
					 currentLength--;
				}	else return;   */
				while(currentLength!=0) {
				      strSymbol=str.substring(currentLength - 1, currentLength);
				      if(strSymbol.indexOf("+") != -1||strSymbol.indexOf("-") != -1||strSymbol.indexOf("*") != -1||strSymbol.indexOf("/") != -1||strSymbol.indexOf("(") != -1||strSymbol.indexOf(")") != -1)  break;
				      else     currentLength--;
			          }
				strFormula = str.substring(currentLength, length);//此时已经得到了想运算数字字符串即StrFormula
			    // System.out.println(strFormula+"ahaha");
			    Double double1 = Double.parseDouble(strFormula);  // 将字符串转换为double	    
			    switch(symbol) {
			    case "%":
			    	 strInterrupt.push(String.valueOf(double1*0.01));
			    	// System.out.println(strInterrupt);
			    	 break;
			    case "10^-1":
			    	strInterrupt.push(String.valueOf(1.0/double1));
			    	break;
			    case "^2":
			    	strInterrupt.push(String.valueOf(double1*double1));
			    	break;
			    case "^3":
			    	strInterrupt.push(String.valueOf(double1*double1*double1));
			    	break;
			    case "!":
			    	if(strFormula.indexOf(".")!=-1) {
			    		strInterrupt.push(String.valueOf(gamma(double1+1)));
			    	}
			    	else{
			    		 int int1=Integer.parseInt(strFormula);
			    		 System.out.println(strFormula+int1);
			    		 int int2=int1;
				    	 while(int1>1) {
				    		 int2=int2*(int1-1);
				    		 int1--;
				    	 }
				    	 System.out.println(int2);
				    	 strInterrupt.push(Integer.toString(int2));
				    	 System.out.println(strInterrupt.peek());
			    	}
			    	break;
			    default:
			    	break;
			    }   
			    length = trueFormula.length();
			    currentLength=length;
			    while(currentLength!=0) {
				      strSymbol=trueFormula.substring(currentLength - 1, currentLength);
				      if(strSymbol.indexOf("+") != -1||strSymbol.indexOf("-") != -1||strSymbol.indexOf("*") != -1||strSymbol.indexOf("/") != -1)  break;
				      else     currentLength--;
			          }
			    trueFormula=trueFormula.substring(0, currentLength)+(String)strInterrupt.pop();
			   // System.out.println(trueFormula+"end");
			   // System.out.println(str+symbol);
			    jTextField.setText(str+symbol);
			}else {
				return;
			}
		}else if(strSymbol.equals("+")||strSymbol.equals("-")||strSymbol.equals("*")||strSymbol.equals("/")) {
			if(symbol.equals("(")||symbol.equals("sin")||symbol.equals("cos")||symbol.equals("tan")||symbol.equals("ln")||symbol.equals("e^")||symbol.equals("10^")||symbol.equals("√")) {
				if(symbol.equals("(")) {
				str=str+symbol;
				}else {
				str=str+symbol+"(";
				}
				switch(symbol) {
				case "(" :
					intInterrupt.push(0);
					break;
				case "sin":
					intInterrupt.push(1);
					break;
				case "cos":
					intInterrupt.push(2);
					break;
				case "tan":
					intInterrupt.push(3);
					break;
				case "ln":
					intInterrupt.push(4);
					break;
				case "e^":
					intInterrupt.push(5);
					break;
				case "10^":
					intInterrupt.push(6);
					break;
				case "√":
					intInterrupt.push(7);
					break;
			    default:
			    	break;
				}	
				jTextField.setText(str);	
			}else {
				return;
			}
		}else {
			return;
		}
	}
	
	public void deleteKeyLogic(JTextField jTextField) {
		String string = jTextField.getText();
		if (string.indexOf("=") != -1) {          
			jTextField.setText("0");
			trueFormula="";
			jTextField.requestFocus();             /*把输入焦点放在调用这个方法的控件上*/ 
			return;
			}
		int length = string.length();
		if (length > 0) {
			if(string.substring(length-1,length).equals(")")) {
				int currentLength=length;   
				while(currentLength!=0) {
				    	  String strSymbol=string.substring(currentLength - 1, currentLength);
					      if(strSymbol.indexOf("+") != -1||strSymbol.indexOf("-") != -1||strSymbol.indexOf("*") != -1||strSymbol.indexOf("/") != -1)  break;
					      else     currentLength--;
				          }
				jTextField.setText(string.substring(0,currentLength));
				if(strInterrupt.isEmpty()) {
					length = trueFormula.length();
					currentLength=length;    
				    while(currentLength!=0) {
				    	  String strSymbol=trueFormula.substring(currentLength - 1, currentLength);
					      if(strSymbol.indexOf("+") != -1||strSymbol.indexOf("-") != -1||strSymbol.indexOf("*") != -1||strSymbol.indexOf("/") != -1)  break;
					      else     currentLength--;
				          }
				    trueFormula=trueFormula.substring(0, currentLength);
				}else {
					String deleteString=(String)strInterrupt.pop();
					length =deleteString.length();
					currentLength=length;    
				    while(currentLength!=0) {
				    	  String strSymbol=deleteString.substring(currentLength - 1, currentLength);
					      if(strSymbol.indexOf("+") != -1||strSymbol.indexOf("-") != -1||strSymbol.indexOf("*") != -1||strSymbol.indexOf("/") != -1)  break;
					      else     currentLength--;
				          }
				    deleteString=deleteString.substring(0, currentLength);
				    strInterrupt.push((String)deleteString);
				}				
			    return;			    
			}
			string = string.substring(0, length - 1);
			jTextField.setText(string);
		   // System.out.println(string+"delete");
			}
		else {
			jTextField.setText(string);
			return;
		}
		if(intInterrupt.isEmpty()) {
			length=trueFormula.length();
			if (length > 0) {
				trueFormula = trueFormula.substring(0, length - 1);
			    // System.out.println(trueFormula +"        delete");
				}
			else
				return;
		}else {
			if(strInterrupt.isEmpty()) {
				intInterrupt.pop();
				length=string.length();
				int currentLength=length;   
				while(currentLength!=0) {
				    	  String strSymbol=string.substring(currentLength - 1, currentLength);
					      if(strSymbol.indexOf("+") != -1||strSymbol.indexOf("-") != -1||strSymbol.indexOf("*") != -1||strSymbol.indexOf("/") != -1)  break;
					      else     currentLength--;
				          }
				length = trueFormula.length();
				currentLength=length;    
			    while(currentLength!=0) {
			    	  String strSymbol=trueFormula.substring(currentLength - 1, currentLength);
				      if(strSymbol.indexOf("+") != -1||strSymbol.indexOf("-") != -1||strSymbol.indexOf("*") != -1||strSymbol.indexOf("/") != -1)  break;
				      else     currentLength--;
			          }
			    trueFormula=trueFormula.substring(0, currentLength);
				jTextField.setText(string.substring(0,currentLength));
			}else {
				String  stringInter=(String)strInterrupt.pop();
				length=(stringInter).length();
				if(length==1){ 
					return;
				}else {
					strInterrupt.push((String)stringInter.substring(0,length-1));
				}
			}
		}

	}
	
	/* 构造方法*/
	public Calculator() {
		Container container = getContentPane();                           /* 定义一个顶级容器*/
		setLayout(new GridLayout(2, 1));
		//setLayout(new FlowLayout());
		final JTextField displayScreen = new JTextField();
		//JTextField a123 = new JTextField();
		displayScreen.setHorizontalAlignment(displayScreen.RIGHT);
		displayScreen.setFont(new Font("宋体",Font.BOLD,30));              /*设置字体格式与大小*/
		displayScreen.setEditable(false);                                 /*禁止编辑*/
		
		
		/* 图标的定义声明*/
		Image icon =Toolkit.getDefaultToolkit().getImage("./src/icon/计算器.png");
		ImageIcon icon_point=new ImageIcon("./src/icon/小数点.png");
		ImageIcon icon_equal=new ImageIcon("./src/icon/等于.png");
		ImageIcon icon_root=new ImageIcon("./src/icon/平方.png");
		ImageIcon icon_sqrt =new ImageIcon("./src/icon/根号.png");
		ImageIcon icon_division =new ImageIcon("./src/icon/除号.png");
		ImageIcon icon_plus =new ImageIcon("./src/icon/加号.png");
		ImageIcon icon_minus =new ImageIcon("./src/icon/减号.png");
		ImageIcon icon_multiplication=new ImageIcon("./src/icon/乘号.png");
		ImageIcon icon_delete=new ImageIcon("./src/icon/删除.png");
		ImageIcon icon_button1=new ImageIcon("./src/icon/1.png");
		ImageIcon icon_button2=new ImageIcon("./src/icon/2.png");
		ImageIcon icon_button3=new ImageIcon("./src/icon/3.png");
		ImageIcon icon_button4=new ImageIcon("./src/icon/4.png");
		ImageIcon icon_button5=new ImageIcon("./src/icon/5.png");
		ImageIcon icon_button6=new ImageIcon("./src/icon/6.png");
		ImageIcon icon_button7=new ImageIcon("./src/icon/7.png");
		ImageIcon icon_button8=new ImageIcon("./src/icon/8.png");
		ImageIcon icon_button9=new ImageIcon("./src/icon/9.png");
		ImageIcon icon_button0=new ImageIcon("./src/icon/0.png");
		
		
		/* 组件的定义*/
		JButton jButton0 = new JButton(icon_button0);
		JButton jButton1 = new JButton(icon_button1);
		JButton jButton2 = new JButton(icon_button2);
		JButton jButton3 = new JButton(icon_button3);
		JButton jButton4 = new JButton(icon_button4);
		JButton jButton5 = new JButton(icon_button5);
		JButton jButton6 = new JButton(icon_button6);
		JButton jButton7 = new JButton(icon_button7);
		JButton jButton8 = new JButton(icon_button8);
		JButton jButton9 = new JButton(icon_button9);
		JButton point = new JButton(icon_point);
		JButton equal = new JButton(icon_equal);
		JButton plus = new JButton(icon_plus);
		JButton minus = new JButton(icon_minus);
		JButton multiplication = new JButton(icon_multiplication);
		JButton division = new JButton(icon_division);
		JButton sqrt = new JButton(icon_sqrt);
		JButton root = new JButton(icon_root);
		JButton delete = new JButton(icon_delete);
		JButton AC = new JButton("AC");
		JButton percent=new JButton("%");
		 AC.setForeground(new Color(18, 150, 219));       /* 设置显示字体颜色*/
		
		JPanel jPanel = new JPanel();
		/*3,3为水平垂直间距*/
		jPanel.setLayout(new GridLayout(6,4, 4, 4)); 
		
		
		/*第一排键位*/
		jPanel.add(AC);
		jPanel.add(delete);
		jPanel.add(root);
		jPanel.add(sqrt);
		/*第二排键位*/
		jPanel.add(jButton7);
		jPanel.add(jButton8);
		jPanel.add(jButton9);
		jPanel.add(plus);
		/*第三排键位*/
		jPanel.add(jButton4);
		jPanel.add(jButton5);
		jPanel.add(jButton6);
		jPanel.add(minus);
		/*第四排键位*/
		jPanel.add(jButton1);
		jPanel.add(jButton2);
		jPanel.add(jButton3);
		jPanel.add(division);
		/*第五排键位*/
		jPanel.add(multiplication);
		jPanel.add(jButton0);
		jPanel.add(point);
		GridBagConstraints gbs = new GridBagConstraints();
		gbs.fill=GridBagConstraints.BOTH;
		gbs.gridwidth=1;    gbs.gridheight=2;
		jPanel.add(equal,gbs);
		
		jPanel.add(percent);
		/* 将组件放入容器中*/
		displayScreen.setSize(100,500);
        jPanel.setSize(100,500);
		container.add(displayScreen,"North");
		//container.add(a123,"North");
		container.add(jPanel);
		setSize(400,500);                                     /*设置窗口大小*/
		setVisible(true);                                     /*保证窗口刷新*/
		setTitle("计算器");                                    /*设置标题*/
        setIconImage(icon);                                   /*   角标设置*/
		setLocationRelativeTo(null);                          /* 界面居中*/
		//setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);              /*关闭按钮能够关闭*/
		
		
		/*计算器功能实现*/ 
		displayScreen.setText("0");                  /*初始化显示0*/
		
		
		
		jButton0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				numberKeyLogic(displayScreen,"0");
			}
		});	
		jButton1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				numberKeyLogic(displayScreen,"1");
			}
		});	
		jButton2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				numberKeyLogic(displayScreen,"2");
			}
		});
		jButton3.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				numberKeyLogic(displayScreen,"3");
			}
		});
		jButton4.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				numberKeyLogic(displayScreen,"4");
			}
		});
		jButton5.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				numberKeyLogic(displayScreen,"5");
			}
		});
		jButton6.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				numberKeyLogic(displayScreen,"6");
			}
		});
		jButton7.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				numberKeyLogic(displayScreen,"7");
			}
		});
		jButton8.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				numberKeyLogic(displayScreen,"8");
			}
		});
		jButton9.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				numberKeyLogic(displayScreen,"9");
			}
		});
		

		point.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				operationKeyLogic(displayScreen,".");
			}
		});
		plus.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				operationKeyLogic(displayScreen,"+");
			}
		});
		minus.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				operationKeyLogic(displayScreen,"-");
			}
		});
		multiplication.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				operationKeyLogic(displayScreen,"*");
			}
		});
		division.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				operationKeyLogic(displayScreen,"/");
			}
		});
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				deleteKeyLogic(displayScreen);
			}
		});

		AC.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				displayScreen.setText("0");
				strInterrupt.clear();
            	intInterrupt.clear();
            	trueFormula="";
			}
		});
		
		percent.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					interruptKeyLogic(displayScreen,"ln");
				} catch (ScriptException e) {
					// TODO Auto-generated catch block
					 e.printStackTrace();
					 displayScreen.setText("错误");
				}
			}
		
		});
		root.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
			//	String string = displayScreen.getText();
				/*if (displayScreen.getText().indexOf("=") != -1) {          
					displayScreen.setText("0");
					displayScreen.requestFocus();             //把输入焦点放在调用这个方法的控件上
					return;
				} */
				try {
					interruptKeyLogic(displayScreen,"ln");
				} catch (ScriptException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		
				//squareKeyLogic(displayScreen,"root");
			/*	Double double1 = Double.parseDouble(string);// 将字符串转换为double
				Double double2 = double1 * double1;
				String string2 = string.valueOf(double2);// 将double转换为string
				displayScreen.setText(string2);*/
			}
		});	
		sqrt.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//String string = displayScreen.getText();
				/*if (displayScreen.getText().indexOf("=") != -1) {          
					displayScreen.setText("0");
					displayScreen.requestFocus();             //把输入焦点放在调用这个方法的控件上
					return;
					}*/
				squareKeyLogic(displayScreen,"sqrt");
				/*Double double1 = Double.parseDouble(string);// 将字符串转换为double
				Double double2 = (Double) Math.sqrt(double1);
				String string2 = string.valueOf(double2);// 将double转换为string
				displayScreen.setText(string2);*/
			}
		});
		

		equal.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				 try {  
	                    // 获取文本框内容
	                    String formula = displayScreen.getText();
	                    // 计算公式
	                    if(formula.isEmpty()) {                    	
	                    	numberKeyLogic(displayScreen,"0");
	                    	return;
	                    }
	                    if(displayScreen.getText().indexOf("=") != -1) {
	                    	return;
	                    }else {
	                    	//System.out.println("1122141");
	                    	//System.out.println(strInterrupt.isEmpty());
	                    	while(!intInterrupt.isEmpty()) {
	                    		//System.out.println(strInterrupt.peek()+"1122141");
	                    		interruptKeyLogic(displayScreen,")");
	                    		
	                    		formula=formula+")";
	                    		//System.out.println(formula+"========");
	                    	}
	                    	
	                    	String results = jse.eval(trueFormula).toString();	                    	
	                    	//System.out.println(trueFormula);
	                    	// 如果结果字符串过长，只显示结果
	                    	if((formula.length()+results.length())>=20) {
	                    	displayScreen.setText("="+results);
	                    	}
	                    	else {
	                    	displayScreen.setText(formula+"="+results);
	                    	}
	                    	//displayScreen.setText(formula);
	                    	strInterrupt.clear();
	                    	intInterrupt.clear();
	                    	trueFormula="";
	                    }       	                  
	               //如果出错
	               } catch (Exception t) {  
	            	   displayScreen.setText("错误");
	            	   strInterrupt.clear();
                   	   intInterrupt.clear();
                   	   //trueFormula="";
	               }          
                  
			}
		});
	}

}


