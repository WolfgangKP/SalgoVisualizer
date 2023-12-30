package salgo_visualizer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.Timer;

/**
 * This class manages the graphical user interface (GUI) of Salgo Visualizer.
 * 
 * @author Wolfgang Koch-Paiz
 */
public class GUI {

	private static final JFrame MAIN_WINDOW = new JFrame(); // Main window
	private static final JPanel HEADER_PANEL = new JPanel(); // Header panel
	private static final JLabel DISCOV_LABEL = new JLabel(); // Discov. label
	private static final JLabel VISITED_LABEL = new JLabel(); // Visited label
	private static final JLabel PATH_LABEL = new JLabel(); // Path label
	private static final JPanel CONTROL_PANEL = new JPanel(); // Control panel
	private static final JRadioButton START_BUTTON = new JRadioButton(); // Start button
	private static final JRadioButton FINISH_BUTTON = new JRadioButton(); // Finish button
	private static final JRadioButton CUSTOM_BUTTON = new JRadioButton(); // Custom button
	private static final JRadioButton WALL_BUTTON = new JRadioButton(); // Wall button
	private static final JPanel GRID_PANEL = new JPanel(); // Grid panel
	private static int gridSize; // Current grid size setting (ex: 7 -> 7 x 7 nodes)
	private static int searchSpeed; // Current search speed setting (ex: 7 -> 7-millisecond delay between
									// iterations)
	private static int weight; // Current weight setting
	private static boolean canSearchMode = true; // True when user can search, false otherwise

	/**
	 * Shows the Salgo Visualizer GUI.
	 */
	public static void show() {

		GUI.setUpHeaderPanel();
		GUI.setUpControlPanel();
		GUI.setUpGridPanel();
		GUI.MAIN_WINDOW.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		GUI.MAIN_WINDOW.pack();
		GUI.MAIN_WINDOW.setLocationRelativeTo(null);
		GUI.MAIN_WINDOW.setVisible(true);
	}

	/*
	 * Sets up the header panel.
	 */
	private static void setUpHeaderPanel() {

		/*
		 * Header panel -> title panel | data panel
		 */
		GUI.HEADER_PANEL.setLayout(new FlowLayout(FlowLayout.LEADING));
		GUI.HEADER_PANEL.setBorder(BorderFactory.createRaisedSoftBevelBorder());

		/*
		 * Title panel -> title label
		 */
		JPanel titlePanel = new JPanel();
		titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 2, Color.ORANGE));

		JLabel titleLabel = new JLabel();
		titleLabel.setText("Salgo Visualizer");
		titleLabel.setFont(new Font("Montserrat", Font.BOLD, 20));
		titleLabel.setForeground(Color.BLUE);

		titlePanel.add(titleLabel);

		/*
		 * Data panel -> discovered panel | visited panel | path panel
		 */
		JPanel dataPanel = new JPanel();

		JPanel discoveredPanel = new JPanel();
		discoveredPanel.setBorder(BorderFactory.createEtchedBorder());
		GUI.DISCOV_LABEL.setText("Discov.  .....");
		discoveredPanel.add(GUI.DISCOV_LABEL);

		JPanel visitedPanel = new JPanel();
		visitedPanel.setBorder(BorderFactory.createEtchedBorder());
		GUI.VISITED_LABEL.setText("Visited  .....");
		visitedPanel.add(GUI.VISITED_LABEL);

		JPanel pathPanel = new JPanel();
		pathPanel.setBorder(BorderFactory.createEtchedBorder());
		GUI.PATH_LABEL.setText("Path  .....");
		pathPanel.add(GUI.PATH_LABEL);

		dataPanel.add(discoveredPanel);
		dataPanel.add(visitedPanel);
		dataPanel.add(pathPanel);

		GUI.HEADER_PANEL.add(titlePanel);
		GUI.HEADER_PANEL.add(dataPanel);

		GUI.MAIN_WINDOW.add(HEADER_PANEL, BorderLayout.NORTH);
	}

	/*
	 * Sets up the control panel.
	 */
	private static void setUpControlPanel() {

		/*
		 * Control panel -> size, speed and weight panel | create maze panel | node type
		 * panel | drop-down menu | search and clear panel | spacer | my name label
		 */
		GUI.CONTROL_PANEL.setLayout(new GridBagLayout());
		GridBagConstraints controlPanelConstraints = new GridBagConstraints();
		GUI.CONTROL_PANEL.setBorder(BorderFactory.createTitledBorder("Control Panel"));

		/*
		 * Size, speed and weight panel -> size panel | speed panel | weight panel
		 */
		JPanel sizeSpeedAndWeightPanel = new JPanel();
		sizeSpeedAndWeightPanel.setBorder(BorderFactory.createRaisedSoftBevelBorder());

		JPanel sizePanel = new JPanel(new GridBagLayout());
		GridBagConstraints sizePanelConstraints = new GridBagConstraints();
		JLabel sizeLabel = new JLabel();
		sizeLabel.setText("| Size |");
		JSlider sizeSlider = new JSlider(JSlider.VERTICAL, 2, 50, 25);
		GUI.gridSize = sizeSlider.getValue();
		sizeSlider.setPreferredSize(new Dimension(20, 130));
		JLabel sizeValueLabel = new JLabel();
		sizeValueLabel.setText("" + sizeSlider.getValue());
		sizeSlider.addChangeListener((e) -> {

			GUI.gridSize = sizeSlider.getValue();
			sizeValueLabel.setText("" + GUI.gridSize);
			GUI.MAIN_WINDOW.getContentPane().remove(GUI.GRID_PANEL);
			setUpGridPanel();
			GUI.MAIN_WINDOW.revalidate();
			GUI.MAIN_WINDOW.repaint();
		});
		sizePanelConstraints.gridx = 0;
		sizePanelConstraints.gridy = 0;
		sizePanelConstraints.weighty = 0;
		sizePanel.add(sizeLabel, sizePanelConstraints);
		sizePanelConstraints.gridx = 0;
		sizePanelConstraints.gridy = 1;
		sizePanelConstraints.weighty = 0;
		sizePanel.add(sizeSlider, sizePanelConstraints);
		sizePanelConstraints.gridx = 0;
		sizePanelConstraints.gridy = 2;
		sizePanelConstraints.weighty = 0;
		sizePanel.add(sizeValueLabel, sizePanelConstraints);

		JPanel speedPanel = new JPanel(new GridBagLayout());
		GridBagConstraints speedPanelConstraints = new GridBagConstraints();
		JLabel speedLabel = new JLabel();
		speedLabel.setText("| Speed |");
		JSlider speedSlider = new JSlider(JSlider.VERTICAL, 0, 1000, 1);
		GUI.searchSpeed = speedSlider.getValue();
		speedSlider.setPreferredSize(new Dimension(20, 130));
		JLabel speedValueLabel = new JLabel();
		speedValueLabel.setText("" + speedSlider.getValue());
		speedSlider.addChangeListener((e) -> {

			GUI.searchSpeed = speedSlider.getValue();
			if (Node.timer.isRunning()) {
				Node.timer.stop();
				Node.timer.setDelay(GUI.searchSpeed);
				Node.timer.start();
			}
			speedValueLabel.setText("" + GUI.searchSpeed);
		});
		speedPanelConstraints.gridx = 0;
		speedPanelConstraints.gridy = 0;
		speedPanelConstraints.weighty = 0;
		speedPanel.add(speedLabel, speedPanelConstraints);
		speedPanelConstraints.gridx = 0;
		speedPanelConstraints.gridy = 1;
		speedPanelConstraints.weighty = 0;
		speedPanel.add(speedSlider, speedPanelConstraints);
		speedPanelConstraints.gridx = 0;
		speedPanelConstraints.gridy = 2;
		speedPanelConstraints.weighty = 0;
		speedPanel.add(speedValueLabel, speedPanelConstraints);

		JPanel weightPanel = new JPanel(new GridBagLayout());
		GridBagConstraints weightPanelConstraints = new GridBagConstraints();
		JLabel weightLabel = new JLabel();
		weightLabel.setText("| Weight |");
		JSlider weightSlider = new JSlider(JSlider.VERTICAL, 0, 100, 1);
		GUI.weight = weightSlider.getValue();
		weightSlider.setPreferredSize(new Dimension(20, 130));
		JLabel weightValueLabel = new JLabel();
		weightValueLabel.setText("" + weightSlider.getValue());
		weightSlider.addChangeListener((e) -> {

			GUI.weight = weightSlider.getValue();
			weightValueLabel.setText("" + GUI.weight);
		});
		weightPanelConstraints.gridx = 0;
		weightPanelConstraints.gridy = 0;
		weightPanel.add(weightLabel, weightPanelConstraints);
		weightPanelConstraints.gridx = 0;
		weightPanelConstraints.gridy = 1;
		weightPanel.add(weightSlider, weightPanelConstraints);
		weightPanelConstraints.gridx = 0;
		weightPanelConstraints.gridy = 2;
		weightPanel.add(weightValueLabel, weightPanelConstraints);

		sizeSpeedAndWeightPanel.add(sizePanel);
		sizeSpeedAndWeightPanel.add(speedPanel);
		sizeSpeedAndWeightPanel.add(weightPanel);

		/*
		 * Create maze panel -> create maze button
		 */
		JPanel generateMazePanel = new JPanel(new GridLayout(1, 1));
		generateMazePanel.setBorder(BorderFactory.createEtchedBorder());

		JButton generateMazeButton = new JButton();
		generateMazeButton.setText("Generate Maze");
		generateMazeButton.setBackground(Color.LIGHT_GRAY);
		generateMazeButton.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		generateMazeButton.setFocusable(false);
		generateMazeButton.addActionListener((e) -> {

			GUI.canSearchMode = false;
			Node.canContinueUpdatingNodeType = false;
			Node.generateMaze();
		});
		generateMazePanel.add(generateMazeButton);

		/*
		 * Node type panel -> start button | finish button | custom button | wall button
		 */
		JPanel nodeTypePanel = new JPanel(new GridLayout(2, 2));
		nodeTypePanel.setBorder(BorderFactory.createRaisedSoftBevelBorder());

		GUI.START_BUTTON.setText("Start");
		GUI.START_BUTTON.setFocusable(false);
		GUI.START_BUTTON.addActionListener((e) -> {

			GUI.FINISH_BUTTON.setSelected(false);
			GUI.CUSTOM_BUTTON.setSelected(false);
			GUI.WALL_BUTTON.setSelected(false);
		});
		GUI.FINISH_BUTTON.setText("Finish");
		GUI.FINISH_BUTTON.setFocusable(false);
		GUI.FINISH_BUTTON.addActionListener((e) -> {

			GUI.START_BUTTON.setSelected(false);
			GUI.CUSTOM_BUTTON.setSelected(false);
			GUI.WALL_BUTTON.setSelected(false);
		});
		GUI.CUSTOM_BUTTON.setText("Custom");
		GUI.CUSTOM_BUTTON.setFocusable(false);
		GUI.CUSTOM_BUTTON.addActionListener((e) -> {

			GUI.START_BUTTON.setSelected(false);
			GUI.FINISH_BUTTON.setSelected(false);
			GUI.WALL_BUTTON.setSelected(false);
		});
		GUI.WALL_BUTTON.setText("Wall");
		GUI.WALL_BUTTON.setFocusable(false);
		GUI.WALL_BUTTON.addActionListener((e) -> {

			GUI.START_BUTTON.setSelected(false);
			GUI.FINISH_BUTTON.setSelected(false);
			GUI.CUSTOM_BUTTON.setSelected(false);
		});

		nodeTypePanel.add(GUI.START_BUTTON);
		nodeTypePanel.add(GUI.FINISH_BUTTON);
		nodeTypePanel.add(GUI.CUSTOM_BUTTON);
		nodeTypePanel.add(GUI.WALL_BUTTON);

		/*
		 * Drop-down menu
		 */
		JComboBox<String> dropDownMenu = new JComboBox<>();
		dropDownMenu.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		dropDownMenu.addItem("A*");
		dropDownMenu.addItem("Breadth First");
		dropDownMenu.addItem("Depth First");
		dropDownMenu.addItem("Dijkstra");
		dropDownMenu.addItem("Greedy Best First");
		dropDownMenu.setFocusable(false);

		/*
		 * Search and clear panel -> search button | clear button
		 */
		JPanel searchAndClearPanel = new JPanel(new GridLayout(1, 2));
		searchAndClearPanel.setBorder(BorderFactory.createEtchedBorder());

		JButton searchButton = new JButton();
		searchButton.setText("Search");
		searchButton.setBackground(Color.ORANGE);
		searchButton.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		searchButton.setFocusable(false);
		searchButton.addActionListener((e) -> {

			if (Node.start == null || Node.finish == null || !GUI.canSearchMode) {
				// Does nothing
			} else {
				GUI.canSearchMode = false;
				Node.canContinueUpdatingNodeType = false;
				for (int i = 0; i < GUI.gridSize; i++) {
					for (int j = 0; j < GUI.gridSize; j++) {
						Node.nodeSearchTable[i][j].setBorder(BorderFactory.createLoweredSoftBevelBorder());
					}
				}
				GUI.GRID_PANEL.repaint();
				if (dropDownMenu.getSelectedItem().equals("A*")) {
					Node.doAStar();
				} else if (dropDownMenu.getSelectedItem().equals("Breadth First")) {
					Node.doBreadthFirst();
				} else if (dropDownMenu.getSelectedItem().equals("Depth First")) {
					Node.doDepthFirst();
				} else if (dropDownMenu.getSelectedItem().equals("Dijkstra")) {
					Node.doDijkstra();
				} else if (dropDownMenu.getSelectedItem().equals("Greedy Best First")) {
					Node.doGreedyBestFirst();
				}
			}
		});

		JButton clearButton = new JButton();
		clearButton.setText("Clear");
		clearButton.setBackground(Color.LIGHT_GRAY);
		clearButton.setBorder(BorderFactory.createRaisedSoftBevelBorder());
		clearButton.setFocusable(false);
		clearButton.addActionListener((e) -> {

			GUI.MAIN_WINDOW.getContentPane().remove(GUI.GRID_PANEL);
			setUpGridPanel();
			GUI.MAIN_WINDOW.revalidate();
			GUI.MAIN_WINDOW.repaint();
			GUI.START_BUTTON.setSelected(false);
			GUI.FINISH_BUTTON.setSelected(false);
			GUI.CUSTOM_BUTTON.setSelected(false);
			GUI.WALL_BUTTON.setSelected(false);
			GUI.canSearchMode = true;
			Node.canContinueUpdatingNodeType = true;
			GUI.DISCOV_LABEL.setText("Discov.  .....");
			GUI.VISITED_LABEL.setText("Visited  .....");
			GUI.PATH_LABEL.setText("Path  .....");
		});

		searchAndClearPanel.add(searchButton);
		searchAndClearPanel.add(clearButton);

		/*
		 * Spacer
		 */
		JLabel spacer = new JLabel();

		/*
		 * My name label
		 */
		JLabel myNameLabel = new JLabel();
		myNameLabel.setText("Wolfgang Alberto Koch-Paiz");
		myNameLabel.setFont(new Font("Montserrat", Font.ITALIC, 7));

		controlPanelConstraints.gridx = 0;
		controlPanelConstraints.gridy = 0;
		controlPanelConstraints.weighty = 0;
		controlPanelConstraints.fill = GridBagConstraints.BOTH;
		GUI.CONTROL_PANEL.add(sizeSpeedAndWeightPanel, controlPanelConstraints);

		controlPanelConstraints.gridx = 0;
		controlPanelConstraints.gridy = 1;
		controlPanelConstraints.weighty = 0;
		controlPanelConstraints.fill = GridBagConstraints.BOTH;
		GUI.CONTROL_PANEL.add(generateMazePanel, controlPanelConstraints);

		controlPanelConstraints.gridx = 0;
		controlPanelConstraints.gridy = 2;
		controlPanelConstraints.weighty = 0;
		controlPanelConstraints.fill = GridBagConstraints.BOTH;
		GUI.CONTROL_PANEL.add(nodeTypePanel, controlPanelConstraints);

		controlPanelConstraints.gridx = 0;
		controlPanelConstraints.gridy = 3;
		controlPanelConstraints.weighty = 0;
		controlPanelConstraints.fill = GridBagConstraints.BOTH;
		GUI.CONTROL_PANEL.add(dropDownMenu, controlPanelConstraints);

		controlPanelConstraints.gridx = 0;
		controlPanelConstraints.gridy = 4;
		controlPanelConstraints.weighty = 0;
		controlPanelConstraints.fill = GridBagConstraints.BOTH;
		GUI.CONTROL_PANEL.add(searchAndClearPanel, controlPanelConstraints);

		controlPanelConstraints.gridx = 0;
		controlPanelConstraints.gridy = 5;
		controlPanelConstraints.weighty = 1;
		controlPanelConstraints.fill = GridBagConstraints.BOTH;
		GUI.CONTROL_PANEL.add(spacer, controlPanelConstraints);

		controlPanelConstraints.gridx = 0;
		controlPanelConstraints.gridy = 6;
		controlPanelConstraints.weighty = 0;
		GUI.CONTROL_PANEL.add(myNameLabel, controlPanelConstraints);

		GUI.MAIN_WINDOW.add(CONTROL_PANEL, BorderLayout.WEST);
	}

	/*
	 * Sets up the grid panel.
	 */
	private static void setUpGridPanel() {

		Node.timer.stop();
		Node.start = null;
		Node.finish = null;
		Node.setNodeSearchTable();

		GUI.GRID_PANEL.removeAll();
		GUI.GRID_PANEL.setLayout(new GridBagLayout());
		GridBagConstraints gridPanelConstraints = new GridBagConstraints();
		GUI.GRID_PANEL.setPreferredSize(new Dimension(700, 700));

		int nodeSize = Math.min(GUI.GRID_PANEL.getWidth() / GUI.gridSize, GUI.GRID_PANEL.getHeight() / GUI.gridSize);

		gridPanelConstraints.weightx = 1.0;
		gridPanelConstraints.weighty = 1.0;
		gridPanelConstraints.fill = GridBagConstraints.BOTH;

		for (int i = 0; i < gridSize; i++) {
			for (int j = 0; j < gridSize; j++) {
				Node node = new Node(i, j);
				node.setPreferredSize(new Dimension(nodeSize, nodeSize));
				gridPanelConstraints.gridx = j;
				gridPanelConstraints.gridy = i;
				GUI.GRID_PANEL.add(node, gridPanelConstraints);
				node.weight = 1;
				node.setToolTipText("" + node.weight);
				Node.nodeSearchTable[i][j] = node;
			}
		}

		GUI.GRID_PANEL.revalidate();
		GUI.GRID_PANEL.repaint();
		GUI.MAIN_WINDOW.add(GRID_PANEL);
		GUI.START_BUTTON.setSelected(false);
		GUI.FINISH_BUTTON.setSelected(false);
		GUI.CUSTOM_BUTTON.setSelected(false);
		GUI.WALL_BUTTON.setSelected(false);
		GUI.canSearchMode = true;
		Node.canContinueUpdatingNodeType = true;
		GUI.DISCOV_LABEL.setText("Discov.  .....");
		GUI.VISITED_LABEL.setText("Visited  .....");
		GUI.PATH_LABEL.setText("Path  .....");
	}

	/*
	 * This class represents the grid nodes of Salgo Visualizer.
	 */
	private static class Node extends JButton implements MouseListener {

		private static final long serialVersionUID = 1L;

		private int row; // Node's row position in grid
		private int col; // Node's column position in grid
		private static Node start; // Start node
		private static Node finish; // Finish node
		private boolean isCustom; // Node's status as custom node
		private boolean isWall; // Node's status as wall node
		private int weight; // Node's weight
		private static boolean canContinueUpdatingNodeType = true; // True when a node's type can be updated, false
																	// otherwise
		private Node predecessor; // Node's predecessor
		private static Node nodeSearchTable[][]; // Table for searching grid
		private static Timer timer = new Timer(0, null); // Timer for search algorithm visualization
		private int travelCost; // "Cost" of travel from start to this node
		private static int discoveredCount; // Number of nodes "discovered" last search
		private static int visitedCount; // Number of nodes "visited" last search
		private static boolean mousePressedAndCouldContinueUpdatingNodeType; // True if the mouse was pressed and this
																				// node's type could be updated, false
																				// otherwise;

		/*
		 * Constructor for the Node class.
		 * 
		 * Parameters: row ~ type int - row position in the grid, col ~ type int -
		 * column position in the grid
		 */
		private Node(int row, int col) {

			this.row = row;
			this.col = col;
			this.setBackground(Color.LIGHT_GRAY);
			this.setBorder(BorderFactory.createLoweredSoftBevelBorder());
			this.addMouseListener(this);
		}

		/*
		 * Sets node search table.
		 */
		private static void setNodeSearchTable() {

			Node.nodeSearchTable = new Node[GUI.gridSize][GUI.gridSize];
		}

		/*
		 * Updates the node to "passage" (a light gray, weight one, walkable node).
		 */
		private void updateNodeTypeToPassage() {

			this.setBackground(Color.LIGHT_GRAY);
			this.weight = 1;
			this.setToolTipText("" + this.weight);
			this.isCustom = false;
			this.isWall = false;
			GUI.GRID_PANEL.repaint();
		}

		/*
		 * Updates the specified node to one of four non-passage types: start, finish,
		 * custom, wall (other nodes updated accordingly).
		 */
		private void updateNodeTypeToNonPassage() {

			if (GUI.START_BUTTON.isSelected()) {
				if (Node.start == this) {
					// Does nothing
				} else if (Node.start != null) {
					if (Node.finish == this) {
						Node.finish = null;
					}
					Node.start.updateNodeTypeToPassage();
					this.setBackground(Color.GREEN);
					this.weight = 0;
					this.setToolTipText(null);
					Node.start = this;
				} else {
					if (Node.finish == this) {
						Node.finish = null;
					}
					this.updateNodeTypeToPassage();
					this.setBackground(Color.GREEN);
					this.weight = 0;
					this.setToolTipText(null);
					Node.start = this;
				}
			} else if (GUI.FINISH_BUTTON.isSelected()) {
				if (Node.finish == this) {
					this.weight = GUI.weight;
					this.setToolTipText("" + this.weight);
				} else if (Node.finish != null) {
					if (Node.start == this) {
						Node.start = null;
					}
					Node.finish.updateNodeTypeToPassage();
					this.setBackground(Color.RED);
					this.weight = GUI.weight;
					this.setToolTipText("" + this.weight);
					Node.finish = this;
				} else {
					if (Node.start == this) {
						Node.start = null;
					}
					this.updateNodeTypeToPassage();
					this.setBackground(Color.RED);
					this.weight = GUI.weight;
					this.setToolTipText("" + this.weight);
					Node.finish = this;
				}
			} else if (GUI.CUSTOM_BUTTON.isSelected()) {
				if (this.isCustom) {
					this.weight = GUI.weight;
					this.setToolTipText("" + this.weight);
				} else if (Node.start == this) {
					Node.start = null;
					this.setBackground(Color.YELLOW);
					this.weight = GUI.weight;
					this.setToolTipText("" + this.weight);
					this.isCustom = true;
				} else if (Node.finish == this) {
					Node.finish = null;
					this.setBackground(Color.YELLOW);
					this.weight = GUI.weight;
					this.setToolTipText("" + this.weight);
					this.isCustom = true;
				} else if (this.isWall) {
					this.isWall = false;
					this.setBackground(Color.YELLOW);
					this.weight = GUI.weight;
					this.setToolTipText("" + this.weight);
					this.isCustom = true;
				} else {
					this.setBackground(Color.YELLOW);
					this.weight = GUI.weight;
					this.setToolTipText("" + this.weight);
					this.isCustom = true;
				}
			} else if (GUI.WALL_BUTTON.isSelected()) {
				if (this.isWall) {
					// Does nothing
				} else if (Node.start == this) {
					Node.start = null;
					this.setBackground(Color.GRAY);
					this.isWall = true;
				} else if (Node.finish == this) {
					Node.finish = null;
					this.setBackground(Color.GRAY);
					this.weight = 0;
					this.setToolTipText(null);
					this.isWall = true;
				} else if (this.isCustom) {
					this.isCustom = false;
					this.setBackground(Color.GRAY);
					this.weight = 0;
					this.setToolTipText(null);
					this.isWall = true;
				} else {
					this.setBackground(Color.GRAY);
					this.weight = 0;
					this.setToolTipText(null);
					this.isWall = true;
				}
			}

			GUI.GRID_PANEL.repaint();
		}

		@Override
		public void mouseClicked(MouseEvent e) {

			// Do nothing
		}

		@Override
		public void mousePressed(MouseEvent e) {

			Node.mousePressedAndCouldContinueUpdatingNodeType = !Node.mousePressedAndCouldContinueUpdatingNodeType;

			if (Node.canContinueUpdatingNodeType) {
				updateNodeTypeToNonPassage();
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {

			// Do nothing
		}

		@Override
		public void mouseEntered(MouseEvent e) {

			if (Node.canContinueUpdatingNodeType && Node.mousePressedAndCouldContinueUpdatingNodeType) {
				updateNodeTypeToNonPassage();
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {

			// Do nothing
		}

		/*
		 * Returns this node's neighbors (for the purpose of searching).
		 */
		private HashSet<Node> getNeighborsForSearch() {

			HashSet<Node> neighbors = new HashSet<>();

			if (this.row > 0 && !Node.nodeSearchTable[this.row - 1][this.col].isWall) {
				neighbors.add(Node.nodeSearchTable[this.row - 1][this.col]); // Upper neighbor
			}
			if (this.row < GUI.gridSize - 1 && !Node.nodeSearchTable[this.row + 1][this.col].isWall) {
				neighbors.add(Node.nodeSearchTable[this.row + 1][this.col]); // Lower neighbor
			}
			if (this.col > 0 && !Node.nodeSearchTable[this.row][this.col - 1].isWall) {
				neighbors.add(Node.nodeSearchTable[this.row][this.col - 1]); // Left neighbor
			}
			if (this.col < GUI.gridSize - 1 && !Node.nodeSearchTable[this.row][this.col + 1].isWall) {
				neighbors.add(Node.nodeSearchTable[this.row][this.col + 1]); // Right neighbor
			}

			return neighbors;
		}

		/*
		 * Returns this node's heuristic of the finish node (Manhattan distance).
		 */
		private int heuristicOfFinish() {

			int colDistance = Math.abs(this.col - finish.col);
			int rowDistance = Math.abs(this.row - finish.row);

			return colDistance + rowDistance;
		}

		/*
		 * Marks this node as discovered by updating its border.
		 */
		private void markAsDiscovered() {

			this.setBorder(BorderFactory.createLineBorder(Color.PINK, 2));
			GUI.GRID_PANEL.repaint();
		}

		/*
		 * Marks this node as visited by updating its border.
		 */
		private void markAsVisited() {

			this.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));
			GUI.GRID_PANEL.repaint();
		}

		/*
		 * Retraces the path from finish to start by updating its border (finish node's
		 * travelCost updated along the way, end-of-search procedures are executed).
		 */
		private void retracePath() {

			Node retrace = this;
			
			System.out.println("Finished!");
			Node.finish.markAsDiscovered();
			Node.discoveredCount++;
			Node.finish.travelCost = finish.weight;
			while (retrace != Node.start) {
				Node.finish.travelCost += retrace.weight;
				retrace.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
				retrace = retrace.predecessor;
			}
			Node.timer.stop();
			GUI.DISCOV_LABEL.setText("Discov.  " + Node.discoveredCount);
			GUI.VISITED_LABEL.setText("Visited  " + Node.visitedCount);
			GUI.PATH_LABEL.setText("Path  " + Node.finish.travelCost);
			GUI.canSearchMode = true;
			Node.canContinueUpdatingNodeType = true;
		}

		/*
		 * Does the A* search algorithm.
		 */
		private static void doAStar() {

			Node.timer = new Timer(GUI.searchSpeed, null);

			GUI.DISCOV_LABEL.setText("Discov.  .....");
			GUI.VISITED_LABEL.setText("Visited  .....");
			GUI.PATH_LABEL.setText("Path  .....");

			Node.discoveredCount = 0;
			Node.visitedCount = 0;

			HashSet<Node> visitedSet = new HashSet<>();
			Queue<Node> discoveredQueue = new PriorityQueue<>((nodeOne, nodeTwo) -> {

				return (nodeOne.travelCost + nodeOne.heuristicOfFinish())
						- (nodeTwo.travelCost + nodeTwo.heuristicOfFinish());
			});

			Node.start.markAsDiscovered();
			discoveredCount++;
			discoveredQueue.add(start);
			visitedSet.add(Node.start);

			Node.timer.addActionListener((e) -> {

				if (!discoveredQueue.isEmpty()) {
					Node currentNode = discoveredQueue.poll();
					currentNode.markAsVisited();
					Node.visitedCount++;
					for (Node neighbor : currentNode.getNeighborsForSearch()) {
						if (!visitedSet.contains(neighbor)) {
							if (neighbor == Node.finish) {
								currentNode.retracePath();
							} else {
								neighbor.markAsDiscovered();
								Node.discoveredCount++;
								neighbor.travelCost = currentNode.travelCost + neighbor.weight;
								neighbor.predecessor = currentNode;
								discoveredQueue.add(neighbor);
								visitedSet.add(neighbor);
								neighbor.setToolTipText("W:" + neighbor.weight + ", TC:" + neighbor.travelCost + ", H:"
										+ neighbor.heuristicOfFinish());
							}
						}
					}
				} else {
					Node.timer.stop();
					System.out.println("Finish Unreachable!");
					GUI.DISCOV_LABEL.setText("Discov.  " + Node.discoveredCount);
					GUI.VISITED_LABEL.setText("Visited  " + Node.visitedCount);
					GUI.canSearchMode = true;
					Node.canContinueUpdatingNodeType = true;
				}
			});

			Node.timer.start();
		}

		/*
		 * Does the Breadth First search algorithm.
		 */
		private static void doBreadthFirst() {

			Node.timer = new Timer(GUI.searchSpeed, null);

			GUI.DISCOV_LABEL.setText("Discov.  .....");
			GUI.VISITED_LABEL.setText("Visited  .....");
			GUI.PATH_LABEL.setText("Path  .....");

			Node.discoveredCount = 0;
			Node.visitedCount = 0;

			HashSet<Node> visitedSet = new HashSet<>();
			Queue<Node> discoveredQueue = new LinkedList<>();

			Node.start.markAsDiscovered();
			Node.discoveredCount++;
			discoveredQueue.add(Node.start);

			Node.timer.addActionListener((e) -> {

				if (!discoveredQueue.isEmpty()) {
					Node currentNode = discoveredQueue.poll();
					if (!visitedSet.contains(currentNode)) {
						currentNode.markAsVisited();
						Node.visitedCount++;
						visitedSet.add(currentNode);
						for (Node neighbor : currentNode.getNeighborsForSearch()) {
							if (neighbor == Node.finish) {
								currentNode.retracePath();
							} else if (!visitedSet.contains(neighbor)) {
								neighbor.markAsDiscovered();
								Node.discoveredCount++;
								neighbor.predecessor = currentNode;
								discoveredQueue.add(neighbor);
							}
						}
					}
				} else {
					Node.timer.stop();
					System.out.println("Finish Unreachable!");
					GUI.DISCOV_LABEL.setText("Discov.  " + Node.discoveredCount);
					GUI.VISITED_LABEL.setText("Visited  " + Node.visitedCount);
					GUI.canSearchMode = true;
					Node.canContinueUpdatingNodeType = true;
				}
			});

			Node.timer.start();
		}

		/*
		 * Does the Depth First search algorithm.
		 */
		private static void doDepthFirst() {

			Node.timer = new Timer(GUI.searchSpeed, null);

			GUI.DISCOV_LABEL.setText("Discov.  .....");
			GUI.VISITED_LABEL.setText("Visited  .....");
			GUI.PATH_LABEL.setText("Path  .....");

			Node.discoveredCount = 0;
			Node.visitedCount = 0;

			HashSet<Node> visitedSet = new HashSet<>();
			Stack<Node> discoveredStack = new Stack<>();

			Node.start.markAsDiscovered();
			Node.discoveredCount++;
			discoveredStack.push(Node.start);

			Node.timer.addActionListener((e) -> {

				if (!discoveredStack.isEmpty()) {
					Node currentNode = discoveredStack.pop();
					if (!visitedSet.contains(currentNode)) {
						currentNode.markAsVisited();
						Node.visitedCount++;
						visitedSet.add(currentNode);
						for (Node neighbor : currentNode.getNeighborsForSearch()) {
							if (neighbor == Node.finish) {
								currentNode.retracePath();
							} else if (!visitedSet.contains(neighbor)) {
								neighbor.markAsDiscovered();
								Node.discoveredCount++;
								neighbor.predecessor = currentNode;
								discoveredStack.push(neighbor);
							}
						}
					}
				} else {
					Node.timer.stop();
					System.out.println("Finish Unreachable!");
					GUI.DISCOV_LABEL.setText("Discov.  " + Node.discoveredCount);
					GUI.VISITED_LABEL.setText("Visited  " + Node.visitedCount);
					GUI.canSearchMode = true;
					Node.canContinueUpdatingNodeType = true;
				}
			});

			Node.timer.start();
		}

		/*
		 * Does the Dijkstra search algorithm.
		 */
		private static void doDijkstra() {

			Node.timer = new Timer(GUI.searchSpeed, null);

			GUI.DISCOV_LABEL.setText("Discov.  .....");
			GUI.VISITED_LABEL.setText("Visited  .....");
			GUI.PATH_LABEL.setText("Path  .....");

			Node.discoveredCount = 0;
			Node.visitedCount = 0;

			HashSet<Node> visitedSet = new HashSet<>();
			Queue<Node> discoveredQueue = new PriorityQueue<>((nodeOne, nodeTwo) -> {

				return nodeOne.travelCost - nodeTwo.travelCost;
			});

			Node.start.markAsDiscovered();
			discoveredCount++;
			discoveredQueue.add(start);
			visitedSet.add(Node.start);

			Node.timer.addActionListener((e) -> {

				if (!discoveredQueue.isEmpty()) {
					Node currentNode = discoveredQueue.poll();
					currentNode.markAsVisited();
					Node.visitedCount++;
					for (Node neighbor : currentNode.getNeighborsForSearch()) {
						if (!visitedSet.contains(neighbor)) {
							if (neighbor == Node.finish) {
								currentNode.retracePath();
							} else {
								neighbor.markAsDiscovered();
								Node.discoveredCount++;
								neighbor.travelCost = currentNode.travelCost + neighbor.weight;
								neighbor.predecessor = currentNode;
								discoveredQueue.add(neighbor);
								visitedSet.add(neighbor);
								neighbor.setToolTipText("W:" + neighbor.weight + ", TC:" + neighbor.travelCost);
							}
						}
					}
				} else {
					Node.timer.stop();
					System.out.println("Finish Unreachable!");
					GUI.DISCOV_LABEL.setText("Discov.  " + Node.discoveredCount);
					GUI.VISITED_LABEL.setText("Visited  " + Node.visitedCount);
					GUI.canSearchMode = true;
					Node.canContinueUpdatingNodeType = true;
				}
			});

			Node.timer.start();
		}

		/*
		 * Does the Greedy Best First search algorithm.
		 */
		private static void doGreedyBestFirst() {

			Node.timer = new Timer(GUI.searchSpeed, null);

			GUI.DISCOV_LABEL.setText("Discov.  .....");
			GUI.VISITED_LABEL.setText("Visited  .....");
			GUI.PATH_LABEL.setText("Path  .....");

			Node.discoveredCount = 0;
			Node.visitedCount = 0;

			HashSet<Node> visitedSet = new HashSet<>();
			Queue<Node> discoveredQueue = new PriorityQueue<>((nodeOne, nodeTwo) -> {

				return nodeOne.heuristicOfFinish() - nodeTwo.heuristicOfFinish();
			});

			Node.start.markAsDiscovered();
			discoveredCount++;
			discoveredQueue.add(Node.start);
			visitedSet.add(Node.start);

			Node.timer.addActionListener((e) -> {

				if (!discoveredQueue.isEmpty()) {
					Node currentNode = discoveredQueue.poll();
					currentNode.markAsVisited();
					Node.visitedCount++;
					for (Node neighbor : currentNode.getNeighborsForSearch()) {
						if (!visitedSet.contains(neighbor)) {
							if (neighbor == Node.finish) {
								currentNode.retracePath();
							} else {
								neighbor.markAsDiscovered();
								Node.discoveredCount++;
								neighbor.predecessor = currentNode;
								discoveredQueue.add(neighbor);
								visitedSet.add(neighbor);
								neighbor.setToolTipText("W:" + neighbor.weight + ", H:" + neighbor.heuristicOfFinish());
							}
						}
					}
				} else {
					Node.timer.stop();
					System.out.println("Finish Unreachable!");
					GUI.DISCOV_LABEL.setText("Discov.  " + Node.discoveredCount);
					GUI.VISITED_LABEL.setText("Visited  " + Node.visitedCount);
					GUI.canSearchMode = true;
					Node.canContinueUpdatingNodeType = true;
				}
			});

			Node.timer.start();
		}

		/*
		 * Returns this node's neighbors (for purpose of maze generation).
		 */
		private HashSet<Node> getNeighborsForMaze() {

			HashSet<Node> neighbors = new HashSet<>();

			if (this.row > 1) {
				Node upperNeighbor = Node.nodeSearchTable[this.row - 2][this.col];
				upperNeighbor.predecessor = Node.nodeSearchTable[this.row - 1][this.col];
				neighbors.add(upperNeighbor);
			}
			if (this.row < GUI.gridSize - 2) {
				Node lowerNeighbor = Node.nodeSearchTable[this.row + 2][this.col];
				lowerNeighbor.predecessor = Node.nodeSearchTable[this.row + 1][this.col];
				neighbors.add(lowerNeighbor);
			}
			if (this.col > 1) {
				Node leftNeighbor = Node.nodeSearchTable[this.row][this.col - 2];
				leftNeighbor.predecessor = Node.nodeSearchTable[this.row][this.col - 1];
				neighbors.add(leftNeighbor);
			}
			if (this.col < GUI.gridSize - 2) {
				Node rightNeighbor = Node.nodeSearchTable[this.row][this.col + 2];
				rightNeighbor.predecessor = Node.nodeSearchTable[this.row][this.col + 1];
				neighbors.add(rightNeighbor);
			}

			return neighbors;
		}

		/*
		 * Generates a maze using the Depth First search algorithm.
		 */
		private static void generateMaze() {

			Node.timer.stop();

			GUI.DISCOV_LABEL.setText("Discov.  .....");
			GUI.VISITED_LABEL.setText("Visited  .....");
			GUI.PATH_LABEL.setText("Path  .....");

			for (int i = 0; i < GUI.gridSize; i++) {
				for (int j = 0; j < GUI.gridSize; j++) {
					Node node = Node.nodeSearchTable[i][j];
					node.setBorder(BorderFactory.createLoweredSoftBevelBorder());
					GUI.START_BUTTON.setSelected(false);
					GUI.FINISH_BUTTON.setSelected(false);
					GUI.CUSTOM_BUTTON.setSelected(false);
					GUI.WALL_BUTTON.setSelected(true);
					node.updateNodeTypeToNonPassage();
				}
			}

			Random random = new Random();
			int rowCoordinate = random.nextInt(GUI.gridSize);
			int colCoordinate = random.nextInt(GUI.gridSize);

			Node origin = nodeSearchTable[rowCoordinate][colCoordinate];
			origin.predecessor = origin;

			Node.timer = new Timer(GUI.searchSpeed, null);

			HashSet<Node> visitedSet = new HashSet<>();
			Stack<Node> discoveredStack = new Stack<>();
			discoveredStack.push(origin);

			Node.timer.addActionListener((e) -> {

				if (!discoveredStack.isEmpty()) {
					Node currentNode = discoveredStack.pop();
					currentNode.updateNodeTypeToPassage();
					currentNode.predecessor.updateNodeTypeToPassage();
					if (!visitedSet.contains(currentNode)) {
						visitedSet.add(currentNode);
						for (Node neighbor : currentNode.getNeighborsForMaze()) {
							if (!visitedSet.contains(neighbor)) {
								discoveredStack.add(neighbor);
							}
						}
					}
				} else {
					Node.timer.stop();
					GUI.canSearchMode = true;
					Node.canContinueUpdatingNodeType = true;
				}
			});

			Node.timer.start();
		}
	}
}
