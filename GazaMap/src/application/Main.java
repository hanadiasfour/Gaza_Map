package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.regex.Pattern;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

	// to store points for comboBox
	private ObservableList<Point> list = FXCollections.observableArrayList();

	@Override
	public void start(Stage primaryStage) {

		classicSLL points = new classicSLL();// to hold the vertex

		BorderPane root = new BorderPane();
		MyPane thePane = new MyPane();
		root.setCenter(thePane);

		readTheFile(points);// read file
		// setting combo box contents
		thePane.getSource().setItems(list);
		thePane.getDestination().setItems(list);
		thePane.placeCircles(points);// adds the main cities as dots

		thePane.getRun().setOnAction(e -> {

			// resetting values of text areas
			thePane.getDistance().clear();
			thePane.getPath().clear();

			// getting starting and ending points
			Point source = thePane.getSource().getSelectionModel().getSelectedItem();
			Point destination = thePane.getDestination().getSelectionModel().getSelectedItem();

			if (source == null || destination == null) {// when no options were picked
				thePane.popWarning("Pick both Source and Destination to continue.");

			} else if (source.equals(destination)) {// in-case the source = destination
				thePane.popWarning("Source and Destination are the same.");
				thePane.getDistance().setText("0");// no distance
				thePane.getPath().setText(source.getName());// same name

			} else {// there is a legit path to calculate

				clearDistances(points);// resetting distances to infinite

				thePane.clear(points);// removing previously added street points

				classicSLL path = getPath(source, destination, points);// obtains shortest path

				thePane.drawPath(path);// draws line between the two points and streets

				DecimalFormat formater = new DecimalFormat("#.###");// restricting the digits
				String endResult = formater.format(destination.getDistance());

				// setting answers
				thePane.getDistance().setText(endResult + "");
				thePane.getPath().setText(path.toString());
				thePane.addStreetCircles(path);

			}

		});

		Scene scene = new Scene(root, 400, 400);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setMaximized(true);
		primaryStage.setTitle("Gaza Map");

	}

	// loops through points to set their priority (distance) to infinite
	private void clearDistances(classicSLL points) {

		SLLNode current = points.getFirstNode();

		while (current != null) {

			current.getElement().setDistance(Double.MAX_VALUE);

			current = current.getNext();
		}

	}

	// performs dijekstra algorithm to get shortest path wich is returned in a
	// linked list
	private classicSLL getPath(Point source, Point destination, classicSLL points) {

		Heap heap = new Heap(850);// initializing priority queue

		// giving the source the lowest priority
		source.setDistance(0);
		heap.insertMinHeap(source);// adding to heap

		// until heap is empty
		while (!heap.isEmpty()) {

			Point p = heap.removeMin();// get top of heap
			classicSLL adj = points.getNode(p.getName()).getEdges();// obtain adjacent list

			// looping the adjacent list and adding to the heap
			SLLNode current = adj.getFirstNode();
			while (current != null) {

				Point second = current.getElement();
				double newDis = haversineDistance(p, second);// finding distance

				if (p.getDistance() + newDis < second.getDistance()) {// the new distance is shorter
					second.setDistance(p.getDistance() + newDis);
					second.setPre(p);
					heap.insertMinHeap(second);
				}

				current = current.getNext();// next adjacent
			}

		}

		classicSLL path = new classicSLL();// to fill the shortest path from source to destination
		SLLNode current = points.getNode(destination.getName());

		while (current != null) {
			path.addFirst(current.getElement());

			if (current.getElement().getDistance() == 0)
				break;

			current = points.getNode(current.getElement().getPre().getName());
		}

		return path;

	}

	// returns the distance in meters between the two points from the map
	private static double haversineDistance(Point p1, Point p2) {

		final double R = 6371.000;// the radius of the earth

		// converting the latitude and longitude for each point to radians
		double lat1 = Math.toRadians(p1.getLatitude());
		double lon1 = Math.toRadians(p1.getLongitude());
		double lat2 = Math.toRadians(p2.getLatitude());
		double lon2 = Math.toRadians(p2.getLongitude());

		// plugging in the values in the haversine formula
		double latDiff = lat2 - lat1;
		double lonDiff = lon2 - lon1;
		double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2)
				+ Math.cos(lat1) * Math.cos(lat2) * Math.sin(lonDiff / 2) * Math.sin(lonDiff / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return R * c;// returning the distance
	}

	public static void main(String[] args) {
		launch(args);
	}

	private void readTheFile(classicSLL points) {

		try {

			File selectedFile = new File("GazaMapData.txt");

			Scanner input = new Scanner(selectedFile);// scanning file

			try {

				if (!input.hasNext())
					throw new NumberFormatException();

				// storing numbers
				String[] numbers = input.nextLine().split(" ");

				int vertexNum = Integer.parseInt(numbers[0]);
				int edgeNum = Integer.parseInt(numbers[1]);

				// reading points
				for (int i = 0; input.hasNextLine() && i < vertexNum; i++) {

					String[] line = input.nextLine().split(" ");

					Point p = new Point(line[0], Double.parseDouble(line[1]), Double.parseDouble(line[2]),
							isStreetName(line[0]));

					points.addLast(p);

					if (!p.isStreet())// adding to observable list
						list.add(p);

				}

				// reading edges
				for (int i = 0; input.hasNextLine() && i < edgeNum; i++) {

					String[] line = input.nextLine().split(" ");

					SLLNode thePointNode = points.getNode(line[0]);
					Point second = points.get(line[1]);
					thePointNode.getEdges().addLast(second);

				}

			} catch (NumberFormatException e) {
				System.out.println("something wrong 1.0");
				e.printStackTrace();
			}

		} catch (NullPointerException | FileNotFoundException e1) {
			System.out.println("something wrong 2.0");
			e1.printStackTrace();
		}

	}

	private boolean isStreetName(String streetName) {
		String regex = "^S\\d{1,3}$";// the name should match this

		Pattern pattern = Pattern.compile(regex);

		// Check if the street name matches the pattern
		return pattern.matcher(streetName).matches();
	}

}
