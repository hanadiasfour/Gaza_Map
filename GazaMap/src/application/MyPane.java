package application;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MyPane extends HBox {
	private static int click = 0;
	private ComboBox<Point> source, destination;
	private Button run;
	private TextArea path;
	private TextField distance;
	private Pane picturePane;
	private ImageView img = new ImageView(new Image("MyGazaMapPic.png"));

	public MyPane() {

		// initialization
		picturePane = new Pane();
		source = new ComboBox<Point>();
		destination = new ComboBox<Point>();
		run = new Button("Run");
		path = new TextArea();
		distance = new TextField();
		Label sourcelbl = new Label("Source:");
		Label destinationlbl = new Label("Destination:");
		Label pathlbl = new Label("Path:");
		Label distancelbl = new Label("Distance(in km):");

		// setting css class for personalization
		sourcelbl.getStyleClass().add("my-text");
		destinationlbl.getStyleClass().add("my-text");
		pathlbl.getStyleClass().add("my-text");
		distancelbl.getStyleClass().add("my-text");
		run.getStyleClass().add("my-button");
		source.getStyleClass().add("my-source-box");
		destination.getStyleClass().add("my-dest-box");

		// setting properties
		source.setPrefWidth(250);
		source.setPrefHeight(40);
		destination.setPrefWidth(250);
		destination.setPrefHeight(40);
		path.setWrapText(true);
		path.setPrefSize(600, 300);
		distance.setPrefSize(100, 50);
		img.setLayoutX(4);
		img.setLayoutY(4);

		picturePane.setStyle("-fx-border-color:black; -fx-background-color:white; -fx-border-width:2;");
		picturePane.getChildren().addAll(img);

		path.setFont(Font.font("Time New Roman", 17));
		distance.setFont(Font.font("Time New Roman", 15));

		// to hold the interaction features of the application
		VBox controls = new VBox(10);
		controls.getChildren().addAll(sourcelbl, source, destinationlbl, destination, run, pathlbl, path, distancelbl,
				distance);
		controls.setPadding(new Insets(20, 0, 0, 10));

		getChildren().addAll(picturePane, controls);
		setSpacing(20);

	}

	// takes the list of points and adds the cities to the map
	public void placeCircles(classicSLL points) {

		SLLNode curr = points.getFirstNode();

		while (curr != null) {// looping until null

			if (!curr.getElement().isStreet()) {// only adding the cities

				Circle c = new Circle(5);// creating the circle
				c.setFill(Color.RED);
				c.setCenterX(curr.getElement().getX());
				c.setCenterY(curr.getElement().getY());
				c.setId(curr.getElement().getName());
				curr.getElement().setCircle(c);

				c.setOnMouseClicked(e -> {// setting action for selection

					if (click % 2 == 0)
						source.getSelectionModel().select(points.get(c.getId()));
					else
						destination.getSelectionModel().select(points.get(c.getId()));

					click++;

				});

				// a label to hold the name
				Label city = new Label(curr.getElement().getName());
				city.setId(curr.getElement().getName() + "lbl");
				city.setFont(Font.font(null, FontWeight.BOLD, 12));
				city.setLayoutX(curr.getElement().getX() + 6);
				city.setLayoutY(curr.getElement().getY() - 10);

				// adding to pane
				picturePane.getChildren().addAll(c, city);
			}
			curr = curr.getNext();

		}

	}

	// draws the line between two cities
	public void drawPath(classicSLL path) {

		SLLNode curr = path.getFirstNode();
		SLLNode next = curr.getNext();

		while (next != null) {// loop through the path

			Point p1 = curr.getElement();
			Point p2 = next.getElement();

			// creating line
			Line l = new Line(p1.getX(), p1.getY(), p2.getX(), p2.getY());
			l.setStroke(Color.PURPLE);
			l.setStrokeWidth(3.5);
			l.setEffect(new Glow(2.7));

			picturePane.getChildren().add(l);

			// next nodes
			curr = next;
			next = next.getNext();

		}

		path.getFirstNode().getElement().getCircle().toFront();
		path.getFirstNode().getElement().getCircle().setFill(Color.GREEN);
		path.getFirstNode().getElement().getCircle().setEffect(new Glow(2.5));
		curr.getElement().getCircle().toFront();
		curr.getElement().getCircle().setFill(Color.BLUE);
		curr.getElement().getCircle().setEffect(new Glow(2.5));

	}

	// adds the circles for the street when the path is drawn
	public void addStreetCircles(classicSLL path) {

		SLLNode curr = path.getFirstNode().getNext();// start from the nest node

		while (curr.getNext() != null) {// loop through path

			Point p = curr.getElement();
			Circle c = new Circle(2.5);
			c.setCenterX(p.getX());
			c.setCenterY(p.getY());
			c.setFill(Color.WHITESMOKE);

			// creating context menu to show the street name
			ContextMenu contextMenu = new ContextMenu();
			MenuItem menuItem = new MenuItem(p.getName());
			menuItem.setStyle("-fx-font-size: 17;");
			contextMenu.getItems().add(menuItem);
			c.setOnMouseEntered(e -> contextMenu.show(c, e.getScreenX() + 30, e.getScreenY() - 15));
			c.setOnMouseExited(e -> contextMenu.hide());

			picturePane.getChildren().add(c);

			curr = curr.getNext();
		}
	}

	public void clear(classicSLL points) {

		img.toFront();

		SLLNode curr = points.getFirstNode();

		while (curr != null) {// looping points

			if (!curr.getElement().isStreet()) {// only keeping the city points

				for (Node node : picturePane.getChildren()) {// looping the pane children to keep the cities
					if (node.getId() != null && node.getId().equals(curr.getElement().getName())) {
						node.toFront();
						((Circle) node).setFill(Color.RED);
						((Circle) node).setEffect(null);
						break;
					}
				}

				for (Node node : picturePane.getChildren()) {// keeping the city labels
					if (node.getId() != null && node.getId().equals(curr.getElement().getName() + "lbl")) {
						node.toFront();
						break;
					}
				}
			}
			curr = curr.getNext();

		}

	}

	public void popWarning(String whatswrong) {

		BorderPane content = new BorderPane();
		Label lbl = new Label(whatswrong);
		lbl.getStyleClass().add("my-text");
		content.setCenter(lbl);

		Scene scene = new Scene(content, 300, 100);
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.show();

	}

	public ComboBox<Point> getSource() {
		return source;
	}

	public ComboBox<Point> getDestination() {
		return destination;
	}

	public Button getRun() {
		return run;
	}

	public TextArea getPath() {
		return path;
	}

	public TextField getDistance() {
		return distance;
	}

	public Pane getPicturePane() {
		return picturePane;
	}

}
