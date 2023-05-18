package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;

import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.css.StyleClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.StageStyle;

public class loginController implements Initializable {

//	username and password
	public static Map<String,String> accounts = new HashMap<>();
	public static Map<String,Integer> accounts_gold = new HashMap<>(); 
	final String FILE_PATH = "src/data/accountData";
	final String USERNAME = "Username"; final String PASSWORD = "Password";
	final String GOLD = "Gold"; final Integer INIT_GOLD = 1000; 
	
//	------------------------------------------------------
	@FXML
	private AnchorPane loginRoot;

	@FXML
    private ImageView gifBKG;
 
//	login form--------------------------------------------
    @FXML
    private AnchorPane loginForm;
    
    @FXML
    private JFXButton loginSignUpButton;
    
    @FXML
    private TextField loginUserName;

    @FXML
    private TextField loginShowPassWord;
    
    @FXML
    private PasswordField loginHidePassWord;
    
    @FXML
    private HBox passWordHBox;
    
    @FXML 
    private ImageView passWordImageView;
    boolean isShowPassWord = false;
    
    @FXML
    private Label loginErr;
    
    @FXML
    private JFXButton loginEnterButton;
    
    @FXML
    private Hyperlink loginForgotPassWord;
    
//  forgot password form------------------------------------------
    @FXML
    private AnchorPane forgotPassWordForm;
    
    @FXML
    private JFXButton forgotPassWordBackButton;
    
    @FXML
    private TextField forgotPassWordUserName;
    
    @FXML
    private PasswordField forgotPassWordNew;
    
    @FXML
    private PasswordField forgotPassWordConfirm;
    
    @FXML
    private Label forgotPassWordErr;
    
    @FXML
    private JFXButton forgotPassWordEnterButton;
    
    final String userNotExistErr = "Username does not exist";

//  sign up form--------------------------------------------------
    @FXML
    private AnchorPane signUpForm;
    
    @FXML
    private JFXButton signUpLoginButton;
    
    @FXML
    private TextField signUpUserName;
    
    @FXML
    private PasswordField signUpPassWord;
    
    @FXML
    private PasswordField signUpConfirmPassWord;
    
    @FXML
    private Label signUpErr;
    
    @FXML
    private JFXButton signUpEnterButton;
    
    final String patternErr = "Must not have \'\\\', \'/\', space and not be empty";
    final String userExistErr = "Username already exists";
    final String confirmErr = "The password confirmation does not match";
    
//------------------------------------------------------------- 
    
    EventHandler<KeyEvent> enterHandlerTextField = (event)->{
    	if(event.getCode() == KeyCode.ENTER)
    		loginRoot.requestFocus();
    };
    
    public void exitFocused(MouseEvent event) {
    	if(event.getButton() == MouseButton.PRIMARY)
    		loginRoot.requestFocus();
    }
        
    private ChangeListener<Boolean> focusPassWordHBox = (observableVal,oldVal,newVal)->{
    	if(loginErr.isVisible()) {
    		loginErr.setVisible(false);
    		loginUserName.getStyleClass().remove("login-err");
    	}
    	if(newVal) {
			passWordHBox.setStyle("	-fx-background-color: rgba(0, 0, 0, 0.35);\r\n"
					+ "	-fx-text-fill: rgb(228, 220, 207);\r\n"
					+ "	-fx-border-color: linear-gradient(to bottom, #b3ffab, #12fff7);\r\n"
					+ "	-fx-border-width: 2 2 2 2;\r\n"
					+ "	-fx-border-radius: 3;\r\n"
					+ "	-fx-background-radius: 3;\r\n"
					+ "	-fx-prompt-text-fill: rgba(228, 220, 207, 0.65);\r\n"
					+ "	-fx-effect: dropshadow(three-pass-box,rgba(0, 255, 198, 0.35),10,0.85,0,0);");
			if(isShowPassWord)
				passWordImageView.setImage(new Image("/img/lightEye.png"));
			else 
				passWordImageView.setImage(new Image("/img/lightCloseEye.png"));
		}else {
			passWordHBox.setStyle("	-fx-background-color: transparent;\r\n"
					+ "	-fx-border-width: 0 0 2 0;\r\n"
					+ "	-fx-border-color: #ff1a5f;\r\n"
					+ "	-fx-font-size: 15px;\r\n"
					+ "	-fx-font-family: \"Arial\";\r\n"
					+ "	-fx-font-weight: bold;\r\n"
					+ "	-fx-cursor: text;\r\n"
					+ "	-fx-prompt-text-fill: rgba(69, 69, 69, 0.65);\r\n"
					+ "	-fx-text-fill: #181823;\r\n"
					+ "	-fx-border-radius: 3 3 0 0;\r\n"
					+ "	-fx-background-radius: 3 3 0 0;");
			if(isShowPassWord)
				passWordImageView.setImage(new Image("/img/darkEye.png"));
			else 
				passWordImageView.setImage(new Image("/img/darkCloseEye.png"));
			
			if(loginShowPassWord.isHover() || loginHidePassWord.isHover()) {
				passWordHBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.35);"
						+ "	-fx-border-width: 0 0 2 0;\r\n"
						+ "	-fx-border-color: #ff1a5f;\r\n"
						+ "	-fx-font-size: 15px;\r\n"
						+ "	-fx-font-family: \"Arial\";\r\n"
						+ "	-fx-font-weight: bold;\r\n"
						+ "	-fx-cursor: text;\r\n"
						+ "	-fx-prompt-text-fill: rgba(69, 69, 69, 0.65);\r\n"
						+ "	-fx-text-fill: #181823;\r\n"
						+ "	-fx-border-radius: 3 3 0 0;\r\n"
						+ "	-fx-background-radius: 3 3 0 0;");
				if(isShowPassWord)
					passWordImageView.setImage(new Image("/img/lightEye.png"));
				else 
					passWordImageView.setImage(new Image("/img/lightCloseEye.png"));
			}
			if(!loginHidePassWord.isFocused() && !loginShowPassWord.isFocused()) {
				if(!loginEnterButton.isDisabled())
					loginEnterButton.requestFocus();
			}
		}
    };
    
    private ChangeListener<Boolean> hoverPassWordHBox = (observableVal,oldVal,newVal) -> {
    	if(loginShowPassWord.isFocused() || loginHidePassWord.isFocused()) return;
    	if(loginErr.isVisible()) {
    		loginUserName.getStyleClass().remove("login-err");
    		loginErr.setVisible(false);
    	}
    	if(newVal) {
    		passWordHBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.35);"
					+ "	-fx-border-width: 0 0 2 0;\r\n"
					+ "	-fx-border-color: #ff1a5f;\r\n"
					+ "	-fx-font-size: 15px;\r\n"
					+ "	-fx-font-family: \"Arial\";\r\n"
					+ "	-fx-font-weight: bold;\r\n"
					+ "	-fx-cursor: text;\r\n"
					+ "	-fx-prompt-text-fill: rgba(69, 69, 69, 0.65);\r\n"
					+ "	-fx-text-fill: #181823;\r\n"
					+ "	-fx-border-radius: 3 3 0 0;\r\n"
					+ "	-fx-background-radius: 3 3 0 0;");
			if(isShowPassWord)
				passWordImageView.setImage(new Image("/img/lightEye.png"));
			else 
				passWordImageView.setImage(new Image("/img/lightCloseEye.png"));
    	}else {
			passWordHBox.setStyle("	-fx-background-color: transparent;\r\n"
					+ "	-fx-border-width: 0 0 2 0;\r\n"
					+ "	-fx-border-color: #ff1a5f;\r\n"
					+ "	-fx-font-size: 15px;\r\n"
					+ "	-fx-font-family: \"Arial\";\r\n"
					+ "	-fx-font-weight: bold;\r\n"
					+ "	-fx-cursor: text;\r\n"
					+ "	-fx-prompt-text-fill: rgba(69, 69, 69, 0.65);\r\n"
					+ "	-fx-text-fill: #181823;\r\n"
					+ "	-fx-border-radius: 3 3 0 0;\r\n"
					+ "	-fx-background-radius: 3 3 0 0;");
			if(isShowPassWord)
				passWordImageView.setImage(new Image("/img/darkEye.png"));
			else 
				passWordImageView.setImage(new Image("/img/darkCloseEye.png"));
    	}
    };
    
    public void clearLoginForm() {
		loginUserName.clear();
		loginHidePassWord.clear();
		loginHidePassWord.setVisible(true);
		loginShowPassWord.setVisible(false);
		isShowPassWord = false;
		passWordImageView.setImage(new Image("/img/darkCloseEye.png"));
		if(loginErr.isVisible()) {
			loginErr.setVisible(false);
			passWordHBox.setStyle("-fx-background-color: transparent;");
			loginUserName.getStyleClass().remove("login-err");
		}
    }
    
    public void clearSignUpForm() {
    	signUpUserName.clear(); signUpPassWord.clear(); signUpConfirmPassWord.clear();
    	signUpErr.setText("");
    	signUpPassWord.setDisable(true);
    	signUpUserName.getStyleClass().remove("login-err");
    	signUpPassWord.getStyleClass().remove("login-err");
    	signUpConfirmPassWord.getStyleClass().remove("login-err");
    }
	
    public void clearForgotPassWordForm() {
    	forgotPassWordUserName.clear();
		forgotPassWordNew.clear();
		forgotPassWordConfirm.clear();
		forgotPassWordUserName.getStyleClass().remove("login-err");
		forgotPassWordNew.getStyleClass().remove("login-err");
		forgotPassWordConfirm.getStyleClass().remove("login-err");
		forgotPassWordErr.setText("");
		forgotPassWordNew.setDisable(true);
    }
    
    public void switchForm(ActionEvent event) {
    	Node node = (Node)event.getSource();
    	if(node == loginSignUpButton) {
    		clearLoginForm();
    		loginForm.setVisible(false);
    		signUpForm.setVisible(true);
    		signUpUserName.requestFocus();
    	}else if(node == loginForgotPassWord) {
    		forgotPassWordUserName.setText(loginUserName.getText());
    		clearLoginForm();
    		loginForm.setVisible(false);
    		forgotPassWordForm.setVisible(true);
    		forgotPassWordUserName.requestFocus();
    		forgotPassWordUserName.selectEnd();
    	}else if(node == signUpLoginButton) {
    		clearSignUpForm();
    		signUpForm.setVisible(false);
    		loginForm.setVisible(true);
    		loginRoot.requestFocus();
    	}else if(node == forgotPassWordBackButton) {
    		clearForgotPassWordForm();
    		forgotPassWordForm.setVisible(false);
    		loginForm.setVisible(true);
    		loginRoot.requestFocus();
    	}
    }
    
	public void getAccountData() {
		try {
			BufferedReader br = Files.newBufferedReader(Paths.get(FILE_PATH));
			Object[] line = br.lines().toArray();
			String username = ""; String password = "";
			Integer gold = -1;
			for(Object i : line) {
				String[] read = i.toString().split(": ");
				if(read[0].equals(USERNAME)) {
					username = read[1];
				}else if(read[0].equals(PASSWORD)) {
					password = read[1];
				}else if(read[0].equals(GOLD)) {
					gold = Integer.parseInt(read[1]);
				}
				if(!username.equals("") && !password.equals("") && gold != -1) {
					accounts.put(username, password);
					accounts_gold.put(username, gold);
					username = ""; password = ""; gold = -1;
				}
			}
			br.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
    
    public void loginAction(ActionEvent event) throws IOException {
    	String username = loginUserName.getText();
    	String password = loginShowPassWord.getText();
    	if(accounts.containsKey(username) && accounts.get(username).equals(password)) {
    		Scene scene = ((Node)event.getSource()).getScene();
    		Parent root = FXMLLoader.load(getClass().getResource("/fxml/gameMenu.fxml"));
    		scene.setRoot(root);
    	}else {
    		loginErr.setVisible(true);
    		loginShowPassWord.clear();
    		loginShowPassWord.setVisible(false);
    		loginHidePassWord.setVisible(true);
    		isShowPassWord = false;
    		passWordImageView.setImage(new Image("/img/darkCloseEye.png"));
    		loginUserName.getStyleClass().add("login-err");
    		passWordHBox.setStyle("-fx-background-color: rgba(225,18,153,0.5);");
    	}
    }
	
    public void signUpAction(ActionEvent event) {
    	String username = signUpUserName.getText();
    	String password = signUpConfirmPassWord.getText();
    	accounts.put(username, password);
    	try {
    		FileWriter fw = new FileWriter(new File(FILE_PATH), true);
    		fw.write(USERNAME + ": " + username);
    		fw.write(System.getProperty("line.separator"));
    		fw.write(PASSWORD + ": " + password);
    		fw.write(System.getProperty("line.separator"));
    		fw.write(GOLD + ": " + INIT_GOLD);
    		fw.write(System.getProperty("line.separator"));
    		fw.write(System.getProperty("line.separator"));
    		fw.close();
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    	Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("Sign Up");
    	alert.setHeaderText(null);
    	alert.setContentText("You have successfully sign up an account!");
    	alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/alertDialog.css").toExternalForm());
    	alert.showAndWait();
    
		clearSignUpForm();
		signUpForm.setVisible(false);
		loginForm.setVisible(true);
		loginRoot.requestFocus();
    }
    
    public void forgotPassWordAction(ActionEvent event) {
    	String username = forgotPassWordUserName.getText();
    	String newPassword = forgotPassWordConfirm.getText();
    	accounts.put(username, newPassword);
    	try {
    		FileWriter fw = new FileWriter(new File(FILE_PATH));
    		for(Map.Entry<String, String> entry : accounts.entrySet()) {
    			fw.write(USERNAME + ": " + entry.getKey());
    			fw.write(System.getProperty("line.separator"));
    			fw.write(PASSWORD + ": " + entry.getValue());
    			fw.write(System.getProperty("line.separator"));
    			fw.write(GOLD + ": " + accounts_gold.get(entry.getKey()));
    			fw.write(System.getProperty("line.separator"));
    			fw.write(System.getProperty("line.separator"));
    		}
    		fw.close();
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    	Alert alert = new Alert(AlertType.INFORMATION);
    	alert.setTitle("Change password");
    	alert.setHeaderText(null);
    	alert.setContentText("You have successfully change password!");
    	alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/alertDialog.css").toExternalForm());
    	alert.showAndWait();
    
		clearForgotPassWordForm();
		forgotPassWordForm.setVisible(false);
		loginForm.setVisible(true);
		loginRoot.requestFocus();
    }
    
    public boolean isValidPattern(String str) {
    	if(str.contains(" ") || str.contains("\\") || str.contains("/") || str.equals(""))
    		return false;
    	return true;
    }
    
    public void requestError(Node node) {
    	Pane parent = (Pane)node.getParent();
    	ObservableList<Node> childrenList = parent.getChildren();
    	for(Node i : childrenList) {
    		if(i.getStyleClass().remove("login-err"))
    			break;
    	}
    	node.getStyleClass().add("login-err");
    }
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		loginRoot.requestFocus();
		gifBKG.setImage(new Image("/img/loginGIF.gif"));
		
//		login form------------------------------------------------------------------------------------
		loginEnterButton.getStyleClass().add("enter-button");
		loginUserName.addEventHandler(KeyEvent.KEY_PRESSED, enterHandlerTextField);
		loginHidePassWord.addEventHandler(KeyEvent.KEY_PRESSED, enterHandlerTextField);
		loginShowPassWord.addEventHandler(KeyEvent.KEY_PRESSED, enterHandlerTextField);		
		loginUserName.textProperty().addListener((observableVal,oldVal,newVal)->{
			if(newVal.length() > 0 && loginHidePassWord.getText().length() > 0)
				loginEnterButton.setDisable(false);
			else 
				loginEnterButton.setDisable(true);
		});
		loginUserName.hoverProperty().addListener((observableVal,oldVal,newVal)->{
			if(loginErr.isVisible()) {
				passWordHBox.setStyle("-fx-background-color: transparent");
				loginUserName.getStyleClass().remove("login-err");
				loginErr.setVisible(false);
			}
		});
		loginUserName.focusedProperty().addListener((observableVal,oldVal,newVal)->{
			if(loginErr.isVisible()) {
				passWordHBox.setStyle("-fx-background-color: transparent");
				loginUserName.getStyleClass().remove("login-err");
				loginErr.setVisible(false);
			}
			if(!newVal) {
				if(!loginEnterButton.isDisabled()) {
					loginEnterButton.requestFocus();
				}else if(isShowPassWord)
					loginShowPassWord.requestFocus();
				else
					loginHidePassWord.requestFocus();
			}
		});
		
		loginShowPassWord.focusedProperty().addListener(focusPassWordHBox);
		loginShowPassWord.hoverProperty().addListener(hoverPassWordHBox);
		loginHidePassWord.focusedProperty().addListener(focusPassWordHBox);
		loginHidePassWord.hoverProperty().addListener(hoverPassWordHBox);
		loginHidePassWord.textProperty().addListener((observableVal,oldVal,newVal)->{
			loginShowPassWord.setText(newVal);
			if(newVal.length() > 0 && loginUserName.getText().length() > 0)
				loginEnterButton.setDisable(false);
			else 
				loginEnterButton.setDisable(true);
				
		});
		loginShowPassWord.textProperty().addListener((observableVal,oldVal,newVal)->{
			loginHidePassWord.setText(newVal);
		});
		loginShowPassWord.setVisible(false);
		loginHidePassWord.setVisible(true);
		
		passWordImageView.setImage(new Image("/img/darkCloseEye.png"));
		passWordImageView.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> {
			if(event.getButton() != MouseButton.PRIMARY)
				return;
			isShowPassWord = !isShowPassWord;
			loginShowPassWord.setVisible(true);
			loginHidePassWord.setVisible(true);
			if(isShowPassWord) {
				if(loginHidePassWord.isFocused()) {
					passWordImageView.setImage(new Image("/img/lightEye.png"));
					loginShowPassWord.requestFocus();
					loginShowPassWord.selectEnd();
				}else {
					passWordImageView.setImage(new Image("/img/darkEye.png"));
				}
			}else {
				if(loginShowPassWord.isFocused()) {
					passWordImageView.setImage(new Image("/img/lightCloseEye.png"));
					loginHidePassWord.requestFocus();
					loginHidePassWord.selectEnd();
				}else {
					passWordImageView.setImage(new Image("/img/darkCloseEye.png"));
				}
			}
			loginShowPassWord.setVisible(isShowPassWord);
			loginHidePassWord.setVisible(!isShowPassWord);
			event.consume();
		});
		
//		sign up form--------------------------------------------------------------------------------
		signUpEnterButton.getStyleClass().add("enter-button");
		signUpUserName.addEventHandler(KeyEvent.KEY_PRESSED, enterHandlerTextField);
		signUpPassWord.addEventHandler(KeyEvent.KEY_PRESSED, enterHandlerTextField);
		signUpConfirmPassWord.addEventHandler(KeyEvent.KEY_PRESSED, enterHandlerTextField);		
		
		signUpPassWord.setDisable(true); signUpConfirmPassWord.setDisable(true); 
		signUpEnterButton.setDisable(true); signUpErr.setText("");
		
		signUpPassWord.disabledProperty().addListener((obervableVal,oldVal,newVal)->{
			if(newVal) {
				signUpConfirmPassWord.setDisable(true);
			}else {
				String password = signUpPassWord.getText();
				if(password.equals("")) 
					signUpPassWord.requestFocus();
				else if(!isValidPattern(password)) {
					signUpErr.setText(patternErr);
					requestError(signUpPassWord);
					signUpConfirmPassWord.setDisable(true);
				}else {
					signUpConfirmPassWord.setDisable(false);
				}
			}
		});
		signUpConfirmPassWord.disabledProperty().addListener((observableVal,oldVal,newVal)->{
			if(newVal) {
				signUpEnterButton.setDisable(true);
			}else {
				String conFirmPassWord = signUpConfirmPassWord.getText();
				if(conFirmPassWord.equals("")) 
					signUpConfirmPassWord.requestFocus();
				else if(!conFirmPassWord.equals(signUpPassWord.getText())) {
					signUpErr.setText(confirmErr);
					requestError(signUpConfirmPassWord);
					signUpEnterButton.setDisable(true);
				}else {
					signUpEnterButton.setDisable(false);
					signUpEnterButton.requestFocus();
				}
			}
		});
		signUpUserName.focusedProperty().addListener((observableVal,oldVal,newVal)->{
			if(!newVal) {
				String username = signUpUserName.getText();
				if(!isValidPattern(username)) {
					signUpErr.setText(patternErr);
					requestError(signUpUserName);
					signUpPassWord.setDisable(true);
				}else if(accounts.containsKey(username)) {
					signUpErr.setText(userExistErr);
					requestError(signUpUserName);
					signUpPassWord.setDisable(true);
				}else {
					//username hop le
					if(signUpUserName.getStyleClass().remove("login-err"))
						signUpErr.setText("");
					signUpPassWord.setDisable(false);
				}
			}
		});
		signUpPassWord.focusedProperty().addListener((observableVal,oldVal,newVal)->{
			if(!newVal) {
				String password = signUpPassWord.getText();
				if(!isValidPattern(password)) {
					signUpErr.setText(patternErr);
					requestError(signUpPassWord);
					signUpConfirmPassWord.setDisable(true);
				}else {
					//password hop le
					if(signUpPassWord.getStyleClass().remove("login-err")) {
						signUpErr.setText("");
					}
					if(signUpConfirmPassWord.isDisabled()) {
						signUpConfirmPassWord.setDisable(false);
					}else {
						String confirmPassWord = signUpConfirmPassWord.getText();
						if(!confirmPassWord.equals(password)) {
							signUpErr.setText(confirmErr);
							requestError(signUpConfirmPassWord);
							signUpEnterButton.setDisable(true);
						}else {
							signUpErr.setText("");
							signUpConfirmPassWord.getStyleClass().remove("login-err");
							signUpEnterButton.setDisable(false);
							signUpEnterButton.requestFocus();
						}
					}
				}
			}
		});
		signUpConfirmPassWord.focusedProperty().addListener((observableVal,oldVal,newVal)->{
			if(!newVal) {
				String confirmPassWord = signUpConfirmPassWord.getText();
				if(!confirmPassWord.equals(signUpPassWord.getText())) {
					signUpErr.setText(confirmErr);
					requestError(signUpConfirmPassWord);
					signUpEnterButton.setDisable(true);
				}else {
					signUpErr.setText("");
					signUpConfirmPassWord.getStyleClass().remove("login-err");
					signUpEnterButton.setDisable(false);
					signUpEnterButton.requestFocus();
				}
			}
		});
		
		
//		forgot password form--------------------------------------------------------------------------
		forgotPassWordEnterButton.getStyleClass().add("enter-button");
		forgotPassWordUserName.addEventHandler(KeyEvent.KEY_PRESSED, enterHandlerTextField);
		forgotPassWordNew.addEventHandler(KeyEvent.KEY_PRESSED, enterHandlerTextField);
		forgotPassWordConfirm.addEventHandler(KeyEvent.KEY_PRESSED, enterHandlerTextField);
		
		forgotPassWordNew.setDisable(true); forgotPassWordConfirm.setDisable(true); 
		forgotPassWordEnterButton.setDisable(true); forgotPassWordErr.setText("");
		
		forgotPassWordNew.disabledProperty().addListener((obervableVal,oldVal,newVal)->{
			if(newVal) {
				forgotPassWordConfirm.setDisable(true);
			}else {
				String password = forgotPassWordNew.getText();
				if(password.equals("")) 
					forgotPassWordNew.requestFocus();
				else if(!isValidPattern(password)) {
					forgotPassWordErr.setText(patternErr);
					requestError(forgotPassWordNew);
					forgotPassWordConfirm.setDisable(true);
				}else {
					forgotPassWordConfirm.setDisable(false);
				}
			}
		});
		forgotPassWordConfirm.disabledProperty().addListener((observableVal,oldVal,newVal)->{
			if(newVal) {
				forgotPassWordEnterButton.setDisable(true);
			}else {
				String conFirmPassWord = forgotPassWordConfirm.getText();
				if(conFirmPassWord.equals("")) 
					forgotPassWordConfirm.requestFocus();
				else if(!conFirmPassWord.equals(forgotPassWordNew.getText())) {
					forgotPassWordErr.setText(confirmErr);
					requestError(forgotPassWordConfirm);
					forgotPassWordEnterButton.setDisable(true);
				}else {
					forgotPassWordEnterButton.setDisable(false);
					forgotPassWordEnterButton.requestFocus();
				}
			}
		});
		forgotPassWordUserName.focusedProperty().addListener((observableVal,oldVal,newVal)->{
			if(!newVal) {
				String username = forgotPassWordUserName.getText();
				if(!isValidPattern(username)) {
					forgotPassWordErr.setText(patternErr);
					requestError(forgotPassWordUserName);
					forgotPassWordNew.setDisable(true);
				}else if(!accounts.containsKey(username)) {
					forgotPassWordErr.setText(userNotExistErr);
					requestError(forgotPassWordUserName);
					forgotPassWordNew.setDisable(true);
				}else {
					//username hop le
					if(forgotPassWordUserName.getStyleClass().remove("login-err"))
						forgotPassWordErr.setText("");
					forgotPassWordNew.setDisable(false);
				}
			}
		});
		forgotPassWordNew.focusedProperty().addListener((observableVal,oldVal,newVal)->{
			if(!newVal) {
				String password = forgotPassWordNew.getText();
				if(!isValidPattern(password)) {
					forgotPassWordErr.setText(patternErr);
					requestError(forgotPassWordNew);
					forgotPassWordConfirm.setDisable(true);
				}else {
					//password hop le
					if(forgotPassWordNew.getStyleClass().remove("login-err")) {
						forgotPassWordErr.setText("");
					}
					if(forgotPassWordConfirm.isDisabled()) {
						forgotPassWordConfirm.setDisable(false);
					}else {
						String confirmPassWord = forgotPassWordConfirm.getText();
						if(!confirmPassWord.equals(password)) {
							forgotPassWordErr.setText(confirmErr);
							requestError(forgotPassWordConfirm);
							forgotPassWordEnterButton.setDisable(true);
						}else {
							forgotPassWordErr.setText("");
							forgotPassWordConfirm.getStyleClass().remove("login-err");
							forgotPassWordEnterButton.setDisable(false);
							forgotPassWordEnterButton.requestFocus();
						}
					}
				}
			}
		});
		forgotPassWordConfirm.focusedProperty().addListener((observableVal,oldVal,newVal)->{
			if(!newVal) {
				String confirmPassWord = forgotPassWordConfirm.getText();
				if(!confirmPassWord.equals(forgotPassWordNew.getText())) {
					forgotPassWordErr.setText(confirmErr);
					requestError(forgotPassWordConfirm);
					forgotPassWordEnterButton.setDisable(true);
				}else {
					forgotPassWordErr.setText("");
					forgotPassWordConfirm.getStyleClass().remove("login-err");
					forgotPassWordEnterButton.setDisable(false);
					forgotPassWordEnterButton.requestFocus();
				}
			}
		});
//		username password
		getAccountData();
		
//	
		
		
	
	}
}

