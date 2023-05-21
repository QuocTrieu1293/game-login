package application;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaErrorEvent;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class introVidController implements Initializable {

	@FXML
	private MediaView mediaView;

	@FXML
	private Label skipIntro;

	@FXML
	private ImageView backgroundGIF;

	@FXML
	private ImageView loadingGIF;
	
	final Media media = new Media(Paths.get("./src/asset/introVid.mp4").toUri().toString());
	MediaPlayer player;
	
	Task<AnchorPane> loadingLoginTask = new Task<AnchorPane>() {
		
		@Override
		protected AnchorPane call() throws Exception {
			AnchorPane root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
			return root;
		}
	}; 

	private void initPlayer() {
		player = new MediaPlayer(media);
		mediaView.setMediaPlayer(player);
		player.setAutoPlay(true);
		player.setOnPlaying(new Runnable() {

			@Override
			public void run() {
				skipIntro.setVisible(true);
				skipIntro.requestFocus();
			}
		});
		player.currentTimeProperty().addListener((observableVal, oldVal, newVal) -> {
			
			if (newVal.greaterThanOrEqualTo(Duration.seconds(24))) {
				skipIntro.setVisible(false);
			}
			if (newVal.greaterThanOrEqualTo(Duration.seconds(29))) {
				Thread loadingThread = new Thread(loadingLoginTask);
				loadingThread.setDaemon(true);
				loadingThread.start();
			}
		});
		player.setOnError(()->{
			System.out.println(player.getError().toString() + 
					"\nType: " + player.getError().getType());
			player.dispose();
			initPlayer();
		});
		
	};
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		loadingGIF.setVisible(false);
		backgroundGIF.setVisible(false); backgroundGIF.setImage(new Image("/asset/loginGIF.gif"));
		skipIntro.setVisible(false);
		
		loadingLoginTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			
			@Override
			public void handle(WorkerStateEvent arg0) {
				AnchorPane root = loadingLoginTask.getValue();
				Scene scene = skipIntro.getScene();
				double fadeSeconds = 0.2;
				
				FadeTransition fadeTransition = new FadeTransition(Duration.seconds(fadeSeconds));
				fadeTransition.setFromValue(1); fadeTransition.setToValue(0.25);
				fadeTransition.setNode(loadingGIF);
				
				FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(fadeSeconds));
				fadeTransition1.setFromValue(1); fadeTransition1.setToValue(0.25);
				fadeTransition1.setNode(backgroundGIF);
				fadeTransition.play(); fadeTransition1.play();
				
				fadeTransition.statusProperty().addListener((observableVal,oldVal,newVal)->{
					if(newVal.equals(Animation.Status.STOPPED) && 
						fadeTransition1.getStatus().equals(Animation.Status.STOPPED)) {
						scene.setRoot(root);
					}
				});
				
				fadeTransition1.statusProperty().addListener((observableVal,oldVal,newVal)->{
					if(newVal.equals(Animation.Status.STOPPED) && 
						fadeTransition.getStatus().equals(Animation.Status.STOPPED)) {
						scene.setRoot(root);
					}
				});
				
			}
		});
		
		loadingLoginTask.setOnScheduled(new EventHandler<WorkerStateEvent>() {
			
			@Override
			public void handle(WorkerStateEvent arg0) {
				backgroundGIF.setVisible(true);
				loadingGIF.setVisible(true);
				mediaView.setVisible(false);
			}
		});
		
		initPlayer();
		
		mediaView.setSmooth(true);
		
		FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.5), mediaView);
		fadeIn.setFromValue(0);
		fadeIn.setToValue(1);
		skipIntro.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				if (event.getCode() == KeyCode.SPACE) {
					player.seek(Duration.seconds(24));
					fadeIn.play();
				}
			}

		});
		skipIntro.focusedProperty().addListener((observableVal, oldVal, newVal) -> {
			if (!newVal) {
				skipIntro.requestFocus();
			}
		});
		BoxBlur boxBlur = new BoxBlur(2, 2, 1);
		skipIntro.setEffect(boxBlur);
		KeyValue keyVal = new KeyValue(skipIntro.textFillProperty(), Paint.valueOf("#001E6C"));
		KeyFrame keyFrame = new KeyFrame(Duration.seconds(1.2), keyVal);
		Timeline timeLine = new Timeline(keyFrame);
		timeLine.setAutoReverse(true);
		timeLine.setCycleCount(Timeline.INDEFINITE);
		timeLine.play();
		
	}
}