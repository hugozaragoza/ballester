package ballester.gameworld.examples;

import static java.lang.Math.random;

import java.util.Random;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
import javafx.util.Duration;

@SuppressWarnings("restriction")
public class ColorfulCircles extends Application {

	@Override
	public void start(Stage primaryStage) {
		Group root = new Group();
		Scene scene = new Scene(root, 800, 600, Color.WHITE);
		primaryStage.setScene(scene);
		Random r = new Random();

		Group circles = new Group();
		for (int i = 0; i < 30; i++) {

			Circle circle = new Circle(150, new Color(r.nextDouble(), r.nextDouble(), r.nextDouble(), r.nextDouble()));
			circle.setStrokeType(StrokeType.OUTSIDE);
			circle.setStroke(Color.ALICEBLUE);
			circle.setStrokeWidth(4);
			circles.getChildren().add(circle);
		}
		root.getChildren().add(circles);

		Timeline timeline = new Timeline();
		for (Node circle : circles.getChildren()) {
			timeline.getKeyFrames()
					.addAll(new KeyFrame(Duration.ZERO, // set start position at
														// 0
							new KeyValue(circle.translateXProperty(), random() * 800),
							new KeyValue(circle.translateYProperty(), random() * 600)),
							new KeyFrame(new Duration(40000), // set end
																// position at
																// 40s
									new KeyValue(circle.translateXProperty(), random() * 800),
									new KeyValue(circle.translateYProperty(), random() * 600)));
		}
		// play 40s of animation
		timeline.play();

		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}