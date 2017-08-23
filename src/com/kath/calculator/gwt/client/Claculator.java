package com.kath.calculator.gwt.client;

import com.kath.calculator.gwt.shared.FieldVerifier;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.thirdparty.guava.common.base.Joiner;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Claculator implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network " + "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);

	private VerticalPanel mainPanel = new VerticalPanel();
	private HorizontalPanel addPanel = new HorizontalPanel();
	
	private HorizontalPanel addPanelnr1 = new HorizontalPanel();
	private HorizontalPanel addPanelnr2 = new HorizontalPanel();
	private HorizontalPanel addPanelnr3 = new HorizontalPanel();
	private HorizontalPanel addPanelnr4 = new HorizontalPanel();
	
	private HorizontalPanel addPanel2 = new HorizontalPanel();
	private TextBox operand1TextBox = new TextBox();
	private TextBox operand2TextBox = new TextBox();
	private Button calculateButton = new Button("Calc");
	private MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
	private SuggestBox operatorTextBox = new SuggestBox(oracle);
	private FlexTable flexTable = new FlexTable();

	Button[] numbers = new Button[10];
	Button[] OPSign = new Button[6];

	public void onModuleLoad() {
		
		flexTable.setText(0, 0, "History table:");
		flexTable.getRowFormatter().addStyleName(0, "HistoryHeader");
		flexTable.addStyleName("watchList");
		
		flexTable.getCellFormatter().addStyleName(0, 0, "watchListHeader");
				
		for (int i = 0; i < numbers.length; i++) {
			numbers[i] = new Button("" + i);
			addPanelnr1.add(numbers[i]);
			if(i == 4 || i == 5 || i ==6 || i == 7) {
			addPanelnr2.add(numbers[i]);	
			}else if(i == 8 || i==9) {
				addPanelnr3.add(numbers[i]);
			}		
			numbers[i].addClickHandler(new NumberClickHandler());	
		}

		OPSign[0] = new Button("+");
		OPSign[1] = new Button("-");
		OPSign[2] = new Button("*");
		OPSign[3] = new Button("/");
		OPSign[4] = new Button("%");
		OPSign[5] = new Button(".");		

		for (int i = 0; i < OPSign.length; i++) {
			addPanel2.add(OPSign[i]);
			if(i == 1 || i == 2) {
				addPanelnr3.add(OPSign[i]);
			}
			 OPSign[i].addClickHandler(new OPSignClickHandeler());
		}
		
		addPanel.addStyleName("TextboxTable");
		addPanelnr1.addStyleName("numberPanel");
		addPanelnr2.addStyleName("numberPanel");
		addPanelnr3.addStyleName("numberPanel");	
		
		addPanel.add(operand1TextBox);
		addPanel.add(operatorTextBox);
		addPanel.add(operand2TextBox);
		addPanel.add(calculateButton);

		mainPanel.add(addPanel);
		mainPanel.add(addPanelnr1);
		mainPanel.add(addPanelnr2);
		mainPanel.add(addPanelnr3);
		mainPanel.add(addPanel2);
		mainPanel.add(flexTable);
		RootPanel.get("calc").add(mainPanel);

		calculateButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				calculate();
			}
		});

		operand2TextBox.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					event.preventDefault();
					calculate();
				}
			}
		});
	}


	private class NumberClickHandler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			  Button clicked = (Button) event.getSource();	
			  if(operand1TextBox.getText().equals("") || isInteger(operand1TextBox.getText()) && operatorTextBox.getText().equals("")) {
			  operand1TextBox.setText(operand1TextBox.getText() + clicked.getText());
			  }
			  else if(!operatorTextBox.getText().equals("")) {
				  operand2TextBox.setText(operand2TextBox.getText() + clicked.getText());
			  }
		}
	}

	private class OPSignClickHandeler implements ClickHandler {
		@Override
		public void onClick(ClickEvent event) {
			Button clicked = (Button) event.getSource();
			operatorTextBox.setText(clicked.getText());
		}

	}

	public void calculate() {

		final String operator = operatorTextBox.getText().trim();
		calculateButton.setFocus(true);
		if ((!operator.equals("-") && !operator.equals("+") && !operator.equals("*") && !operator.equals("/"))
				&& !operator.equals("%") || !isInteger(operand1TextBox.getText().trim())
				|| !isInteger(operand2TextBox.getText().trim())) {
			Window.alert("You have entered a non valid binary operator or one of the operands is not an integer");

			return;
		}

		float operand1 = Float.parseFloat(operand1TextBox.getText());
		float operand2 = Float.parseFloat(operand2TextBox.getText());
		SumList sm = new SumList();
		sm.setTextbox1(operand1);
		sm.setTextbox2(operand2);

		if (operator.equals("+")) {
			double answer = addition(sm);
			sm.setTextboxSum(answer);
			sm.setSign("+");
			addRow(sm);
			Window.alert("The answer is: " + answer);

		} else if (operator.equals("-")) {
			double answer = subtraction(sm);
			sm.setTextboxSum(answer);
			sm.setSign("-");
			addRow(sm);
			Window.alert("The answer is: " + answer);

		} else if (operator.equals("*")) {
			double answer = multiplication(sm);
			sm.setTextboxSum(answer);
			sm.setSign("*");
			addRow(sm);
			Window.alert("The answer is: " + answer);

		} else if (operator.equals("/")) {
			double answer = division(sm);
			sm.setTextboxSum(answer);
			sm.setSign("/");
			addRow(sm);
			Window.alert("The answer is: " + answer);
			
		} else if (operator.equals("%")) {
			double answer =  modulo(sm);
			sm.setTextboxSum(answer);
			sm.setSign("%");
			addRow(sm);
			Window.alert("The answer is: " + answer);
		}

	}

	public void addRow(SumList suml) {
		float operand1 = suml.getTextbox1();
		String op1 = Float.toString(operand1);
		float operand2 = suml.getTextbox2();
		String op2 = Float.toString(operand2);
		double answer = suml.getTextboxSum();
		String ans = Double.toString(answer);
		String sign = suml.getSign();

		int row = flexTable.getRowCount();
		flexTable.setText(row, 1, op1);
		flexTable.setText(row, 2, sign);
		flexTable.setText(row, 3, op2 + " = ");
		flexTable.setText(row, 4, ans);
		
		flexTable.getRowFormatter().addStyleName(row, "historyRows");
		flexTable.getCellFormatter().addStyleName(row, 1, "numericColumn");
		flexTable.getCellFormatter().addStyleName(row, 2, "numericColumnSign");
		flexTable.getCellFormatter().addStyleName(row, 3, "numericColumn");
		flexTable.getCellFormatter().addStyleName(row, 4, "numericColumn");
	}

	public double addition(SumList sumlist) {
		float operand1 = sumlist.getTextbox1();
		float operand2 = sumlist.getTextbox2();
		double answer;
		answer = operand1 + operand2;
		return answer;
	}

	public double subtraction(SumList sumlist) {
		float operand1 = sumlist.getTextbox1();
		float operand2 = sumlist.getTextbox2();
		double answer;
		answer = operand1 - operand2;
		return answer;
	}

	public double multiplication(SumList sumlist) {
		float operand1 = sumlist.getTextbox1();
		float operand2 = sumlist.getTextbox2();
		double answer;
		answer = operand1 * operand2;
		return answer;
	}

	public double division(SumList sumlist) {
		float operand1 = sumlist.getTextbox1();
		float operand2 = sumlist.getTextbox2();
		double answer;
		answer = operand1 / operand2;
		return answer;
	}

	public double modulo(SumList sumlist) {
		float operand1 = sumlist.getTextbox1();
		float operand2 = sumlist.getTextbox2();
		double answer;
		answer = operand1%operand2;
		return answer;
	}
	// Checkes if a String could be seen as an integer
	public boolean isInteger(String input) {
		try {
			Float.parseFloat(input);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
}
