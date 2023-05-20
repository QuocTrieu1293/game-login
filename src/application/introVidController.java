package application;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

public class introVidController implements Initializable {

	@FXML
	private MediaView mediaView;
	@FXML
	private Label skipIntro;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		skipIntro.setVisible(false);
		Media media = new Media(Paths.get("./src/asset/introVid.mp4").toUri().toString());
		MediaPlayer player = new MediaPlayer(media);
		mediaView.setMediaPlayer(player);

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
		});
		player.setOnEndOfMedia(new Runnable() {

			@Override
			public void run() {
				mediaView.setVisible(false);
				skipIntro.setVisible(false);
				Scene scene = skipIntro.getScene();
				Parent root;
				try {
					root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
					scene.setRoot(root);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});
		
		FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.5), mediaView);
		fadeIn.setFromValue(0); fadeIn.setToValue(1);
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
		KeyValue keyVal = new KeyValue(skipIntro.textFillProperty(),Paint.valueOf("#001E6C"));
		KeyFrame keyFrame = new KeyFrame(Duration.seconds(1.2), keyVal);
		Timeline timeLine = new Timeline(keyFrame);
		timeLine.setAutoReverse(true);
		timeLine.setCycleCount(Timeline.INDEFINITE);
		timeLine.play();

		player.play();
	}
}